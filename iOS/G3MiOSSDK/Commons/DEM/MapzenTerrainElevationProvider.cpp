//
//  MapzenTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

#include "MapzenTerrainElevationProvider.hpp"

#include "ErrorHandling.hpp"


MapzenTerrainElevationProvider::~MapzenTerrainElevationProvider() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool MapzenTerrainElevationProvider::isReadyToRender(const G3MRenderContext* rc) {
  THROW_EXCEPTION("Diego at work!");
}

void MapzenTerrainElevationProvider::initialize(const G3MContext* context) {
  THROW_EXCEPTION("Diego at work!");
}
