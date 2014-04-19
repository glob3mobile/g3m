//
//  DebugTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3MiOSSDK__DebugTileImageProvider__
#define __G3MiOSSDK__DebugTileImageProvider__

#include "CanvasTileImageProvider.hpp"

#include "IImageListener.hpp"

class DebugTileImageProvider : public CanvasTileImageProvider {
private:
  class ImageListener : public IImageListener {
  private:
    Tile*              _tile;
    TileImageListener* _listener;
    bool               _deleteListener;

  public:
    ImageListener(Tile* tile,
                  TileImageListener* listener,
                  bool deleteListener) :
    _listener(listener),
    _deleteListener(deleteListener)
    {
    }

    void imageCreated(const IImage* image);

  };

public:
  virtual ~DebugTileImageProvider() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  TileImageContribution contribution(Tile* tile);

  void create(Tile* tile,
              const Vector2I& resolution,
              TileImageListener* listener,
              bool deleteListener);

  void cancel(Tile* tile);

};

#endif
