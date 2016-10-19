//
//  PyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidTerrainElevationProvider.hpp"


PyramidTerrainElevationProvider::Node::Node(const PyramidTerrainElevationProvider::Node* parent,
                                    const size_t childID,
                                    const Sector& sector) :
_parent(parent),
_childID(childID),
_sector(sector)
{

}

PyramidTerrainElevationProvider::Node::~Node() {

}

PyramidTerrainElevationProvider::PyramidTerrainElevationProvider(const size_t rootNodesCount,
                                                 const int    nodeWidth,
                                                 const int    nodeHeight) :
_nodeWidth(nodeWidth),
_nodeHeight(nodeHeight)
{
  for (size_t i = 0; i < rootNodesCount; i++) {
    Node* rootNode = createNode(NULL, i);
    _rootNodes.push_back(rootNode);
  }
}

PyramidTerrainElevationProvider::~PyramidTerrainElevationProvider() {
  const size_t size = _rootNodes.size();
  for (size_t i = 0; i < size; i++) {
    Node* rootNode = _rootNodes[i];
    delete rootNode;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}
