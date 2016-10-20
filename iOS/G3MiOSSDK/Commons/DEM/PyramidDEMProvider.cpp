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
#include "DEMSubscription.hpp"


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

long long PyramidDEMProvider::subscribe(const Sector&   sector,
                                        const Vector2I& extent,
                                        DEMListener*    listener,
                                        const bool      deleteListener) {
  DEMSubscription* subscription = new DEMSubscription(sector, extent, listener, deleteListener);

  bool holdSubscription = false;
  std::vector<PyramidNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    PyramidNode* rootNode = rootNodes->at(i);
    if (rootNode->addSubscription(subscription)) {
      holdSubscription = true;
    }
  }

  if (holdSubscription) {
    return subscription->_id;
  }

  delete subscription;
  return -1;
}

void PyramidDEMProvider::unsubscribe(const long long subscriptionID) {
  if (subscriptionID < 0) {
    return;
  }

  if (_rootNodes != NULL) {
    DEMSubscription* subscription = NULL;

    for (size_t i = 0; i < _rootNodesCount; i++) {
      PyramidNode* rootNode = _rootNodes->at(i);
      DEMSubscription* removedSubscription = rootNode->removeSubscription(subscriptionID);
      if (removedSubscription != NULL) {
        if (subscription == NULL) {
          subscription = removedSubscription;
        }
        else {
          if (subscription != removedSubscription) {
            THROW_EXCEPTION("Logic error!");
          }
        }
      }
    }

    delete subscription;
  }
}
