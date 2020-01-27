//
//  TileImageListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

#ifndef __G3M__TileImageListener__
#define __G3M__TileImageListener__

class IImage;
#include <string>

class TileImageListener {
public:
  virtual ~TileImageListener() {
  }

  virtual void imageCreated(const std::string&           tileID,
                            const IImage*                image,
                            const std::string&           imageID,
                            const TileImageContribution* contribution) = 0;

  virtual void imageCreationError(const std::string& tileID,
                                  const std::string& error) = 0;

  virtual void imageCreationCanceled(const std::string& tileID) = 0;

};

#endif
