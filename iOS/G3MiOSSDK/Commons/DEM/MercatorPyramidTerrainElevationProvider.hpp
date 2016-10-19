//
//  MercatorPyramidTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef MercatorPyramidTerrainElevationProvider_hpp
#define MercatorPyramidTerrainElevationProvider_hpp

#include "PyramidTerrainElevationProvider.hpp"

class MercatorPyramidTerrainElevationProvider : public PyramidTerrainElevationProvider {
protected:
  MercatorPyramidTerrainElevationProvider::Node*
  createNode(const MercatorPyramidTerrainElevationProvider::Node* parent,
             const size_t childID);

public:
  MercatorPyramidTerrainElevationProvider();

};

#endif
