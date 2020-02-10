//
//  CanvasImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

#include "CanvasImageBuilder.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "IImageListener.hpp"
#include "IImageBuilderListener.hpp"
#include "IStringUtils.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"


CanvasImageBuilder::~CanvasImageBuilder() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

class CanvasImageBuilder_ImageListener : public IImageListener {
private:
  const std::string      _imageName;
  IImageBuilderListener* _listener;
  const bool             _deleteListener;


public:
  CanvasImageBuilder_ImageListener(const std::string& imageName,
                                   IImageBuilderListener* listener,
                                   bool deleteListener) :
  _imageName(imageName),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~CanvasImageBuilder_ImageListener() {
    if (_deleteListener) {
      delete _listener;
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void imageCreated(const IImage* image) {
    _listener->imageCreated(image, _imageName);

    if (_deleteListener) {
      delete _listener;
    }
    _listener = NULL;
  }
};

void CanvasImageBuilder::build(const G3MContext* context,
                               IImageBuilderListener* listener,
                               bool deleteListener) {
  ICanvas* canvas = context->getFactory()->createCanvas(_retina);
  canvas->initialize(_width, _height);

  buildOnCanvas(context, canvas);

  canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                          new CanvasImageBuilder_ImageListener(getImageName(context),
                                                                                               listener,
                                                                                               deleteListener),
                                                          true),
                      true);
}

const std::string CanvasImageBuilder::getResolutionID(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();

  return (
          su->toString(_width) + "x" + su->toString(_height) +
          (_retina ? "@2x" : "")
          );
}
