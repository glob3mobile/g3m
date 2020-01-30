//
//  CanvasOwnerImageListenerWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#include "CanvasOwnerImageListenerWrapper.hpp"

CanvasOwnerImageListenerWrapper::CanvasOwnerImageListenerWrapper(ICanvas* canvas,
                                                                 IImageListener* listener,
                                                                 bool autodelete) :
CanvasOwnerImageListener(canvas),
_listener(listener),
_autodelete(autodelete)
{

}

CanvasOwnerImageListenerWrapper::~CanvasOwnerImageListenerWrapper() {
  if (_autodelete) {
    delete _listener;
  }
#if JAVA_CODE
  super.dispose();
#endif
}

void CanvasOwnerImageListenerWrapper::imageCreated(const IImage* image) {
  _listener->imageCreated(image);
}
