//
//  PyramidDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidDEMProvider.hpp"

#include "ErrorHandling.hpp"
#include "DEMPyramidNode.hpp"


PyramidDEMProvider::PyramidDEMProvider(const double deltaHeight,
                                       const size_t rootNodesCount) :
DEMProvider(deltaHeight),
_rootNodesCount(rootNodesCount),
_rootNodes(NULL)
{
}

std::vector<DEMPyramidNode*>* PyramidDEMProvider::getRootNodes() {
  if (_rootNodes == NULL) {
    _rootNodes = new std::vector<DEMPyramidNode*>();
    for (size_t i = 0; i < _rootNodesCount; i++) {
      DEMPyramidNode* rootNode = createNode(NULL, i);
      _rootNodes->push_back(rootNode);
    }
  }
  return _rootNodes;
}

PyramidDEMProvider::~PyramidDEMProvider() {
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesCount; i++) {
      DEMPyramidNode* rootNode = _rootNodes->at(i);
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
  std::vector<DEMPyramidNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    DEMPyramidNode* rootNode = rootNodes->at(i);
    if (rootNode->insertGrid(z, x, y,
                             grid, sticky,
                             this)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}

long long PyramidDEMProvider::subscribe(const Sector&   sector,
                                        const Vector2I& extent,
                                        DEMListener*    listener) {
  THROW_EXCEPTION("Not yet done");
}

void PyramidDEMProvider::unsubscribe(const long long subscriptionID,
                                     const bool      deleteListener) {
  THROW_EXCEPTION("Not yet done");
}
