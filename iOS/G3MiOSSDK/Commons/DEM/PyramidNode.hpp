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
class DEMSubscription;


class PyramidNode {
private:
  std::vector<PyramidNode*>* getChildren();

  DEMGrid* _grid;
  bool _stickyGrid;

  std::vector<PyramidNode*>* _children;
  size_t                     _childrenSize;

  const PyramidNode*  _parent;
  const size_t        _childID;
  PyramidDEMProvider* _pyramidDEMProvider;

  std::vector<DEMSubscription*>* _subscriptions;

  const Geodetic2D _resolution;

public:
  const Sector _sector;
  const int    _z;
  const int    _x;
  const int    _y;

  PyramidNode(const PyramidNode*  parent,
              const size_t        childID,
              const Sector&       sector,
              const int           z,
              const int           x,
              const int           y,
              PyramidDEMProvider* pyramidDEMProvider);

  ~PyramidNode();

  bool insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool stickyGrid);

  void addSubscription(DEMGrid* grid,
                       DEMSubscription* subscription);

  void removeSubscription(DEMSubscription* subscription);
  
};

#endif
