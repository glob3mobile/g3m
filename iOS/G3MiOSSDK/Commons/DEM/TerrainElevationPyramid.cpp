//
//  TerrainElevationPyramid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "TerrainElevationPyramid.hpp"


TerrainElevationPyramid::Node::Node(const TerrainElevationPyramid::Node* parent,
                                    const size_t childID,
                                    const Sector& sector) :
_parent(parent),
_childID(childID),
_sector(sector)
{

}

TerrainElevationPyramid::Node::~Node() {

}

TerrainElevationPyramid::TerrainElevationPyramid(const size_t rootNodesCount)
{
  for (size_t i = 0; i < rootNodesCount; i++) {
    Node* rootNode = createNode(NULL, i);
    _rootNodes.push_back(rootNode);
  }
}

TerrainElevationPyramid::~TerrainElevationPyramid() {
  const size_t size = _rootNodes.size();
  for (size_t i = 0; i < size; i++) {
    Node* rootNode = _rootNodes[i];
    delete rootNode;
  }
}
