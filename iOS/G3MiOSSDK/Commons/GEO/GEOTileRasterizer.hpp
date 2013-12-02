//
//  GEOTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEOTileRasterizer__
#define __G3MiOSSDK__GEOTileRasterizer__

#include "CanvasTileRasterizer.hpp"
#include "QuadTree.hpp"

class GEORasterSymbol;


class GEOTileRasterizer : public CanvasTileRasterizer {
private:
  QuadTree _quadTree;

public:
  GEOTileRasterizer()
  {
  }

  std::string getId() const {
    return "GEOTileRasterizer";
  }

  void initialize(const G3MContext* context);

  void rawRasterize(const IImage* image,
                    const TileRasterizerContext& trc,
                    IImageListener* listener,
                    bool autodelete) const;

  void addSymbol(const GEORasterSymbol* symbol);

  void clear();

  ICanvas* getCanvas(int width, int height) const {
    return CanvasTileRasterizer::getCanvas(width, height);
  }
};

#endif
