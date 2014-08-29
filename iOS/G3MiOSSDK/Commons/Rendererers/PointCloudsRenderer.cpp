//
//  PointCloudsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

#include "PointCloudsRenderer.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "Sector.hpp"
#include "Tile.hpp"
#include "DownloadPriority.hpp"
#include "BingMapsLayer.hpp"
#include "ErrorHandling.hpp"
#include "IFactory.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONNumber.hpp"
#include "Box.hpp"
#include "CompositeMesh.hpp"
#include "DirectMesh.hpp"
#include "Camera.hpp"

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onDownload(const URL& url,
                                                                         IByteBuffer* buffer,
                                                                         bool expired) {
  _pointCloud->downloadedMetadata(buffer);
}

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onError(const URL& url) {
  _pointCloud->errorDownloadingMetadata();
}

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onCancel(const URL& url) {
  // do nothing
}

void PointCloudsRenderer::PointCloudMetadataDownloadListener::onCanceledDownload(const URL& url,
                                                                                 IByteBuffer* buffer,
                                                                                 bool expired) {
  // do nothing
}


void PointCloudsRenderer::PointCloud::initialize(const G3MContext* context) {
  _downloader = context->getDownloader();
  _downloadingMetadata = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata = false;

  const URL metadataURL(_serverURL, _cloudName);

  _downloader->requestBuffer(metadataURL,
                             _downloadPriority,
                             _timeToCache,
                             _readExpired,
                             new PointCloudsRenderer::PointCloudMetadataDownloadListener(this),
                             true);
}

PointCloudsRenderer::PointCloud::~PointCloud() {
#ifdef C_CODE
  for (std::map<std::string, TileLayout*>::iterator it = _visibleTiles.begin();
       it != _visibleTiles.end();
       it++) {
    TileLayout* tileLayout = it->second;
    tileLayout->cancel();
    tileLayout->_release();
  }
#endif
#ifdef JAVA_CODE
  for (final java.util.Map.Entry<String, TileLayout> entry : _visibleTiles.entrySet()) {
    final TileLayout tileLayout = entry.getValue();
    tileLayout.cancel();
    tileLayout._release();
  }
#endif

  for (std::map<std::string, PointCloudNode*>::iterator it = _nodes.begin();
       it != _nodes.end();
       it++) {
    PointCloudNode* node = it->second;
    node->releaseAllFromPointCloud();
    //delete node;
  }


  delete _initializationTimer;

  delete _sector;
}

void PointCloudsRenderer::PointCloud::errorDownloadingMetadata() {
  _downloadingMetadata = false;
  _errorDownloadingMetadata = true;
}

void PointCloudsRenderer::PointCloud::downloadedMetadata(IByteBuffer* buffer) {
  _downloadingMetadata = false;

  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer, true);
  if (jsonBaseObject) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject) {
      // const std::string name = jsonObject->getAsString("name", "");
      _pointsCount = (long long) jsonObject->getAsNumber("pointsCount", 0);

      const JSONObject* sectorJSONObject = jsonObject->getAsObject("sector");
      if (sectorJSONObject) {
        const double lowerLatitude  = sectorJSONObject->getAsNumber("lowerLatitude", 0);
        const double lowerLongitude = sectorJSONObject->getAsNumber("lowerLongitude", 0);
        const double upperLatitude  = sectorJSONObject->getAsNumber("upperLatitude", 0);
        const double upperLongitude = sectorJSONObject->getAsNumber("upperLongitude", 0);

        _sector = new Sector(Geodetic2D::fromDegrees(lowerLatitude, lowerLongitude),
                             Geodetic2D::fromDegrees(upperLatitude, upperLongitude));
      }

      _minHeight = jsonObject->getAsNumber("minHeight", 0);
      _maxHeight = jsonObject->getAsNumber("maxHeight", 0);
    }
    else {
      _errorParsingMetadata = true;
    }

    delete jsonBaseObject;
  }
  else {
    _errorParsingMetadata = true;
  }
  
  delete buffer;
}

RenderState PointCloudsRenderer::PointCloud::getRenderState(const G3MRenderContext* rc) {
  if (_downloadingMetadata) {
    return RenderState::busy();
  }

  if (_errorDownloadingMetadata) {
    return RenderState::error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
  }

  if (_errorParsingMetadata) {
    return RenderState::error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
  }

  return RenderState::ready();
}

void PointCloudsRenderer::PointCloud::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                            const std::vector<std::string>* tilesStoppedRendering) {
  if (_sector) {
    const int tilesStartedRenderingSize = tilesStartedRendering->size();
    for (int i = 0; i < tilesStartedRenderingSize; i++) {
      const Tile* tile = tilesStartedRendering->at(i);
      if (!tile->_mercator) {
        THROW_EXCEPTION("Tile has to be mercator");
      }

      if (tile->_sector.touchesWith(*_sector)) {
        _tilesStartedRendering.push_back(tile);
//        const std::string tileID = tile->_id;
//        if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
//          THROW_EXCEPTION("Logic error");
//        }
//
//        _visibleTiles[tileID] = new PointCloudsRenderer::TileLayout(this,
//                                                                    _cloudName,
//                                                                    tileID,
//                                                                    BingMapsLayer::getQuadKey(tile));
//        _visibleTilesNeedsInitialization = true;
      }
    }

    const int tilesStoppedRenderingSize = tilesStoppedRendering->size();
    for (int i = 0; i < tilesStoppedRenderingSize; i++) {
      const std::string tileID = tilesStoppedRendering->at(i);
      _tilesStoppedRendering.push_back(tileID);
//      if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
//        PointCloudsRenderer::TileLayout* tileLayout = _visibleTiles[tileID];
//        tileLayout->cancel();
//        tileLayout->_release();
//        _visibleTiles.erase(tileID);
//      }
    }

    const int startedSize = _tilesStartedRendering.size();
    const int stoppedSize = _tilesStoppedRendering.size();
    const bool anyStarted = startedSize > 0;
    const bool anyStopped = stoppedSize > 0;
    if (anyStarted || anyStopped) {
      if (anyStarted) {
        if (anyStopped) {
          // wait initialization of started before proceeding with the stopped
          // ILogger::instance()->logInfo("===> case 1: %d %d", startedSize, stoppedSize);

          TileLayoutStopper* stopper = new TileLayoutStopper(this, startedSize, _tilesStoppedRendering);

          for (int i = 0; i < startedSize; i++) {
            const Tile* tile = _tilesStartedRendering[i];
            const std::string tileID = tile->_id;
            if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
              THROW_EXCEPTION("Logic error");
            }
            _visibleTiles[tileID] = new PointCloudsRenderer::TileLayout(this,
                                                                        _cloudName,
                                                                        tileID,
                                                                        BingMapsLayer::getQuadKey(tile),
                                                                        stopper);
            _visibleTilesNeedsInitialization = true;
          }

          stopper->_release();
        }
        else {
          // just initialize the started
          //ILogger::instance()->logInfo("===> case 2: %d %d", startedSize, stoppedSize);

          for (int i = 0; i < startedSize; i++) {
            const Tile* tile = _tilesStartedRendering[i];
            const std::string tileID = tile->_id;
            if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
              THROW_EXCEPTION("Logic error");
            }
            _visibleTiles[tileID] = new PointCloudsRenderer::TileLayout(this,
                                                                        _cloudName,
                                                                        tileID,
                                                                        BingMapsLayer::getQuadKey(tile),
                                                                        NULL);
            _visibleTilesNeedsInitialization = true;
          }
        }
      }
      else {
        // proceed with the stopped
        // ILogger::instance()->logInfo("===> case 3: %d %d", startedSize, stoppedSize);
        stopTiles(_tilesStoppedRendering);
      }

      _tilesStartedRendering.clear();
      _tilesStoppedRendering.clear();
    }
  }
}

void PointCloudsRenderer::PointCloud::stopTiles(const std::vector<std::string>& tilesToStop) {

  const int tilesToStopSize = tilesToStop.size();
  for (int i = 0; i < tilesToStopSize; i++) {
    const std::string tileID = tilesToStop[i];

    if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
      PointCloudsRenderer::TileLayout* tileLayout = _visibleTiles[tileID];
      tileLayout->cancel();
      tileLayout->_release();
      _visibleTiles.erase(tileID);
    }
  }

}

PointCloudsRenderer::TileLayout::TileLayout(PointCloud* pointCloud,
                                            const std::string& cloudName,
                                            const std::string& tileID,
                                            const std::string& tileQuadKey,
                                            TileLayoutStopper* stopper) :
_pointCloud(pointCloud),
_cloudName(cloudName),
_tileID(tileID),
_tileQuadKey(tileQuadKey),
_stopper(stopper),
_isInitialized(false),
_downloader(NULL),
_layoutRequestID(-1),
_canceled(false)
{
  //ILogger::instance()->logInfo(" => Start rendering tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
  if (_stopper != NULL) {
    _stopper->_retain();
  }
}

void PointCloudsRenderer::TileLayout::cancel() {
  if (_canceled) {
#warning TODO
  }
  else {
    _canceled = true;
    if (_downloader != NULL && _layoutRequestID >= 0) {
      ILogger::instance()->logInfo(" => Canceling initialization of tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
      _downloader->cancelRequest(_layoutRequestID);
    }

    const int size = _nodesIDs.size();
    for (int i = 0; i < size; i++) {
      const std::string nodeID = _nodesIDs[i];
      _pointCloud->removeNode(nodeID);
    }
  }
}

PointCloudsRenderer::TileLayout::~TileLayout() {
//  ILogger::instance()->logInfo(" => Stop rendering tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");
  if (_stopper != NULL) {
    _stopper->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

PointCloudsRenderer::TileLayoutBufferDownloadListener::TileLayoutBufferDownloadListener(TileLayout* tileLayout) {
  _tileLayout = tileLayout;
  _tileLayout->_retain();
}

void PointCloudsRenderer::TileLayoutBufferDownloadListener::onDownload(const URL& url,
                                                                       IByteBuffer* buffer,
                                                                       bool expired) {
  _tileLayout->onDownload(buffer);
}

void PointCloudsRenderer::TileLayoutBufferDownloadListener::onError(const URL& url) {
  _tileLayout->onError();
}

void PointCloudsRenderer::TileLayoutBufferDownloadListener::onCancel(const URL& url) {
  _tileLayout->onCancel();
}

PointCloudsRenderer::TileLayoutBufferDownloadListener::~TileLayoutBufferDownloadListener() {
  _tileLayout->_release();
  _tileLayout = NULL;
}

PointCloudsRenderer::TileLayoutStopper::TileLayoutStopper(PointCloud* pointCloud,
                                                          int totalSteps,
                                                          const std::vector<std::string>& tilesToStop) :
_pointCloud(pointCloud),
_totalSteps(totalSteps),
_stepsDone(0)
{
#ifdef C_CODE
  _tilesToStop = tilesToStop;
#endif
#ifdef JAVA_CODE
  _tilesToStop = new java.util.ArrayList<String>(tilesToStop);
#endif
}

void PointCloudsRenderer::TileLayoutStopper::stepDone() {
  _stepsDone++;
  if (_stepsDone == _totalSteps) {
    _pointCloud->stopTiles(_tilesToStop);
  }
}

void PointCloudsRenderer::TileLayout::onDownload(IByteBuffer* buffer) {
  if (!_canceled) {
    const JSONBaseObject* jsonBaseObject  = IJSONParser::instance()->parse(buffer);
    if (jsonBaseObject != NULL) {
      const JSONArray* jsonArray =  jsonBaseObject->asArray();
      if (jsonArray != NULL) {
        //ILogger::instance()->logInfo("\"%s\" => %s", _tileQuadKey.c_str(), jsonArray->description().c_str());
        //ILogger::instance()->logInfo("\"%s\"", _tileQuadKey.c_str());

        const int size = jsonArray->size();
        for (int i = 0; i < size; i++) {
          const std::string nodeID = jsonArray->getAsString(i)->value();
          //ILogger::instance()->logInfo("  => %s", nodeID.c_str());
          _pointCloud->createNode(nodeID);
          _nodesIDs.push_back(nodeID);
        }

        _isInitialized = true;
      }
      delete jsonBaseObject;
    }
  }
  
  delete buffer;
  
  _layoutRequestID = -1;

  if (_stopper != NULL) {
    _stopper->stepDone();
  }
}

void PointCloudsRenderer::TileLayout::onError() {
  ILogger::instance()->logError("Error downloading layout for %s", _tileQuadKey.c_str());
  _layoutRequestID = -1;
  if (_stopper != NULL) {
    _stopper->stepDone();
  }
}

void PointCloudsRenderer::TileLayout::onCancel() {
  _layoutRequestID = -1;
  if (_stopper != NULL) {
    _stopper->stepDone();
  }
}

void PointCloudsRenderer::TileLayout::initialize(const G3MContext* context,
                                                 const URL& serverURL,
                                                 long long downloadPriority,
                                                 const TimeInterval& timeToCache,
                                                 bool readExpired) {

//  ILogger::instance()->logInfo("  => Initializing tile " + _tileID + " (" + _tileQuadKey + ") for cloud \"" + _cloudName + "\"");

  if (_downloader == NULL) {
    _downloader = context->getDownloader();
  }
  _layoutRequestID = _downloader->requestBuffer(URL(serverURL, _cloudName + "/layout/" + _tileQuadKey),
                                                downloadPriority,
                                                timeToCache,
                                                readExpired,
                                                new TileLayoutBufferDownloadListener(this),
                                                true);
}

void PointCloudsRenderer::PointCloud::render(const G3MRenderContext* rc,
                                             GLState* glState) {
  if (_visibleTilesNeedsInitialization || _nodesNeedsInitialization) {
    if (_initializationTimer == NULL) {
      _initializationTimer = rc->getFactory()->createTimer();
    }
    else {
      _initializationTimer->start();
    }
  }

  if (_visibleTilesNeedsInitialization) {
    _visibleTilesNeedsInitialization = false;
#ifdef C_CODE
    for (std::map<std::string, TileLayout*>::iterator it = _visibleTiles.begin();
         it != _visibleTiles.end();
         it++) {
      TileLayout* tileLayout = it->second;
      if (!tileLayout->isInitialized()) {
        tileLayout->initialize(rc, _serverURL, _downloadPriority, _timeToCache, _readExpired);
        if (_initializationTimer->elapsedTimeInMilliseconds() > 20) {
          _visibleTilesNeedsInitialization = true; // force another initialization lap for the next frame
          break;
        }
      }
    }
#endif
#ifdef JAVA_CODE
    for (final TileLayout tileLayout : _visibleTiles.values()) {
      if (!tileLayout.isInitialized()) {
        tileLayout.initialize(rc, _serverURL, _downloadPriority, _timeToCache, _readExpired);
        if (_initializationTimer.elapsedTimeInMilliseconds() > 20) {
          _visibleTilesNeedsInitialization = true; // force another initialization lap for the next frame
          break;
        }
      }
    }
#endif
  }


  if (_nodesNeedsInitialization) {
    _nodesNeedsInitialization = false;
    for (std::map<std::string, PointCloudNode*>::iterator it = _nodes.begin();
         it != _nodes.end();
         it++) {
      PointCloudNode* node = it->second;
      if (!node->isInitialized()) {
        node->initialize(rc, _serverURL, _downloadPriority, _timeToCache, _readExpired);
        if (_initializationTimer->elapsedTimeInMilliseconds() > 20) {
          _nodesNeedsInitialization = true; // force another initialization lap for the next frame
          break;
        }
      }
    }
  }


  for (std::map<std::string, PointCloudNode*>::iterator it = _nodes.begin();
       it != _nodes.end();
       it++) {
    PointCloudNode* node = it->second;
    node->render(rc, glState);
  }
}

PointCloudsRenderer::~PointCloudsRenderer() {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    delete cloud;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PointCloudsRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                                int width, int height) {

}

void PointCloudsRenderer::onChangedContext() {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->initialize(_context);
  }
}

RenderState PointCloudsRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    const RenderState childRenderState = cloud->getRenderState(rc);

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

void PointCloudsRenderer::addPointCloud(const URL& serverURL,
                                        const std::string& cloudName) {
  addPointCloud(serverURL,
                cloudName,
                DownloadPriority::MEDIUM,
                TimeInterval::fromDays(30),
                true);
}

void PointCloudsRenderer::addPointCloud(const URL& serverURL,
                                        const std::string& cloudName,
                                        long long downloadPriority,
                                        const TimeInterval& timeToCache,
                                        bool readExpired) {
  PointCloud* pointCloud = new PointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired);
  if (_context != NULL) {
    pointCloud->initialize(_context);
  }
  _clouds.push_back(pointCloud);
  _cloudsSize = _clouds.size();
}

void PointCloudsRenderer::removeAllPointClouds() {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    delete cloud;
  }
  _clouds.clear();
  _cloudsSize = _clouds.size();
}

void PointCloudsRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState) {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->render(rc, glState);
  }
}

void PointCloudsRenderer::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                const std::vector<std::string>* tilesStoppedRendering) {
//  for (int i = 0; i < _cloudsSize; i++) {
//    PointCloud* cloud = _clouds[i];
//    cloud->changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
//  }
#warning Diego at work
//  //ILogger::instance()->logInfo("=====================");
//  for (int i = 0; i < tilesStartedRendering->size(); i++) {
//    const std::string tileID = tilesStartedRendering->at(i)->_id;
//    //ILogger::instance()->logInfo(" started: %s", tileID.c_str());
//    if (_visibleTiles.find(tileID) != _visibleTiles.end()) {
//      THROW_EXCEPTION("Logic Error");
//    }
//    _visibleTiles[tileID] = NULL;
//  }
//  for (int i = 0; i < tilesStoppedRendering->size(); i++) {
//    const std::string tileID = tilesStoppedRendering->at(i);
//    //ILogger::instance()->logInfo(" stopped: %s", tileID.c_str());
//    if (_visibleTiles.find(tileID) == _visibleTiles.end()) {
//      THROW_EXCEPTION("Logic Error");
//    }
//    _visibleTiles.erase(tileID);
//  }
//
//  std::string msg = "==> visibles: [";
//  for (std::map<std::string, void*>::iterator it = _visibleTiles.begin();
//       it != _visibleTiles.end();
//       it++) {
//    const std::string tileID = it->first;
//    msg += " " + tileID;
//  }
//  msg += " ]";
//  ILogger::instance()->logInfo(msg.c_str());
}

void PointCloudsRenderer::PointCloudsTileRenderingListener::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                                                  const std::vector<std::string>* tilesStoppedRendering) {
  _pointCloudsRenderer->changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
}

void PointCloudsRenderer::PointCloud::createNode(const std::string& nodeID) {
#warning DGD at work!

  if (_nodes.find(nodeID) == _nodes.end()) {
    //ILogger::instance()->logInfo(" creating node: %s", nodeID.c_str());
    _nodes[nodeID] = new PointCloudNode(this, _cloudName, nodeID);
    _nodesNeedsInitialization = true;
  }
  else {
    //ILogger::instance()->logInfo(" retaining node: %s", nodeID.c_str());
    _nodes[nodeID]->retainFromPointCloud();
  }
}

void PointCloudsRenderer::PointCloud::removeNode(const std::string& nodeID) {
#warning DGD at work!
  if (_nodes.find(nodeID) == _nodes.end()) {
    THROW_EXCEPTION("Logic error");
  }
  PointCloudNode* node = _nodes[nodeID];

  if (node->getReferenceCountFromPointCloud() == 1) {
    node->cancel();
    _nodes.erase(nodeID);
  }

  if (node->releaseFromPointCloud()) {
    //ILogger::instance()->logInfo(" deleting node: %s", nodeID.c_str());
//    node->cancel();
//    delete node;
  }
//  else {
//    ILogger::instance()->logInfo(" releasing node: %s", nodeID.c_str());
//  }
}



void PointCloudsRenderer::PointCloudNode::initialize(const G3MContext* context,
                                                     const URL& serverURL,
                                                     long long downloadPriority,
                                                     const TimeInterval& timeToCache,
                                                     bool readExpired) {

  if (_downloader == NULL) {
    _downloader = context->getDownloader();
  }
  _metadataRequestID = _downloader->requestBuffer(URL(serverURL, _cloudName + "/metadata/" + _id),
                                                downloadPriority,
                                                timeToCache,
                                                readExpired,
                                                new NodeMetadataBufferDownloadListener(this),
                                                true);
}

void PointCloudsRenderer::PointCloudNode::cancel() {
  if (_canceled) {
#warning TODO
  }
  else {
    _canceled = true;
    if (_downloader != NULL && _metadataRequestID >= 0) {
      ILogger::instance()->logInfo(" => Canceling initialization of node " + _id + " for cloud \"" + _cloudName + "\"");
      _downloader->cancelRequest(_metadataRequestID);
    }
  }
}


PointCloudsRenderer::NodeMetadataBufferDownloadListener::NodeMetadataBufferDownloadListener(PointCloudNode* node) {
  _node = node;
  _node->_retain();
}

PointCloudsRenderer::NodeMetadataBufferDownloadListener::~NodeMetadataBufferDownloadListener() {
  _node->_release();
}

void PointCloudsRenderer::NodeMetadataBufferDownloadListener::onDownload(const URL& url,
                                                                         IByteBuffer* buffer,
                                                                         bool expired) {
  _node->onMetadataDownload(buffer);
}

void PointCloudsRenderer::NodeMetadataBufferDownloadListener::onError(const URL& url) {
  _node->onMetadataError();
}

void PointCloudsRenderer::NodeMetadataBufferDownloadListener::onCancel(const URL& url) {
  _node->onMetadataCancel();
}

PointCloudsRenderer::PointCloudNode::PointCloudNode(PointCloud* pointCloud,
                                                    const std::string& cloudName,
                                                    const std::string& id) :
_pointCloud(pointCloud),
_cloudName(cloudName),
_id(id),
_referenceCountFromPointCloud(1),
_isInitialized(false),
_downloader(NULL),
_metadataRequestID(-1),
_canceled(false),
_bounds(NULL),
_lodLevels(NULL),
_lodPoints(NULL),
_averagePoint(NULL),
_mesh(NULL),
_glState(new GLState())
{
}

PointCloudsRenderer::PointCloudNode::~PointCloudNode() {
  delete _mesh;

  delete _bounds;
  delete _lodLevels;

  delete _averagePoint;

  _glState->_release();

  if (_lodPoints != NULL) {
    const int lodPointsSize = _lodPoints->size();
    for (int i = 0; i < lodPointsSize; i++) {
      IFloatBuffer* levelBuffer = _lodPoints->at(i);
      delete levelBuffer;
    }
    delete _lodPoints;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PointCloudsRenderer::PointCloudNode::onMetadataError() {
  ILogger::instance()->logError("Error downloading metadata for node %s", _id.c_str());
  _metadataRequestID = -1;
}

void PointCloudsRenderer::PointCloudNode::onMetadataCancel() {
  _metadataRequestID = -1;
}

void PointCloudsRenderer::PointCloudNode::onMetadataDownload(IByteBuffer* buffer) {
  if (!_canceled) {
    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer);

    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject != NULL) {
      const JSONArray* boundsArray = jsonObject->getAsArray("cartesianEllipsoidalBounds");

      const Vector3D lower(boundsArray->getAsNumber(0)->value(),
                           boundsArray->getAsNumber(1)->value(),
                           boundsArray->getAsNumber(2)->value());
      const Vector3D upper(boundsArray->getAsNumber(3)->value(),
                           boundsArray->getAsNumber(4)->value(),
                           boundsArray->getAsNumber(5)->value());
      _bounds = new Box(lower, upper);

      const JSONArray* lodLevelsArray = jsonObject->getAsArray("lodLevels");
      const int lodLevelsSize = lodLevelsArray->size();
      _lodLevels = new std::vector<int>();
      for (int i = 0; i < lodLevelsSize; i++) {
        _lodLevels->push_back((int) lodLevelsArray->getAsNumber(i)->value());
      }

      const JSONArray* averagePointArray = jsonObject->getAsArray("cartesianEllipsoidalAveragePoint");
      _averagePoint = new Vector3D(averagePointArray->getAsNumber(0)->value(),
                                   averagePointArray->getAsNumber(1)->value(),
                                   averagePointArray->getAsNumber(2)->value());

      const JSONArray* lodLevelsPointsArray = jsonObject->getAsArray("lodLevelsCartesianEllipsoidalDeltaPoints");
      const int lodLevelsPointsSize = lodLevelsPointsArray->size();
      _lodPoints = new std::vector<IFloatBuffer*>();
      for (int i = 0; i < lodLevelsPointsSize; i++) {
        const JSONArray* lodLevelPoints = lodLevelsPointsArray->getAsArray(i);
        const int lodLevelPointsSize = lodLevelPoints->size();

        if ((lodLevelPointsSize / 3.0) != _lodLevels->at(i)) {
          THROW_EXCEPTION("Logic error");
        }

        IFloatBuffer* levelBuffer = IFactory::instance()->createFloatBuffer(lodLevelPointsSize);
        for (int j = 0; j < lodLevelPointsSize; j++) {
          const float value = (float) lodLevelPoints->getAsNumber(j)->value();
          levelBuffer->rawPut(j, value);
        }
        _lodPoints->push_back(levelBuffer);
      }

#warning DGD at work;
    }

    delete jsonBaseObject;
  }

  delete buffer;

  _metadataRequestID = -1;
}

Mesh* PointCloudsRenderer::PointCloudNode::getMesh() {
  if (_mesh == NULL) {
    if (_lodPoints != NULL) {
//      CompositeMesh* compositeMesh = new CompositeMesh();
//
//      const int lodPointsSize = _lodPoints->size();
//      for (int i = 0; i < lodPointsSize; i++) {
//        Mesh* levelMesh = new DirectMesh(GLPrimitive::points(),
//                                         false,
//                                         *_averagePoint,
//                                         _lodPoints->at(i),
//                                         1,
//                                         2,
//                                         new Color(Color::white()),
//                                         NULL,
//                                         0,
//                                         false);
//        compositeMesh->addMesh(levelMesh);
//      }
//
//      _mesh = compositeMesh;

      _mesh = new DirectMesh(GLPrimitive::points(),
                             false,
                             *_averagePoint,
                             _lodPoints->at(0),
                             1,
                             2,
                             new Color(Color::white()),
                             NULL,
                             0,
                             false);
    }
  }
  return _mesh;
}

void PointCloudsRenderer::PointCloudNode::updateGLState(const G3MRenderContext* rc) {
  const Camera* cam = rc->getCurrentCamera();

  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  }
  else {
    f->setMatrix(cam->getModelViewMatrix44D());
  }
}


void PointCloudsRenderer::PointCloudNode::render(const G3MRenderContext* rc,
                                                 GLState* glState) {
  const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();
  updateGLState(rc);

  _glState->setParent(glState);

//  if (_bounds != NULL) {
//    _bounds->render(rc, *_glState);
//  }

  Mesh* mesh = getMesh();
  if (mesh != NULL) {
    // mesh->render(rc, glState);
    // const BoundingVolume* boundingVolume = mesh->getBoundingVolume();
    const BoundingVolume* boundingVolume = _bounds;
    if ( boundingVolume != NULL && boundingVolume->touchesFrustum(frustum) ) {
      mesh->render(rc, _glState);
    }
  }
}
