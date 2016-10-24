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


PyramidDEMProvider::PyramidDEMProvider(const double    deltaHeight,
                                       const size_t    rootNodesCount,
                                       const Vector2S& tileExtent) :
DEMProvider(deltaHeight),
_rootNodesCount(rootNodesCount),
_tileExtent(tileExtent),
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
                             grid, stickyGrid)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}

DEMSubscription* PyramidDEMProvider::subscribe(const Sector&   sector,
                                               const Vector2S& extent,
                                               DEMListener*    listener,
                                               const bool      deleteListener) {
  DEMSubscription* subscription = new DEMSubscription(this,
                                                      sector,
                                                      extent,
                                                      listener,
                                                      deleteListener);

  bool holdSubscription = false;
  std::vector<PyramidNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    PyramidNode* rootNode = rootNodes->at(i);
    if (rootNode->addSubscription(subscription)) {
      holdSubscription = true;
    }
  }

  subscription->_release();

  if (holdSubscription) {
#warning TODO: fire event!
    return subscription;
  }

  return NULL;
}

void PyramidDEMProvider::unsubscribe(DEMSubscription* subscription) {
  THROW_EXCEPTION("Not yet done!");
  if (subscription == NULL) {
    return;
  }

#warning Diego at work!
  //  if (_rootNodes != NULL) {
  //    DEMSubscription* subscription = NULL;
  //
  //    for (size_t i = 0; i < _rootNodesCount; i++) {
  //      PyramidNode* rootNode = _rootNodes->at(i);
  //      DEMSubscription* removedSubscription = rootNode->removeSubscription(subscriptionID);
  //      if (removedSubscription != NULL) {
  //        if (subscription == NULL) {
  //          subscription = removedSubscription;
  //        }
  //        else {
  //          if (subscription != removedSubscription) {
  //            THROW_EXCEPTION("Logic error!");
  //          }
  //        }
  //      }
  //    }
  //
  //    delete subscription;
  //  }
}
