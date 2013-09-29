//
//  CanvasImageFactory.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

#ifndef __G3MiOSSDK__CanvasImageFactory__
#define __G3MiOSSDK__CanvasImageFactory__

#include "ImageFactory.hpp"

class ICanvas;

class CanvasImageFactory : public ImageFactory {
protected:

  virtual void drawOn(ICanvas* canvas,
                      int width,
                      int height) = 0;

public:
  CanvasImageFactory() {

  }

  void create(const G3MRenderContext* rc,
              int width,
              int height,
              IImageListener* listener,
              bool deleteListener);
  
};

#endif
