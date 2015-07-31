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

/*

 http://localhost:8080/server-mapboo/public/VectorialStreaming/GEONames-PopulatedPlaces_LOD/features?node=&properties=name|population|featureClass|featureCode

 http://localhost:8080/server-mapboo/public/VectorialStreaming/GEONames-PopulatedPlaces_LOD

 */


VectorStreamingRenderer::Node::~Node() {
  unload();

  delete _sector;
  delete _averagePosition;
  delete _boundingVolume;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

BoundingVolume* VectorStreamingRenderer::Node::getBoundingVolume(const G3MRenderContext *rc) {
  if (_boundingVolume == NULL) {
    const Planet* planet = rc->getPlanet();

#ifdef C_CODE
    const Vector3D c[5] = {
      planet->toCartesian( _sector->getNE()     ),
      planet->toCartesian( _sector->getNW()     ),
      planet->toCartesian( _sector->getSE()     ),
      planet->toCartesian( _sector->getSW()     ),
      planet->toCartesian( _sector->getCenter() )
    };

    std::vector<Vector3D> points(c, c+5);
#endif
#ifdef JAVA_CODE
    java.util.ArrayList<Vector3D>points = new java.util.ArrayList<Vector3D>(5);
    _cornersD.add( planet.toCartesian( _sector.getNE()     ) );
    _cornersD.add( planet.toCartesian( _sector.getNW()     ) );
    _cornersD.add( planet.toCartesian( _sector.getSE()     ) );
    _cornersD.add( planet.toCartesian( _sector.getSW()     ) );
    _cornersD.add( planet.toCartesian( _sector.getCenter() ) );
#endif

    _boundingVolume = Sphere::enclosingSphere(points);
  }

  return _boundingVolume;
}

bool VectorStreamingRenderer::Node::isBigEnough(const G3MRenderContext *rc) {
  //  if ((_sector->_deltaLatitude._degrees  > 80) ||
  //      (_sector->_deltaLongitude._degrees > 80)) {
  //    return true;
  //  }

  const double projectedArea = getBoundingVolume(rc)->projectedArea(rc);
  return (projectedArea > 100000);
}

long long VectorStreamingRenderer::Node::renderFeatures(const G3MRenderContext *rc,
                                                        const GLState *glState) {
#warning TODO

  return 0;
}

void VectorStreamingRenderer::Node::loadFeatures() {
#warning TODO
}

void VectorStreamingRenderer::Node::unloadFeatures() {
#warning TODO
}

void VectorStreamingRenderer::Node::cancelLoadFeatures() {
#warning TODO
}

void VectorStreamingRenderer::Node::loadChildren() {
#warning TODO
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
#warning TODO
}

void VectorStreamingRenderer::Node::removeMarks() {
#warning TODO
}

bool VectorStreamingRenderer::Node::isVisible(const G3MRenderContext* rc,
                                              const Frustum* frustumInModelCoordinates) {
  //  if ((_sector->_deltaLatitude._degrees  > 80) ||
  //      (_sector->_deltaLongitude._degrees > 80)) {
  //    return true;
  //  }

  return getBoundingVolume(rc)->touchesFrustum(frustumInModelCoordinates);
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

long long VectorStreamingRenderer::Node::render(const G3MRenderContext* rc,
                                                const Frustum* frustumInModelCoordinates,
                                                const long long cameraTS,
                                                GLState* glState) {

  long long renderedCount = 0;

#warning Show Bounding Volume
  getBoundingVolume(rc)->render(rc, glState, Color::red());

  const bool bigEnough = isBigEnough(rc);
  if (bigEnough) {
    const bool visible = isVisible(rc, frustumInModelCoordinates);
    if (visible) {
      if (_loadedFeatures) {
        renderedCount += renderFeatures(rc, glState);

        if (_children == NULL) {
          // don't load children until my features are loaded
          if (!_loadingChildren) {
            _loadingChildren = true;
            loadChildren();
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
          loadFeatures();
        }
      }
    }
    else {
      if (_wasVisible) {
        unload();
      }
    }
    _wasVisible = visible;
  }
  else {
    if (_wasBigEnough) {
      unload();
    }
  }
  _wasBigEnough = bigEnough;

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
  const double lat = json->getAsNumber(0)->value();
  const double lon = json->getAsNumber(1)->value();

  return new Geodetic2D(Angle::fromDegrees(lat), Angle::fromDegrees(lon));
}

VectorStreamingRenderer::Node* VectorStreamingRenderer::GEOJSONUtils::parseNode(const JSONObject* json) {
  const std::string id              = json->getAsString("id")->value();
  Sector*           sector          = GEOJSONUtils::parseSector( json->getAsArray("sector") );
  int               featuresCount   = (int) json->getAsNumber("featuresCount")->value();
  Geodetic2D*       averagePosition = GEOJSONUtils::parseGeodetic2D( json->getAsArray("averagePosition") );

  std::vector<std::string> children;
  const JSONArray* childrenJSON = json->getAsArray("children");
  for (int i = 0; i < childrenJSON->size(); i++) {
    children.push_back( childrenJSON->getAsString(i)->value() );
  }

  return new Node(id, sector, featuresCount, averagePosition, children);
}

VectorStreamingRenderer::MetadataParserAsyncTask::~MetadataParserAsyncTask() {
  delete _buffer;

  delete _sector;
  delete _averagePosition;

  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodes->size(); i++) {
      Node* node = _rootNodes->at(i);
      node->_release();
    }
    delete _rootNodes;
  }
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
      _sector          = GEOJSONUtils::parseSector( jsonObject->getAsArray("sector") );
      _featuresCount   = (long long) jsonObject->getAsNumber("featuresCount")->value();
      _averagePosition = GEOJSONUtils::parseGeodetic2D( jsonObject->getAsArray("averagePosition") );
      _nodesCount      = (int) jsonObject->getAsNumber("featuresCount")->value();
      _minNodeDepth    = (int) jsonObject->getAsNumber("minNodeDepth")->value();
      _maxNodeDepth    = (int) jsonObject->getAsNumber("maxNodeDepth")->value();

      const JSONArray* rootNodesJSON = jsonObject->getAsArray("rootNodes");
      _rootNodes = new std::vector<Node*>();
      for (int i = 0; i < rootNodesJSON->size(); i++) {
        Node* node = GEOJSONUtils::parseNode( rootNodesJSON->getAsObject(i) );
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
                               _featuresCount,
                               _averagePosition,
                               _nodesCount,
                               _minNodeDepth,
                               _maxNodeDepth,
                               _rootNodes);
    _sector          = NULL; // moved ownership to _vectorSet
    _averagePosition = NULL; // moved ownership to _vectorSet
    _rootNodes       = NULL; // moved ownership to _vectorSet
  }
}

void VectorStreamingRenderer::MetadataDownloadListener::onDownload(const URL& url,
                                                                   IByteBuffer* buffer,
                                                                   bool expired) {
  if (_verbose) {
#ifdef C_CODE
    ILogger::instance()->logInfo("Downloaded metadata for \"%s\" (bytes=%ld)",
                                 _vectorSet->getName().c_str(),
                                 buffer->size());
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%d)", _vectorSet.getName(), buffer.size());
#endif
  }

  _threadUtils->invokeAsyncTask(new MetadataParserAsyncTask(_vectorSet, buffer),
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
  delete _averagePosition;
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodes->size(); i++) {
      Node* node = _rootNodes->at(i);
      node->_release();
    }
    delete _rootNodes;
  }
}

void VectorStreamingRenderer::VectorSet::parsedMetadata(Sector* sector,
                                                        long long featuresCount,
                                                        Geodetic2D* averagePosition,
                                                        int nodesCount,
                                                        int minNodeDepth,
                                                        int maxNodeDepth,
                                                        std::vector<Node*>* rootNodes) {
  _downloadingMetadata = false;

  _sector          = sector;
  _featuresCount   = featuresCount;
  _averagePosition = averagePosition;
  _nodesCount      = nodesCount;
  _minNodeDepth    = minNodeDepth;
  _maxNodeDepth    = maxNodeDepth;
  _rootNodes       = rootNodes;
  _rootNodesSize   = _rootNodes->size();

  if (_verbose) {
    ILogger::instance()->logInfo("Metadata for \"%s\"",         _name.c_str());
    ILogger::instance()->logInfo("   Sector           : %s",    _sector->description().c_str());
#ifdef C_CODE
    ILogger::instance()->logInfo("   Features Count   : %ld",   _featuresCount);
#endif
#ifdef JAVA_CODE
    ILogger.instance().logInfo("   Features Count   : %d",   _featuresCount);
#endif
    ILogger::instance()->logInfo("   Average Position : %s",    _averagePosition->description().c_str());
    ILogger::instance()->logInfo("   Nodes Count      : %d",    _nodesCount);
    ILogger::instance()->logInfo("   Depth            : %d/%d", _minNodeDepth, _maxNodeDepth);
    ILogger::instance()->logInfo("   Root Nodes       : %d",    _rootNodesSize);
  }

}

void VectorStreamingRenderer::VectorSet::initialize(const G3MContext* context) {
  _downloadingMetadata = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata = false;

  const URL metadataURL(_serverURL, _name);

  if (_verbose) {
    ILogger::instance()->logInfo("Downloading metadata for \"%s\"", _name.c_str());
  }

  context->getDownloader()->requestBuffer(metadataURL,
                                          _downloadPriority,
                                          _timeToCache,
                                          _readExpired,
                                          new VectorStreamingRenderer::MetadataDownloadListener(this,
                                                                                                context->getThreadUtils(),
                                                                                                _verbose),
                                          true);
}

RenderState VectorStreamingRenderer::VectorSet::getRenderState(const G3MRenderContext* rc) {
  if (_downloadingMetadata) {
    return RenderState::busy();
  }

  if (_errorDownloadingMetadata) {
    return RenderState::error("Error downloading metadata of \"" + _name + "\" from \"" + _serverURL.getPath() + "\"");
  }

  if (_errorParsingMetadata) {
    return RenderState::error("Error parsing metadata of \"" + _name + "\" from \"" + _serverURL.getPath() + "\"");
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
                                           const VectorSetSymbolizer* symbolizer,
                                           const bool                 deleteSymbolizer,
                                           long long                  downloadPriority,
                                           const TimeInterval&        timeToCache,
                                           bool                       readExpired,
                                           bool                       verbose) {
  VectorSet* vectorSet = new VectorSet(serverURL,
                                       name,
                                       symbolizer,
                                       deleteSymbolizer,
                                       downloadPriority,
                                       timeToCache,
                                       readExpired,
                                       verbose);
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
