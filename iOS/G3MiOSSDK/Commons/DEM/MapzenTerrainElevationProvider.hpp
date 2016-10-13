//
//  MapzenTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#ifndef MapzenTerrainElevationProvider_hpp
#define MapzenTerrainElevationProvider_hpp

#include "TerrainElevationProvider.hpp"


class MapzenTerrainElevationProvider : public TerrainElevationProvider {
public:
  ~MapzenTerrainElevationProvider();

  bool isReadyToRender(const G3MRenderContext* rc);

  void initialize(const G3MContext* context);


};

#endif
