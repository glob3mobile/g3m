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
    const Node*  _parent;
    const size_t _childID;
    const Sector _sector;

  public:
    Node(const Node* parent,
         const size_t childID,
         const Sector& sector);

    ~Node();

    bool insertGrid(TerrainElevationGrid* grid,
                    const bool            sticky,
                    const int             gridWidth,
                    const int             gridHeight);

  };


  void insertGrid(TerrainElevationGrid* grid,
                  const bool sticky);


private:
  const size_t       _rootNodesCount;
  std::vector<Node*> _rootNodes;

  const int _gridWidth;
  const int _gridHeight;

protected:
  virtual Node* createNode(const Node* parent,
                           size_t childID) = 0;

  PyramidTerrainElevationProvider(const size_t rootNodesCount,
                                  const int    gridWidth,
                                  const int    gridHeight);

  virtual ~PyramidTerrainElevationProvider();


public:

};

#endif
