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

#include "GFont.hpp"
#include "Color.hpp"

class Sector;

class DebugTileImageProvider : public CanvasTileImageProvider {
private:
  class ImageListener : public IImageListener {
  private:
    const std::string           _tileId;
#ifdef C_CODE
    const TileImageContribution* _contribution;
#endif
#ifdef JAVA_CODE
    private final TileImageContribution _contribution;
#endif

    TileImageListener*          _listener;
    bool                        _deleteListener;

    static const std::string getImageId(const std::string& tileId);

  public:
    ImageListener(const std::string&           tileId,
                  const TileImageContribution* contribution,
                  TileImageListener*           listener,
                  bool                         deleteListener);

    ~ImageListener();

    void imageCreated(const IImage* image);

  };
  
#ifdef C_CODE
  const GFont _font;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
#endif
  const Color _color;
  
  const bool _showIDLabel;
  const bool _showSectorLabels;
  const bool _showTileBounds;
  
  std::string getIDLabel(const Tile* tile) const;
  
  std::string getSectorLabel1(const Sector& sector) const;
  std::string getSectorLabel2(const Sector& sector) const;
  std::string getSectorLabel3(const Sector& sector) const;
  std::string getSectorLabel4(const Sector& sector) const;

protected:
  virtual ~DebugTileImageProvider() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  
  DebugTileImageProvider();
  
  DebugTileImageProvider(const GFont& font,
                         const Color& color,
                         bool showIDLabel,
                         bool showSectorLabels,
                         bool showTileBounds);


  const TileImageContribution* contribution(const Tile* tile);

  void create(const Tile* tile,
              const TileImageContribution* contribution,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener,
              FrameTasksExecutor* frameTasksExecutor);

  void cancel(const std::string& tileId);

};

#endif
