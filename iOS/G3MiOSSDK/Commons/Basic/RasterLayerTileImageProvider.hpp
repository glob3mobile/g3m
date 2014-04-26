//
//  RasterLayerTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#ifndef __G3MiOSSDK__RasterLayerTileImageProvider__
#define __G3MiOSSDK__RasterLayerTileImageProvider__

#include "TileImageProvider.hpp"

#include <map>
#include <string>

class RasterLayer;
class IDownloader;


class RasterLayerTileImageProvider : public TileImageProvider {
private:
  const RasterLayer* _layer;
  IDownloader*       _downloader;

  std::map<std::string, long long> _requestsIdsPerTile;

protected:
  ~RasterLayerTileImageProvider();

public:

  RasterLayerTileImageProvider(const RasterLayer* layer,
                               IDownloader* downloader) :
  _layer(layer),
  _downloader(downloader)
  {
  }

  const TileImageContribution* contribution(const Tile* tile);

  void create(const Tile* tile,
              const TileImageContribution* contribution,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener);

  void cancel(const Tile* tile);


  void requestFinish(const std::string& tileId);

};

#endif
