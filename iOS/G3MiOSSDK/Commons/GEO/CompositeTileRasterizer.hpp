//
//  CompositeTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/12/13.
//
//

#ifndef __G3MiOSSDK__CompositeTileRasterizer__
#define __G3MiOSSDK__CompositeTileRasterizer__

#include "CanvasTileRasterizer.hpp"
#include <vector>

#include "ChangedListener.hpp"

class CompositeTileRasterizer : public CanvasTileRasterizer, ChangedListener {
private:
  std::vector<TileRasterizer*> _children;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

public:
  CompositeTileRasterizer() :
  _context(NULL)
  {

  }

  ~CompositeTileRasterizer();

  void initialize(const G3MContext* context);

  std::string getId() const;

  void rawRasterize(const IImage* image,
                    const TileRasterizerContext& trc,
                    IImageListener* listener,
                    bool autodelete) const;

  void addTileRasterizer(TileRasterizer* tileRasterizer);

  void changed();
  
};

#endif
