//
//  VectorStreamingRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/30/15.
//
//

#include "VectorStreamingRenderer.hpp"
#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "IJSONParser.hpp"
#include "Sector.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"
#include "JSONString.hpp"
#include "Camera.hpp"
#include "Sphere.hpp"
#include "GEOJSONParser.hpp"
#include "GEOObject.hpp"
#include "Mark.hpp"
#include "Geodetic2D.hpp"
#include "MarksRenderer.hpp"
#include "G3MRenderContext.hpp"
#include "Planet.hpp"


VectorStreamingRenderer::Cluster::~Cluster() {
  delete _position;
}


VectorStreamingRenderer::ChildrenParserAsyncTask::~ChildrenParserAsyncTask() {
  _node->_childrenTask = NULL;
  _node->_release();
  
  delete _buffer;
  
  if (_children != NULL) {
    for (size_t i = 0; i > _children->size(); i++) {
      Node* child = _children->at(i);
      child->_release();
    }
    delete _children;
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::ChildrenParserAsyncTask::runInBackground(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);
  
  delete _buffer;
  _buffer = NULL;
  
  if (jsonBaseObject != NULL) {
    const JSONArray* nodesJSON = jsonBaseObject->asArray();
    if (nodesJSON != NULL) {
      _children = new std::vector<Node*>();
      for (size_t i = 0; i < nodesJSON->size(); i++) {
        const JSONObject* nodeJSON = nodesJSON->getAsObject(i);
        _children->push_back( GEOJSONUtils::parseNode(nodeJSON,
                                                      _node->getVectorSet(),
                                                      _verbose) );
      }
    }
    
    delete jsonBaseObject;
  }
}

void VectorStreamingRenderer::ChildrenParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  
  _node->parsedChildren(_children);
  _children = NULL; // moved ownership to _node
}

void VectorStreamingRenderer::NodeChildrenDownloadListener::onDownload(const URL& url,
                                                                       IByteBuffer* buffer,
                                                                       bool expired) {
  if (_verbose) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Downloaded children (bytes=%ld)",
                                 _node->getFullName().c_str(),
                                 buffer->size());
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Downloaded children (bytes=%d)",
                               _node.getFullName(),
                               buffer.size());
#endif
  }
  _node->_childrenTask = new ChildrenParserAsyncTask(_node, _verbose, buffer);
  _threadUtils->invokeAsyncTask(_node->_childrenTask, true);
}

void VectorStreamingRenderer::NodeChildrenDownloadListener::onError(const URL& url) {
  _node->errorDownloadingChildren();
}

void VectorStreamingRenderer::NodeChildrenDownloadListener::onCancel(const URL& url) {
  // do nothing
}

void VectorStreamingRenderer::NodeChildrenDownloadListener::onCanceledDownload(const URL& url,
                                                                               IByteBuffer* buffer,
                                                                               bool expired) {
  // do nothing
}

VectorStreamingRenderer::FeaturesParserAsyncTask::~FeaturesParserAsyncTask() {
  _node->_featuresTask = NULL;
  _node->_release();
  
  delete _buffer;
  
  if (_clusters != NULL) {
    for (size_t i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
  }
  
  delete _features;
  
  if (_children != NULL) {
    for (size_t i = 0; i < _children->size(); i++) {
      Node* child = _children->at(i);
      child->_release();
    }
    delete _children;
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<VectorStreamingRenderer::Cluster*>* VectorStreamingRenderer::FeaturesParserAsyncTask::parseClusters(const JSONBaseObject* jsonBaseObject) {
  if (jsonBaseObject == NULL) {
    return NULL;
  }
  
  const JSONArray* jsonArray = jsonBaseObject->asArray();
  if ((jsonArray == NULL) || (jsonArray->size() == 0)) {
    return NULL;
  }
  
  std::vector<VectorStreamingRenderer::Cluster*>* clusters = new std::vector<VectorStreamingRenderer::Cluster*>();
  const size_t clustersCount = jsonArray->size();
  for (size_t i = 0; i < clustersCount; i++) {
    const JSONObject* clusterJson = jsonArray->getAsObject(i);
    const Geodetic2D* position = GEOJSONUtils::parseGeodetic2D( clusterJson->getAsArray("position") );
    const long long   size     = (long long) clusterJson->getAsNumber("size")->value();
    
    clusters->push_back( new Cluster(position, size) );
  }
  
  return clusters;
}

std::vector<VectorStreamingRenderer::Node*>* VectorStreamingRenderer::FeaturesParserAsyncTask::parseChildren(const JSONBaseObject* jsonBaseObject) {
  if (jsonBaseObject == NULL) {
    return NULL;
  }
  
  const JSONArray* jsonArray = jsonBaseObject->asArray();
  if ((jsonArray == NULL) || (jsonArray->size() == 0)) {
    return NULL;
  }
  
  std::vector<Node*>* result = new std::vector<Node*>();
  for (size_t i = 0; i < jsonArray->size(); i++) {
    const JSONObject* nodeJSON = jsonArray->getAsObject(i);
    result->push_back( GEOJSONUtils::parseNode(nodeJSON,
                                               _node->getVectorSet(),
                                               _verbose) );
  }
  
  return result;
}


void VectorStreamingRenderer::FeaturesParserAsyncTask::runInBackground(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);
  
  delete _buffer;
  _buffer = NULL;
  
  if (jsonBaseObject != NULL) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    
    _clusters = parseClusters( jsonObject->get("clusters") );
    _features = GEOJSONParser::parse( jsonObject->get("features")->asObject() , _verbose);
    _children = parseChildren( jsonObject->get("children") );
    
    delete jsonBaseObject;
  }
}

void VectorStreamingRenderer::FeaturesParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  _node->parsedFeatures(_clusters, _features, _children);
  _clusters = NULL; // moved ownership to _node
  _features = NULL; // moved ownership to _node
  _children = NULL; // moved ownership to _node
}

void VectorStreamingRenderer::NodeFeaturesDownloadListener::onDownload(const URL& url,
                                                                       IByteBuffer* buffer,
                                                                       bool expired) {
  if (_verbose) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Downloaded features (bytes=%ld)",
                                 _node->getFullName().c_str(),
                                 buffer->size());
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Downloaded features (bytes=%d)",
                               _node.getFullName(),
                               buffer.size());
#endif
  }
  _node->_featuresTask = new FeaturesParserAsyncTask(_node, _verbose, buffer);
  _threadUtils->invokeAsyncTask(_node->_featuresTask, true);
}

void VectorStreamingRenderer::NodeFeaturesDownloadListener::onError(const URL& url) {
  _node->errorDownloadingFeatures();
}

void VectorStreamingRenderer::NodeFeaturesDownloadListener::onCancel(const URL& url) {
  // do nothing
}

void VectorStreamingRenderer::NodeFeaturesDownloadListener::onCanceledDownload(const URL& url,
                                                                               IByteBuffer* buffer,
                                                                               bool expired) {
  // do nothing
}

VectorStreamingRenderer::Node::Node(const VectorSet*                vectorSet,
                                    const std::string&              id,
                                    const Sector*                   nodeSector,
                                    const int                       clustersCount,
                                    const int                       featuresCount,
                                    const std::vector<std::string>& childrenIDs,
                                    std::vector<Node*>*             children,
                                    const bool                      verbose) :
_parent(NULL),
_vectorSet(vectorSet),
_id(id),
_nodeSector(nodeSector),
_clustersCount(clustersCount),
_featuresCount(featuresCount),
_childrenIDs(childrenIDs),
_verbose(verbose),
_loadedFeatures(false),
_loadingFeatures(false),
_children(children),
_childrenSize(children == NULL ? 0 : children->size()),
_loadingChildren(false),
_isBeingRendered(false),
_boundingVolume(NULL),
_featuresRequestID(-1),
_childrenRequestID(-1),
_downloader(NULL),
_clusters(NULL),
_features(NULL),
_clusterMarksCount(0),
_featureMarksCount(0),
_childrenTask(NULL),
_featuresTask(NULL)
{
  setChildren(children);
}

void VectorStreamingRenderer::Node::setChildren(std::vector<Node*>* children) {
  _loadingChildren = false;
  
  if ((children == NULL) && (_children != NULL)) {
    return;
  }
  
  if (children != _children) {
    if (_children != NULL) {
      for (size_t i = 0; i < _childrenSize; i++) {
        Node* child = _children->at(i);
        child->unload();
        child->_release();
      }
      delete _children;
    }
    
    _children = children;
    _childrenSize = (children == NULL) ? 0 : _children->size();
    if (_children != NULL) {
      for (size_t i = 0; i < _childrenSize; i++) {
        Node* child = _children->at(i);
        child->setParent(this);
      }
    }
  }
}

void VectorStreamingRenderer::Node::setParent(Node* parent) {
  if (_parent != NULL) {
    THROW_EXCEPTION("Node already has a parent");
  }
  _parent = parent;
  _parent->_retain();
}


VectorStreamingRenderer::Node::~Node() {
  unload();
  
  delete _features;
  
  if (_clusters != NULL) {
    for (size_t i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
  }
  
  delete _nodeSector;
  delete _boundingVolume;
  
  if (_parent != NULL) {
    _parent->_release();
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::Node::parsedChildren(std::vector<Node*>* children) {
  setChildren(children);
}

void VectorStreamingRenderer::Node::parsedFeatures(std::vector<Cluster*>* clusters,
                                                   GEOObject*             features,
                                                   std::vector<Node*>*    children) {
  _loadedFeatures = true;
  _loadingFeatures = false;
  _featuresRequestID = -1;
  
  parsedChildren(children);
  
  delete _features;
  _featureMarksCount = 0;
  
  if (features != NULL) {
    _features = features;
    _featureMarksCount = _features->createFeatureMarks(_vectorSet, this);
    
    if (_verbose && (_featureMarksCount > 0)) {
#ifdef C_CODE
      ILogger::instance()->logInfo("\"%s\": Created %ld feature-marks",
                                   getFullName().c_str(),
                                   _featureMarksCount);
#endif
#ifdef JAVA_CODE
      ILogger.instance().logInfo("\"%s\": Created %d feature-marks",
                                 getFullName(),
                                 _featureMarksCount);
#endif
    }
    delete _features;
    _features = NULL;
  }
  
  if (_clusters != NULL) {
    for (size_t i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
    _clusters = NULL;
    _clusterMarksCount = 0;
  }
  
  if (clusters != NULL) {
    _clusters = clusters;
    createClusterMarks();
  }
}

void VectorStreamingRenderer::Node::createClusterMarks() {
  _clusterMarksCount = _vectorSet->createClusterMarks(this, _clusters);
  
  if (_verbose && (_clusterMarksCount > 0)) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Created %ld cluster-marks",
                                 getFullName().c_str(),
                                 _clusterMarksCount);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Created %d cluster-marks",
                               getFullName(),
                               _clusterMarksCount);
#endif
  }
}


BoundingVolume* VectorStreamingRenderer::Node::getBoundingVolume(const G3MRenderContext *rc) {
  if (_boundingVolume == NULL) {
    const Planet* planet = rc->getPlanet();
    
#ifdef C_CODE
    const Vector3D c[5] = {
      planet->toCartesian( _nodeSector->getNE()     ),
      planet->toCartesian( _nodeSector->getNW()     ),
      planet->toCartesian( _nodeSector->getSE()     ),
      planet->toCartesian( _nodeSector->getSW()     ),
      planet->toCartesian( _nodeSector->getCenter() )
    };
    
    std::vector<Vector3D> points(c, c+5);
#endif
#ifdef JAVA_CODE
    java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(5);
    points.add( planet.toCartesian( _nodeSector.getNE()     ) );
    points.add( planet.toCartesian( _nodeSector.getNW()     ) );
    points.add( planet.toCartesian( _nodeSector.getSE()     ) );
    points.add( planet.toCartesian( _nodeSector.getSW()     ) );
    points.add( planet.toCartesian( _nodeSector.getCenter() ) );
#endif
    
    _boundingVolume = Sphere::enclosingSphere(points);
  }
  
  return _boundingVolume;
}



void VectorStreamingRenderer::Node::loadFeatures(const G3MRenderContext* rc) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("\"%s\": Downloading features for node \'%s\'",
  //                                 _vectorSet->getName().c_str(),
  //                                 _id.c_str());
  //  }
  
  _downloader = rc->getDownloader();
  _featuresRequestID = _downloader->requestBuffer(_vectorSet->getNodeFeaturesURL(_id),
                                                  _vectorSet->getDownloadPriority() + _featuresCount + _clustersCount,
                                                  _vectorSet->getTimeToCache(),
                                                  _vectorSet->getReadExpired(),
                                                  new NodeFeaturesDownloadListener(this,
                                                                                   rc->getThreadUtils(),
                                                                                   _verbose),
                                                  true);
}

void VectorStreamingRenderer::Node::unloadFeatures() {
  _loadedFeatures  = false;
  _loadingFeatures = false;
  
  delete _features;
  _features = NULL;
  
  if (_clusters != NULL) {
    for (size_t i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
    _clusters = NULL;
  }
}

void VectorStreamingRenderer::Node::cancelLoadFeatures() {
  if (_featuresRequestID != -1) {
    _downloader->cancelRequest(_featuresRequestID);
    _featuresRequestID = -1;
  }
}

void VectorStreamingRenderer::Node::loadChildren(const G3MRenderContext* rc) {
  const size_t childrenIDsSize = _childrenIDs.size();
  if (childrenIDsSize == 0) {
    std::vector<Node*>* children = new std::vector<Node*>();
    parsedChildren(children);
    return;
  }
  
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("\"%s\": Downloading children for node \'%s\'",
  //                                 _vectorSet->getName().c_str(),
  //                                 _id.c_str());
  //  }
  
  _downloader = rc->getDownloader();
  _childrenRequestID = _downloader->requestBuffer(_vectorSet->getNodeChildrenURL(_id, _childrenIDs),
                                                  _vectorSet->getDownloadPriority(),
                                                  _vectorSet->getTimeToCache(),
                                                  _vectorSet->getReadExpired(),
                                                  new NodeChildrenDownloadListener(this,
                                                                                   rc->getThreadUtils(),
                                                                                   _verbose),
                                                  true);
}

void VectorStreamingRenderer::Node::unloadChildren() {
  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      Node* child = _children->at(i);
      child->unload();
      child->_release();
    }
    
    delete _children;
    _children = NULL;
    _childrenSize = 0;
  }
}

void VectorStreamingRenderer::Node::cancelTasks() {
  if (_featuresTask != NULL) {
    _featuresTask->cancel();
  }
  if (_childrenTask != NULL) {
    _childrenTask->cancel();
  }
}

void VectorStreamingRenderer::Node::cancelLoadChildren() {
  if (_childrenRequestID != -1) {
    _downloader->cancelRequest(_childrenRequestID);
    _childrenRequestID = -1;
  }
}

VectorStreamingRenderer::NodeAllMarksFilter::NodeAllMarksFilter(const Node* node) :
_nodeClusterToken( node->getClusterMarkToken() ),
_nodeFeatureToken( node->getFeatureMarkToken() )
{
}

bool VectorStreamingRenderer::NodeAllMarksFilter::test(const Mark* mark) const {
  const std::string token = mark->getToken();
  return ((token == _nodeClusterToken) ||
          (token == _nodeFeatureToken));
}

VectorStreamingRenderer::NodeClusterMarksFilter::NodeClusterMarksFilter(const Node* node) :
_nodeClusterToken( node->getClusterMarkToken() )
{
}

bool VectorStreamingRenderer::NodeClusterMarksFilter::test(const Mark* mark) const {
  const std::string token = mark->getToken();
  return (token == _nodeClusterToken);
}

void VectorStreamingRenderer::Node::removeMarks() {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("\"%s\": Removing marks",
  //                                 getFullName().c_str());
  //  }
  
  const size_t removed = _vectorSet->getMarksRenderer()->removeAllMarks(NodeAllMarksFilter(this),
                                                                        true, /* animated */
                                                                        true  /* deleteMarks */);
  
  if (_verbose && removed > 0) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Removed %ld marks",
                                 getFullName().c_str(),
                                 removed);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Removed %d marks",
                               getFullName(),
                               removed);
#endif
  }
}

bool VectorStreamingRenderer::Node::isVisible(const G3MRenderContext* rc,
                                              const VectorStreamingRenderer::VectorSet* vectorSet,
                                              const Frustum* frustumInModelCoordinates) {
  if ((_nodeSector->_deltaLatitude._degrees  >= vectorSet->_minSectorSize._degrees) ||
      (_nodeSector->_deltaLongitude._degrees >= vectorSet->_minSectorSize._degrees)) {
    return true;
  }
  
  return getBoundingVolume(rc)->touchesFrustum(frustumInModelCoordinates);
}

bool VectorStreamingRenderer::Node::isBigEnough(const G3MRenderContext *rc,
                                                const VectorStreamingRenderer::VectorSet* vectorSet                                                ) {
  if ((_nodeSector->_deltaLatitude._degrees  >= vectorSet->_minSectorSize._degrees) ||
      (_nodeSector->_deltaLongitude._degrees >= vectorSet->_minSectorSize._degrees)) {
    return true;
  }
  
  const double projectedArea = getBoundingVolume(rc)->projectedArea(rc);
  return (projectedArea >= vectorSet->_minProjectedArea);
}

void VectorStreamingRenderer::Node::unload() {
  cancelTasks();
  
  if (_loadingFeatures) {
    cancelLoadFeatures();
    _loadingFeatures = false;
  }
  
  if (_loadingChildren) {
    _loadingChildren = true;
    cancelLoadChildren();
  }
  
  if (_loadedFeatures) {
    unloadFeatures();
    _loadedFeatures = false;
  }
  
  unloadChildren();
  
  if (_parent != NULL) {
    _parent->childStopRendered();
  }
  
  removeMarks();
}

void VectorStreamingRenderer::Node::childRendered() {
  if (_clusters != NULL) {
    if (_clusters->size() > 0) {
      if (_clusterMarksCount > 0) {
        size_t removed = _vectorSet->getMarksRenderer()->removeAllMarks(NodeClusterMarksFilter(this),
                                                                        true, /* animated */
                                                                        true  /* deleteMarks */);
        
        _clusterMarksCount -= removed;
        
        if (_verbose && removed > 0) {
#ifdef C_CODE
          ILogger::instance()->logInfo("\"%s\": Removed %ld cluster-marks",
                                       getFullName().c_str(),
                                       removed);
#endif
#ifdef JAVA_CODE
          ILogger.instance().logInfo("\"%s\": Removed %d cluster-marks",
                                     getFullName(),
                                     removed);
#endif
        }
        
      }
    }
  }
}

void VectorStreamingRenderer::Node::childStopRendered() {
  if (_clusters != NULL) {
    if (_clusters->size() > 0) {
      if (_clusterMarksCount <= 0) {
        createClusterMarks();
        //Checking to ensure no children marks are drawn.
        if (_children != NULL && _children->size() > 0) {
          for (int i=0; i<_children->size(); i++) {
            Node *child = _children->at(i);
            if (child->_featureMarksCount > 0) {
              child->unload();
            }
          }
        }
      }
    }
  }
}

long long VectorStreamingRenderer::Node::render(const G3MRenderContext* rc,
                                                const VectorStreamingRenderer::VectorSet* vectorSet,
                                                const Frustum* frustumInModelCoordinates,
                                                const long long cameraTS,
                                                GLState* glState) {
  long long renderedCount = 0;

  bool wasRendered = false;
  
  const bool visible = isVisible(rc, vectorSet, frustumInModelCoordinates);
  if (visible) {
    const bool bigEnough = isBigEnough(rc, vectorSet);
    if (bigEnough) {
      wasRendered = true;
      if (_loadedFeatures) {
        renderedCount += _featureMarksCount + _clusterMarksCount;
        if (_parent != NULL) {
          _parent->childRendered();
        }
        // don't load nor render children until the features are loaded
        if (_children == NULL) {
          if (!_loadingChildren) {
            _loadingChildren = true;
            loadChildren(rc);
          }
        }
        if (_children != NULL) {
          for (size_t i = 0; i < _childrenSize; i++) {
            Node* child = _children->at(i);
            renderedCount += child->render(rc,
                                           vectorSet,
                                           frustumInModelCoordinates,
                                           cameraTS,
                                           glState);
          }
        }
      }
      else {
        if (!_loadingFeatures) {
          _loadingFeatures = true;
          loadFeatures(rc);
        }
      }
    }
  }
  
  if (_isBeingRendered != wasRendered) {
    if (_isBeingRendered) {
      unload();
    }
    _isBeingRendered = wasRendered;
  }
  
  return renderedCount;
}

Sector* VectorStreamingRenderer::GEOJSONUtils::parseSector(const JSONArray* json) {
  const double lowerLat = json->getAsNumber(0)->value();
  const double lowerLon = json->getAsNumber(1)->value();
  const double upperLat = json->getAsNumber(2)->value();
  const double upperLon = json->getAsNumber(3)->value();
  
  return new Sector(Geodetic2D::fromDegrees(lowerLat, lowerLon),
                    Geodetic2D::fromDegrees(upperLat, upperLon));
}

Geodetic2D* VectorStreamingRenderer::GEOJSONUtils::parseGeodetic2D(const JSONArray* json) {
  const double lon = json->getAsNumber(0)->value();
  const double lat = json->getAsNumber(1)->value();
  
  return new Geodetic2D(Angle::fromDegrees(lat), Angle::fromDegrees(lon));
}

VectorStreamingRenderer::Node* VectorStreamingRenderer::GEOJSONUtils::parseNode(const JSONObject* json,
                                                                                const VectorSet*  vectorSet,
                                                                                const bool        verbose) {
  const std::string id            = json->getAsString("id")->value();
  const Sector*     nodeSector    = GEOJSONUtils::parseSector( json->getAsArray("nodeSector") );
  const int         clustersCount = (int) json->getAsNumber("clustersCount", 0.0);
  const int         featuresCount = (int) json->getAsNumber("featuresCount", 0.0);
  
  std::vector<std::string> childrenIDs;
  std::vector<Node*>*      children = NULL;
  const JSONArray* childrenJSON = json->getAsArray("children");
  for (size_t i = 0; i < childrenJSON->size(); i++) {
    const JSONString* childID = childrenJSON->getAsString(i);
    if (childID != NULL) {
      childrenIDs.push_back( childID->value() );
    }
    else {
      const JSONObject* jsonChild = childrenJSON->getAsObject(i);
      if (jsonChild != NULL) {
        Node* child = parseNode(jsonChild,
                                vectorSet,
                                verbose);
        if (child != NULL) {
          if (children == NULL) {
            children = new std::vector<Node*>();
          }
          children->push_back(child);
        }
      }
    }
  }
  
  return new Node(vectorSet,
                  id,
                  nodeSector,
                  clustersCount,
                  featuresCount,
                  childrenIDs,
                  children,
                  verbose);
}

VectorStreamingRenderer::MetadataParserAsyncTask::~MetadataParserAsyncTask() {
  delete _buffer;
  
  delete _metadata;
  
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodes->size(); i++) {
      Node* node = _rootNodes->at(i);
      node->_release();
    }
    delete _rootNodes;
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::MetadataParserAsyncTask::runInBackground(const G3MContext* context) {
  
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);
  
  delete _buffer;
  _buffer = NULL;
  
  if (jsonBaseObject == NULL) {
    _parsingError = true;
    return;
  }
  
  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject == NULL) {
    _parsingError = true;
  }
  else {
    // check for errors
    const JSONString* errorCodeJSON = jsonObject->getAsString("errorCode");
    if (errorCodeJSON != NULL) {
      _parsingError = true;
      
      const std::string errorCode = errorCodeJSON->value();
      
      const JSONString* errorDescriptionJSON = jsonObject->getAsString("errorDescription");
      if (errorDescriptionJSON == NULL) {
        ILogger::instance()->logError("\"%s\": %s",
                                      _vectorSet->getName().c_str(),
                                      errorCode.c_str());
      }
      else {
        const std::string errorDescription = errorDescriptionJSON->value();
        ILogger::instance()->logError("\"%s\": %s (%s)",
                                      _vectorSet->getName().c_str(),
                                      errorCode.c_str(),
                                      errorDescription.c_str());
      }
    }
    else {
      const Sector*            sector            = GEOJSONUtils::parseSector( jsonObject->getAsArray("sector") );
      const long long          clustersCount     = (long long) jsonObject->getAsNumber("clustersCount", 0);
      const long long          featuresCount     = (long long) jsonObject->getAsNumber("featuresCount", 0);
      const int                nodesCount        = (int) jsonObject->getAsNumber("nodesCount")->value();
      const int                minNodeDepth      = (int) jsonObject->getAsNumber("minNodeDepth")->value();
      const int                maxNodeDepth      = (int) jsonObject->getAsNumber("maxNodeDepth")->value();
      const std::string        language          = jsonObject->getAsString("language")->value();
      const std::string        nameFieldName     = jsonObject->getAsString("nameFieldName")->value();
      const std::string        urlFieldName      = jsonObject->getAsString("urlFieldName")->value();
      const MagnitudeMetadata* magnitudeMetadata = MagnitudeMetadata::fromJSON( jsonObject->getAsObject("magnitude") );

      _metadata = new Metadata(sector,
                               clustersCount,
                               featuresCount,
                               nodesCount,
                               minNodeDepth,
                               maxNodeDepth,
                               language,
                               nameFieldName,
                               urlFieldName,
                               magnitudeMetadata);

      const JSONArray* rootNodesJSON = jsonObject->getAsArray("rootNodes");
      _rootNodes = new std::vector<Node*>();
      for (size_t i = 0; i < rootNodesJSON->size(); i++) {
        Node* node = GEOJSONUtils::parseNode(rootNodesJSON->getAsObject(i),
                                             _vectorSet,
                                             _verbose);
        _rootNodes->push_back(node);
      }
    }
  }
  
  delete jsonBaseObject;
}

void VectorStreamingRenderer::MetadataParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_parsingError) {
    _vectorSet->errorParsingMetadata();
  }
  else {
    _vectorSet->parsedMetadata(_metadata,
                               _rootNodes);
    _metadata  = NULL; // moved ownership to _vectorSet
    _rootNodes = NULL; // moved ownership to _vectorSet
  }
}

void VectorStreamingRenderer::MetadataDownloadListener::onDownload(const URL& url,
                                                                   IByteBuffer* buffer,
                                                                   bool expired) {
  if (_verbose) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Downloaded metadata (bytes=%ld)",
                                 _vectorSet->getName().c_str(),
                                 buffer->size());
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Downloaded metadata (bytes=%d)",
                               _vectorSet.getName(),
                               buffer.size());
#endif
  }
  
  _threadUtils->invokeAsyncTask(new MetadataParserAsyncTask(_vectorSet, _verbose, buffer),
                                true);
}

void VectorStreamingRenderer::MetadataDownloadListener::onError(const URL& url) {
  _vectorSet->errorDownloadingMetadata();
}

void VectorStreamingRenderer::MetadataDownloadListener::onCancel(const URL& url) {
  // do nothing
}

void VectorStreamingRenderer::MetadataDownloadListener::onCanceledDownload(const URL& url,
                                                                           IByteBuffer* buffer,
                                                                           bool expired) {
  // do nothing
}

const VectorStreamingRenderer::MagnitudeMetadata* VectorStreamingRenderer::MagnitudeMetadata::fromJSON(const JSONObject* jsonObject) {
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string name    = jsonObject->getAsString("name")->value();
  const double      min     = jsonObject->getAsNumber("min")->value();
  const double      max     = jsonObject->getAsNumber("max")->value();
  const double      average = jsonObject->getAsNumber("average")->value();

  return new MagnitudeMetadata(name,
                               min,
                               max,
                               average);
}

VectorStreamingRenderer::Metadata::~Metadata() {
  delete _sector;
#ifdef C_CODE
  delete _magnitudeMetadata;
#endif
}

void VectorStreamingRenderer::VectorSet::errorDownloadingMetadata() {
  _downloadingMetadata = false;
  _errorDownloadingMetadata = true;
}

void VectorStreamingRenderer::VectorSet::errorParsingMetadata() {
  _downloadingMetadata = false;
  _errorParsingMetadata = true;
}

VectorStreamingRenderer::VectorSet::~VectorSet() {
  if (_deleteSymbolizer) {
    delete _symbolizer;
  }
  
  delete _metadata;
  
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesSize; i++) {
      Node* node = _rootNodes->at(i);
      node->unload();
      node->_release();
    }
    delete _rootNodes;
  }
}

void VectorStreamingRenderer::VectorSet::parsedMetadata(Metadata* metadata,
                                                        std::vector<Node*>* rootNodes) {
  _downloadingMetadata = false;

  _metadata      = metadata;
  _rootNodes     = rootNodes;
  _rootNodesSize = _rootNodes->size();
  
  if (_verbose) {
    ILogger::instance()->logInfo("\"%s\": Metadata",         _name.c_str());
    ILogger::instance()->logInfo("   Sector        : %s",    _metadata->_sector->description().c_str());
#ifdef C_CODE
    ILogger::instance()->logInfo("   Clusters Count: %ld",   _metadata->_clustersCount);
    ILogger::instance()->logInfo("   Features Count: %ld",   _metadata->_featuresCount);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("   Clusters Count: %d",   _metadata._clustersCount);
    ILogger.instance().logInfo("   Features Count: %d",   _metadata._featuresCount);
#endif
    ILogger::instance()->logInfo("   Nodes Count   : %d",    _metadata->_nodesCount);
    ILogger::instance()->logInfo("   Depth         : %d/%d", _metadata->_minNodeDepth, _metadata->_maxNodeDepth);
    ILogger::instance()->logInfo("   Root Nodes    : %d",    _rootNodesSize);
  }
  
}

const URL VectorStreamingRenderer::VectorSet::getMetadataURL() const {
  if (_format == VectorStreamingRenderer::Format::SERVER) {
    return URL(_serverURL, _name);
  }
  return URL(_serverURL, _name + "/metadata.json");
}

const std::string VectorStreamingRenderer::VectorSet::toNodesDirectories(const std::string& nodeID) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  const IStringUtils* su = IStringUtils::instance();
  isb->addString( "nodes/" );
  const size_t length = nodeID.length();
  for (size_t i = 0; i < length; i++) {
    const std::string c = su->substring(nodeID, i, i+1);
    isb->addString(c);
    isb->addString("/");
  }
  const std::string result = isb->getString();
  delete isb;
  return result;
}

const URL VectorStreamingRenderer::VectorSet::getNodeFeaturesURL(const std::string& nodeID) const {
  if (_format == VectorStreamingRenderer::Format::SERVER) {
    return URL(_serverURL,
               _name + "/features" +
               "?node=" + nodeID +
               "&properties=" + _properties,
               true);
  }
  return URL(_serverURL,
             _name + "/" + toNodesDirectories(nodeID) + "/features.json");
}

const URL VectorStreamingRenderer::VectorSet::getNodeChildrenURL(const std::string& nodeID,
                                                                 const std::vector<std::string>& childrenIDs) const {
  if (_format == VectorStreamingRenderer::Format::SERVER) {
    std::string nodes = "";
    const size_t childrenIDsSize = childrenIDs.size();
    
    for (size_t i = 0; i < childrenIDsSize; i++) {
      if (i > 0) {
        nodes += "|";
      }
#ifdef C_CODE
      nodes += childrenIDs[i];
#endif
#ifdef JAVA_CODE
      nodes += childrenIDs.get(i);
#endif
    }
    
    return URL(_serverURL,
               _name +
               "?nodes=" + nodes,
               true);
    
  }
  return URL(_serverURL,
             _name + "/" + toNodesDirectories(nodeID) + "/children.json");
}


void VectorStreamingRenderer::VectorSet::initialize(const G3MContext* context) {
  _downloadingMetadata = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata = false;
  
  if (_verbose) {
    ILogger::instance()->logInfo("\"%s\": Downloading metadata", _name.c_str());
  }
  
  context->getDownloader()->requestBuffer(getMetadataURL(),
                                          _downloadPriority,
                                          _timeToCache,
                                          _readExpired,
                                          new MetadataDownloadListener(this,
                                                                       context->getThreadUtils(),
                                                                       _verbose),
                                          true);
}

RenderState VectorStreamingRenderer::VectorSet::getRenderState(const G3MRenderContext* rc) {
  if (_haltOnError) {
    if (_downloadingMetadata) {
      return RenderState::busy();
    }
    
    if (_errorDownloadingMetadata) {
      return RenderState::error("Error downloading metadata of \"" + _name + "\" from \"" + _serverURL._path + "\"");
    }
    
    if (_errorParsingMetadata) {
      return RenderState::error("Error parsing metadata of \"" + _name + "\" from \"" + _serverURL._path + "\"");
    }
  }
  
  return RenderState::ready();
}

void VectorStreamingRenderer::VectorSet::render(const G3MRenderContext* rc,
                                                const Frustum* frustumInModelCoordinates,
                                                const long long cameraTS,
                                                GLState* glState) {
  if (_rootNodesSize > 0) {
    long long renderedCount = 0;
    for (size_t i = 0; i < _rootNodesSize; i++) {
      Node* rootNode = _rootNodes->at(i);
      renderedCount += rootNode->render(rc,
                                        this,
                                        frustumInModelCoordinates,
                                        cameraTS,
                                        glState);
    }
    
    if (_lastRenderedCount != renderedCount) {
      if (_verbose) {
#ifdef C_CODE
        ILogger::instance()->logInfo("\"%s\": Rendered %ld features", _name.c_str(), renderedCount);
#endif
#ifdef JAVA_CODE
        ILogger.instance().logInfo("\"%s\": Rendered %d features", _name, renderedCount);
#endif
      }
      _lastRenderedCount = renderedCount;
    }
    
  }
}

long long VectorStreamingRenderer::VectorSet::createClusterMarks(const Node* node,
                                                                 const std::vector<Cluster*>* clusters) const {
  long long counter = 0;
  if (clusters != NULL) {
    const size_t clustersCount = clusters->size();
    for (size_t i = 0; i < clustersCount; i++) {
      const Cluster* cluster = clusters->at(i);
      if (cluster != NULL) {
        Mark* mark = _symbolizer->createClusterMark(_metadata,
                                                    node,
                                                    cluster);
        if (mark != NULL) {
          mark->setToken( node->getClusterMarkToken() );
          _renderer->getMarkRenderer()->addMark( mark );
          counter++;
        }
      }
    }
  }
  
  return counter;
}


long long VectorStreamingRenderer::VectorSet::createFeatureMark(const Node* node,
                                                                const GEO2DPointGeometry* geometry) const {
  Mark* mark = _symbolizer->createFeatureMark(_metadata,
                                              node,
                                              geometry);
  if (mark == NULL) {
    return 0;
  }
  
  mark->setToken( node->getFeatureMarkToken() );
  _renderer->getMarkRenderer()->addMark( mark );
  
  return 1;
}

long long VectorStreamingRenderer::VectorSet::createFeatureMark(const Node* node,
                                                                const GEO3DPointGeometry* geometry) const {
  Mark* mark = _symbolizer->createFeatureMark(_metadata,
                                              node,
                                              geometry);
  if (mark == NULL) {
    return 0;
  }

  mark->setToken( node->getFeatureMarkToken() );
  _renderer->getMarkRenderer()->addMark( mark );

  return 1;
}

VectorStreamingRenderer::VectorStreamingRenderer(MarksRenderer* markRenderer) :
_markRenderer(markRenderer),
_vectorSetsSize(0),
_glState(new GLState())
{
}

VectorStreamingRenderer::~VectorStreamingRenderer() {
  for (size_t i = 0; i < _vectorSetsSize; i++) {
    VectorSet* vectorSet = _vectorSets[i];
    delete vectorSet;
  }
  
  _glState->_release();
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::removeAllVectorSets() {
  for (size_t i = 0; i < _vectorSetsSize; i++) {
    VectorSet* vectorSet = _vectorSets[i];
    delete vectorSet;
  }
  _vectorSets.clear();
  _vectorSetsSize = 0;
}

void VectorStreamingRenderer::onChangedContext() {
  for (size_t i = 0; i < _vectorSetsSize; i++) {
    VectorSet* vectorSet = _vectorSets[i];
    vectorSet->initialize(_context);
  }
}

RenderState VectorStreamingRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;
  
  for (size_t i = 0; i < _vectorSetsSize; i++) {
    VectorSet* vectorSet = _vectorSets[i];
    const RenderState childRenderState = vectorSet->getRenderState(rc);
    
    const RenderState_Type childRenderStateType = childRenderState._type;
    
    if (childRenderStateType == RENDER_ERROR) {
      errorFlag = true;
      
      const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
      _errors.insert(_errors.end(),
                     childErrors.begin(),
                     childErrors.end());
#endif
#ifdef JAVA_CODE
      _errors.addAll(childErrors);
#endif
    }
    else if (childRenderStateType == RENDER_BUSY) {
      busyFlag = true;
    }
  }
  
  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag) {
    return RenderState::busy();
  }
  else {
    return RenderState::ready();
  }
}

void VectorStreamingRenderer::addVectorSet(const URL&                 serverURL,
                                           const std::string&         name,
                                           const std::string&         properties,
                                           const VectorSetSymbolizer* symbolizer,
                                           const bool                 deleteSymbolizer,
                                           long long                  downloadPriority,
                                           const TimeInterval&        timeToCache,
                                           bool                       readExpired,
                                           bool                       verbose,
                                           bool                       haltOnError,
                                           const Format               format,
                                           const Angle&               minSectorSize,
                                           const double               minProjectedArea) {
  VectorSet* vectorSet = new VectorSet(this,
                                       serverURL,
                                       name,
                                       properties,
                                       symbolizer,
                                       deleteSymbolizer,
                                       downloadPriority,
                                       timeToCache,
                                       readExpired,
                                       verbose,
                                       haltOnError,
                                       format,
                                       minSectorSize,
                                       minProjectedArea);
  if (_context != NULL) {
    vectorSet->initialize(_context);
  }
  _vectorSets.push_back(vectorSet);
  _vectorSetsSize = _vectorSets.size();
}

void VectorStreamingRenderer::updateGLState(const Camera* camera) {
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }
}

void VectorStreamingRenderer::render(const G3MRenderContext* rc,
                                     GLState* glState) {
  if (_vectorSetsSize > 0) {
    const Camera* camera = rc->getCurrentCamera();
    const Frustum* frustumInModelCoordinates = camera->getFrustumInModelCoordinates();
    
    const long long cameraTS = camera->getTimestamp();
    
    updateGLState(camera);
    _glState->setParent(glState);
    
    for (size_t i = 0; i < _vectorSetsSize; i++) {
      VectorSet* vectorSector = _vectorSets[i];
      vectorSector->render(rc,
                           frustumInModelCoordinates,
                           cameraTS,
                           _glState);
    }
  }
}

