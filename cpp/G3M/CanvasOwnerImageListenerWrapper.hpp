//
//  CanvasOwnerImageListenerWrapper.hpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#ifndef CanvasOwnerImageListenerWrapper_hpp
#define CanvasOwnerImageListenerWrapper_hpp

#include "IImageListener.hpp"

class ICanvas;


class CanvasOwnerImageListenerWrapper : public IImageListener {
private:
  ICanvas* _canvas;

  IImageListener* _listener;
  const bool      _autodelete;

public:
  CanvasOwnerImageListenerWrapper(ICanvas* canvas,
                                  IImageListener* listener,
                                  bool autodelete);

  ~CanvasOwnerImageListenerWrapper();

  void imageCreated(const IImage* image);

};

#endif
