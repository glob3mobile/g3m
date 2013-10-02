//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#ifndef G3MiOSSDK_TilesRenderParameters_hpp
#define G3MiOSSDK_TilesRenderParameters_hpp


enum Quality {
  QUALITY_LOW,
  QUALITY_MEDIUM,
  QUALITY_HIGH
};


class TilesRenderParameters {
public:
  const bool    _renderDebug;
  const bool    _useTilesSplitBudget;
  const bool    _forceFirstLevelTilesRenderOnStart;
  const bool    _incrementalTileQuality;
  const Quality _quality;

  TilesRenderParameters(const bool    renderDebug,
                        const bool    useTilesSplitBudget,
                        const bool    forceFirstLevelTilesRenderOnStart,
                        const bool    incrementalTileQuality,
                        const Quality quality) :
  _renderDebug(renderDebug),
  _useTilesSplitBudget(useTilesSplitBudget),
  _forceFirstLevelTilesRenderOnStart(forceFirstLevelTilesRenderOnStart),
  _incrementalTileQuality(incrementalTileQuality),
  _quality(quality)
  {
  }

  ~TilesRenderParameters() {
  }

};

#endif
