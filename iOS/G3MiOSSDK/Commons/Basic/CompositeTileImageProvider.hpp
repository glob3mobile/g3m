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
  std::vector<TileImageProvider*> _children;
  int                             _childrenSize;

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
              TileImageListener* listener,
              bool deleteListener);

  void cancel(const Tile* tile);

};

#endif
