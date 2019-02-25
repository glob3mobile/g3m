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
    const std::string           _tileID;
    const TileImageContribution* _contribution;

    TileImageListener*          _listener;
    bool                        _deleteListener;

    static const std::string getImageID(const std::string& tileID);

  public:
    ImageListener(const std::string&           tileID,
                  const TileImageContribution* contribution,
                  TileImageListener*           listener,
                  bool                         deleteListener);

    ~ImageListener();

    void imageCreated(const IImage* image);

  };
  
  const GFont _font;
  const Color _color;
  
  const bool _showIDLabel;
  const bool _showSectorLabels;
  const bool _showTileBounds;
  
  const std::string getIDLabel(const Tile* tile) const;
  
  const std::string getSectorLabel1(const Sector& sector) const;
  const std::string getSectorLabel2(const Sector& sector) const;
  const std::string getSectorLabel3(const Sector& sector) const;
  const std::string getSectorLabel4(const Sector& sector) const;

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
              const Vector2S& resolution,
              long long tileTextureDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener,
              FrameTasksExecutor* frameTasksExecutor);

  void cancel(const std::string& tileID);

};

#endif
