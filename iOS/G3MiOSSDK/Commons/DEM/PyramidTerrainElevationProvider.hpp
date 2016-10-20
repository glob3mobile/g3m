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

class TerrainElevationGrid;
class PyramidTerrainElevationNode;


class PyramidTerrainElevationProvider : public TerrainElevationProvider {
protected:

  void insertGrid(int z, int x, int y,
                  TerrainElevationGrid* grid,
                  const bool sticky);

private:
  const size_t                               _rootNodesCount;
  std::vector<PyramidTerrainElevationNode*>* _rootNodes;


  std::vector<PyramidTerrainElevationNode*>* getRootNodes();

protected:
  PyramidTerrainElevationProvider(const size_t rootNodesCount);

  virtual ~PyramidTerrainElevationProvider();

public:
  virtual PyramidTerrainElevationNode* createNode(const PyramidTerrainElevationNode*  parent,
                                                  const size_t childID) = 0;
  
};

#endif
