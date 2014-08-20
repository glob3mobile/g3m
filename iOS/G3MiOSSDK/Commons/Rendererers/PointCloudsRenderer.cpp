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
#include "DownloadPriority.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "Sector.hpp"
#include "Tile.hpp"


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
  IDownloader* downloader = context->getDownloader();
#warning TODO: allows cache
  _downloadingMetadata = true;
  _errorDownloadingMetadata = false;
  _errorParsingMetadata = false;

  const URL metadataURL(_serverURL, _cloudName);

  downloader->requestBuffer(metadataURL,
                            DownloadPriority::HIGHEST,
                            TimeInterval::zero(),
                            true,
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


void PointCloudsRenderer::PointCloud::changedTileRendering(const std::vector<const Tile*>* started,
                                                           const std::vector<const Tile*>* stopped) {
#warning DGD at work!
  if (_sector) {
    ILogger::instance()->logInfo("changedTileRendering");
    for (int i = 0; i < started->size(); i++) {
      const Tile* tile = started->at(i);
      if (tile->_sector.touchesWith(*_sector)) {
        ILogger::instance()->logInfo("   Start rendering tile " + tile->_id + " for cloud " + _cloudName);
      }
    }

    for (int i = 0; i < stopped->size(); i++) {
      const Tile* tile = stopped->at(i);
      if (tile->_sector.touchesWith(*_sector)) {
        ILogger::instance()->logInfo("   Stop rendering tile " + tile->_id + " for cloud " + _cloudName);
      }
    }
  }
}

//void PointCloudsRenderer::PointCloud::startRendering(const Tile* tile) {
//  if (_sector) {
//    if (tile->_sector.touchesWith(*_sector)) {
//#warning DGD at work!
//      ILogger::instance()->logInfo("Start rendering tile " + tile->_id + " for cloud " + _cloudName);
//    }
//  }
//}
//
//void PointCloudsRenderer::PointCloud::stopRendering(const Tile* tile) {
//  if (_sector) {
//    if (tile->_sector.touchesWith(*_sector)) {
//#warning DGD at work!
//      ILogger::instance()->logInfo("Stop rendering tile " + tile->_id + " for cloud " + _cloudName);
//    }
//  }
//}

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
  PointCloud* pointCloud = new PointCloud(serverURL, cloudName);
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

//void PointCloudsRenderer::startRendering(const Tile* tile) {
//  const int cloudsSize = _clouds.size();
//  for (int i = 0; i < cloudsSize; i++) {
//    PointCloud* cloud = _clouds[i];
//    cloud->startRendering(tile);
//  }
//}
//
//void PointCloudsRenderer::stopRendering(const Tile* tile) {
//  const int cloudsSize = _clouds.size();
//  for (int i = 0; i < cloudsSize; i++) {
//    PointCloud* cloud = _clouds[i];
//    cloud->stopRendering(tile);
//  }
//}

void PointCloudsRenderer::changedTileRendering(const std::vector<const Tile*>* started,
                                               const std::vector<const Tile*>* stopped) {
  const int cloudsSize = _clouds.size();
  for (int i = 0; i < cloudsSize; i++) {
    PointCloud* cloud = _clouds[i];
    cloud->changedTileRendering(started, stopped);
  }
}
