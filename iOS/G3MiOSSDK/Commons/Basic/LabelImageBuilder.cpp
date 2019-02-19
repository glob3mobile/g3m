//
//  LabelImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#include "LabelImageBuilder.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IMathUtils.hpp"
#include "IStringUtils.hpp"
#include "IImageListener.hpp"
#include "IImageBuilderListener.hpp"
#include "ErrorHandling.hpp"


const std::string LabelImageBuilder::getImageName() const {
  const IStringUtils* su = IStringUtils::instance();
  return (_text                       + "/" +
          _font.description()         + "/" +
//          _background->description()  + "/" +
          _margin.description()       + "/" +
          _color.id()                 + "/" +
          _shadowColor.id()           + "/" +
          su->toString(_shadowBlur)   + "/" +
          _shadowOffset.description() + "/" +
          _backgroundColor.id()       + "/" +
          su->toString(_cornerRadius));
}


class LabelImageBuilder_ImageListener : public IImageListener {
private:
  IImageBuilderListener* _listener;
  bool                   _deleteListener;
  const std::string      _imageName;

public:
  LabelImageBuilder_ImageListener(IImageBuilderListener* listener,
                                  bool deleteListener,
                                  const std::string& imageName) :
  _listener(listener),
  _deleteListener(deleteListener),
  _imageName(imageName)
  {
  }


  void imageCreated(const IImage* image) {
    _listener->imageCreated(image, _imageName);
    if (_deleteListener) {
      delete _listener;
    }
    _listener = NULL;
  }

  ~LabelImageBuilder_ImageListener() {
    delete _listener;
  }
};

void LabelImageBuilder::setText(const std::string& text) {
  if (_isMutable) {
    if (_text != text) {
      _text = text;
      changed();
    }
  }
  else {
    THROW_EXCEPTION("Can't change text on an inmutable LabelImageBuilder");
  }
}

void LabelImageBuilder::build(const G3MContext* context,
                              IImageBuilderListener* listener,
                              bool deleteListener) {

  ICanvas* canvas = context->getFactory()->createCanvas(true);

  canvas->setFont(_font);

  const Vector2F textExtent = canvas->textExtent(_text);

  const IMathUtils* mu = context->getMathUtils();

  const int width  = (int) mu->ceil(textExtent._x + (_margin._x * 2));
  const int height = (int) mu->ceil(textExtent._y + (_margin._y * 2));
  canvas->initialize(width, height);

  if (!_backgroundColor.isFullTransparent()) {
    canvas->setFillColor(_backgroundColor);
    if (_cornerRadius > 0) {
      canvas->fillRoundedRectangle(0, 0,
                                   width, height,
                                   _cornerRadius);
    }
    else {
      canvas->fillRectangle(0, 0,
                            width, height);
    }
  }

  if (!_shadowColor.isFullTransparent()) {
    canvas->setShadow(_shadowColor,
                      _shadowBlur,
                      _shadowOffset._x,
                      _shadowOffset._y);
  }

  canvas->setFillColor(_color);
  canvas->fillText(_text, _margin._x, _margin._y);

  canvas->createImage(new LabelImageBuilder_ImageListener(listener,
                                                          deleteListener,
                                                          getImageName()),
                      true);
  
  delete canvas;
}
