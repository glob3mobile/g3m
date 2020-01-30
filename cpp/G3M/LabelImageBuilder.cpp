//
//  LabelImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#include "LabelImageBuilder.hpp"

#include "IStringUtils.hpp"
#include "ImageBackground.hpp"
#include "CanvasOwnerImageListener.hpp"
#include "IImageBuilderListener.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "ErrorHandling.hpp"
#include "NullImageBackground.hpp"


class LabelImageBuilder_ImageListener : public CanvasOwnerImageListener {
private:
  IImageBuilderListener* _listener;
  bool                   _deleteListener;
  const std::string      _imageName;

public:
  LabelImageBuilder_ImageListener(ICanvas* canvas,
                                  IImageBuilderListener* listener,
                                  bool deleteListener,
                                  const std::string& imageName) :
  CanvasOwnerImageListener(canvas),
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
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};


LabelImageBuilder::LabelImageBuilder(const std::string&     text,
                                     const GFont&           font,
                                     const Color&           color,
                                     const Color&           shadowColor,
                                     const float            shadowBlur,
                                     const Vector2F&        shadowOffset,
                                     const ImageBackground* background,
                                     const bool             isMutable) :
_text(text),
_font(font),
_color(color),
_shadowColor(shadowColor),
_shadowBlur(shadowBlur),
_shadowOffset(shadowOffset),
_background((background == NULL) ? new NullImageBackground() : background),
_isMutable(isMutable)
{
}

LabelImageBuilder::~LabelImageBuilder() {
  delete _background;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const std::string LabelImageBuilder::getImageName() const {
  const IStringUtils* su = IStringUtils::instance();
  return (_text                       + "/" +
          _font.description()         + "/" +
          _color.id()                 + "/" +
          _shadowColor.id()           + "/" +
          su->toString(_shadowBlur)   + "/" +
          _shadowOffset.description() + "/" +
          _background->description() );
}


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
  
  const Vector2F contentPos = _background->initializeCanvas(canvas,
                                                            textExtent._x,
                                                            textExtent._y);
  
  if (!_shadowColor.isFullTransparent()) {
    canvas->setShadow(_shadowColor,
                      _shadowBlur,
                      _shadowOffset._x,
                      _shadowOffset._y);
  }
  
  canvas->setFillColor(_color);
  canvas->fillText(_text, contentPos._x, contentPos._y);
  
  canvas->createImage(new LabelImageBuilder_ImageListener(canvas /* transfer canvas to be deleted AFTER the image creation */,
                                                          listener,
                                                          deleteListener,
                                                          getImageName()),
                      true);
}
