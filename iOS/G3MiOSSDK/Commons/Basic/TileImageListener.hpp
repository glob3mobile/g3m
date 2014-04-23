//
//  TileImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3MiOSSDK__TileImageListener__
#define __G3MiOSSDK__TileImageListener__

class Tile;
class IImage;
class Sector;
class RectangleF;
#include <string>

class TileImageListener {
public:
  virtual ~TileImageListener() {
  }

  virtual void imageCreated(const Tile*        tile,
                            const IImage*      image,
                            const std::string& imageId,
                            const Sector&      imageSector,
                            const RectangleF&  imageRectangle,
                            const float        alpha) = 0;

  virtual void imageCreationError(const Tile*        tile,
                                  const std::string& error) = 0;

  virtual void imageCreationCanceled(const Tile* tile) = 0;

};

#endif
