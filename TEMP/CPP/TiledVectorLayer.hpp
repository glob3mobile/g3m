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
#include "TimeInterval.hpp"
#include "URL.hpp"
class TileImageContribution;
class IDownloader;
class IBufferDownloadListener;
class IStringUtils;
class GEORasterSymbolizer;
class TiledVectorLayerTileImageProvider;

class TiledVectorLayer : public VectorLayer {
private:
#ifdef C_CODE
  const GEORasterSymbolizer* _symbolizer;
#endif
#ifdef JAVA_CODE
  private GEORasterSymbolizer _symbolizer;
#endif
  const std::string          _urlTemplate;
  const Sector               _dataSector;
#ifdef C_CODE
  const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
  private final TimeInterval _timeToCache;
#endif
  const bool                 _readExpired;

#ifdef C_CODE
  mutable const IMathUtils*   _mu;
  mutable const IStringUtils* _su;
#endif
#ifdef JAVA_CODE
  private IMathUtils   _mu;
  private IStringUtils _su;
#endif
  mutable TiledVectorLayerTileImageProvider* _tileImageProvider;

  TiledVectorLayer(const GEORasterSymbolizer*                            symbolizer,
                   const std::string&                                    urlTemplate,
                   const Sector&                                         dataSector,
                   const std::vector<const LayerTilesRenderParameters*>& parametersVector,
                   const TimeInterval&                                   timeToCache,
                   const bool                                            readExpired,
                   const float                                           transparency,
                   const LayerCondition*                                 condition,
                   std::vector<const Info*>*                             layerInfo);

  const URL createURL(const Tile* tile) const;

protected:
  std::string getLayerType() const {
    return "TiledVectorLayer";
  }

  bool rawIsEquals(const Layer* that) const;


public:

  static TiledVectorLayer* newMercator(const GEORasterSymbolizer* symbolizer,
                                       const std::string&         urlTemplate,
                                       const Sector&              dataSector,
                                       const int                  firstLevel,
                                       const int                  maxLevel,
                                       const TimeInterval&        timeToCache  = TimeInterval::fromDays(30),
                                       const bool                 readExpired  = true,
                                       const float                transparency = 1,
                                       const LayerCondition*      condition    = NULL,
                                       std::vector<const Info*>*  layerInfo    = new std::vector<const Info*>());

  ~TiledVectorLayer();

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  RenderState getRenderState();

  const std::string description() const;

  TiledVectorLayer* copy() const;

  const TileImageContribution* contribution(const Tile* tile) const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  class RequestGEOJSONBufferData {
  public:
#ifdef C_CODE
    const URL          _url;
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    public final URL          _url;
    public final TimeInterval _timeToCache;
#endif
    const bool         _readExpired;

    RequestGEOJSONBufferData(const URL&          url,
                             const TimeInterval& timeToCache,
                             const bool          readExpired) :
    _url(url),
    _timeToCache(timeToCache),
    _readExpired(readExpired)
    {
    }
  };

  RequestGEOJSONBufferData* getRequestGEOJSONBufferData(const Tile* tile) const;

  const GEORasterSymbolizer*  symbolizerCopy() const;

  const Sector getDataSector() const {
    return _dataSector;
  }

  void setSymbolizer(const GEORasterSymbolizer* symbolizer,
                     bool deletePrevious);

  const std::vector<URL*> getDownloadURLs(const Tile* tile) const;

};

#endif
