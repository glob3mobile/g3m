//
//  MercatorPyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "MercatorPyramidTerrainElevationProvider.hpp"


#include "Sector.hpp"
#include "ErrorHandling.hpp"


MercatorPyramidTerrainElevationProvider::MercatorPyramidTerrainElevationProvider(const int gridWidth,
                                                                                 const int gridHeight) :
PyramidTerrainElevationProvider(1,
                                gridWidth,
                                gridHeight)
{
}

MercatorPyramidTerrainElevationProvider::Node*
MercatorPyramidTerrainElevationProvider::createNode(const MercatorPyramidTerrainElevationProvider::Node* parent,
                                                    size_t childID) {
  if (parent == NULL) {
    // creating root node
    return new Node(NULL, // parent
                    childID,
                    Sector::FULL_SPHERE);
  }
  THROW_EXCEPTION("Man at work!");
}
