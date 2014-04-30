//
//  TiledVectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#ifndef __G3MiOSSDK__TiledVectorLayer__
#define __G3MiOSSDK__TiledVectorLayer__

#include "VectorLayer.hpp"
#include "Sector.hpp"
class TileImageContribution;
class IDownloader;
class IBufferDownloadListener;
class TimeInterval;
class IStringUtils;

class TiledVectorLayer : public VectorLayer {
private:
  const std::string _urlTemplate;
  const Sector      _sector;
  const long long   _timeToCacheMS;
  const bool        _readExpired;

#ifdef C_CODE
  mutable const IMathUtils*   _mu;
  mutable const IStringUtils* _su;
#endif
#ifdef JAVA_CODE
  private IMathUtils   _mu;
  private IStringUtils _su;
#endif

  TiledVectorLayer(const std::string&                urlTemplate,
                   const Sector&                     sector,
                   const LayerTilesRenderParameters* parameters,
                   const TimeInterval&               timeToCache,
                   const bool                        readExpired,
                   const float                       transparency,
                   const LayerCondition*             condition);

  const URL createURL(const Tile* tile) const;

protected:
  std::string getLayerType() const {
    return "TiledVectorLayer";
  }

  bool rawIsEquals(const Layer* that) const;


public:
  static TiledVectorLayer* newMercator(const std::string&    urlTemplate,
                                       const Sector&         sector,
                                       const int             firstLevel,
                                       const int             maxLevel,
                                       const TimeInterval&   timeToCache,
                                       const bool            readExpired,
                                       const float           transparency = 1,
                                       const LayerCondition* condition    = NULL);

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  RenderState getRenderState();

  const std::string description() const;

  TiledVectorLayer* copy() const;

  const TileImageContribution* contribution(const Tile* tile) const;

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                const Tile* tile) const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  long long requestGEOJSONBuffer(const Tile* tile,
                                 IDownloader* downloader,
                                 long long tileDownloadPriority,
                                 bool logDownloadActivity,
                                 IBufferDownloadListener* listener,
                                 bool deleteListener) const;
  
};

#endif
