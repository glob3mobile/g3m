//
//  LayerTilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//

#ifndef __G3MiOSSDK__LayerTilesRenderParameters__
#define __G3MiOSSDK__LayerTilesRenderParameters__

#include <vector>

#include "Sector.hpp"
#include "Vector2I.hpp"

class LayerTilesRenderParameters {
private:
  static const Vector2I calculateTopSectorSplitsParametersWGS84(const Sector& topSector);
public:
  const Sector _topSector;
  const int    _topSectorSplitsByLatitude;
  const int    _topSectorSplitsByLongitude;
  const int    _firstLevel;
  const int    _maxLevel;
  const int    _maxLevelForPoles;
#ifdef C_CODE
  const Vector2I _tileTextureResolution;
  const Vector2I _tileMeshResolution;
#endif
#ifdef JAVA_CODE
  public final Vector2I _tileTextureResolution;
  public final Vector2I _tileMeshResolution;
#endif
  const bool _mercator;

  LayerTilesRenderParameters(const Sector&   topSector,
                             const int       topSectorSplitsByLatitude,
                             const int       topSectorSplitsByLongitude,
                             const int       firstLevel,
                             const int       maxLevel,
                             const Vector2I& tileTextureResolution,
                             const Vector2I& tileMeshResolution,
                             const bool      mercator) :
  _topSector(topSector),
  _topSectorSplitsByLatitude(topSectorSplitsByLatitude),
  _topSectorSplitsByLongitude(topSectorSplitsByLongitude),
  _firstLevel(firstLevel),
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

  
  static LayerTilesRenderParameters* createDefaultWGS84(const int firstLevel,
                                                        const int maxLevel) {
    return createDefaultWGS84(Sector::fullSphere(), firstLevel, maxLevel);
  }

  static LayerTilesRenderParameters* createDefaultWGS84(const Sector& topSector,
                                                        const int firstLevel,
                                                        const int maxLevel) {
    const Vector2I splitsParameters = calculateTopSectorSplitsParametersWGS84(topSector);
    const int  topSectorSplitsByLatitude  = splitsParameters._x;
    const int  topSectorSplitsByLongitude = splitsParameters._y;
    const bool mercator = false;

    return new LayerTilesRenderParameters(topSector,
                                          topSectorSplitsByLatitude,
                                          topSectorSplitsByLongitude,
                                          firstLevel,
                                          maxLevel,
                                          LayerTilesRenderParameters::defaultTileTextureResolution(),
                                          LayerTilesRenderParameters::defaultTileMeshResolution(),
                                          mercator);
  }
  
  static LayerTilesRenderParameters* createDefaultWGS84(const Sector& topSector,
                                                        const int topSectorSplitsByLatitude,
                                                        const int topSectorSplitsByLongitude,
                                                        const int firstLevel,
                                                        const int maxLevel) {
    const bool mercator = false;
    
    return new LayerTilesRenderParameters(topSector,
                                          topSectorSplitsByLatitude,
                                          topSectorSplitsByLongitude,
                                          firstLevel,
                                          maxLevel,
                                          LayerTilesRenderParameters::defaultTileTextureResolution(),
                                          LayerTilesRenderParameters::defaultTileMeshResolution(),
                                          mercator);
  }

  static LayerTilesRenderParameters* createDefaultMercator(const int firstLevel,
                                                           const int maxLevel) {
    const Sector topSector = Sector::fullSphere();
    const int  topSectorSplitsByLatitude  = 1;
    const int  topSectorSplitsByLongitude = 1;
    const bool mercator = true;
    
    return new LayerTilesRenderParameters(topSector,
                                          topSectorSplitsByLatitude,
                                          topSectorSplitsByLongitude,
                                          firstLevel,
                                          maxLevel,
                                          LayerTilesRenderParameters::defaultTileTextureResolution(),
                                          LayerTilesRenderParameters::defaultTileMeshResolution(),
                                          mercator);
  }

  static const std::vector<const LayerTilesRenderParameters*> createDefaultMultiProjection(const int mercatorFirstLevel = 2,
                                                                                           const int mercatorMaxLevel   = 18,
                                                                                           const int wgs84firstLevel    = 0,
                                                                                           const int wgs84maxLevel      = 18) {
    std::vector<const LayerTilesRenderParameters*> result;
    result.push_back( LayerTilesRenderParameters::createDefaultWGS84(wgs84firstLevel,
                                                                     wgs84maxLevel) );  // WGS84 tiles-pyramid layout is preferred
    result.push_back( LayerTilesRenderParameters::createDefaultMercator(mercatorFirstLevel,
                                                                        mercatorMaxLevel) );
    return result;
  }

  ~LayerTilesRenderParameters() {
  }


  bool isEquals(const LayerTilesRenderParameters* that) const;

  LayerTilesRenderParameters* copy() const;
  
};

#endif
