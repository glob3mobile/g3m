//
//  URLTemplateLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#ifndef __G3MiOSSDK__URLTemplateLayer__
#define __G3MiOSSDK__URLTemplateLayer__

#include "Layer.hpp"

class URLTemplateLayer : public Layer {
private:
  const std::string _urlTemplate;
  const Sector      _sector;
  const bool        _isTransparent;

#ifdef C_CODE
  mutable const IMathUtils*   _mu;
  mutable const IStringUtils* _su;
#endif
#ifdef JAVA_CODE
  private IMathUtils   _mu;
  private IStringUtils _su;
#endif

  URLTemplateLayer(const std::string&                urlTemplate,
                   const Sector&                     sector,
                   bool                              isTransparent,
                   const TimeInterval&               timeToCache,
                   bool                              readExpired,
                   LayerCondition*                   condition,
                   const LayerTilesRenderParameters* parameters);

  const std::string getPath(const Tile* tile,
                            const Sector& sector) const;

protected:
  std::string getLayerType() const {
    return "URLTemplate";
  }

  bool rawIsEquals(const Layer* that) const;

public:
  static URLTemplateLayer* newMercator(const std::string&  urlTemplate,
                                       const Sector&       sector,
                                       bool                isTransparent,
                                       const int           firstLevel,
                                       const int           maxLevel,
                                       const TimeInterval& timeToCache,
                                       bool                readExpired = true,
                                       LayerCondition*     condition = NULL);

  static URLTemplateLayer* newWGS84(const std::string&  urlTemplate,
                                    const Sector&       sector,
                                    bool                isTransparent,
                                    const int           firstLevel,
                                    const int           maxLevel,
                                    const TimeInterval& timeToCache,
                                    bool                readExpired = true,
                                    LayerCondition*     condition = NULL);

  const std::string description() const;

  URLTemplateLayer* copy() const;

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const Tile* tile) const;
  
};

#endif
