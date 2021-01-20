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

#include "XPCPointCloud.hpp"
#include "XPCPoint.hpp"



class XPCNodeContentParserAsyncTask : public GAsyncTask {
private:
  XPCNode*      _node;
  IByteBuffer*  _buffer;
  const Planet* _planet;

  std::vector<XPCNode*>*  _children;
  std::vector<XPCPoint*>* _points;
  DirectMesh*             _mesh;

public:
  XPCNodeContentParserAsyncTask(XPCNode* node,
                                IByteBuffer* buffer,
                                const Planet* planet) :
  _node(node),
  _buffer(buffer),
  _planet(planet),
  _children(NULL),
  _points(NULL),
  _mesh(NULL)
  {
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
  }

  void runInBackground(const G3MContext* context) {
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
      _points = new std::vector<XPCPoint*>();

      const int pointsCount = it.nextInt32();
      for (int i = 0; i < pointsCount; i++) {
        XPCPoint* point = XPCPoint::fromByteBufferIterator(it);
        _points->push_back( point );
      }
    }


    if (it.hasNext()) {
      THROW_EXCEPTION("Logic error");
    }


    FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

    const size_t pointsSize = _points->size();
    for (int i = 0; i < pointsSize; i++) {
      XPCPoint* point = _points->at(i);
      vertices->addDegrees(point->_y,
                           point->_x,
                           point->_z);
    }

#warning TODO_______    verticalExaggeration
#warning TODO_______    deltaHeight

    _mesh = new DirectMesh(GLPrimitive::points(),
                           true,
                           vertices->getCenter(),
                           vertices->create(),
                           1,
                           1,
                           Color::newFromRGBA(1,1,1,1) // flatColor
                           );

    delete vertices;
  }

  void onPostExecute(const G3MContext* context) {
    _node->setContent( _children, _points, _mesh );
    _children = NULL; // moved ownership to _node
    _points   = NULL; // moved ownership to _node
    _mesh     = NULL; // moved ownership to _node
  }

};


class XPCNodeContentDownloadListener : public IBufferDownloadListener {
private:
  XPCNode*            _node;
  const IThreadUtils* _threadUtils;
  const Planet*       _planet;

public:

  XPCNodeContentDownloadListener(XPCNode*            node,
                                 const IThreadUtils* threadUtils,
                                 const Planet* planet) :
  _node(node),
  _threadUtils(threadUtils),
  _planet(planet)
  {
    _node->_retain();
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _threadUtils->invokeAsyncTask(new XPCNodeContentParserAsyncTask(_node, buffer, _planet),
                                  true);
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
  }


};


XPCNode::XPCNode(const std::string& id,
                 const Sector* sector,
                 const double minZ,
                 const double maxZ) :
_id(id),
_sector(sector),
_minZ(minZ),
_maxZ(maxZ),
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
_mesh(NULL)
{

}


XPCNode::~XPCNode() {
  delete _sector;

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
    planet->toCartesian( _sector->getNE()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNE()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNW()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getNW()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSE()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSE()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSW()     , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getSW()     , (_maxZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getCenter() , (_minZ + deltaHeight) * verticalExaggeration ),
    planet->toCartesian( _sector->getCenter() , (_maxZ + deltaHeight) * verticalExaggeration )
  };

  std::vector<Vector3D> points(c, c+10);
#endif
#ifdef JAVA_CODE
  java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
  points.add( planet.toCartesian( _sector.getNE()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNE()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNW()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getNW()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSE()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSE()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSW()     , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getSW()     , (_maxZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getCenter() , (_minZ + deltaHeight) * verticalExaggeration ) );
  points.add( planet.toCartesian( _sector.getCenter() , (_maxZ + deltaHeight) * verticalExaggeration ) );
#endif

#warning TODO: check if the sphere fits into the parent's one
  //  if (_parent) {
  //    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
  //  }

  //return Sphere::enclosingSphere(points, 0.1);
  return Sphere::enclosingSphere(points, 0);
}


long long XPCNode::render(const XPCPointCloud* pointCloud,
                          const std::string& treeID,
                          const G3MRenderContext* rc,
                          GLState* glState,
                          const Frustum* frustum,
                          long long nowInMS) {

  long long renderedCount = 0;

  bool renderedInThisFrame = false;

  const Sphere* bounds = getBounds(rc, pointCloud);
  if (bounds != NULL) {
    const bool isVisible = bounds->touchesFrustum(frustum);
    if (isVisible) {
      if ((_projectedArea == -1) || ((_projectedAreaTS + 100) < nowInMS)) {
        const double projectedArea = bounds->projectedArea(rc);
        _projectedArea   = projectedArea;
        _projectedAreaTS = nowInMS;
      }

      const bool isBigEnough = (_projectedArea >= pointCloud->getMinProjectedArea());
      if (isBigEnough) {
        renderedInThisFrame = true;

//        ILogger::instance()->logInfo("- Rendering node \"%s\"", _id.c_str());

        if (_loadedContent) {
#warning ________rawRender
          _mesh->render(rc, glState);
          renderedCount += _mesh->getRenderVerticesCount();
//          renderedCount += rawRender(pointCloud,
//                                     rc,
//                                     glState);
        }
        else {
          if (!_loadingContent) {
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
                                           nowInMS);
          }
        }

      }
    }
  }

  if (_renderedInPreviousFrame != renderedInThisFrame) {
    if (_renderedInPreviousFrame) {
#warning ________unload
      //      unload();
    }
    _renderedInPreviousFrame = renderedInThisFrame;
  }

  return renderedCount;
}


void XPCNode::loadContent(const XPCPointCloud* pointCloud,
                          const std::string& treeID,
                          const G3MRenderContext* rc) {
  _downloader = rc->getDownloader();
  const long long deltaPriority = 100 * _id.length();

  _contentRequestID = pointCloud->requestNodeContentBuffer(_downloader,
                                                           treeID,
                                                           _id,
                                                           deltaPriority,
                                                           new XPCNodeContentDownloadListener(this,
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

  const double minZ = it.nextDouble();
  const double maxZ = it.nextDouble();

  return new XPCNode(nodeID, sector, minZ, maxZ);
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

  _mesh = mesh;
}
