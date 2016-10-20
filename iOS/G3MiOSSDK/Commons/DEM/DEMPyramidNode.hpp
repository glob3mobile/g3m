//
//  DEMPyramidNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#ifndef DEMPyramidNode_hpp
#define DEMPyramidNode_hpp

#include <vector>
#include "Sector.hpp"

class PyramidDEMProvider;
class DEMGrid;


class DEMPyramidNode {
private:
  std::vector<DEMPyramidNode*>* getChildren(PyramidDEMProvider* pyramidDEMProvider);

public:
  const DEMPyramidNode* _parent;
  const size_t _childID;
  const Sector _sector;
  const int    _z;
  const int    _x;
  const int    _y;

  DEMGrid* _grid;
  bool _stickyGrid;

  std::vector<DEMPyramidNode*>* _children;

  DEMPyramidNode(const DEMPyramidNode* parent,
                 const size_t  childID,
                 const Sector& sector,
                 const int     z,
                 const int     x,
                 const int     y);

  ~DEMPyramidNode();

  bool insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool sticky,
                  PyramidDEMProvider* pyramidDEMProvider);

};

#endif
