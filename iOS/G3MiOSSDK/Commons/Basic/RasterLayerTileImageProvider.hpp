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

class RasterLayer;
class IDownloader;

class RasterLayerTileImageProvider : public TileImageProvider {
private:
  const RasterLayer* _layer;
  IDownloader*       _downloader;

public:

  RasterLayerTileImageProvider(const RasterLayer* layer,
                               IDownloader* downloader) :
  _layer(layer),
  _downloader(downloader)
  {
  }

  TileImageContribution contribution(const Tile* tile);

  void create(const LayerTilesRenderParameters* layerTilesRenderParameters,
              const Tile* tile,
              const TileImageContribution& contribution,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              TileImageListener* listener,
              bool deleteListener);

  void cancel(const Tile* tile);

};

#endif
