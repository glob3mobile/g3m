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
#include "URL.hpp"

class TilesRenderParameters {
public:
  const bool _renderDebug;
  const bool _useTilesSplitBudget;
  const bool _forceFirstLevelTilesRenderOnStart;
  const bool _incrementalTileQuality;

  TilesRenderParameters(const bool renderDebug,
                        const bool useTilesSplitBudget,
                        const bool forceFirstLevelTilesRenderOnStart,
                        const bool incrementalTileQuality) :
  _renderDebug(renderDebug),
  _useTilesSplitBudget(useTilesSplitBudget),
  _forceFirstLevelTilesRenderOnStart(forceFirstLevelTilesRenderOnStart),
  _incrementalTileQuality(incrementalTileQuality)
  {

  }

  ~TilesRenderParameters() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

};

#endif
