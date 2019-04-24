//
//  VectorStreamingRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/30/15.
//
//

#include "VectorStreamingRenderer.hpp"

#include "Geodetic2D.hpp"
#include "IJSONParser.hpp"
#include "JSONArray.hpp"
#include "IThreadUtils.hpp"
#include "GEOObject.hpp"
#include "JSONObject.hpp"
#include "JSONNumber.hpp"
#include "GEOJSONParser.hpp"
#include "ErrorHandling.hpp"
#include "Sector.hpp"
#include "BoundingVolume.hpp"
#include "G3MRenderContext.hpp"
#include "Vector3D.hpp"
#include "Planet.hpp"
#include "Sphere.hpp"
#include "IDownloader.hpp"
#include "Mark.hpp"
#include "MarksRenderer.hpp"
#include "JSONString.hpp"
#include "Camera.hpp"
#include "GEOMeshes.hpp"
#include "G3MMeshParser.hpp"
#include "Mesh.hpp"
#include "MeshRenderer.hpp"


VectorStreamingRenderer::Cluster::~Cluster() {
  delete _position;
}

VectorStreamingRenderer::ChildrenParserAsyncTask::~ChildrenParserAsyncTask() {
  _node->_childrenTask = NULL;
  _node->_release();
  
  delete _buffer;
  
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

void VectorStreamingRenderer::ChildrenParserAsyncTask::runInBackground(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  const VectorSet* vectorSet = _node->getVectorSetOrNULL();
  if (vectorSet == NULL) {
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
        _children->push_back( GEOJSONUtils::parseNode(nodeJSON, vectorSet, _verbose) );
      }
    }
    
    delete jsonBaseObject;
  }
}

void VectorStreamingRenderer::ChildrenParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  const VectorSet* vectorSet = _node->getVectorSetOrNULL();
  if (vectorSet == NULL) {
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

std::vector<VectorStreamingRenderer::Cluster*>* VectorStreamingRenderer::FeaturesParserAsyncTask::parseClusters(const JSONArray* jsonArray) {
  if ((jsonArray == NULL) || (jsonArray->size() == 0)) {
    return NULL;
  }
  
  std::vector<VectorStreamingRenderer::Cluster*>* clusters = new std::vector<VectorStreamingRenderer::Cluster*>();
  const size_t clustersCount = jsonArray->size();
  for (size_t i = 0; i < clustersCount; i++) {
    const JSONObject* clusterJson = jsonArray->getAsObject(i);
    const Geodetic2D* position    = GEOJSONUtils::parseGeodetic2D( clusterJson->getAsArray("position") );
    const long long   size        = (long long) clusterJson->getAsNumber("size")->value();
    
    clusters->push_back( new Cluster(position, size) );
  }
  
  return clusters;
}

std::vector<VectorStreamingRenderer::Node*>* VectorStreamingRenderer::FeaturesParserAsyncTask::parseChildren(const JSONArray* jsonArray,
                                                                                                             const VectorSet* vectorSet) {
  if (_isCanceled || (jsonArray == NULL) || (jsonArray->size() == 0)) {
    return NULL;
  }
  
  std::vector<Node*>* result = new std::vector<Node*>();
  for (size_t i = 0; i < jsonArray->size(); i++) {
    const JSONObject* nodeJSON = jsonArray->getAsObject(i);
    result->push_back( GEOJSONUtils::parseNode(nodeJSON, vectorSet, _verbose) );
  }
  
  return result;
}

GEOMeshes* VectorStreamingRenderer::FeaturesParserAsyncTask::parseMeshes(const JSONObject* jsonObject,
                                                                         const Planet* planet) {
  if (jsonObject == NULL) {
    return NULL;
  }

  std::vector<Mesh*> meshes = G3MMeshParser::parse(jsonObject, planet);
  if (meshes.empty()) {
    return NULL;
  }

  return new GEOMeshes(meshes);
}

GEOObject* VectorStreamingRenderer::FeaturesParserAsyncTask::parseFeatures(const JSONObject* jsonObject,
                                                                           const Planet* planet) {
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string type = jsonObject->getAsString("type", "");
  if (type == "MeshCollection") {
    return parseMeshes( jsonObject->getAsObject("meshes"), planet );
  }

  return GEOJSONParser::parse(jsonObject, _verbose);
}

void VectorStreamingRenderer::FeaturesParserAsyncTask::runInBackground(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  const VectorSet* vectorSet = _node->getVectorSetOrNULL();
  if (vectorSet == NULL) {
    return;
  }

  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);
  
  delete _buffer;
  _buffer = NULL;
  
  if (jsonBaseObject != NULL) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    
    _clusters = parseClusters( jsonObject->getAsArray("clusters") );
    _features = parseFeatures( jsonObject->getAsObject("features"), context->getPlanet() );
    _children = parseChildren( jsonObject->getAsArray("children"), vectorSet );
    
    delete jsonBaseObject;
  }
}

void VectorStreamingRenderer::FeaturesParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_isCanceled) {
    return;
  }
  const VectorSet* vectorSet = _node->getVectorSetOrNULL();
  if (vectorSet == NULL) {
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

  const VectorSet* vectorSet = _node->getVectorSetOrNULL();
  if (vectorSet == NULL) {
    delete buffer;
  }
  else {
    _node->_featuresTask = new FeaturesParserAsyncTask(_node, _verbose, buffer);
    _threadUtils->invokeAsyncTask(_node->_featuresTask, true);
  }
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
                                    const double                    minHeight,
                                    const double                    maxHeight,
                                    const int                       clustersCount,
                                    const int                       featuresCount,
                                    const std::vector<std::string>& childrenIDs,
                                    std::vector<Node*>*             children,
                                    const bool                      verbose) :
_parent(NULL),
_vectorSetOrNULL(vectorSet),
_vectorSetName( vectorSet->getName() ),
_id(id),
_nodeSector(nodeSector),
_minHeight(minHeight),
_maxHeight(maxHeight),
_clustersCount(clustersCount),
_featuresCount(featuresCount),
_childrenIDs(childrenIDs),
_verbose(verbose),
_loadedFeatures(false),
_loadingFeatures(false),
_children(children),
_childrenSize((children == NULL) ? 0 : children->size()),
_loadingChildren(false),
_isBeingRendered(false),
_boundingSphere(NULL),
_featuresRequestID(-1),
_childrenRequestID(-1),
_downloader(NULL),
_clusters(NULL),
_clusterSymbolsCount(0),
_featureSymbolsCount(0),
_childrenTask(NULL),
_featuresTask(NULL)
{
  setChildren(children);

  _vectorSetOrNULL->_retain();
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

  if (_clusters != NULL) {
    for (size_t i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
  }
  
  delete _nodeSector;
  delete _boundingSphere;
  
  if (_parent != NULL) {
    _parent->_release();
  }

  if (_vectorSetOrNULL != NULL) {
    _vectorSetOrNULL->_release();
    _vectorSetOrNULL = NULL;
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
  
  _featureSymbolsCount = 0;

  if (features != NULL) {
    if (_vectorSetOrNULL != NULL) {
      _featureSymbolsCount = features->symbolize(_vectorSetOrNULL, this);
    }
    delete features;

    if (_verbose && (_featureSymbolsCount > 0)) {
#ifdef C_CODE
      ILogger::instance()->logInfo("\"%s\": Created %ld feature-symbols",
                                   getFullName().c_str(),
                                   _featureSymbolsCount);
#endif
#ifdef JAVA_CODE
      ILogger.instance().logInfo("\"%s\": Created %d feature-symbols",
                                 getFullName(),
                                 _featureSymbolsCount);
#endif
    }
  }
  
  if (_clusters != NULL) {
    for (size_t i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
    _clusters = NULL;
    _clusterSymbolsCount = 0;
  }
  
  if (clusters != NULL) {
    _clusters = clusters;
    createClusterMarks();
  }
}

void VectorStreamingRenderer::Node::createClusterMarks() {
  if (_vectorSetOrNULL != NULL) {
    _clusterSymbolsCount = _vectorSetOrNULL->symbolizeClusters(this, _clusters);
  }
  
  if (_verbose && (_clusterSymbolsCount > 0)) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Created %ld cluster-symbols",
                                 getFullName().c_str(),
                                 _clusterSymbolsCount);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Created %d cluster-symbols",
                               getFullName(),
                               _clusterSymbolsCount);
#endif
  }
}


BoundingVolume* VectorStreamingRenderer::Node::getBoundingVolume(const G3MRenderContext *rc,
                                                                 const VectorStreamingRenderer::VectorSet* vectorSet) {
  if (_boundingSphere == NULL) {
    const Planet* planet = rc->getPlanet();

    const IMathUtils* mu = IMathUtils::instance();
    double minHeight;
    if (ISNAN(vectorSet->_minHeight)) {
      if (ISNAN(_minHeight)) {
        minHeight = 0;
      }
      else {
        minHeight = _minHeight;
      }
    }
    else {
      if (ISNAN(_minHeight)) {
        minHeight = vectorSet->_minHeight;
      }
      else {
        minHeight = mu->min(vectorSet->_minHeight, _minHeight);
      }
    }

    double maxHeight;
    if (ISNAN(vectorSet->_maxHeight)) {
      if (ISNAN(_maxHeight)) {
        maxHeight = 0;
      }
      else {
        maxHeight = _maxHeight;
      }
    }
    else {
      if (ISNAN(_maxHeight)) {
        maxHeight = vectorSet->_maxHeight;
      }
      else {
        maxHeight = mu->max(vectorSet->_maxHeight, _maxHeight);
      }
    }

#ifdef C_CODE
    const Vector3D c[10] = {
      planet->toCartesian( _nodeSector->getNE()    , minHeight ),
      planet->toCartesian( _nodeSector->getNE()    , maxHeight ),
      planet->toCartesian( _nodeSector->getNW()    , minHeight ),
      planet->toCartesian( _nodeSector->getNW()    , maxHeight ),
      planet->toCartesian( _nodeSector->getSE()    , minHeight ),
      planet->toCartesian( _nodeSector->getSE()    , maxHeight ),
      planet->toCartesian( _nodeSector->getSW()    , minHeight ),
      planet->toCartesian( _nodeSector->getSW()    , maxHeight ),
      planet->toCartesian( _nodeSector->getCenter(), minHeight ),
      planet->toCartesian( _nodeSector->getCenter(), maxHeight )
    };
    
    std::vector<Vector3D> points(c, c+10);
#endif
#ifdef JAVA_CODE
    java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(10);
    points.add( planet.toCartesian( _nodeSector.getNE()    , minHeight ) );
    points.add( planet.toCartesian( _nodeSector.getNE()    , maxHeight ) );
    points.add( planet.toCartesian( _nodeSector.getNW()    , minHeight ) );
    points.add( planet.toCartesian( _nodeSector.getNW()    , maxHeight ) );
    points.add( planet.toCartesian( _nodeSector.getSE()    , minHeight ) );
    points.add( planet.toCartesian( _nodeSector.getSE()    , maxHeight ) );
    points.add( planet.toCartesian( _nodeSector.getSW()    , minHeight ) );
    points.add( planet.toCartesian( _nodeSector.getSW()    , maxHeight ) );
    points.add( planet.toCartesian( _nodeSector.getCenter(), minHeight ) );
    points.add( planet.toCartesian( _nodeSector.getCenter(), maxHeight ) );
#endif
    
    _boundingSphere = Sphere::enclosingSphere(points, 0.1);

    if (_parent) {
      _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
    }
  }

  return _boundingSphere;
}

void VectorStreamingRenderer::Node::updateBoundingSphereWith(const G3MRenderContext *rc,
                                                             const VectorStreamingRenderer::VectorSet* vectorSet,
                                                             Sphere* childSphere) {
  getBoundingVolume(rc, vectorSet); // force _boundingSphere creation
  if ((_boundingSphere == NULL) || childSphere->fullContainedInSphere(_boundingSphere) ) {
    return;
  }

  Sphere* old = _boundingSphere;
  _boundingSphere = _boundingSphere->mergedWithSphere(childSphere, 0.1);
  delete old;
  if (_parent) {
    _parent->updateBoundingSphereWith(rc, vectorSet, _boundingSphere);
  }
}

void VectorStreamingRenderer::Node::loadFeatures(const G3MRenderContext* rc) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("\"%s\": Downloading features for node \'%s\'",
  //                                 _vectorSet->getName().c_str(),
  //                                 _id.c_str());
  //  }

  if (_vectorSetOrNULL == NULL) {
    return;
  }
  
  _downloader = rc->getDownloader();
  const long long depthPriority = 100 * getDepth();
  _featuresRequestID = _downloader->requestBuffer(_vectorSetOrNULL->getNodeFeaturesURL(_id),
                                                  _vectorSetOrNULL->getDownloadPriority() + depthPriority + _featuresCount + _clustersCount,
                                                  _vectorSetOrNULL->getTimeToCache(),
                                                  _vectorSetOrNULL->getReadExpired(),
                                                  new NodeFeaturesDownloadListener(this,
                                                                                   rc->getThreadUtils(),
                                                                                   _verbose),
                                                  true);
}

void VectorStreamingRenderer::Node::unloadFeatures() {
  _loadedFeatures  = false;
  _loadingFeatures = false;

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
  if (_vectorSetOrNULL == NULL) {
    return;
  }

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
  const long long depthPriority = 100 * getDepth();
  _childrenRequestID = _downloader->requestBuffer(_vectorSetOrNULL->getNodeChildrenURL(_id, _childrenIDs),
                                                  _vectorSetOrNULL->getDownloadPriority() + depthPriority,
                                                  _vectorSetOrNULL->getTimeToCache(),
                                                  _vectorSetOrNULL->getReadExpired(),
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
    _featuresTask = NULL;
  }
  if (_childrenTask != NULL) {
    _childrenTask->cancel();
    _childrenTask = NULL;
  }
}

void VectorStreamingRenderer::Node::cancelLoadChildren() {
  if (_childrenRequestID != -1) {
    _downloader->cancelRequest(_childrenRequestID);
    _childrenRequestID = -1;
  }
}

VectorStreamingRenderer::NodeAllMarkFilter::NodeAllMarkFilter(const Node* node) :
_clusterToken( node->getClusterToken() ),
_featureToken( node->getFeatureToken() )
{
}

bool VectorStreamingRenderer::NodeAllMarkFilter::test(const Mark* mark) const {
  const std::string token = mark->getToken();
  return ((token == _clusterToken) || (token == _featureToken));
}

VectorStreamingRenderer::NodeAllMeshFilter::NodeAllMeshFilter(const Node* node) :
_featureToken( node->getFeatureToken() )
{
}

bool VectorStreamingRenderer::NodeAllMeshFilter::test(const Mesh* mesh) const {
  const std::string token = mesh->getToken();
  return (token == _featureToken);
}

VectorStreamingRenderer::NodeClusterMarkFilter::NodeClusterMarkFilter(const Node* node) :
_clusterToken( node->getClusterToken() )
{
}

bool VectorStreamingRenderer::NodeClusterMarkFilter::test(const Mark* mark) const {
  const std::string token = mark->getToken();
  return (token == _clusterToken);
}

void VectorStreamingRenderer::Node::removeFeaturesSymbols() {
  size_t removed = 0;

  if (_vectorSetOrNULL == NULL) {
    return;
  }

  MarksRenderer* marksRenderer = _vectorSetOrNULL->getMarksRenderer();
  if (marksRenderer != NULL) {
    removed += marksRenderer->removeAllMarks(NodeAllMarkFilter(this),
                                             true, /* animated */
                                             true  /* deleteMarks */);
  }

  MeshRenderer* meshRenderer = _vectorSetOrNULL->getMeshRenderer();
  if (meshRenderer != NULL) {
    removed += meshRenderer->removeAllMeshes(NodeAllMeshFilter(this),
                                             true /* deleteMeshes */);
  }

  if (_verbose && (removed > 0)) {
#ifdef C_CODE
    ILogger::instance()->logInfo("\"%s\": Removed %ld features symbols",
                                 getFullName().c_str(),
                                 removed);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("\"%s\": Removed %d features symbols",
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
  
  return getBoundingVolume(rc, vectorSet)->touchesFrustum(frustumInModelCoordinates);
}

bool VectorStreamingRenderer::Node::isBigEnough(const G3MRenderContext *rc,
                                                const VectorStreamingRenderer::VectorSet* vectorSet) {
  if ((_nodeSector->_deltaLatitude._degrees  >= vectorSet->_minSectorSize._degrees) ||
      (_nodeSector->_deltaLongitude._degrees >= vectorSet->_minSectorSize._degrees)) {
    return true;
  }
  
  const double projectedArea = getBoundingVolume(rc, vectorSet)->projectedArea(rc);
  return (projectedArea >= vectorSet->_minProjectedArea);
}

void VectorStreamingRenderer::Node::unload() {
  cancelTasks();
  
  if (_loadingFeatures) {
    _loadingFeatures = false;
    cancelLoadFeatures();
  }
  
  if (_loadingChildren) {
    _loadingChildren = false;
    cancelLoadChildren();
  }
  
  if (_loadedFeatures) {
    _loadedFeatures = false;
    unloadFeatures();
  }

  removeFeaturesSymbols();

  unloadChildren();
  
  if (_parent != NULL) {
    _parent->childStopRendered();
  }
}

void VectorStreamingRenderer::Node::childRendered() {
  if (_vectorSetOrNULL == NULL) {
    return;
  }

  if (_clusters != NULL) {
    if (_clusters->size() > 0) {
      if (_clusterSymbolsCount > 0) {
        size_t removed = 0;
        MarksRenderer* marksRenderer = _vectorSetOrNULL->getMarksRenderer();
        if (marksRenderer != NULL) {
          removed = marksRenderer->removeAllMarks(NodeClusterMarkFilter(this),
                                                  true, /* animated */
                                                  true  /* deleteMarks */);
        }
        _clusterSymbolsCount -= removed;
        
        if (_verbose && (removed > 0)) {
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
      if (_clusterSymbolsCount <= 0) {
        createClusterMarks();
        // Checking to ensure no children marks are drawn.
        if ((_children != NULL) && (_children->size() > 0)) {
          for (int i = 0; i < _children->size(); i++) {
            Node* child = _children->at(i);
            if (child->_featureSymbolsCount > 0) {
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
                                                GLState* glState) {
  long long renderedCount = 0;

  bool wasRendered = false;
  
  const bool visible = isVisible(rc, vectorSet, frustumInModelCoordinates);
  if (visible) {
    const bool bigEnough = isBigEnough(rc, vectorSet);
    if (bigEnough) {
      wasRendered = true;
      if (_loadedFeatures) {
        renderedCount += _featureSymbolsCount + _clusterSymbolsCount;
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
  const std::string id         = json->getAsString("id")->value();
  const Sector*     nodeSector = GEOJSONUtils::parseSector( json->getAsArray("nodeSector") );

  const double minHeight = json->getAsNumber("minHeight", NAND);
  const double maxHeight = json->getAsNumber("maxHeight", NAND);

  const int clustersCount = (int) json->getAsNumber("clustersCount", 0.0);
  const int featuresCount = (int) json->getAsNumber("featuresCount", 0.0);

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
                  minHeight,
                  maxHeight,
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

  _vectorSet->_release();

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
      const std::string        urlFieldName      = jsonObject->getAsString("urlFieldName", "");
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
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesSize; i++) {
      Node* node = _rootNodes->at(i);
      node->unload();
      node->cancel();
      node->_release();
    }
    delete _rootNodes;
  }

  if (_deleteSymbolizer) {
    delete _symbolizer;
  }

  delete _metadata;
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
                                                GLState* glState) {
  if (_rootNodesSize > 0) {
    long long renderedCount = 0;
    for (size_t i = 0; i < _rootNodesSize; i++) {
      Node* rootNode = _rootNodes->at(i);
      renderedCount += rootNode->render(rc,
                                        this,
                                        frustumInModelCoordinates,
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

int VectorStreamingRenderer::VectorSet::symbolizeClusters(const Node* node,
                                                          const std::vector<Cluster*>* clusters) const {
  int counter = 0;
  if (clusters != NULL) {
    const size_t clustersCount = clusters->size();
    for (size_t i = 0; i < clustersCount; i++) {
      const Cluster* cluster = clusters->at(i);
      if (cluster != NULL) {
        Mark* mark = _symbolizer->createClusterMark(_metadata,
                                                    node,
                                                    cluster);
        if (mark != NULL) {
          mark->setToken( node->getClusterToken() );
          getMarksRenderer()->addMark( mark );
          counter++;
        }
      }
    }
  }
  
  return counter;
}

int VectorStreamingRenderer::VectorSet::symbolizeMeshes(const Node* node,
                                                        const std::vector<Mesh*>& meshes) const {
  int count = 0;
  
  for (size_t i = 0; i < meshes.size(); i++) {
    Mesh* mesh = meshes[i];
    if (mesh != NULL) {
      count++;
      mesh->setToken( node->getFeatureToken() );
      getMeshRenderer()->addMesh( mesh );
    }
  }

  return count;
}

int VectorStreamingRenderer::VectorSet::symbolizeGeometry(const Node* node,
                                                          const GEO2DPointGeometry* geometry) const {
  int count = 0;

  {
    Mark* mark = _symbolizer->createGeometryMark(_metadata,
                                                 node,
                                                 geometry);
    if (mark != NULL) {
      count++;
      mark->setToken( node->getFeatureToken() );
      getMarksRenderer()->addMark( mark );
    }
  }
  
  return count;
}

int VectorStreamingRenderer::VectorSet::symbolizeGeometry(const Node* node,
                                                          const GEO3DPointGeometry* geometry) const {
  int count = 0;

  {
    Mark* mark = _symbolizer->createGeometryMark(_metadata,
                                                 node,
                                                 geometry);
    if (mark != NULL) {
      count++;
      mark->setToken( node->getFeatureToken() );
      getMarksRenderer()->addMark( mark );
    }
  }

  return count;
}

VectorStreamingRenderer::VectorStreamingRenderer(MarksRenderer* marksRenderer,
                                                 MeshRenderer*  meshRenderer) :
_marksRenderer(marksRenderer),
_meshRenderer(meshRenderer),
_vectorSetsSize(0),
_glState(new GLState())
{
}

VectorStreamingRenderer::~VectorStreamingRenderer() {
  removeAllVectorSets();
  
  _glState->_release();
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::removeAllVectorSets() {
  for (size_t i = 0; i < _vectorSetsSize; i++) {
    VectorSet* vectorSet = _vectorSets[i];
    vectorSet->cancel();
    vectorSet->_release();
  }
  _vectorSets.clear();
  _vectorSetsSize = _vectorSets.size();
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
                                           const double               minProjectedArea,
                                           const double               minHeight,
                                           const double               maxHeight) {
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
                                       minProjectedArea,
                                       minHeight,
                                       maxHeight);
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

    updateGLState(camera);
    _glState->setParent(glState);
    
    for (size_t i = 0; i < _vectorSetsSize; i++) {
      VectorSet* vectorSector = _vectorSets[i];
      vectorSector->render(rc,
                           frustumInModelCoordinates,
                           _glState);
    }
  }
}

void VectorStreamingRenderer::Node::cancel() {
  cancelTasks();

  if (_children != NULL) {
    for (size_t i = 0; i < _children->size(); i++) {
      Node* child = _children->at(i);
      child->cancel();
    }
  }

  removeFeaturesSymbols();

  if (_vectorSetOrNULL != NULL) {
    _vectorSetOrNULL->_release();
    _vectorSetOrNULL = NULL;
  }
}

void VectorStreamingRenderer::VectorSet::cancel() {
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesSize; i++) {
      Node* node = _rootNodes->at(i);
      node->cancel();
    }
  }
}
