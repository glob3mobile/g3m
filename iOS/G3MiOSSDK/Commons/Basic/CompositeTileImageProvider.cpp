//
//  CompositeTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

#include "CompositeTileImageProvider.hpp"
#include "TileImageListener.hpp"
#include "Tile.hpp"

CompositeTileImageProvider::~CompositeTileImageProvider() {
  for (int i = 0; i < _childrenSize; i++) {
    TileImageProvider* child = _children[i];
//    delete child;
    child->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}


TileImageContribution CompositeTileImageProvider::contribution(const Tile* tile) {
#warning TODO
  return TileImageContribution::none();
}

void CompositeTileImageProvider::create(const Tile* tile,
                                        const TileImageContribution& contribution,
                                        const Vector2I& resolution,
                                        long long tileDownloadPriority,
                                        bool logDownloadActivity,
                                        TileImageListener* listener,
                                        bool deleteListener) {
#warning TODO

  listener->imageCreationError(tile->_id, "Not yet implemented");
  if (deleteListener) {
    delete listener;
  }
}

void CompositeTileImageProvider::cancel(const Tile* tile) {
#warning TODO
}
