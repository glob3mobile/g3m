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
#include "Vector2I.hpp"

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
                  const bool stickyGrid);

  PyramidDEMProvider(const double    deltaHeight,
                     const size_t    rootNodesCount,
                     const Vector2I& tileExtent);

  virtual ~PyramidDEMProvider();


public:
#ifdef C_CODE
  const Vector2I   _tileExtent;
#endif
#ifdef JAVA_CODE
  protected final Vector2I _tileExtent;
#endif

  virtual PyramidNode* createNode(const PyramidNode*  parent,
                                  const size_t childID) = 0;

  long long subscribe(const Sector&   sector,
                      const Vector2I& extent,
                      DEMListener*    listener,
                      const bool      deleteListener);

  void unsubscribe(const long long subscriptionID);
  
};

#endif
