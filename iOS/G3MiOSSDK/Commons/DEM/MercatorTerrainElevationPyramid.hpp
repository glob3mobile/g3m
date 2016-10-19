//
//  MercatorTerrainElevationPyramid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef MercatorTerrainElevationPyramid_hpp
#define MercatorTerrainElevationPyramid_hpp

#include "TerrainElevationPyramid.hpp"

class MercatorTerrainElevationPyramid : public TerrainElevationPyramid {
protected:
  TerrainElevationPyramid::Node*
  createNode(const TerrainElevationPyramid::Node* parent,
             size_t childID);

public:
  MercatorTerrainElevationPyramid();

};

#endif
