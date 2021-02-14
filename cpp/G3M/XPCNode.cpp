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
#include "DirectMesh.hpp"
#include "Color.hpp"
#include "IDownloader.hpp"
#include "IIntBuffer.hpp"
#include "IFactory.hpp"
#include "ITimer.hpp"
#include "MutableColor.hpp"

#include "XPCPointCloud.hpp"
#include "XPCMetadata.hpp"
#include "XPCDimension.hpp"
#include "XPCPointColorizer.hpp"
#include "XPCSelectionResult.hpp"
#include "XPCRenderingState.hpp"


class XPCNodeContentParserAsyncTask : public GAsyncTask {
private:
  const XPCPointCloud* _pointCloud;
  XPCNode*             _node;

  IByteBuffer*  _buffer;

  const Planet* _planet;

  std::vector<XPCNode*>*  _children;
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
      ILogger::instance()->logError("Unsupported format version");
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

    const int pointsCount = it.nextInt32();

    if (pointsCount <= 0) {
      return;
    }

    IFloatBuffer* cartesianVertices = IFactory::instance()->createFloatBuffer( pointsCount * 3 /* X, Y, Z */ );

    double* heights = new double[pointsCount];

    const double deltaHeight          = _pointCloud->getDeltaHeight();
    const float  verticalExaggeration = _pointCloud->getVerticalExaggeration();

    const float centerLatitudeDegrees  = it.nextFloat();
    const float centerLongitudeDegrees = it.nextFloat();
    const float centerHeight           = it.nextFloat();

    const Vector3D cartesianCenter = _planet->toCartesian(Angle::fromDegrees(centerLatitudeDegrees),
                                                          Angle::fromDegrees(centerLongitudeDegrees),
                                                          ((double) centerHeight + deltaHeight) * verticalExaggeration);
    {
      MutableVector3D bufferCartesian;

      for (int i = 0; i < pointsCount; i++) {
        const double latitudeDegrees  = (double) it.nextFloat() + centerLatitudeDegrees;
        const double longitudeDegrees = (double) it.nextFloat() + centerLongitudeDegrees;

        const double rawHeight    = (double) it.nextFloat() + centerHeight;

        heights[i] = rawHeight;

        const double scaledHeight = (rawHeight + deltaHeight) * verticalExaggeration;

        _planet->toCartesianFromDegrees(latitudeDegrees,
                                        longitudeDegrees,
                                        scaledHeight,
                                        bufferCartesian);

        cartesianVertices->rawPut((i * 3) + 0, (float) (bufferCartesian._x - cartesianCenter._x) );
        cartesianVertices->rawPut((i * 3) + 1, (float) (bufferCartesian._y - cartesianCenter._y) );
        cartesianVertices->rawPut((i * 3) + 2, (float) (bufferCartesian._z - cartesianCenter._z) );
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

    IFloatBuffer* colors = IFactory::instance()->createFloatBuffer( pointsCount * 4 /* R, G, B, A */ );
    MutableColor  bufferColor;

    XPCPointColorizer* pointsColorizer = _pointCloud->getPointsColorizer();

    for (int i = 0; i < pointsCount; i++) {
      if (pointsColorizer == NULL) {
        colors->rawPut((i * 4) + 0, 1 /* red   */);
        colors->rawPut((i * 4) + 1, 1 /* green */);
        colors->rawPut((i * 4) + 2, 1 /* blue  */);
        colors->rawPut((i * 4) + 3, 1 /* alpha */);
      }
      else {
        pointsColorizer->colorize(metadata,
                                  heights,
                                  dimensionsValues,
                                  i,
                                  bufferColor);
        colors->rawPut((i * 4) + 0, bufferColor._red);
        colors->rawPut((i * 4) + 1, bufferColor._green);
        colors->rawPut((i * 4) + 2, bufferColor._blue);
        colors->rawPut((i * 4) + 3, bufferColor._alpha);
      }
    }

    delete [] heights;

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
                           cartesianCenter,
                           cartesianVertices,
                           1,
                           _pointCloud->getDevicePointSize(),
                           NULL, // flatColor
                           colors,
                           _pointCloud->depthTest());
  }

  void onPostExecute(const G3MContext* context) {
    if (_node->isCanceled()) {
      return;
    }

    _node->setContent( _children, _mesh );
    _children = NULL; // moved ownership to _node
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
//      if (_pointCloud->isVerbose()) {
//#ifdef C_CODE
//        ILogger::instance()->logInfo("Downloaded content for \"%s\" node \"%s\" (bytes=%ld)",
//                                     _pointCloud->getCloudName().c_str(),
//                                     _node->getID().c_str(),
//                                     buffer->size());
//#endif
//#ifdef JAVA_CODE
//        ILogger.instance().logInfo("Downloaded content for \"%s\" node \"%s\" (bytes=%d)",
//                                   _pointCloud.getCloudName(),
//                                   _node.getID(),
//                                   buffer.size());
//#endif
//      }

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
                 const int pointsCount,
                 const double minHeight,
                 const double maxHeight) :
_id(id),
_sector(sector),
_pointsCount(pointsCount),
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

  delete _mesh;
}


const Sector* XPCNode::getSector() const {
  return _sector;
}

const double XPCNode::getMinHeight() const {
  return _minHeight;
}

const double XPCNode::getMaxHeight() const {
  return _maxHeight;
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

  const double deltaHeight          = pointCloud->getDeltaHeight();
  const float  verticalExaggeration = pointCloud->getVerticalExaggeration();

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

//#warning TODO: check if the sphere fits into the parent's one
  //  if (_parent) {
  //    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
  //  }

  return Sphere::enclosingSphere(points, 0);
}

void XPCNode::cancelLoadContent() {
  if (_contentRequestID != -1) {
    _downloader->cancelRequest(_contentRequestID);
    _contentRequestID = -1;
  }
}

void XPCNode::unloadContent() {
  delete _mesh;
  _mesh = NULL;

  _loadedContent = false;
}

void XPCNode::unloadChildren() {
  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      XPCNode* child = _children->at(i);
      child->cancel();
      child->_release();
    }

    delete _children;
    _children = NULL;
    _childrenSize = 0;
  }
}

void XPCNode::cancel() {
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

void XPCNode::reload() {
  if (_loadingContent) {
    _loadingContent = false;
    cancelLoadContent();
  }

  if (_loadedContent) {
    _loadedContent = false;
    unloadContent();
  }

  unloadChildren();

  delete _mesh;
  _mesh = NULL;
}

void XPCNode::loadContent(const XPCPointCloud* pointCloud,
                          const std::string& treeID,
                          const G3MRenderContext* rc) {
  _downloader = rc->getDownloader();

  // const long long deltaPriority = ((100 - _id.length()) * 1000) + _pointsCount;
  // const long long deltaPriority = (_id.length() * 1000) + _pointsCount;

  const long long deltaPriority = 100 - _id.length();

//  const size_t depth = _id.length();
//  const long long deltaPriority =  (depth == 0) ? 100 : _id.length();

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

  const int pointsCount = it.nextInt32();

  const double minHeight = it.nextDouble();
  const double maxHeight = it.nextDouble();

  return new XPCNode(nodeID, sector, pointsCount, minHeight, maxHeight);
}


void XPCNode::setContent(std::vector<XPCNode*>* children,
                         DirectMesh* mesh) {
  _loadedContent = true;

  for (size_t i = 0; i < _childrenSize; i++) {
    XPCNode* child = _children->at(i);
    child->_release();
  }

  _children     = children;
  _childrenSize = (_children == NULL) ? 0 : _children->size();

  delete _mesh;
  _mesh = mesh;
}

bool XPCNode::isCanceled() const {
  return _canceled;
}

long long XPCNode::render(const XPCPointCloud* pointCloud,
                          const std::string& treeID,
                          const G3MRenderContext* rc,
                          ITimer* lastSplitTimer,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS,
                          bool renderDebug,
                          const XPCSelectionResult* selectionResult,
                          XPCRenderingState& renderingState) {

  long long renderedCount = 0;

  bool renderedInThisFrame = false;

  const Sphere* bounds = getBounds(rc, pointCloud);
  if (bounds != NULL) {
    const bool isVisible = bounds->touchesFrustum(frustum);
    if (isVisible) {

      if (renderDebug) {
        bounds->render(rc, glState, Color::WHITE);
      }

      if ((_projectedArea == -1) || ((_projectedAreaTS + 50 /* 167 */) < nowInMS)) {
        _projectedArea   = bounds->projectedArea(rc);
        _projectedAreaTS = nowInMS;
      }

      const bool isBigEnough = (_id.length() == 0) || (_projectedArea >= pointCloud->getMinProjectedArea());
      if (isBigEnough) {
        renderedInThisFrame = true;

        //if (selectionRay != NULL) {
        //  if (touchesRay(selectionRay)) {
        //    bounds->render(rc, glState, Color::YELLOW);
        //  }
        //}

        //ILogger::instance()->logInfo("- Rendering node \"%s\"", _id.c_str());

        if (_children != NULL) {
          for (size_t i = 0; i < _childrenSize; i++) {
            XPCNode* child = _children->at(i);
            renderedCount += child->render(pointCloud,
                                           treeID,
                                           rc,
                                           lastSplitTimer,
                                           glState,
                                           frustum,
                                           nowInMS,
                                           renderDebug,
                                           selectionResult,
                                           renderingState);
          }
          //if (_childrenSize == 0) {
          //  renderingState._pointSize = pointCloud->getDevicePointSize();
          //}
        }

        if (_children == NULL) {
          renderingState.reset();
        }

        if (_loadedContent) {
          if (_mesh != NULL) {
            renderedCount += _mesh->getRenderVerticesCount();

            const float pointSize = pointCloud->getDevicePointSize();
            if (pointCloud->isDynamicPointSize()) {
              const IMathUtils* mu = IMathUtils::instance();

              const float dynPointSize = mu->clamp(mu->sqrt( (float) (_projectedArea / renderedCount) ),
                                                   pointSize,
                                                   renderingState._pointSize);

              renderingState._pointSize = dynPointSize;

              _mesh->setPointSize(dynPointSize);
            }
            else {
              _mesh->setPointSize(pointSize);
            }

            _mesh->render(rc, glState);
          }
        }
        else {
          if (!_loadingContent) {
            if ( (_id.length() == 0) || (lastSplitTimer->elapsedTimeInMilliseconds() > 0) ) {
              lastSplitTimer->start();
              _canceled = false;
              _loadingContent = true;
              loadContent(pointCloud, treeID, rc);
            }
          }
        }


      }
    }
  }

  if (_renderedInPreviousFrame != renderedInThisFrame) {
    if (_renderedInPreviousFrame) {
      cancel();
    }
    _renderedInPreviousFrame = renderedInThisFrame;
  }

  return renderedCount;
}

const bool XPCNode::selectPoints(XPCSelectionResult* selectionResult,
                                 const XPCPointCloud* pointCloud,
                                 const std::string& treeID) const {
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
    const std::string nodeID = getID();

    const size_t verticesCount = _mesh->getVerticesCount();

    MutableVector3D vertex;
    for (int i = 0; i < verticesCount; i++) {
      _mesh->getVertex(i, vertex);

      if ( selectionResult->evaluateCantidate(vertex,
                                              pointCloud,
                                              treeID,
                                              nodeID,
                                              i) ) {
        selectedPoint = true;
      }
    }
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      XPCNode* child = _children->at(i);
      if (child->selectPoints(selectionResult,
                              pointCloud,
                              treeID)) {
        selectedPoint = true;
      }
    }
  }

  return selectedPoint;
}
