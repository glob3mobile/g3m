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

#include "Sector.hpp"
#include "Vector2I.hpp"

class DEMGrid;
class PyramidNode;


class PyramidDEMProvider : public DEMProvider {
private:

  const size_t               _rootNodesCount;
  std::vector<PyramidNode*>* _rootNodes;

  std::vector<PyramidNode*>* getRootNodes();


  class Subscription {
  private:
    const Sector     _sector;
#ifdef C_CODE
    const Vector2I   _extent;
#endif
#ifdef JAVA_CODE
    private final Vector2I _extent;
#endif
    const Geodetic2D _resolution;

    DEMListener* _listener;
    const bool   _deleteListener;

  public:
    Subscription(const Sector&   sector,
                 const Vector2I& extent,
                 DEMListener*    listener,
                 const bool      deleteListener);

    ~Subscription();

  };


protected:

  void insertGrid(int z,
                  int x,
                  int y,
                  DEMGrid* grid,
                  const bool stickyGrid);

  PyramidDEMProvider(const double deltaHeight,
                     const size_t rootNodesCount);

  virtual ~PyramidDEMProvider();


public:

  virtual PyramidNode* createNode(const PyramidNode*  parent,
                                  const size_t childID) = 0;

  long long subscribe(const Sector&   sector,
                      const Vector2I& extent,
                      DEMListener*    listener,
                      const bool      deleteListener);

  void unsubscribe(const long long subscriptionID);
  
};

#endif
