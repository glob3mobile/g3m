//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#ifndef G3MiOSSDK_TilesRenderParameters_hpp
#define G3MiOSSDK_TilesRenderParameters_hpp

#include "Sector.hpp"
#include "Vector2I.hpp"

class TilesRenderParameters {
public:
  const Sector    _topSector;
  const int       _splitsByLatitude;
  const int       _splitsByLongitude;
  const int       _topLevel;
  const int       _maxLevel;
  const Vector2I  _tileTextureResolution;
  const Vector2I  _tileMeshResolution;
  const bool      _renderDebug;
  const bool      _useTilesSplitBudget;
  const bool      _forceTopLevelTilesRenderOnStart;
  const bool      _incrementalTileQuality;

  TilesRenderParameters(const Sector    topSector,
                        const int       splitsByLatitude,
                        const int       splitsByLongitude,
                        const int       topLevel,
                        const int       maxLevel,
                        const Vector2I& tileTextureResolution,
                        const Vector2I& tileMeshResolution,
                        const bool      renderDebug,
                        const bool      useTilesSplitBudget,
                        const bool      forceTopLevelTilesRenderOnStart,
                        const bool      incrementalTileQuality) :
  _topSector(topSector),
  _splitsByLatitude(splitsByLatitude),
  _splitsByLongitude(splitsByLongitude),
  _topLevel(topLevel),
  _maxLevel(maxLevel),
  _tileTextureResolution(tileTextureResolution),
  _tileMeshResolution(tileMeshResolution),
  _renderDebug(renderDebug),
  _useTilesSplitBudget(useTilesSplitBudget),
  _forceTopLevelTilesRenderOnStart(forceTopLevelTilesRenderOnStart),
  _incrementalTileQuality(incrementalTileQuality)
  {

  }

  ~TilesRenderParameters() {
  }

  static TilesRenderParameters* createDefault(const bool renderDebug,
                                              const bool useTilesSplitBudget,
                                              const bool forceTopLevelTilesRenderOnStart,
                                              const bool incrementalTileQuality) {
    const int K = 1;
    //const int _TODO_RESET_K_TO_1 = 0;
    const int splitsByLatitude = 1 * K;
    const int splitsByLongitude = 2 * K;
    const int topLevel = 0;
    const int maxLevel = 17;
    const Vector2I tileTextureResolution(256, 256);
    const Vector2I tileMeshResolution(20, 20);

    return new TilesRenderParameters(Sector::fullSphere(),
                                     splitsByLatitude,
                                     splitsByLongitude,
                                     topLevel,
                                     maxLevel,
                                     tileTextureResolution,
                                     tileMeshResolution,
                                     renderDebug,
                                     useTilesSplitBudget,
                                     forceTopLevelTilesRenderOnStart,
                                     incrementalTileQuality);
  }


  static TilesRenderParameters* createSingleSector(const bool renderDebug,
                                                   const bool useTilesSplitBudget,
                                                   const bool forceTopLevelTilesRenderOnStart,
                                                   const bool incrementalQuality) {
    const int splitsByLatitude = 1;
    const int splitsByLongitude = 1;
    const int topLevel = 0;
    const int maxLevel = 2;
    const Vector2I tileTextureResolution(256, 256);
    const Vector2I tileMeshResolution(20, 20);

    //    Sector sector = Sector(Geodetic2D(Angle::fromDegrees(-90), Angle::fromDegrees(-180)),
    //                           Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(180)));
    Sector sector = Sector(Geodetic2D(Angle::zero(), Angle::zero()),
                           Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(90)));

    return new TilesRenderParameters(sector,
                                     splitsByLatitude,
                                     splitsByLongitude,
                                     topLevel,
                                     maxLevel,
                                     tileTextureResolution,
                                     tileMeshResolution,
                                     renderDebug,
                                     useTilesSplitBudget,
                                     forceTopLevelTilesRenderOnStart,
                                     incrementalQuality);
  }

};

#endif
