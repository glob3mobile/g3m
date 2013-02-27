//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#ifndef G3MiOSSDK_TilesRenderParameters_hpp
#define G3MiOSSDK_TilesRenderParameters_hpp

//#include "Sector.hpp"
//#include "Vector2I.hpp"

class TilesRenderParameters {
public:
  const bool      _renderDebug;
  const bool      _useTilesSplitBudget;
  const bool      _forceTopLevelTilesRenderOnStart;
  const bool      _incrementalTileQuality;

  TilesRenderParameters(const bool renderDebug,
                        const bool useTilesSplitBudget,
                        const bool forceTopLevelTilesRenderOnStart,
                        const bool incrementalTileQuality) :
  _renderDebug(renderDebug),
  _useTilesSplitBudget(useTilesSplitBudget),
  _forceTopLevelTilesRenderOnStart(forceTopLevelTilesRenderOnStart),
  _incrementalTileQuality(incrementalTileQuality)
  {

  }

  ~TilesRenderParameters() {
  }

//  static TilesRenderParameters* createDefault(const bool renderDebug,
//                                              const bool useTilesSplitBudget,
//                                              const bool forceTopLevelTilesRenderOnStart,
//                                              const bool incrementalTileQuality) {
//    return new TilesRenderParameters(renderDebug,
//                                     useTilesSplitBudget,
//                                     forceTopLevelTilesRenderOnStart,
//                                     incrementalTileQuality);
//  }

};

#endif
