//
//  LayerTilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//

#ifndef __G3MiOSSDK__LayerTilesRenderParameters__
#define __G3MiOSSDK__LayerTilesRenderParameters__

#include "Sector.hpp"
#include "Vector2I.hpp"

class LayerTilesRenderParameters {
public:
  const Sector    _topSector;
  const int       _splitsByLatitude;
  const int       _splitsByLongitude;
  const int       _maxLevel;
  const int       _maxLevelForPoles;
#ifdef C_CODE
  const Vector2I  _tileTextureResolution;
  const Vector2I  _tileMeshResolution;
#endif
#ifdef JAVA_CODE
  public final Vector2I _tileTextureResolution;
  public final Vector2I _tileMeshResolution;
#endif
  const bool _mercator;

  LayerTilesRenderParameters(const Sector&   topSector,
                             const int       splitsByLatitude,
                             const int       splitsByLongitude,
                             const int       maxLevel,
                             const Vector2I& tileTextureResolution,
                             const Vector2I& tileMeshResolution,
                             const bool mercator) :
  _topSector(topSector),
  _splitsByLatitude(splitsByLatitude),
  _splitsByLongitude(splitsByLongitude),
  _maxLevel(maxLevel),
  _maxLevelForPoles(4),
  _tileTextureResolution(tileTextureResolution),
  _tileMeshResolution(tileMeshResolution),
  _mercator(mercator)
  {

  }

  static const Vector2I defaultTileMeshResolution() {
    return Vector2I(16, 16);
  }

  static const Vector2I defaultTileTextureResolution () {
    return Vector2I(256, 256);
  }

  static LayerTilesRenderParameters* createDefaultNonMercator(const Sector& topSector) {
    const int splitsByLatitude = 4;
    const int splitsByLongitude = 8;
    const int maxLevel = 17;
    const bool mercator = false;

    return new LayerTilesRenderParameters(topSector,
                                          splitsByLatitude,
                                          splitsByLongitude,
                                          maxLevel,
                                          LayerTilesRenderParameters::defaultTileTextureResolution(),
                                          LayerTilesRenderParameters::defaultTileMeshResolution(),
                                          mercator);
  }


  ~LayerTilesRenderParameters() {
  }
  
  
};

#endif
