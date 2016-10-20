//
//  PyramidTerrainElevationNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#ifndef PyramidTerrainElevationNode_hpp
#define PyramidTerrainElevationNode_hpp

#include <vector>
#include "Sector.hpp"

class PyramidTerrainElevationProvider;
class TerrainElevationGrid;


class PyramidTerrainElevationNode {
private:
  std::vector<PyramidTerrainElevationNode*>* getChildren(PyramidTerrainElevationProvider* pyramidTerrainElevationProvider);

public:
  const PyramidTerrainElevationNode* _parent;
  const size_t _childID;
  const Sector _sector;
  const int    _z;
  const int    _x;
  const int    _y;

  TerrainElevationGrid* _grid;
  bool _stickyGrid;

  std::vector<PyramidTerrainElevationNode*>* _children;

  PyramidTerrainElevationNode(const PyramidTerrainElevationNode* parent,
                              const size_t  childID,
                              const Sector& sector,
                              const int     z,
                              const int     x,
                              const int     y);

  ~PyramidTerrainElevationNode();

  bool insertGrid(int z, int x, int y,
                  TerrainElevationGrid* grid,
                  const bool            sticky,
                  PyramidTerrainElevationProvider* pyramidTerrainElevationProvider);

};

#endif
