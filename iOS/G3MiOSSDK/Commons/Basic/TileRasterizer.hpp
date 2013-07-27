//
//  TileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#ifndef __G3MiOSSDK__TileRasterizer__
#define __G3MiOSSDK__TileRasterizer__

#include <string>

class IImage;
class Tile;
class IImageListener;
class ChangedListener;

class TileRasterizer {
private:
  ChangedListener* _listener;
  
public:

  virtual ~TileRasterizer() {

  }

  virtual std::string getId() const = 0;

  virtual void rasterize(IImage* image,
                         const Tile* tile,
                         bool mercator,
                         IImageListener* listener,
                         bool autodelete) const = 0;

  void setChangeListener(ChangedListener* listener);

  void notifyChanges() const;

};

#endif
