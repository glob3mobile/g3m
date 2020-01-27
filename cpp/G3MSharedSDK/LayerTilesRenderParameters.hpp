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
#include "Vector2S.hpp"

class LayerTilesRenderParameters {
private:
  static const Vector2S calculateTopSectorSplitsParametersWGS84(const Sector& topSector);
public:
  const Sector   _topSector;
  const int      _topSectorSplitsByLatitude;
  const int      _topSectorSplitsByLongitude;
  const int      _firstLevel;
  const int      _maxLevel;
  const int      _maxLevelForPoles;
  const Vector2S _tileTextureResolution;
  const Vector2S _tileMeshResolution;
  const bool     _mercator;

  LayerTilesRenderParameters(const Sector&   topSector,
                             const int       topSectorSplitsByLatitude,
                             const int       topSectorSplitsByLongitude,
                             const int       firstLevel,
                             const int       maxLevel,
                             const Vector2S& tileTextureResolution,
                             const Vector2S& tileMeshResolution,
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

  static const Vector2S defaultTileMeshResolution() {
    //return Vector2S((short)16, (short)16);
    return Vector2S((short)32, (short)32);
  }

  static const Vector2S defaultTileTextureResolution () {
    return Vector2S((short)256, (short)256);
  }

  
  static LayerTilesRenderParameters* createDefaultWGS84(const int firstLevel,
                                                        const int maxLevel) {
    return createDefaultWGS84(Sector::FULL_SPHERE, firstLevel, maxLevel);
  }

  static LayerTilesRenderParameters* createDefaultWGS84(const Sector& topSector,
                                                        const int firstLevel,
                                                        const int maxLevel) {
    const Vector2S splitsParameters = calculateTopSectorSplitsParametersWGS84(topSector);
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
    const Sector topSector = Sector::FULL_SPHERE;
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
