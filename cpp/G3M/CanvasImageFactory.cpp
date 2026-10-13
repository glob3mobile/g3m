//
//  CanvasImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

#include "CanvasImageFactory.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "IImageListener.hpp"
#include "IImageFactoryListener.hpp"
#include "IStringUtils.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"


CanvasImageFactory::~CanvasImageFactory() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

class CanvasImageFactory_ImageListener : public IImageListener {
private:
  const std::string      _imageName;
  IImageFactoryListener* _listener;
  const bool             _deleteListener;


public:
  CanvasImageFactory_ImageListener(const std::string& imageName,
                                   IImageFactoryListener* listener,
                                   bool deleteListener) :
  _imageName(imageName),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~CanvasImageFactory_ImageListener() {
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

void CanvasImageFactory::create(const G3MContext* context,
                               IImageFactoryListener* listener,
                               bool deleteListener) {
  ICanvas* canvas = context->getFactory()->createCanvas(_retina);
  canvas->initialize(_width, _height);

  buildOnCanvas(context, canvas);

  canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                          new CanvasImageFactory_ImageListener(getImageName(context),
                                                                                               listener,
                                                                                               deleteListener),
                                                          true),
                      true);
}

const std::string CanvasImageFactory::getResolutionID(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();

  return (
          su->toString(_width) + "x" + su->toString(_height) +
          (_retina ? "@2x" : "")
          );
}
