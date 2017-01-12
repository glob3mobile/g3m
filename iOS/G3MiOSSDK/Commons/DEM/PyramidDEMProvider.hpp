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
#include "Vector2S.hpp"

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
                     const Vector2S& tileExtent);

  virtual ~PyramidDEMProvider();

public:
#ifdef C_CODE
  const Vector2S   _tileExtent;
#endif
#ifdef JAVA_CODE
  public final Vector2S _tileExtent;
#endif

  virtual PyramidNode* createNode(const PyramidNode*  parent,
                                  const size_t childID) = 0;

  DEMSubscription* subscribe(const Sector&   sector,
                             const Vector2S& extent,
                             DEMListener*    listener,
                             const bool      deleteListener);

  void unsubscribe(DEMSubscription* subscription);
  
};

#endif
