//
//  PyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidTerrainElevationProvider.hpp"

#include "ErrorHandling.hpp"
#include "PyramidTerrainElevationNode.hpp"


PyramidTerrainElevationProvider::PyramidTerrainElevationProvider(const size_t rootNodesCount) :
_rootNodesCount(rootNodesCount),
_rootNodes(NULL)
{
}

std::vector<PyramidTerrainElevationNode*>* PyramidTerrainElevationProvider::getRootNodes() {
  if (_rootNodes == NULL) {
    _rootNodes = new std::vector<PyramidTerrainElevationNode*>();
    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidTerrainElevationNode* rootNode = createNode(NULL, i);
      _rootNodes->push_back(rootNode);
    }
  }
  return _rootNodes;
}


PyramidTerrainElevationProvider::~PyramidTerrainElevationProvider() {
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidTerrainElevationNode* rootNode = _rootNodes->at(i);
      delete rootNode;
    }
    delete _rootNodes;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PyramidTerrainElevationProvider::insertGrid(int z, int x, int y,
                                                 TerrainElevationGrid* grid,
                                                 const bool sticky) {
  std::vector<PyramidTerrainElevationNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    PyramidTerrainElevationNode* rootNode = rootNodes->at(i);
    if (rootNode->insertGrid(z, x, y,
                             grid, sticky,
                             this)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}
