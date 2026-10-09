//
//  LabelImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#include "LabelImageFactory.hpp"

#include "ImageBackground.hpp"
#include "IImageListener.hpp"
#include "IImageFactoryListener.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "ErrorHandling.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"


class LabelImageFactory_ImageListener : public IImageListener {
private:
  IImageFactoryListener* _listener;
  bool                   _deleteListener;
  const std::string      _imageName;

public:
  LabelImageFactory_ImageListener(IImageFactoryListener* listener,
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
  
  ~LabelImageFactory_ImageListener() {
    if (_deleteListener) {
      delete _listener;
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};


LabelImageFactory::LabelImageFactory(const std::string& text,
                                     const LabelStyle&  style) :
_text(text),
_style(new LabelStyle(style)),
_isMutable(false)
{
}

LabelImageFactory::LabelImageFactory(const std::string& text,
                                     const LabelStyle&  style,
                                     const bool         isMutable) :
_text(text),
_style(new LabelStyle(style)),
_isMutable(isMutable)
{
}

LabelImageFactory::~LabelImageFactory() {
  delete _style;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const std::string LabelImageFactory::getImageName() const {
  return _text + "/" + _style->description();
}


void LabelImageFactory::setText(const std::string& text) {
  if (_isMutable) {
    if (_text != text) {
      _text = text;
      changed();
    }
  }
  else {
    THROW_EXCEPTION("Can't change text on an inmutable LabelImageFactory");
  }
}

void LabelImageFactory::create(const G3MContext* context,
                              IImageFactoryListener* listener,
                              bool deleteListener) {
  
  ICanvas* canvas = context->getFactory()->createCanvas(true);
  
  canvas->setFont(_style->getFont());
  
  const Vector2F textExtent = canvas->textExtent(_text);
  
  const Vector2F contentPos = _style->initializeCanvas(canvas, textExtent);
  
  if (_style->hasShadow()) {
    const Vector2F shadowOffset = _style->getShadowOffset();
    canvas->setShadow(_style->getShadowColor(),
                      _style->getShadowBlur(),
                      shadowOffset._x,
                      shadowOffset._y);
  }
  
  canvas->setFillColor(_style->getColor());
  canvas->fillText(_text, contentPos._x, contentPos._y);

  canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                          new LabelImageFactory_ImageListener(listener,
                                                                                              deleteListener,
                                                                                              getImageName()),
                                                          true),
                      true);
}
