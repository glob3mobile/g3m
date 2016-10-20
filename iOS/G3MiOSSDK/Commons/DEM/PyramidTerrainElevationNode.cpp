//
//  PyramidTerrainElevationNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#include "PyramidTerrainElevationNode.hpp"

#include "PyramidTerrainElevationProvider.hpp"
#include "TerrainElevationGrid.hpp"
#include "ErrorHandling.hpp"


PyramidTerrainElevationNode::PyramidTerrainElevationNode(const PyramidTerrainElevationNode* parent,
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

PyramidTerrainElevationNode::~PyramidTerrainElevationNode() {
  if (_grid != NULL) {
    _grid->_release();
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _children->size(); i++) {
      PyramidTerrainElevationNode* child = _children->at(i);
      delete child;
    }
    delete _children;
  }
}

std::vector<PyramidTerrainElevationNode*>* PyramidTerrainElevationNode::getChildren(PyramidTerrainElevationProvider* pyramidTerrainElevationProvider) {
  if (_children == NULL) {
    _children = new std::vector<PyramidTerrainElevationNode*>();
    for (size_t i = 0; i < 4; i++) {
      PyramidTerrainElevationNode* child = pyramidTerrainElevationProvider->createNode(this, i);
      _children->push_back(child);
    }
  }
  return _children;
}


bool PyramidTerrainElevationNode::insertGrid(int z, int x, int y,
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

  std::vector<PyramidTerrainElevationNode*>* children = getChildren(pyramidTerrainElevationProvider);
  for (size_t i = 0; i < children->size(); i++) {
    PyramidTerrainElevationNode* child = children->at(i);
    if (child->insertGrid(z, x, y,
                          grid,
                          sticky,
                          pyramidTerrainElevationProvider)) {
      return true;
    }
  }
  return false;
}
