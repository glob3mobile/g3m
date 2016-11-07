//
//  PyramidNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#include "PyramidNode.hpp"

#include "PyramidDEMProvider.hpp"
#include "DEMGrid.hpp"
#include "ErrorHandling.hpp"
#include "DEMSubscription.hpp"


PyramidNode::PyramidNode(const PyramidNode*  parent,
                         const size_t        childID,
                         const Sector&       sector,
                         const int           z,
                         const int           x,
                         const int           y,
                         PyramidDEMProvider* pyramidDEMProvider) :
_parent(parent),
_childID(childID),
_sector(sector),
_resolution(sector._deltaLatitude.div(pyramidDEMProvider->_tileExtent._y),
            sector._deltaLongitude.div(pyramidDEMProvider->_tileExtent._x)),
_z(z), _x(x), _y(y),
_pyramidDEMProvider(pyramidDEMProvider),
_grid(NULL),
_stickyGrid(false),
_children(NULL),
_childrenSize(0),
_subscriptions(NULL)
{

}

PyramidNode::~PyramidNode() {
  if (_grid != NULL) {
    _grid->_release();
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      PyramidNode* child = _children->at(i);
      delete child;
    }
    delete _children;
  }

  if (_subscriptions != NULL) {
    const size_t subscriptionsSize = _subscriptions->size();
    for (size_t i = 0; i < subscriptionsSize; i++) {
      DEMSubscription* subscription = _subscriptions->at(i);
      subscription->_release();
    }
    delete _subscriptions;
  }
}

std::vector<PyramidNode*>* PyramidNode::getChildren() {
  if (_children == NULL) {
    _children = new std::vector<PyramidNode*>();
    _childrenSize = 4;
    for (size_t i = 0; i < _childrenSize; i++) {
      PyramidNode* child = _pyramidDEMProvider->createNode(this, i);
      _children->push_back(child);
    }
  }
  return _children;
}

bool PyramidNode::insertGrid(int z,
                             int x,
                             int y,
                             DEMGrid* grid,
                             const bool stickyGrid) {
  if (z < _z) {
    THROW_EXCEPTION("Logic error!");
  }
  else if (z == _z) {
    if ((x == _x) && (y == _y)) {
      _grid = grid;
      _stickyGrid = stickyGrid;
      return true;
    }
    return false;
  }

  std::vector<PyramidNode*>* children = getChildren();
  for (size_t i = 0; i < _childrenSize; i++) {
    PyramidNode* child = children->at(i);
    if (child->insertGrid(z, x, y,
                          grid, stickyGrid)) {
      return true;
    }
  }
  return false;
}

void PyramidNode::addSubscription(const DEMGrid* grid,
                                  DEMSubscription* subscription) {
  if (subscription->_sector.touchesWith(_sector)) {
    const DEMGrid* bestGrid = (_grid == NULL) ? grid : _grid;

    const bool notEnoughResolution = (_resolution._latitude.greaterThan ( subscription->_resolution._latitude  ) ||
                                      _resolution._longitude.greaterThan( subscription->_resolution._longitude ) );
    if (notEnoughResolution) {
      std::vector<PyramidNode*>* children = getChildren();
      for (size_t i = 0; i < _childrenSize; i++) {
        PyramidNode* child = children->at(i);
        child->addSubscription(bestGrid, subscription);
      }
    }
    else {
      if (_subscriptions == NULL) {
        _subscriptions = new std::vector<DEMSubscription*>();
      }
      subscription->_retain();
      _subscriptions->push_back(subscription);
      if (bestGrid != NULL) {
        //subscription->onGrid( DEMGridUtils::gridFor(bestGrid, subscription) );
#warning Diego at work!
      }
    }
  }
}

void PyramidNode::removeSubscription(DEMSubscription* subscription) {
  if (_subscriptions != NULL) {
    const size_t subscriptionsSize = _subscriptions->size();
    for (size_t i = 0; i < subscriptionsSize; i++) {
      if (_subscriptions->at(i) == subscription) {
        subscription->_release();
#ifdef C_CODE
        _subscriptions->erase(_subscriptions->begin() + i);
#endif
#ifdef JAVA_CODE
        _subscriptions.remove(i);
#endif
        break;
      }
    }
    if (_subscriptions->empty()) {
      delete _subscriptions;
      _subscriptions = NULL;
    }
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _childrenSize; i++) {
      PyramidNode* child = _children->at(i);
      child->removeSubscription(subscription);
    }
  }
  
}
