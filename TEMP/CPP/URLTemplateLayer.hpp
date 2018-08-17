//
//  URLTemplateLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#ifndef __G3MiOSSDK__URLTemplateLayer__
#define __G3MiOSSDK__URLTemplateLayer__

#include "RasterLayer.hpp"

#include "Sector.hpp"
class IStringUtils;


class URLTemplateLayer : public RasterLayer {
private:
  const std::string _urlTemplate;
  const bool        _isTransparent;
  const bool        _tiled;

#ifdef C_CODE
  mutable const IMathUtils*   _mu;
  mutable const IStringUtils* _su;
#endif
#ifdef JAVA_CODE
  private IMathUtils   _mu;
  private IStringUtils _su;
#endif

  const Sector _dataSector;

protected:
  std::string getLayerType() const {
    return "URLTemplate";
  }

  bool rawIsEquals(const Layer* that) const;

  const TileImageContribution* rawContribution(const Tile* tile) const;

  const URL createURL(const Tile* tile) const;

public:
  static URLTemplateLayer* newMercator(const std::string&         urlTemplate,
                                       const Sector&              dataSector,
                                       const bool                 isTransparent,
                                       const int                  firstLevel,
                                       const int                  maxLevel,
                                       const TimeInterval&        timeToCache,
                                       const bool                 readExpired  = true,
                                       const float                transparency = 1,
                                       const LayerCondition*      condition    = NULL,
                                       std::vector<const Info*>*  layerInfo    = new std::vector<const Info*>());

  static URLTemplateLayer* newWGS84(const std::string&        urlTemplate,
                                    const Sector&             dataSector,
                                    const bool                isTransparent,
                                    const int                 firstLevel,
                                    const int                 maxLevel,
                                    const TimeInterval&       timeToCache,
                                    const bool                readExpired  = true,
                                    const LayerCondition*     condition    = NULL,
                                    const float               transparency = 1,
                                    std::vector<const Info*>* layerInfo    = new std::vector<const Info*>());

  URLTemplateLayer(const std::string&                urlTemplate,
                   const Sector&                     dataSector,
                   const bool                        isTransparent,
                   const TimeInterval&               timeToCache,
                   const bool                        readExpired,
                   const LayerCondition*             condition,
                   const LayerTilesRenderParameters* parameters,
                   float                             transparency = 1,
                   std::vector<const Info*>*         layerInfo    = new std::vector<const Info*>());

  const std::string description() const;

  URLTemplateLayer* copy() const;

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;
  
  RenderState getRenderState();

  const Sector getDataSector() const {
    return _dataSector;
  }

};

#endif
