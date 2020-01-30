//
//  CanvasOwnerImageBuilderWrapper.cpp
//  G3MiOSSDK
//
//  Created by Diego on 1/30/20.
//

#include "CanvasOwnerImageBuilderWrapper.hpp"

#include "ICanvas.hpp"


CanvasOwnerImageBuilderWrapper::CanvasOwnerImageBuilderWrapper(ICanvas* canvas,
                                                               IImageBuilder* imageBuilder) :
_canvas(canvas),
_imageBuilder(imageBuilder)
{

}

CanvasOwnerImageBuilderWrapper::~CanvasOwnerImageBuilderWrapper() {
  delete _canvas;
}

bool CanvasOwnerImageBuilderWrapper::isMutable() const {
  return _imageBuilder->isMutable();
}

void CanvasOwnerImageBuilderWrapper::build(const G3MContext* context,
                                           IImageBuilderListener* listener,
                                           bool deleteListener) {
  _imageBuilder->build(context,
                       listener,
                       deleteListener);
}

void CanvasOwnerImageBuilderWrapper::setChangeListener(ChangedListener* listener) {
  _imageBuilder->setChangeListener(listener);
}
