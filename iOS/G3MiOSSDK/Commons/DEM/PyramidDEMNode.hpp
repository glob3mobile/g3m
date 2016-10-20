//
//  PyramidDEMNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#ifndef PyramidDEMNode_hpp
#define PyramidDEMNode_hpp

#include <vector>
#include "Sector.hpp"

class PyramidDEMProvider;
class DEMGrid;


class PyramidDEMNode {
private:
  std::vector<PyramidDEMNode*>* getChildren(PyramidDEMProvider* pyramidDEMProvider);

public:
  const PyramidDEMNode* _parent;
  const size_t _childID;
  const Sector _sector;
  const int    _z;
  const int    _x;
  const int    _y;

  DEMGrid* _grid;
  bool _stickyGrid;

  std::vector<PyramidDEMNode*>* _children;

  PyramidDEMNode(const PyramidDEMNode* parent,
                 const size_t  childID,
                 const Sector& sector,
                 const int     z,
                 const int     x,
                 const int     y);

  ~PyramidDEMNode();

  bool insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool sticky,
                  PyramidDEMProvider* pyramidDEMProvider);

};

#endif
