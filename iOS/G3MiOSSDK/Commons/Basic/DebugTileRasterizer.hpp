//
//  DebugTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#ifndef __G3MiOSSDK__DebugTileRasterizer__
#define __G3MiOSSDK__DebugTileRasterizer__

#include "TileRasterizer.hpp"
#include "GFont.hpp"
#include "Color.hpp"

class ICanvas;
class Sector;

class DebugTileRasterizer : public TileRasterizer {
private:
  mutable ICanvas* _canvas;
  mutable int      _canvasWidth;
  mutable int      _canvasHeight;

#ifdef C_CODE
  const GFont _font;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
#endif
  const Color _color;


  std::string getTileKeyLabel(const Tile* tile) const;

  std::string getSectorLabel1(const Sector& sector) const;
  std::string getSectorLabel2(const Sector& sector) const;
  std::string getSectorLabel3(const Sector& sector) const;
  std::string getSectorLabel4(const Sector& sector) const;

  ICanvas* getCanvas(int width, int height) const;

public:
  DebugTileRasterizer();

  ~DebugTileRasterizer();

  std::string getId() const {
    return "DebugTileRasterizer";
  }

  void rasterize(IImage* image,
                 const Tile* tile,
                 IImageListener* listener,
                 bool autodelete) const;
  
};

#endif
