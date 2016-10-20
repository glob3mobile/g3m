//
//  PyramidDEMProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidDEMProvider.hpp"

#include "ErrorHandling.hpp"
#include "PyramidNode.hpp"


PyramidDEMProvider::PyramidDEMProvider(const double deltaHeight,
                                       const size_t rootNodesCount) :
DEMProvider(deltaHeight),
_rootNodesCount(rootNodesCount),
_rootNodes(NULL)
{
}

std::vector<PyramidNode*>* PyramidDEMProvider::getRootNodes() {
  if (_rootNodes == NULL) {
    _rootNodes = new std::vector<PyramidNode*>();
    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidNode* rootNode = createNode(NULL, i);
      _rootNodes->push_back(rootNode);
    }
  }
  return _rootNodes;
}

PyramidDEMProvider::~PyramidDEMProvider() {
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidNode* rootNode = _rootNodes->at(i);
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
                                    const bool stickyGrid) {
  std::vector<PyramidNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    PyramidNode* rootNode = rootNodes->at(i);
    if (rootNode->insertGrid(z, x, y,
                             grid, stickyGrid,
                             this)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}

PyramidDEMProvider::Subscription::Subscription(const Sector&   sector,
                                               const Vector2I& extent,
                                               DEMListener*    listener,
                                               const bool      deleteListener) :
_sector(sector),
_extent(extent),
_resolution(sector._deltaLatitude.div(extent._y),
            sector._deltaLongitude.div(extent._x)),
_listener(listener),
_deleteListener(deleteListener)
{
}

PyramidDEMProvider::Subscription::~Subscription() {
  if (_deleteListener) {
    delete _listener;
  }
}

long long PyramidDEMProvider::subscribe(const Sector&   sector,
                                        const Vector2I& extent,
                                        DEMListener*    listener,
                                        const bool      deleteListener) {
//  Subscription* subscription = new Subscription(sector, extent, listener, deleteListener);
  THROW_EXCEPTION("Not yet done");
}

void PyramidDEMProvider::unsubscribe(const long long subscriptionID) {
  THROW_EXCEPTION("Not yet done");
}
