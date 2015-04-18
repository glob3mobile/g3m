//
//  GoogleMapsLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#ifndef __G3MiOSSDK__GoogleMapsLayer__
#define __G3MiOSSDK__GoogleMapsLayer__

#include "RasterLayer.hpp"

class GoogleMapsLayer : public RasterLayer {
private:
  const std::string _key;
  const int         _initialLevel;

protected:
  std::string getLayerType() const {
    return "GoogleMaps";
  }

  bool rawIsEquals(const Layer* that) const;


  const TileImageContribution* rawContribution(const Tile* tile) const;

  const URL createURL(const Tile* tile) const;

public:

  GoogleMapsLayer(const std::string&    key,
                  const TimeInterval&   timeToCache,
                  const bool            readExpired    = true,
                  const int             initialLevel   = 2,
                  const float           transparency   = 1,
                  const LayerCondition* condition      = NULL,
                  std::vector<const Info*>*  layerInfo = new std::vector<const Info*>());

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;


  const std::string description() const;

  GoogleMapsLayer* copy() const;

  RenderState getRenderState();

  const Sector getDataSector() const {
    return Sector::fullSphere();
  }
  
};

#endif
