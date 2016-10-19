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


class PyramidTerrainElevationProvider : public TerrainElevationProvider {
protected:

  class Node {
  private:
    const Node*  _parent;
    const size_t _childID;
    const Sector _sector;

  public:
    Node(const PyramidTerrainElevationProvider::Node* parent,
         const size_t childID,
         const Sector& sector);

    ~Node();
  };


private:
  std::vector<PyramidTerrainElevationProvider::Node*> _rootNodes;

  const int _nodeWidth;
  const int _nodeHeight;

protected:
  virtual PyramidTerrainElevationProvider::Node* createNode(const PyramidTerrainElevationProvider::Node* parent,
                                                            size_t childID) = 0;

  PyramidTerrainElevationProvider(const size_t rootNodesCount,
                                  const int    nodeWidth,
                                  const int    nodeHeight);

  virtual ~PyramidTerrainElevationProvider();
  
  
public:
  
};

#endif
