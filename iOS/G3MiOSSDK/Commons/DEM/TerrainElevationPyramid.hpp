//
//  TerrainElevationPyramid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef TerrainElevationPyramid_hpp
#define TerrainElevationPyramid_hpp

#include <vector>

#include "Sector.hpp"


class TerrainElevationPyramid {
protected:

  class Node {
  private:
    const Node*  _parent;
    const size_t _childID;
    const Sector _sector;

  public:
    Node(const TerrainElevationPyramid::Node* parent,
         const size_t childID,
         const Sector& sector);

    ~Node();
  };


private:
  std::vector<TerrainElevationPyramid::Node*> _rootNodes;

  const int _nodeWidth;
  const int _nodeHeight;

protected:
  virtual TerrainElevationPyramid::Node* createNode(const TerrainElevationPyramid::Node* parent,
                                                    size_t childID) = 0;

  TerrainElevationPyramid(const size_t rootNodesCount,
                          const int    nodeWidth,
                          const int    nodeHeight);

  virtual ~TerrainElevationPyramid();

  
public:
  
};

#endif
