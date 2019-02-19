//
//  ImageBackground.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#ifndef ImageBackground_hpp
#define ImageBackground_hpp

class ICanvas;
#include "Vector2F.hpp"


class ImageBackground {
protected:
  ImageBackground() {

  }

public:
  virtual ~ImageBackground() {

  }

  /**
   Initialize the canvas sized to fits the content and the background.
   Answer the content position.
   */
  virtual const Vector2F initializeCanvas(ICanvas* canvas,
                                          const float contentWidth,
                                          const float contentHeight) const = 0;

  virtual const Vector2F initializeCanvas(ICanvas* canvas,
                                          const Vector2F& contentSize) const;

  virtual const std::string description() const = 0;

};

#endif
