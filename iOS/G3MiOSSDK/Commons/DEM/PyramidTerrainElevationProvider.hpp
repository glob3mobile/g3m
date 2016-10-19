//
//  PyramidTerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef PyramidTerrainElevationProvider_hpp
#define PyramidTerrainElevationProvider_hpp

#include "TerrainElevationProvider.hpp"

#include <vector>
#include "Sector.hpp"

class TerrainElevationGrid;


class PyramidTerrainElevationProvider : public TerrainElevationProvider {
protected:

  class Node {
  private:
    std::vector<Node*>* getChildren(PyramidTerrainElevationProvider* pyramidTerrainElevationProvider);

  public:
    const Node*  _parent;
    const size_t _childID;
    const Sector _sector;
    const int    _z;
    const int    _x;
    const int    _y;

    TerrainElevationGrid* _grid;
    bool _stickyGrid;

    std::vector<Node*>* _children;

    Node(const Node*   parent,
         const size_t  childID,
         const Sector& sector,
         const int     z,
         const int     x,
         const int     y);

    ~Node();

    bool insertGrid(int z, int x, int y,
                    TerrainElevationGrid* grid,
                    const bool            sticky,
                    PyramidTerrainElevationProvider* pyramidTerrainElevationProvider);

  };


  void insertGrid(int z, int x, int y,
                  TerrainElevationGrid* grid,
                  const bool sticky);

private:
  const size_t        _rootNodesCount;
  std::vector<Node*>* _rootNodes;


  std::vector<Node*>* getRootNodes();

protected:
  virtual Node* createNode(const Node*  parent,
                           const size_t childID) = 0;

  PyramidTerrainElevationProvider(const size_t rootNodesCount);

  virtual ~PyramidTerrainElevationProvider();
  
public:
  
};

#endif
