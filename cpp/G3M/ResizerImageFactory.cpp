//
//  ResizerImageFactory.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#include "ResizerImageFactory.hpp"

#include "ImageSizer.hpp"
#include "ErrorHandling.hpp"
#include "IImageFactoryListener.hpp"
#include "IStringUtils.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImage.hpp"
#include "IImageListener.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"


class ResizerImageFactory_ImageFactoryListener : public IImageFactoryListener {
private:
  const G3MContext* _context;

  ResizerImageFactory* _builder;

  IImageFactoryListener* _listener;
  const bool             _deleteListener;

public:

  ResizerImageFactory_ImageFactoryListener(const G3MContext* context,
                                           ResizerImageFactory* builder,
                                           IImageFactoryListener* listener,
                                           bool deleteListener) :
  _context(context),
  _builder(builder),
  _listener(listener),
  _deleteListener(deleteListener)
  {

  }

  ~ResizerImageFactory_ImageFactoryListener() {
    if (_deleteListener) {
      delete _listener;
    }
  }

  void imageCreated(const IImage*      image,
                    const std::string& imageName) {
    _builder->imageCreated(image,
                           imageName,
                           _context,
                           _listener,
                           _deleteListener);
    _listener = NULL; // 'listener' ownership went to _builder
  }

  void onError(const std::string& error) {
    _builder->onError(error,
                      _listener,
                      _deleteListener);
    _listener = NULL; // 'listener' ownership went to _builder
  }

};


class ResizerImageFactory_ImageListener : public IImageListener {
private:
  const std::string _imageName;
  IImageFactoryListener* _listener;
  const bool _deleteListener;

public:

  ResizerImageFactory_ImageListener(const std::string& imageName,
                                    IImageFactoryListener* listener,
                                    bool deleteListener) :
  _imageName(imageName),
  _listener(listener),
  _deleteListener(deleteListener)
  {

  }

  ~ResizerImageFactory_ImageListener() {
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


ResizerImageFactory::ResizerImageFactory(IImageFactory* imageFactory,
                                         ImageSizer*    widthSizer,
                                         ImageSizer*    heightSizer) :
_imageFactory(imageFactory),
_widthSizer(widthSizer),
_heightSizer(heightSizer)
{
  if (_imageFactory->isMutable()) {
    THROW_EXCEPTION("Mutable imageFactory is not supported!");
  }
}

ResizerImageFactory::~ResizerImageFactory() {
  delete _imageFactory;

  delete _widthSizer;
  delete _heightSizer;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void ResizerImageFactory::create(const G3MContext* context,
                                IImageFactoryListener* listener,
                                bool deleteListener) {
  _imageFactory->create(context,
                       new ResizerImageFactory_ImageFactoryListener(context,
                                                                    this,
                                                                    listener,
                                                                    deleteListener),
                       true);
}


void ResizerImageFactory::onError(const std::string& error,
                                  IImageFactoryListener* listener,
                                  bool deleteListener) {
  listener->onError(error);
  if (deleteListener) {
    delete listener;
  }
}

void ResizerImageFactory::imageCreated(const IImage*      image,
                                       const std::string& imageName,
                                       const G3MContext* context,
                                       IImageFactoryListener* listener,
                                       bool deleteListener) {
  const int sourceWidth  = image->getWidth();
  const int sourceHeight = image->getHeight();

  const int resultWidth  = _widthSizer->calculate();
  const int resultHeight = _heightSizer->calculate();

  if ((sourceWidth  == resultWidth) && (sourceHeight == resultHeight)) {
    listener->imageCreated(image, imageName);
    if (deleteListener) {
      delete listener;
    }
  }
  else {
    const IStringUtils* su = context->getStringUtils();

    const std::string resizedImageName = imageName + "/" + su->toString(resultWidth) + "x" + su->toString(resultHeight);

    ICanvas* canvas = context->getFactory()->createCanvas(true);

    canvas->initialize(resultWidth, resultHeight);

    const float ratioWidth  = (float) resultWidth  / sourceWidth;
    const float ratioHeight = (float) resultHeight / sourceHeight;

    const float destWidth  = (ratioHeight > ratioWidth) ? resultWidth                 : (sourceWidth * ratioHeight);
    const float destHeight = (ratioHeight > ratioWidth) ? (sourceHeight * ratioWidth) : resultHeight;

    const float destLeft = (resultWidth  - destWidth ) / 2.0f;
    const float destTop  = (resultHeight - destHeight) / 2.0f;

    canvas->drawImage(image,
                      destLeft, destTop,
                      destWidth, destHeight);

    canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                            new ResizerImageFactory_ImageListener(resizedImageName,
                                                                                                  listener,
                                                                                                  deleteListener),
                                                            true),
                        true);

    delete image;
  }

}
