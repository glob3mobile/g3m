//
//  PyramidDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidDEMProvider.hpp"

#include "ErrorHandling.hpp"
#include "PyramidDEMNode.hpp"


PyramidDEMProvider::PyramidDEMProvider(const double deltaHeight,
                                       const size_t rootNodesCount) :
DEMProvider(deltaHeight),
_rootNodesCount(rootNodesCount),
_rootNodes(NULL)
{
}

std::vector<PyramidDEMNode*>* PyramidDEMProvider::getRootNodes() {
  if (_rootNodes == NULL) {
    _rootNodes = new std::vector<PyramidDEMNode*>();
    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidDEMNode* rootNode = createNode(NULL, i);
      _rootNodes->push_back(rootNode);
    }
  }
  return _rootNodes;
}

PyramidDEMProvider::~PyramidDEMProvider() {
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidDEMNode* rootNode = _rootNodes->at(i);
      delete rootNode;
    }
    delete _rootNodes;
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PyramidDEMProvider::insertGrid(int z,
                                    int x,
                                    int y,
                                    DEMGrid* grid,
                                    const bool sticky) {
  std::vector<PyramidDEMNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    PyramidDEMNode* rootNode = rootNodes->at(i);
    if (rootNode->insertGrid(z, x, y,
                             grid, sticky,
                             this)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}
