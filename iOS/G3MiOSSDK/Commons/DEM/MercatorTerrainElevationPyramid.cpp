//
//  MercatorTerrainElevationPyramid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "MercatorTerrainElevationPyramid.hpp"

#include "Sector.hpp"
#include "ErrorHandling.hpp"


MercatorTerrainElevationPyramid::MercatorTerrainElevationPyramid(const int nodeWidth,
                                                                 const int nodeHeight) :
TerrainElevationPyramid(1,
                        nodeWidth,
                        nodeHeight)
{
}

TerrainElevationPyramid::Node*
MercatorTerrainElevationPyramid::createNode(const TerrainElevationPyramid::Node* parent,
                                            size_t childID) {
  if (parent == NULL) {
    // creating root node
    return new Node(NULL, // parent
                    childID,
                    Sector::FULL_SPHERE);
  }
  THROW_EXCEPTION("Man at work!");
}
