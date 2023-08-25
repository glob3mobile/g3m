//
//  CanvasOwnerImageListenerWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#include "CanvasOwnerImageListenerWrapper.hpp"

#include "ICanvas.hpp"


CanvasOwnerImageListenerWrapper::CanvasOwnerImageListenerWrapper(ICanvas* canvas,
                                                                 IImageListener* listener,
                                                                 bool autodelete) :
_canvas(canvas),
_listener(listener),
_autodelete(autodelete)
{
  
}

CanvasOwnerImageListenerWrapper::~CanvasOwnerImageListenerWrapper() {
  if (_autodelete) {
    delete _listener;
  }
  
  delete _canvas;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void CanvasOwnerImageListenerWrapper::imageCreated(const IImage* image) {
  _listener->imageCreated(image);
}
