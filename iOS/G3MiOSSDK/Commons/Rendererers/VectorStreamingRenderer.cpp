//
//  VectorStreamingRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/30/15.
//
//

#include "VectorStreamingRenderer.hpp"
#include "Context.hpp"
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

VectorStreamingRenderer::ChildrenParserAsyncTask::~ChildrenParserAsyncTask() {
  _node->_release();
  delete _buffer;

  if (_children != NULL) {
    for (size_t i = 0; i > _children->size(); i++) {
      Node* child = _children->at(i);
      child->_release();
    }
    _children = NULL;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::ChildrenParserAsyncTask::runInBackground(const G3MContext* context) {
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);
  if (jsonBaseObject != NULL) {
    const JSONArray* nodesJSON = jsonBaseObject->asArray();
    if (nodesJSON != NULL) {
      _children = new std::vector<Node*>();
      for (int i = 0; i < nodesJSON->size(); i++) {
        const JSONObject* nodeJSON = nodesJSON->getAsObject(i);
        _children->push_back( GEOJSONUtils::parseNode(_node,
                                                      nodeJSON,
                                                      _node->getVectorSet(),
                                                      _verbose) );
      }
    }

    delete jsonBaseObject;
  }

  delete _buffer;
  _buffer = NULL;
}

void VectorStreamingRenderer::ChildrenParserAsyncTask::onPostExecute(const G3MContext* context) {
  _node->parsedChildren(_children, _threadUtils);
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

  _threadUtils->invokeAsyncTask(new ChildrenParserAsyncTask(_node, _verbose, buffer, _threadUtils),
                                true);
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
  _node->_release();

  delete _buffer;

  if (_clusters != NULL) {
    for (int i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
  }

  delete _features;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<VectorStreamingRenderer::Cluster*>* VectorStreamingRenderer::FeaturesParserAsyncTask::parseClusters(const JSONArray* clustersJson) {
  if (clustersJson == NULL) {
    return NULL;
  }

  std::vector<VectorStreamingRenderer::Cluster*>* clusters = new std::vector<VectorStreamingRenderer::Cluster*>();
  const size_t clustersCount = clustersJson->size();
  for (int i = 0; i < clustersCount; i++) {
    const JSONObject* clusterJson = clustersJson->getAsObject(i);
    const Geodetic2D* position = GEOJSONUtils::parseGeodetic2D( clusterJson->getAsArray("position") );
    const long long   size     = (long long) clusterJson->getAsNumber("size")->value();

    clusters->push_back( new Cluster(position, size) );
  }

  return clusters;
}


void VectorStreamingRenderer::FeaturesParserAsyncTask::runInBackground(const G3MContext* context) {

  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);
  delete _buffer;
  _buffer = NULL;

  if (jsonBaseObject != NULL) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();

    _clusters = parseClusters( jsonObject->get("clusters")->asArray() );
    _features = GEOJSONParser::parse( jsonObject->get("features")->asObject() , _verbose);

    delete jsonBaseObject;
  }
}

void VectorStreamingRenderer::FeaturesParserAsyncTask::onPostExecute(const G3MContext* context) {
  _node->parsedFeatures(_clusters, _features, _threadUtils);
  _clusters = NULL; // moved ownership to _node
  _features = NULL; // moved ownership to _node
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

  _threadUtils->invokeAsyncTask(new FeaturesParserAsyncTask(_node, _verbose, buffer, _threadUtils),
                                true);
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

VectorStreamingRenderer::Node::~Node() {
  unload();

  delete _features;

  if (_clusters != NULL) {
    for (int i = 0; i < _clusters->size(); i++) {
      Cluster* cluster = _clusters->at(i);
      delete cluster;
    }
    delete _clusters;
  }

  delete _nodeSector;
  delete _minimumSector;
  delete _boundingVolume;

  if (_parent != NULL) {
    _parent->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void VectorStreamingRenderer::Node::parsedChildren(std::vector<Node*>* children,
                                                   const IThreadUtils* threadUtils) {
  if (children != NULL) {
    _children = children;
    _loadingChildren = false;
    _childrenSize = _children->size();
  }
}

void VectorStreamingRenderer::Node::parsedFeatures(std::vector<Cluster*>* clusters,
                                                   GEOObject*             features,
                                                   const IThreadUtils*    threadUtils) {
  _loadedFeatures = true;
  _loadingFeatures = false;
  _featuresRequestID = -1;

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

    //  Delete _features???
    delete _features;
    _features = NULL;
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
      planet->toCartesian( _minimumSector->getNE()     ),
      planet->toCartesian( _minimumSector->getNW()     ),
      planet->toCartesian( _minimumSector->getSE()     ),
      planet->toCartesian( _minimumSector->getSW()     ),
      planet->toCartesian( _minimumSector->getCenter() )
    };

    std::vector<Vector3D> points(c, c+5);
#endif
#ifdef JAVA_CODE
    java.util.ArrayList<Vector3D> points = new java.util.ArrayList<Vector3D>(5);
    points.add( planet.toCartesian( _minimumSector.getNE()     ) );
    points.add( planet.toCartesian( _minimumSector.getNW()     ) );
    points.add( planet.toCartesian( _minimumSector.getSE()     ) );
    points.add( planet.toCartesian( _minimumSector.getSW()     ) );
    points.add( planet.toCartesian( _minimumSector.getCenter() ) );
#endif

    _boundingVolume = Sphere::enclosingSphere(points);
  }

  return _boundingVolume;
}

void VectorStreamingRenderer::Node::loadFeatures(const G3MRenderContext* rc) {
  const URL metadataURL(_vectorSet->getServerURL(),
                        _vectorSet->getName() + "/features" +
                        "?node=" + _id +
                        "&properties=" + _vectorSet->getProperties(),
                        true);

  //  if (_verbose) {
  //    ILogger::instance()->logInfo("\"%s\": Downloading features for node \'%s\'",
  //                                 _vectorSet->getName().c_str(),
  //                                 _id.c_str());
  //  }

  _downloader = rc->getDownloader();
  _featuresRequestID = _downloader->requestBuffer(metadataURL,
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
    parsedChildren(children, rc->getThreadUtils());
    return;
  }

  std::string nodes = "";
  for (size_t i = 0; i < childrenIDsSize; i++) {
    if (i > 0) {
      nodes += "|";
    }
#ifdef C_CODE
    nodes += _childrenIDs[i];
#endif
#ifdef JAVA_CODE
    nodes += _childrenIDs.get(i);
#endif
  }

  const URL childrenURL(_vectorSet->getServerURL(),
                        _vectorSet->getName() +
                        "?nodes=" + nodes,
                        true);

  //  if (_verbose) {
  //    ILogger::instance()->logInfo("\"%s\": Downloading children for node \'%s\'",
  //                                 _vectorSet->getName().c_str(),
  //                                 _id.c_str());
  //  }

  _downloader = rc->getDownloader();

  _childrenRequestID = _downloader->requestBuffer(childrenURL,
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
      child->_release();
    }
    delete _children;
    _children = NULL;
    _childrenSize = 0;
  }
}

void VectorStreamingRenderer::Node::cancelLoadChildren() {
  if (_childrenRequestID != -1) {
    _downloader->cancelRequest(_childrenRequestID);
    _childrenRequestID = -1;
  }
}

VectorStreamingRenderer::NodeAllMarksFilter::NodeAllMarksFilter(const Node* node) {
  _nodeClusterToken = node->getClusterMarkToken();
  _nodeFeatureToken = node->getFeatureMarkToken();
}

bool VectorStreamingRenderer::NodeAllMarksFilter::test(const Mark* mark) const {
  const std::string token = mark->getToken();
  return ((token == _nodeClusterToken) ||
          (token == _nodeFeatureToken));
}

VectorStreamingRenderer::NodeClusterMarksFilter::NodeClusterMarksFilter(const Node* node) {
  _nodeClusterToken = node->getClusterMarkToken();
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

  size_t removed = _vectorSet->getMarksRenderer()->removeAllMarks( NodeAllMarksFilter(this), true );

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
                                              const Frustum* frustumInModelCoordinates) {
  //  if ((_sector->_deltaLatitude._degrees  > 80) ||
  //      (_sector->_deltaLongitude._degrees > 80)) {
  //    return true;
  //  }

  return getBoundingVolume(rc)->touchesFrustum(frustumInModelCoordinates);
}

bool VectorStreamingRenderer::Node::isBigEnough(const G3MRenderContext *rc) {
  if ((_nodeSector->_deltaLatitude._degrees  >= 80) ||
      (_nodeSector->_deltaLongitude._degrees >= 80)) {
    return true;
  }

  const double projectedArea = getBoundingVolume(rc)->projectedArea(rc);
  return (projectedArea > 350000);
}

void VectorStreamingRenderer::Node::unload() {
  removeMarks();

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

  if (_children != NULL) {
    unloadChildren();
  }
}




void VectorStreamingRenderer::Node::childRendered() {
  if (_clusters != NULL) {
    if (_clusters->size() > 0) {
      if (_clusterMarksCount > 0) {
        size_t removed = _vectorSet->getMarksRenderer()->removeAllMarks( NodeClusterMarksFilter(this), true );

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
      }
    }
  }
}

long long VectorStreamingRenderer::Node::render(const G3MRenderContext* rc,
                                                const Frustum* frustumInModelCoordinates,
                                                const long long cameraTS,
                                                GLState* glState) {

  long long renderedCount = 0;

  // #warning Show Bounding Volume
  // getBoundingVolume(rc)->render(rc, glState, Color::red());

  const bool visible = isVisible(rc, frustumInModelCoordinates);
  if (visible) {
    const bool bigEnough = isBigEnough(rc);
    if (bigEnough) {
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
    else {
      if (_wasBigEnough) {
        unload();
        if (_parent != NULL) {
          //_parent->childUnloaded();
          _parent->childStopRendered();
        }
      }
    }
    _wasBigEnough = bigEnough;
  }
  else {
    if (_wasVisible) {
      unload();
      if (_parent != NULL) {
        //_parent->childUnloaded();
        _parent->childStopRendered();
      }
    }
  }
  _wasVisible = visible;

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

VectorStreamingRenderer::Node* VectorStreamingRenderer::GEOJSONUtils::parseNode(Node*             parent,
                                                                                const JSONObject* json,
                                                                                const VectorSet*  vectorSet,
                                                                                const bool        verbose) {
  const std::string id            = json->getAsString("id")->value();
  Sector*           nodeSector    = GEOJSONUtils::parseSector( json->getAsArray("nodeSector") );
  Sector*           minimumSector = GEOJSONUtils::parseSector( json->getAsArray("minimumSector") );
  int               clustersCount = (int) json->getAsNumber("clustersCount")->value();
  int               featuresCount = (int) json->getAsNumber("featuresCount")->value();

  std::vector<std::string> children;
  const JSONArray* childrenJSON = json->getAsArray("children");
  for (int i = 0; i < childrenJSON->size(); i++) {
    children.push_back( childrenJSON->getAsString(i)->value() );
  }

  return new Node(vectorSet,
                  parent,
                  id,
                  nodeSector,
                  minimumSector,
                  clustersCount,
                  featuresCount,
                  children,
                  verbose);
}

VectorStreamingRenderer::MetadataParserAsyncTask::~MetadataParserAsyncTask() {
  delete _buffer;

  delete _sector;

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
      _sector        = GEOJSONUtils::parseSector( jsonObject->getAsArray("sector") );
      _clustersCount = (long long) jsonObject->getAsNumber("clustersCount")->value();
      _featuresCount = (long long) jsonObject->getAsNumber("featuresCount")->value();
      _nodesCount    = (int) jsonObject->getAsNumber("nodesCount")->value();
      _minNodeDepth  = (int) jsonObject->getAsNumber("minNodeDepth")->value();
      _maxNodeDepth  = (int) jsonObject->getAsNumber("maxNodeDepth")->value();

      const JSONArray* rootNodesJSON = jsonObject->getAsArray("rootNodes");
      _rootNodes = new std::vector<Node*>();
      for (int i = 0; i < rootNodesJSON->size(); i++) {
        Node* node = GEOJSONUtils::parseNode(NULL,
                                             rootNodesJSON->getAsObject(i),
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
    _vectorSet->parsedMetadata(_sector,
                               _clustersCount,
                               _featuresCount,
                               _nodesCount,
                               _minNodeDepth,
                               _maxNodeDepth,
                               _rootNodes);
    _sector          = NULL; // moved ownership to _vectorSet
    _rootNodes       = NULL; // moved ownership to _vectorSet
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

  delete _sector;
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodes->size(); i++) {
      Node* node = _rootNodes->at(i);
      node->_release();
    }
    delete _rootNodes;
  }
}

void VectorStreamingRenderer::VectorSet::parsedMetadata(Sector* sector,
                                                        long long clustersCount,
                                                        long long featuresCount,
                                                        int nodesCount,
                                                        int minNodeDepth,
                                                        int maxNodeDepth,
                                                        std::vector<Node*>* rootNodes) {
  _downloadingMetadata = false;

  _sector          = sector;
  _clustersCount   = clustersCount;
  _featuresCount   = featuresCount;
  _nodesCount      = nodesCount;
  _minNodeDepth    = minNodeDepth;
  _maxNodeDepth    = maxNodeDepth;
  _rootNodes       = rootNodes;
  _rootNodesSize   = _rootNodes->size();

  if (_verbose) {
    ILogger::instance()->logInfo("\"%s\": Metadata",         _name.c_str());
    ILogger::instance()->logInfo("   Sector        : %s",    _sector->description().c_str());
#ifdef C_CODE
    ILogger::instance()->logInfo("   Clusters Count: %ld",   _clustersCount);
    ILogger::instance()->logInfo("   Features Count: %ld",   _featuresCount);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("   Clusters Count: %d",   _clustersCount);
    ILogger.instance().logInfo("   Features Count: %d",   _featuresCount);
#endif
    ILogger::instance()->logInfo("   Nodes Count   : %d",    _nodesCount);
    ILogger::instance()->logInfo("   Depth         : %d/%d", _minNodeDepth, _maxNodeDepth);
    ILogger::instance()->logInfo("   Root Nodes    : %d",    _rootNodesSize);
  }

}

void VectorStreamingRenderer::VectorSet::initialize(const G3MContext* context) {
  _downloadingMetadata = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata = false;

  const URL metadataURL(_serverURL, _name);

  if (_verbose) {
    ILogger::instance()->logInfo("\"%s\": Downloading metadata", _name.c_str());
  }

  context->getDownloader()->requestBuffer(metadataURL,
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
      return RenderState::error("Error downloading metadata of \"" + _name + "\" from \"" + _serverURL.getPath() + "\"");
    }

    if (_errorParsingMetadata) {
      return RenderState::error("Error parsing metadata of \"" + _name + "\" from \"" + _serverURL.getPath() + "\"");
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
        Mark* mark = _symbolizer->createClusterMark(cluster, _featuresCount);
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
  Mark* mark = _symbolizer->createFeatureMark(geometry);
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
  //  delete _timer;

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
  for (int i = 0; i < _vectorSetsSize; i++) {
    VectorSet* vectorSet = _vectorSets[i];
    vectorSet->initialize(_context);
  }
}

RenderState VectorStreamingRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  for (int i = 0; i < _vectorSetsSize; i++) {
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
                                           bool                       haltOnError) {
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
                                       haltOnError);
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
  for (int i = 0; i < _vectorSetsSize; i++) {
    const Camera* camera = rc->getCurrentCamera();
    const Frustum* frustumInModelCoordinates = camera->getFrustumInModelCoordinates();

    const long long cameraTS = camera->getTimestamp();

    updateGLState(camera);
    _glState->setParent(glState);
    
    VectorSet* vectorSector = _vectorSets[i];
    vectorSector->render(rc,
                         frustumInModelCoordinates,
                         cameraTS,
                         _glState);
  }
}
