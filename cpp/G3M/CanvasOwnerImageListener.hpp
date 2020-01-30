//
//  CanvasOwnerImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/29/20.
//

#ifndef CanvasOwnerImageListener_hpp
#define CanvasOwnerImageListener_hpp

#include "IImageListener.hpp"

class ICanvas;


class CanvasOwnerImageListener : public IImageListener {
private:
  ICanvas* _canvas;

protected:
  CanvasOwnerImageListener(ICanvas* canvas);

  virtual ~CanvasOwnerImageListener();

};

#endif
