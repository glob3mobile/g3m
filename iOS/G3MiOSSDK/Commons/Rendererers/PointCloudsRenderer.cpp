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
      const std::string name = jsonObject->getAsString("name", "");
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

PointCloudsRenderer::PointCloudNodesLayoutFetcher::PointCloudNodesLayoutFetcher(IDownloader* downloader,
                                                                                PointCloud* pointCloud,
                                                                                const std::vector<const Tile*>& tilesStartedRendering,
                                                                                const std::vector<const Tile*>& tilesStoppedRendering) :
_pointCloud(pointCloud)
{
  const int tilesStartedRenderingSize = tilesStartedRendering.size();
  for (int i = 0; i < tilesStartedRenderingSize; i++) {
    const Tile* tile = tilesStartedRendering[i];

    const std::string quadKey = BingMapsLayer::getQuadKey(tile);

//downloader->requestBuffer(<#const URL &url#>,
//                          <#long long priority#>,
//                          <#const TimeInterval &timeToCache#>,
//                          <#bool readExpired#>,
//                          <#IBufferDownloadListener *listener#>,
//                          <#bool deleteListener#>);
  }
}


void PointCloudsRenderer::PointCloud::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                            const std::vector<const Tile*>* tilesStoppedRendering) {
#warning DGD at work!
  if (_sector) {
    //ILogger::instance()->logInfo("changedTileRendering");
    const int tilesStartedRenderingSize = tilesStartedRendering->size();
    for (int i = 0; i < tilesStartedRenderingSize; i++) {
      const Tile* tile = tilesStartedRendering->at(i);
      if (tile->_sector.touchesWith(*_sector)) {
        ILogger::instance()->logInfo("   Start rendering tile " + tile->_id + " for cloud " + _cloudName);
        _tilesStartedRendering.push_back(tile);
      }
    }

    const int tilesStoppedRenderingSize = tilesStoppedRendering->size();
    for (int i = 0; i < tilesStoppedRenderingSize; i++) {
      const Tile* tile = tilesStoppedRendering->at(i);
      if (tile->_sector.touchesWith(*_sector)) {
        ILogger::instance()->logInfo("   Stop rendering tile " + tile->_id + " for cloud " + _cloudName);
        _tilesStoppedRendering.push_back(tile);
      }
    }

    if (!_tilesStartedRendering.empty() || !_tilesStoppedRendering.empty()) {
      PointCloudNodesLayoutFetcher* nodesLayoutFetcher = new PointCloudNodesLayoutFetcher(_downloader,
                                                                                          this,
                                                                                          _tilesStartedRendering,
                                                                                          _tilesStoppedRendering);

      _tilesStartedRendering.clear();
      _tilesStoppedRendering.clear();
    }
  }
}


void PointCloudsRenderer::PointCloud::render(const G3MRenderContext* rc,
                                             GLState* glState) {
#warning DGD at work!
}

PointCloudsRenderer::~PointCloudsRenderer() {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
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
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->initialize(_context);
  }
}

RenderState PointCloudsRenderer::getRenderState(const G3MRenderContext* rc) {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;

  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
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
}

void PointCloudsRenderer::removeAllPointClouds() {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    delete cloud;
  }
  _clouds.clear();
}

void PointCloudsRenderer::render(const G3MRenderContext* rc,
                                 GLState* glState) {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->render(rc, glState);
  }
}

void PointCloudsRenderer::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                const std::vector<const Tile*>* tilesStoppedRendering) {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
  }
}

void PointCloudsRenderer::PointCloudsTileRenderingListener::changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                                                                  const std::vector<const Tile*>* tilesStoppedRendering) {
  _pointCloudsRenderer->changedTilesRendering(tilesStartedRendering, tilesStoppedRendering);
}
