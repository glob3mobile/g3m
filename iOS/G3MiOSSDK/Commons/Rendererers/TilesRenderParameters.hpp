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
  const bool _renderIncompletePlanet;
#ifdef C_CODE
  const URL  _incompletePlanetTexureURL;
#endif
#ifdef JAVA_CODE
  public final URL _incompletePlanetTexureURL;
#endif

  TilesRenderParameters(const bool renderDebug,
                        const bool useTilesSplitBudget,
                        const bool forceFirstLevelTilesRenderOnStart,
                        const bool incrementalTileQuality,
                        const bool renderIncompletePlanet,
                        const URL& incompletePlanetTexureURL) :
  _renderDebug(renderDebug),
  _useTilesSplitBudget(useTilesSplitBudget),
  _forceFirstLevelTilesRenderOnStart(forceFirstLevelTilesRenderOnStart),
  _incrementalTileQuality(incrementalTileQuality),
  _renderIncompletePlanet(renderIncompletePlanet),
  _incompletePlanetTexureURL(incompletePlanetTexureURL)
  {

  }

  ~TilesRenderParameters() {
  }

};

#endif
