//
//  CanvasOwnerImageListener.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/29/20.
//

#include "CanvasOwnerImageListener.hpp"

#include "ICanvas.hpp"


CanvasOwnerImageListener::CanvasOwnerImageListener(ICanvas* canvas) :
_canvas(canvas)
{

}

CanvasOwnerImageListener::~CanvasOwnerImageListener() {
  delete _canvas;
}
