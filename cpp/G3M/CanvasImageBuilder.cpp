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


CanvasImageBuilder::~CanvasImageBuilder() {
  delete _canvas;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

ICanvas* CanvasImageBuilder::getCanvas(const G3MContext* context) {
  if ((_canvas == NULL) ||
      (_canvasWidth  != _width ) ||
      (_canvasHeight != _height) ||
      (_canvasRetina != _retina)) {
    delete _canvas;

    const IFactory* factory = context->getFactory();

    _canvas = factory->createCanvas(_retina);
    _canvas->initialize(_width, _height);
    _canvasWidth  = _width;
    _canvasHeight = _height;
    _canvasRetina = _retina;
  }
  else {
    _canvas->setFillColor(Color::TRANSPARENT);
    _canvas->fillRectangle(0, 0, _width, _height);
  }

  return _canvas;
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
  ICanvas* canvas = getCanvas(context);

  buildOnCanvas(context, canvas);

  canvas->createImage(new CanvasImageBuilder_ImageListener(getImageName(context),
                                                           listener,
                                                           deleteListener),
                      true);
}

const std::string CanvasImageBuilder::getResolutionID(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();

  return (
          su->toString(_width) + "x" + su->toString(_height) +
          (_retina ? "@2x" : "")
          );
}
