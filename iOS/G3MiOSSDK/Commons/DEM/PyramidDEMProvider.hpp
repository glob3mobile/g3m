//
//  PyramidDEMProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef PyramidDEMProvider_hpp
#define PyramidDEMProvider_hpp

#include "DEMProvider.hpp"

#include <vector>

class DEMGrid;
class PyramidDEMNode;


class PyramidDEMProvider : public DEMProvider {
protected:

  void insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool sticky);

private:
  const size_t                  _rootNodesCount;
  std::vector<PyramidDEMNode*>* _rootNodes;


  std::vector<PyramidDEMNode*>* getRootNodes();

protected:
  PyramidDEMProvider(const size_t rootNodesCount);

  virtual ~PyramidDEMProvider();

public:
  virtual PyramidDEMNode* createNode(const PyramidDEMNode*  parent,
                                     const size_t childID) = 0;

};

#endif
