//
//  DebugTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#ifndef __G3MiOSSDK__DebugTileRasterizer__
#define __G3MiOSSDK__DebugTileRasterizer__

#include "CanvasTileRasterizer.hpp"
#include "GFont.hpp"
#include "Color.hpp"

class ICanvas;
class Sector;

class DebugTileRasterizer : public CanvasTileRasterizer {
private:

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

public:
  DebugTileRasterizer();

  DebugTileRasterizer(const GFont& font,
                      const Color& color,
                      bool showIDLabel,
                      bool showSectorLabels,
                      bool showTileBounds);

  ~DebugTileRasterizer();

  void initialize(const G3MContext* context) {

  }

  std::string getId() const {
    return "DebugTileRasterizer";
  }

  void rawRasterize(const IImage* image,
                    const TileRasterizerContext& trc,
                    IImageListener* listener,
                    bool autodelete) const;
  
};

#endif
