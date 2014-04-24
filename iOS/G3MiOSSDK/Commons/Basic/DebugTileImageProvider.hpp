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
    const std::string           _tileId;
#ifdef C_CODE
    const TileImageContribution _contribution;
#endif
#ifdef JAVA_CODE
    private final TileImageContribution _contribution;
#endif

    TileImageListener*          _listener;
    bool                        _deleteListener;

    static const std::string getImageId(const std::string& tileId);

  public:
    ImageListener(const std::string&           tileId,
                  const TileImageContribution& contribution,
                  TileImageListener*           listener,
                  bool                         deleteListener) :
    _tileId(tileId),
    _contribution(contribution),
    _listener(listener),
    _deleteListener(deleteListener)
    {
    }

    void imageCreated(const IImage* image);

  };

protected:
  virtual ~DebugTileImageProvider() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:

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
