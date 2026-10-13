//
//  CanvasOwnerImageFactoryWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#include "CanvasOwnerImageFactoryWrapper.hpp"

#include "ICanvas.hpp"


CanvasOwnerImageFactoryWrapper::CanvasOwnerImageFactoryWrapper(ICanvas* canvas,
                                                               IImageFactory* imageFactory,
                                                               const bool autodelete) :
_canvas(canvas),
_imageFactory(imageFactory),
_autodelete(autodelete)
{

}

CanvasOwnerImageFactoryWrapper::~CanvasOwnerImageFactoryWrapper() {
  delete _canvas;
  if (_autodelete) {
    delete _imageFactory;
  }
}

bool CanvasOwnerImageFactoryWrapper::isMutable() const {
  return _imageFactory->isMutable();
}

void CanvasOwnerImageFactoryWrapper::create(const G3MContext* context,
                                           IImageFactoryListener* listener,
                                           bool deleteListener) {
  _imageFactory->create(context,
                       listener,
                       deleteListener);
}

void CanvasOwnerImageFactoryWrapper::setChangeListener(ChangedListener* listener) {
  _imageFactory->setChangeListener(listener);
}
