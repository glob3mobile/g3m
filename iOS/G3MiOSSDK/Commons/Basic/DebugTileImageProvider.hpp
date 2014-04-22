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
    const Tile*        _tile;
    TileImageListener* _listener;
    bool               _deleteListener;

    const std::string getImageId(const Tile* tile);

  public:
    ImageListener(const Tile* tile,
                  TileImageListener* listener,
                  bool deleteListener) :
    _tile(tile),
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

  TileImageContribution contribution(const Tile* tile);

  void create(const Tile* tile,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              TileImageListener* listener,
              bool deleteListener);

  void cancel(const Tile* tile);

};

#endif
