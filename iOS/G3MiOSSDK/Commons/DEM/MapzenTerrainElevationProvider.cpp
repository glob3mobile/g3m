//
//  MapzenTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#include "MapzenTerrainElevationProvider.hpp"

#include "RenderState.hpp"
#include "G3MContext.hpp"
#include "IDownloader.hpp"
#include "URL.hpp"
#include "IImageDownloadListener.hpp"
#include "IImage.hpp"
#include "MutableColor255.hpp"
#include "FloatBufferTerrainElevationGrid.hpp"


int MapzenTerrainElevationProvider::_idCounter = 0;


class MapzenTerrainElevationProvider_ImageDownloadListener : public IImageDownloadListener {
  MapzenTerrainElevationProvider* _provider;
  const Sector _sector;
  const double _deltaHeight;

public:
  MapzenTerrainElevationProvider_ImageDownloadListener(MapzenTerrainElevationProvider* provider,
                                                       const Sector& sector,
                                                       double deltaHeight) :
  _provider(provider),
  _sector(sector),
  _deltaHeight(deltaHeight)
  {
    _provider->_retain();
  }

  virtual ~MapzenTerrainElevationProvider_ImageDownloadListener() {
    _provider->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {

    MutableColor255 pixel(0,0,0,0);

    const int width  = image->getWidth();
    const int height = image->getHeight();

    const int bufferSize = width * height;

    float* buffer = new float[bufferSize];

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image->getPixel(x, y, pixel);
        const float elevation = ((pixel._red * 256.0f) + pixel._green + (pixel._blue / 256.0f)) - 32768.0f;
        const int index = ((height-1-y) * width) + x;
        buffer[index] = elevation;
      }
    }

    delete image;

//    _provider->onGrid( new FloatBufferTerrainElevationGrid(_sector,
//                                                           Vector2I(width, height),
//                                                           buffer,
//                                                           bufferSize,
//                                                           _deltaHeight) );
  }

  void onError(const URL& url) {
//    _provider->onDownloadError();
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }

};



MapzenTerrainElevationProvider::MapzenTerrainElevationProvider(const std::string&  apiKey,
                                                               long long           downloadPriority,
                                                               const TimeInterval& timeToCache,
                                                               bool                readExpired) :
_apiKey(apiKey),
_downloadPriority(downloadPriority),
_timeToCache(timeToCache),
_readExpired(readExpired),
_context(NULL),
_instanceID("MapzenTerrainElevationProvider_" + (++_idCounter))
{

}

MapzenTerrainElevationProvider::~MapzenTerrainElevationProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

RenderState MapzenTerrainElevationProvider::getRenderState() {
  return RenderState::error("MapzenTerrainElevationProvider: under construction");
}

void MapzenTerrainElevationProvider::initialize(const G3MContext* context) {
  _context = context;
  IDownloader* downloader = context->getDownloader();

  // https://tile.mapzen.com/mapzen/terrain/v1/terrarium/{z}/{x}/{y}.png?api_key=mapzen-xxxxxxx

  downloader->requestImage(URL("https://tile.mapzen.com/mapzen/terrain/v1/terrarium/0/0/0.png?api_key=" + _apiKey),
                           _downloadPriority,
                           _timeToCache,
                           _readExpired,
                           new MapzenTerrainElevationProvider_ImageDownloadListener(this,
                                                                                    Sector::FULL_SPHERE,
                                                                                    0 /* deltaHeight */),
                           true);
}

void MapzenTerrainElevationProvider::cancel() {
  _context->getDownloader()->cancelRequestsTagged(_instanceID);
}
