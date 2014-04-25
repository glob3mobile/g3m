//
//  CompositeTileImageProvider.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

#ifndef __G3MiOSSDK__CompositeTileImageProvider__
#define __G3MiOSSDK__CompositeTileImageProvider__

#include "CanvasTileImageProvider.hpp"
#include <vector>

class CompositeTileImageProvider : public CanvasTileImageProvider {
private:

//  class ChildContribution {
//  public:
//    ChildContribution(const TileImageContribution& contribution);
//  };

  std::vector<TileImageProvider*> _children;
  int                             _childrenSize;

//  static const int MAX_CHILDREN_CONTRIBUTIONS = 4;
//  ChildContribution _childrenContributions[MAX_CHILDREN_CONTRIBUTIONS];

protected:
  ~CompositeTileImageProvider();

public:
  CompositeTileImageProvider() :
  _childrenSize(0)
  {
  }

  void addProvider(TileImageProvider* child) {
    _children.push_back(child);
    _childrenSize = _children.size();
  }

  TileImageContribution contribution(const Tile* tile);

  void create(const Tile* tile,
              const TileImageContribution& contribution,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener);

  void cancel(const Tile* tile);

};

#endif
