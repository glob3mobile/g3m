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
class PyramidNode;


class PyramidDEMProvider : public DEMProvider {
private:

  const size_t               _rootNodesCount;
  std::vector<PyramidNode*>* _rootNodes;

  std::vector<PyramidNode*>* getRootNodes();


protected:

  void insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool sticky);

  PyramidDEMProvider(const double deltaHeight,
                     const size_t rootNodesCount);

  virtual ~PyramidDEMProvider();


public:

  virtual PyramidNode* createNode(const PyramidNode*  parent,
                                  const size_t childID) = 0;

  long long subscribe(const Sector&   sector,
                      const Vector2I& extent,
                      DEMListener*    listener);

  void unsubscribe(const long long subscriptionID,
                   const bool      deleteListener);
  
};

#endif
