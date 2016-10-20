//
//  PyramidTerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#include "PyramidTerrainElevationProvider.hpp"

#include "ErrorHandling.hpp"
#include "TerrainElevationGrid.hpp"


PyramidTerrainElevationProvider::Node::Node(const PyramidTerrainElevationProvider::Node* parent,
                                            const size_t  childID,
                                            const Sector& sector,
                                            const int     z,
                                            const int     x,
                                            const int     y) :
_parent(parent),
_childID(childID),
_sector(sector),
_z(z), _x(x), _y(y),
_grid(NULL),
_stickyGrid(false),
_children(NULL)
{

}

PyramidTerrainElevationProvider::Node::~Node() {
  if (_grid != NULL) {
    _grid->_release();
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _children->size(); i++) {
      Node* child = _children->at(i);
      delete child;
    }
    delete _children;
  }
}

std::vector<PyramidTerrainElevationProvider::Node*>* PyramidTerrainElevationProvider::Node::getChildren(PyramidTerrainElevationProvider* pyramidTerrainElevationProvider) {
  if (_children == NULL) {
    _children = new std::vector<Node*>();
    for (size_t i = 0; i < 4; i++) {
      Node* child = pyramidTerrainElevationProvider->createNode(this, i);
      _children->push_back(child);
    }
  }
  return _children;
}


bool PyramidTerrainElevationProvider::Node::insertGrid(int z, int x, int y,
                                                       TerrainElevationGrid* grid,
                                                       const bool            sticky,
                                                       PyramidTerrainElevationProvider* pyramidTerrainElevationProvider) {
  if (z < _z) {
    THROW_EXCEPTION("Logic error!");
  }
  else if (z == _z) {
    if ((x == _x) && (y == _y)) {
      _grid = grid;
      _stickyGrid = sticky;
      return true;
    }
    return false;
  }

  std::vector<Node*>* children = getChildren(pyramidTerrainElevationProvider);
  for (size_t i = 0; i < children->size(); i++) {
    Node* child = children->at(i);
    if (child->insertGrid(z, x, y,
                          grid,
                          sticky,
                          pyramidTerrainElevationProvider)) {
      return true;
    }
  }
  return false;
}

PyramidTerrainElevationProvider::PyramidTerrainElevationProvider(const size_t rootNodesCount) :
_rootNodesCount(rootNodesCount),
_rootNodes(NULL)
{
}

std::vector<PyramidTerrainElevationProvider::Node*>* PyramidTerrainElevationProvider::getRootNodes() {
  if (_rootNodes == NULL) {
    _rootNodes = new std::vector<Node*>();
    for (size_t i = 0; i < _rootNodesCount; i++) {
      Node* rootNode = createNode(NULL, i);
      _rootNodes->push_back(rootNode);
    }
  }
  return _rootNodes;
}


PyramidTerrainElevationProvider::~PyramidTerrainElevationProvider() {
  if (_rootNodes != NULL) {
    for (size_t i = 0; i < _rootNodesCount; i++) {
      Node* rootNode = _rootNodes->at(i);
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
  std::vector<Node*>* rootNodes = getRootNodes();
  for (size_t i = 0; i < _rootNodesCount; i++) {
    Node* rootNode = rootNodes->at(i);
    if (rootNode->insertGrid(z, x, y,
                             grid, sticky,
                             this)) {
      return;
    }
  }
  THROW_EXCEPTION("can't insert grid");
}
