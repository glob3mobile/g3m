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
  _tileLayout->onDownload(url, buffer, expired);
}

void PointCloudsRenderer::TileLayoutBufferDownloadListener::onError(const URL& url) {
  _tileLayout->onError(url);
}

void PointCloudsRenderer::TileLayoutBufferDownloadListener::onCancel(const URL& url) {
  _tileLayout->onCancel(url);
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

void PointCloudsRenderer::TileLayout::onDownload(const URL& url,
                                                 IByteBuffer* buffer,
                                                 bool expired) {
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

void PointCloudsRenderer::TileLayout::onError(const URL& url) {
  ILogger::instance()->logError("Error downloading %s", url.getPath().c_str());
  _layoutRequestID = -1;
  if (_stopper != NULL) {
    _stopper->stepDone();
  }
}

void PointCloudsRenderer::TileLayout::onCancel(const URL& url) {
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
  if (_visibleTilesNeedsInitialization) {
    if (_initializationTimer == NULL) {
      _initializationTimer = rc->getFactory()->createTimer();
    }
    else {
      _initializationTimer->start();
    }

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

#warning DGD at work: render nodes

//  for (std::map<std::string, TileLayout*>::iterator it = _visibleTiles.begin();
//       it != _visibleTiles.end();
//       it++) {
//    TileLayout* tileLayout = it->second;
//    tileLayout->render(rc, glState);
//  }
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

void PointCloudsRenderer::PointCloud::createNode(const std::string& nodeID) {
  ILogger::instance()->logInfo(" creating node: %s", nodeID.c_str());
#warning DGD at work!
}

void PointCloudsRenderer::PointCloud::removeNode(const std::string& nodeID) {
  ILogger::instance()->logInfo(" removing node: %s", nodeID.c_str());
#warning DGD at work!
}

void PointCloudsRenderer::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                const std::vector<std::string>* tilesStoppedRendering) {
  for (int i = 0; i < _cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
  }
}

void PointCloudsRenderer::PointCloudsTileRenderingListener::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                                                  const std::vector<std::string>* tilesStoppedRendering) {
  _pointCloudsRenderer->changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
}
