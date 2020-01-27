//
//  PyramidDEMProvider.cpp
//  G3M
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

  std::vector<PyramidNode*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    PyramidNode* rootNode = rootNodes->at(i);
    rootNode->addSubscription(NULL, // grid
                              subscription);
  }

//  const bool released = subscription->_release();
//  if (released) {
//    return NULL;
//  }

#warning TODO: fire event!
  return subscription;
}

void PyramidDEMProvider::unsubscribe(DEMSubscription* subscription) {
  if (subscription != NULL) {
    if (_rootNodes != NULL) {
      for (size_t i = 0; i < _rootNodesCount; i++) {
        PyramidNode* rootNode = _rootNodes->at(i);
        rootNode->removeSubscription(subscription);
      }
    }
  }
}
