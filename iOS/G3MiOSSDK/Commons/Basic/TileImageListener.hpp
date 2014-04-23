//
//  TileImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3MiOSSDK__TileImageListener__
#define __G3MiOSSDK__TileImageListener__

class IImage;
#include <string>

class TileImageListener {
public:
  virtual ~TileImageListener() {
  }

  virtual void imageCreated(const std::string&           tileId,
                            const IImage*                image,
                            const std::string&           imageId,
                            const TileImageContribution& contribution) = 0;

  virtual void imageCreationError(const std::string& tileId,
                                  const std::string& error) = 0;

  virtual void imageCreationCanceled(const std::string& tileId) = 0;

};

#endif
