//
//  TileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3MiOSSDK__TileImageProvider__
#define __G3MiOSSDK__TileImageProvider__

#include "TileImageContribution.hpp"

class Tile;
class TileImageListener;
class Vector2I;

class TileImageProvider {
public:
  virtual ~TileImageProvider() {
  }

  virtual TileImageContribution contribution(const Tile* tile) = 0;

  virtual void create(const Tile* tile,
                      const Vector2I& resolution,
                      long long tileDownloadPriority,
                      TileImageListener* listener,
                      bool deleteListener) = 0;

  virtual void cancel(const Tile* tile) = 0;

};

#endif
