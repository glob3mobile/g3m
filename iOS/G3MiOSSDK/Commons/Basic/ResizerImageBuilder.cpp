//
//  ResizerImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#include "ResizerImageBuilder.hpp"

#include "ImageSizer.hpp"
#include "ErrorHandling.hpp"
#include "IImageBuilderListener.hpp"
#include "IStringUtils.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImage.hpp"
#include "IImageListener.hpp"


class ResizerImageBuilder_IImageBuilderListener : public IImageBuilderListener {
private:
  const G3MContext* _context;

  ResizerImageBuilder* _builder;

  IImageBuilderListener* _listener;
  const bool             _deleteListener;

public:

  ResizerImageBuilder_IImageBuilderListener(const G3MContext* context,
                                            ResizerImageBuilder* builder,
                                            IImageBuilderListener* listener,
                                            bool deleteListener) :
  _context(context),
  _builder(builder),
  _listener(listener),
  _deleteListener(deleteListener)
  {

  }

  ~ResizerImageBuilder_IImageBuilderListener() {
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
    _listener = NULL;
  }

  void onError(const std::string& error) {
    _builder->onError(error,
                      _listener,
                      _deleteListener);
    _listener = NULL;
  }

};


class ResizerImageBuilder_ImageListener : public IImageListener {
private:
  const std::string _imageName;
  IImageBuilderListener* _listener;
  const bool _deleteListener;

public:

  ResizerImageBuilder_ImageListener(const std::string& imageName,
                                    IImageBuilderListener* listener,
                                    bool deleteListener) :
  _imageName(imageName),
  _listener(listener),
  _deleteListener(deleteListener)
  {

  }

  ~ResizerImageBuilder_ImageListener() {
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


ResizerImageBuilder::ResizerImageBuilder(IImageBuilder* imageBuilder,
                                         ImageSizer*    widthSizer,
                                         ImageSizer*    heightSizer) :
_imageBuilder(imageBuilder),
_widthSizer(widthSizer),
_heightSizer(heightSizer)
{
  if (_imageBuilder->isMutable()) {
    THROW_EXCEPTION("Mutable imageBuilder is not supported!");
  }
}

ResizerImageBuilder::~ResizerImageBuilder() {
  delete _imageBuilder;

  delete _widthSizer;
  delete _heightSizer;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void ResizerImageBuilder::build(const G3MContext* context,
                                IImageBuilderListener* listener,
                                bool deleteListener) {
  _imageBuilder->build(context,
                       new ResizerImageBuilder_IImageBuilderListener(context,
                                                                     this,
                                                                     listener,
                                                                     deleteListener),
                       true);
}


void ResizerImageBuilder::onError(const std::string& error,
                                  IImageBuilderListener* listener,
                                  bool deleteListener) {
  listener->onError(error);
  if (deleteListener) {
    delete listener;
  }
}

void ResizerImageBuilder::imageCreated(const IImage*      image,
                                       const std::string& imageName,
                                       const G3MContext* context,
                                       IImageBuilderListener* listener,
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

    canvas->createImage(new ResizerImageBuilder_ImageListener(resizedImageName,
                                                              listener,
                                                              deleteListener),
                        true);

    delete canvas;

    delete image;
  }

}
