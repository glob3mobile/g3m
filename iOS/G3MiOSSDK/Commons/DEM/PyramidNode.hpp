//
//  PyramidNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#ifndef PyramidNode_hpp
#define PyramidNode_hpp

#include <vector>
#include "Sector.hpp"

class PyramidDEMProvider;
class DEMGrid;


class PyramidNode {
private:
  std::vector<PyramidNode*>* getChildren(PyramidDEMProvider* pyramidDEMProvider);

  DEMGrid* _grid;
  bool _stickyGrid;

  std::vector<PyramidNode*>* _children;

public:
  const PyramidNode* _parent;
  const size_t _childID;
  const Sector _sector;
  const int    _z;
  const int    _x;
  const int    _y;

  PyramidNode(const PyramidNode* parent,
              const size_t       childID,
              const Sector&      sector,
              const int          z,
              const int          x,
              const int          y);

  ~PyramidNode();

  bool insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool stickyGrid,
                  PyramidDEMProvider* pyramidDEMProvider);

};

#endif
