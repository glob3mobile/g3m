//
//  XPCNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#include "XPCNode.hpp"

#include "Sector.hpp"
#include "G3MRenderContext.hpp"
#include "Box.hpp"
#include "Planet.hpp"
#include "Sphere.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "GAsyncTask.hpp"
#include "ByteBufferIterator.hpp"
#include "ILogger.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "DirectMesh.hpp"
#include "Color.hpp"
#include "IDownloader.hpp"
#include "IIntBuffer.hpp"
#include "FloatBufferBuilderFromColor.hpp"

#include "XPCPointCloud.hpp"
#include "XPCPoint.hpp"
#include "XPCMetadata.hpp"
#include "XPCDimension.hpp"
#include "XPCPointColorizer.hpp"
#include "XPCSelectionResult.hpp"



class XPCNodeContentParserAsyncTask : public GAsyncTask {
private:
  const XPCPointCloud* _pointCloud;
  XPCNode*             _node;

  IByteBuffer*  _buffer;

  const Planet* _planet;

  std::vector<XPCNode*>*  _children;
  std::vector<XPCPoint*>* _points;
  DirectMesh*             _mesh;

public:
  XPCNodeContentParserAsyncTask(const XPCPointCloud* pointCloud,
                                XPCNode* node,
                                IByteBuffer* buffer,
                                const Planet* planet) :
  _pointCloud(pointCloud),
  _node(node),
  _buffer(buffer),
  _planet(planet),
  _children(NULL),
  _points(NULL),
  _mesh(NULL)
  {
    _pointCloud->_retain();
    _node->_retain();
  }

  ~XPCNodeContentParserAsyncTask() {
    delete _buffer;

    if (_children != NULL) {
      for (size_t i = 0; i < _children->size(); i++) {
        XPCNode* child = _children->at(i);
        child->_release();
      }
      delete _children;
    }

    if (_points != NULL) {
      for (size_t i = 0; i < _points->size(); i++) {
        XPCPoint* point = _points->at(i);
        delete point;
      }
      delete _points;
    }

    delete _mesh;

    _node->_release();
    _pointCloud->_release();
  }

  void runInBackground(const G3MContext* context) {
    if (_node->isCanceled()) {
      return;
    }

    ByteBufferIterator it(_buffer);

    unsigned char version = it.nextUInt8();
    if (version != 1) {
      ILogger::instance()->logError("Unssuported format version");
      return;
    }

    {
      _children = new std::vector<XPCNode*>();

      const int childrenCount = it.nextInt32();
      for (int i = 0; i < childrenCount; i++) {
        XPCNode* child = XPCNode::fromByteBufferIterator(it);
        _children->push_back( child );
      }
    }

    {
#warning TODO____ points should be a floatbuffer + center
      
      _points = new std::vector<XPCPoint*>();

      const int pointsCount = it.nextInt32();
      if (pointsCount > 0) {
        const float centerLatitudeDegrees  = it.nextFloat();
        const float centerLongitudeDegrees = it.nextFloat();
        const float centerHeight           = it.nextFloat();
        for (int i = 0; i < pointsCount; i++) {
          XPCPoint* point = XPCPoint::fromByteBufferIterator(it, centerLatitudeDegrees, centerLongitudeDegrees, centerHeight);
          _points->push_back( point );
        }
      }
    }

    const XPCMetadata* metadata = _pointCloud->getMetadada();

    std::vector<const IByteBuffer*>* dimensionsValues;

    const IIntBuffer* dimensionIndices = _pointCloud->getRequiredDimensionIndices();
    if (dimensionIndices == NULL) {
      dimensionsValues = NULL;
    }
    else {

      const size_t dimensionsCount = dimensionIndices->size();
      dimensionsValues = new std::vector<const IByteBuffer*>();
      for (size_t j = 0; j < dimensionsCount; j++) {
        const size_t dimensionIndex = dimensionIndices->get(j);

        const XPCDimension* dimension = metadata->getDimension( dimensionIndex );

        const IByteBuffer* dimensionValues = dimension->readValues( it );

        dimensionsValues->push_back( dimensionValues );
      }
    }

    if (it.hasNext()) {
      THROW_EXCEPTION("Logic error");
    }

    XPCPointColorizer* pointsColorizer = _pointCloud->getPointsColorizer();
    const float deltaHeight            = _pointCloud->getDeltaHeight();
    const float verticalExaggeration   = _pointCloud->getVerticalExaggeration();

    FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);
    FloatBufferBuilderFromColor colors;

    const size_t pointsSize = _points->size();
    for (int i = 0; i < pointsSize; i++) {
      XPCPoint* point = _points->at(i);
      vertices->addDegrees(point->_latitudeDegrees,
                           point->_longitudeDegrees,
                           (point->_height + deltaHeight) * verticalExaggeration);

      if (pointsColorizer != NULL) {
        const Color color = pointsColorizer->colorize(metadata,
                                                      _points,
                                                      dimensionsValues,
                                                      i);

        colors.add(color);
      }
      else {
        colors.add(1, 1, 1, 1);
      }
    }

    if (dimensionsValues != NULL) {
      for (size_t i = 0; i < dimensionsValues->size(); i++) {
        delete dimensionsValues->at(i);
      }
#ifdef C_CODE
      delete dimensionsValues;
      dimensionsValues = NULL;
#endif
    }

    _mesh = new DirectMesh(GLPrimitive::points(),
                           true,
                           vertices->getCenter(),
                           vertices->create(),
                           1,
                           _pointCloud->getDevicePointSize(),
                           NULL,            // flatColor
                           colors.create(), // const IFloatBuffer* colors
                           true            // depthTest
                           );

    delete vertices;
  }

  void onPostExecute(const G3MContext* context) {
    if (_node->isCanceled()) {
      return;
    }

    _node->setContent( _children, _points, _mesh );
    _children = NULL; // moved ownership to _node
    _points   = NULL; // moved ownership to _node
    _mesh     = NULL; // moved ownership to _node
  }

};


class XPCNodeContentDownloadListener : public IBufferDownloadListener {
private:
  const XPCPointCloud* _pointCloud;
  XPCNode*       _node;

  const IThreadUtils* _threadUtils;
  const Planet*       _planet;

public:

  XPCNodeContentDownloadListener(const XPCPointCloud* pointCloud,
                                 XPCNode* node,
                                 const IThreadUtils* threadUtils,
                                 const Planet* planet) :
  _pointCloud(pointCloud),
  _node(node),
  _threadUtils(threadUtils),
  _planet(planet)
  {
    _pointCloud->_retain();
    _node->_retain();
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    if (_node->isCanceled()) {
      delete buffer;
    }
    else {
      if (_pointCloud->isVerbose()) {
#ifdef C_CODE
        ILogger::instance()->logInfo("Downloaded content for \"%s\" node \"%s\" (bytes=%ld)",
                                     _pointCloud->getCloudName().c_str(),
                                     _node->getID().c_str(),
                                     buffer->size());
#endif
#ifdef JAVA_CODE
        ILogger.instance().logInfo("Downloaded metadata for \"%s\" node \"%s\" (bytes=%d)",
                                   _pointCloud.getCloudName(),
                                   _node.getID(),
                                   buffer.size());
#endif
      }

      _threadUtils->invokeAsyncTask(new XPCNodeContentParserAsyncTask(_pointCloud,
                                                                      _node,
                                                                      buffer,
                                                                      _planet),
                                    true);
    }
  }

  void onError(const URL& url) {
    _node->errorDownloadingContent();
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }

  ~XPCNodeContentDownloadListener() {
    _node->_release();
    _pointCloud->_release();
  }


};


XPCNode::XPCNode(const std::string& id,
                 const Sector* sector,
                 const double minHeight,
                 const double maxHeight) :
_id(id),
_sector(sector),
_minHeight(minHeight),
_maxHeight(maxHeight),
_bounds(NULL),
_renderedInPreviousFrame(false),
_projectedArea(-1),
_projectedAreaTS(-1),
_loadedContent(false),
_loadingContent(false),
_children(NULL),
_childrenSize(0),
_downloader(NULL),
_contentRequestID(-1),
_points(NULL),
_mesh(NULL),
_canceled(false)
{

}


XPCNode::~XPCNode() {
  delete _sector;

  delete _bounds;

  for (size_t i = 0; i < _childrenSize; i++) {
    XPCNode* child = _children->at(i);
    child->_release();
  }

  delete _children;

  if (_points != NULL) {
    for (size_t i = 0; i < _points->size(); i++) {
      XPCPoint* point = _points->at(i);
      delete point;
    }

    delete _points;
  }

  delete _mesh;
}


const Sector* XPCNode::getSector() const {
  return _sector;
}


const Sphere* XPCNode::getBounds(const G3MRenderContext* rc,
                                 const XPCPointCloud* pointCloud) {
  if (_bounds == NULL) {
    _bounds = calculateBounds(rc, pointCloud);
  }
  return _bounds;
}


Sphere* XPCNode::calculateBounds(const G3MRenderContext* rc,
                                 const XPCPointCloud* pointCloud) {
  const Planet* planet = rc->getPlanet();

  const float deltaHeight          = pointCloud->getDeltaHeight();
  const float verticalExaggeration = pointCloud->getVerticalExaggeration();

#ifdef C_CODE
  const Vector3D c[10] = {
    planet->toCartesian( _sector->getNE()     , (_minHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNE()     , (_maxHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNW()     , (_minHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNW()     , (_maxHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSE()     , (_minHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSE()     , (_maxHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSW()     , (_minHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSW()     , (_maxHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getCenter() , (_minHeight + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getCenter() , (_maxHeight + deltaHeight) * verticalExaggeration )
  };

  std::vector<Vector3D> points(c, c+10);
#endif
#ifdef JAVA_CODE
  java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
  points.add( planet.toCartesian( _sector.getNE()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNE()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNW()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNW()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSE()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSE()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSW()     , (_minHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSW()     , (_maxHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getCenter() , (_minHeight + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getCenter() , (_maxHeight + deltaHeight) * verticalExaggeration ) );
#endif

#warning TODO: check if the sphere fits into the parent's one
  //  if (_parent) {
  //    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
  //  }

  //return Sphere::enclosingSphere(points, 0.1);
  return Sphere::enclosingSphere(points, 0);
}

void XPCNode::cancelLoadContent() {
  if (_contentRequestID != -1) {
    _downloader->cancelRequest(_contentRequestID);
    _contentRequestID = -1;
  }
}

void XPCNode::unloadContent() {
  if (_points != NULL) {
    for (size_t i = 0; i < _points->size(); i++) {
      XPCPoint* point = _points->at(i);
      delete point;
    }
    delete _points;
    _points = NULL;
  }

  delete _mesh;
  _mesh = NULL;

  _loadedContent = false;
}

void XPCNode::unloadChildren() {
  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      XPCNode* child = _children->at(i);
      child->unload();
      child->_release();
    }

    delete _children;
    _children = NULL;
    _childrenSize = 0;
  }
}

void XPCNode::unload() {
  _canceled = true;

  if (_loadingContent) {
    _loadingContent = false;
    cancelLoadContent();
  }

  if (_loadedContent) {
    _loadedContent = false;
    unloadContent();
  }

  unloadChildren();
}

void XPCNode::loadContent(const XPCPointCloud* pointCloud,
                          const std::string& treeID,
                          const G3MRenderContext* rc) {
  _downloader = rc->getDownloader();

  const long long deltaPriority = 100 - _id.length() /* + _pointsCount */;

  _contentRequestID = pointCloud->requestNodeContentBuffer(_downloader,
                                                           treeID,
                                                           _id,
                                                           deltaPriority,
                                                           new XPCNodeContentDownloadListener(pointCloud,
                                                                                              this,
                                                                                              rc->getThreadUtils(),
                                                                                              rc->getPlanet()),
                                                           true);
}

void XPCNode::errorDownloadingContent() {
  // I don't know how to deal with it (DGD)  :(
}

XPCNode* XPCNode::fromByteBufferIterator(ByteBufferIterator& it) {
  const std::string nodeID = it.nextZeroTerminatedString();

  const double lowerLatitudeDegrees  = it.nextDouble();
  const double lowerLongitudeDegrees = it.nextDouble();
  const double upperLatitudeDegrees  = it.nextDouble();
  const double upperLongitudeDegrees = it.nextDouble();

  const Sector* sector = Sector::newFromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees,
                                                upperLatitudeDegrees, upperLongitudeDegrees);

  const double minHeight = it.nextDouble();
  const double maxHeight = it.nextDouble();

  return new XPCNode(nodeID, sector, minHeight, maxHeight);
}


void XPCNode::setContent(std::vector<XPCNode*>* children,
                         std::vector<XPCPoint*>* points,
                         DirectMesh* mesh) {
  _loadedContent = true;

  for (size_t i = 0; i < _childrenSize; i++) {
    XPCNode* child = _children->at(i);
    child->_release();
  }

  _children     = children;
  _childrenSize = (_children == NULL) ? 0 : _children->size();

  if (_points != NULL) {
    for (size_t i = 0; i < _points->size(); i++) {
      XPCPoint* point = _points->at(i);
      delete point;
    }

    delete _points;
  }

  _points = points;

  delete _mesh;
  _mesh = mesh;
}

bool XPCNode::isCanceled() const {
  return _canceled;
}

long long XPCNode::render(const XPCPointCloud* pointCloud,
                          const std::string& treeID,
                          const G3MRenderContext* rc,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS,
                          bool renderDebug,
                          const XPCSelectionResult* selectionResult) {

  long long renderedCount = 0;

  bool renderedInThisFrame = false;

  const Sphere* bounds = getBounds(rc, pointCloud);
  if (bounds != NULL) {
    const bool isVisible = bounds->touchesFrustum(frustum);
    if (isVisible) {

      if (renderDebug) {
        bounds->render(rc, glState, Color::WHITE);
      }

      if ((_projectedArea == -1) || ((_projectedAreaTS + 100) < nowInMS)) {
        const double projectedArea = bounds->projectedArea(rc);
        _projectedArea   = projectedArea;
        _projectedAreaTS = nowInMS;
      }

      const bool isBigEnough = (_projectedArea >= pointCloud->getMinProjectedArea());
      if (isBigEnough) {
        renderedInThisFrame = true;

        //        if (selectionRay != NULL) {
        //          if (touchesRay(selectionRay)) {
        //            bounds->render(rc, glState, Color::YELLOW);
        //          }
        //        }

        //        ILogger::instance()->logInfo("- Rendering node \"%s\"", _id.c_str());

        if (_loadedContent) {
          if (_mesh != NULL) {
//            if (selectionResult == NULL) {
              _mesh->render(rc, glState);
              renderedCount += _mesh->getRenderVerticesCount();
//            }
//            else {
//              if (_bounds->touchesRay(selectionResult->_ray)) {
//                _mesh->render(rc, glState);
//                renderedCount += _mesh->getRenderVerticesCount();
//              }
//            }
          }
        }
        else {
          if (!_loadingContent) {
            _canceled = false;
            _loadingContent = true;
            loadContent(pointCloud, treeID, rc);
          }
        }

        if (_children != NULL) {
          for (size_t i = 0; i < _childrenSize; i++) {
            XPCNode* child = _children->at(i);
            renderedCount += child->render(pointCloud,
                                           treeID,
                                           rc,
                                           glState,
                                           frustum,
                                           nowInMS,
                                           renderDebug,
                                           selectionResult);
          }
        }

      }
    }
  }

  if (_renderedInPreviousFrame != renderedInThisFrame) {
    if (_renderedInPreviousFrame) {
      unload();
    }
    _renderedInPreviousFrame = renderedInThisFrame;
  }

  return renderedCount;
}

const bool XPCNode::selectPoints(XPCSelectionResult* selectionResult) const {
  if (_bounds == NULL) {
    return false;
  }

  if (!_bounds->touchesRay(selectionResult->_ray)) {
    return false;
  }

  if (!selectionResult->isInterestedIn(_bounds)) {
    return false;
  }

  bool selectedPoint = false;

  if (_mesh != NULL) {
    const size_t verticesCount = _mesh->getVerticesCount();

    MutableVector3D vertex;
    for (size_t i = 0; i < verticesCount; i++) {
      _mesh->getVertex(i, vertex);
      if ( selectionResult->evaluateCantidate(vertex) ) {
        selectedPoint = true;
      }
    }
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      XPCNode* child = _children->at(i);
      if (child->selectPoints(selectionResult)) {
        selectedPoint = true;
      }
    }
  }

  return selectedPoint;
}
