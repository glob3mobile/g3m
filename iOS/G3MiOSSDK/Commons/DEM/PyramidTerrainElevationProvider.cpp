//
//  PyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidTerrainElevationProvider.hpp"

#include "ErrorHandling.hpp"


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

bool PyramidTerrainElevationProvider::Node::insertGrid(TerrainElevationGrid* grid,
                                                       const bool            sticky,
                                                       const int             gridWidth,
                                                       const int             gridHeight) {
#error Diego at work!
}

PyramidTerrainElevationProvider::PyramidTerrainElevationProvider(const size_t rootNodesCount,
                                                                 const int    gridWidth,
                                                                 const int    gridHeight) :
_rootNodesCount(rootNodesCount),
_gridWidth(gridWidth),
_gridHeight(gridHeight)
{
  for (size_t i = 0; i < _rootNodesCount; i++) {
    Node* rootNode = createNode(NULL, i);
    _rootNodes.push_back(rootNode);
  }
}

PyramidTerrainElevationProvider::~PyramidTerrainElevationProvider() {
  for (size_t i = 0; i < _rootNodesCount; i++) {
    Node* rootNode = _rootNodes[i];
    delete rootNode;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PyramidTerrainElevationProvider::insertGrid(TerrainElevationGrid* grid,
                                                 const bool sticky) {
  for (size_t i = 0; i < _rootNodesCount; i++) {
    Node* rootNode = _rootNodes[i];
    if (rootNode->insertGrid(grid, sticky, _gridWidth, _gridHeight)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}
