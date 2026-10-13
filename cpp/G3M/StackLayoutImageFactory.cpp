//
//  StackLayoutImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "StackLayoutImageFactory.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "IDeviceInfo.hpp"
#include "ICanvas.hpp"
#include "IImageListener.hpp"
#include "ImageBackground.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"


StackLayoutImageFactory::StackLayoutImageFactory(const std::vector<IImageFactory*>& children,
                                                 const ImageBackground*             background) :
LayoutImageFactory(children,
                   background)
{
  
}

StackLayoutImageFactory::StackLayoutImageFactory(IImageFactory*         child0,
                                                 IImageFactory*         child1,
                                                 const ImageBackground* background) :
LayoutImageFactory(child0,
                   child1,
                   background)
{
  
}

class StackLayoutImageFactory_ImageListener : public IImageListener {
private:
  IImageFactoryListener* _listener;
  bool _deleteListener;
  
  const std::string _imageName;
  
public:
  StackLayoutImageFactory_ImageListener(const std::string& imageName,
                                        IImageFactoryListener* listener,
                                        bool deleteListener) :
  _imageName(imageName),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }
  
  void imageCreated(const IImage* image) {
    _listener->imageCreated(image, _imageName);
    if (_deleteListener) {
      delete _listener;
    }
  }
};

void StackLayoutImageFactory::doLayout(const G3MContext* context,
                                       IImageFactoryListener* listener,
                                       bool deleteListener,
                                       const std::vector<ChildResult*>& results)
{
  bool anyError = false;
  std::string error = "";
  std::string imageName = "Stack";
  
  // the children are measured in points, as the retina canvas below draws in points
  const float pixelRatio = context->getFactory()->getDeviceInfo()->getDevicePixelRatio();

  float maxWidth  = 0;
  float maxHeight = 0;
  
  const size_t resultsSize = results.size();
  for (size_t i = 0; i < resultsSize; i++) {
    ChildResult* result = results[i];
    const IImage* image = result->_image;
    
    if (image == NULL) {
      anyError = true;
      error += result->_error + " ";
    }
    else {
      if ((image->getWidth() / pixelRatio) > maxWidth) {
        maxWidth = image->getWidth() / pixelRatio;
      }
      if ((image->getHeight() / pixelRatio) > maxHeight) {
        maxHeight = image->getHeight() / pixelRatio;
      }
      imageName += result->_imageName + "/";
    }
  }
  
  imageName += _background->description();
  
  if (anyError) {
    if (listener != NULL) {
      listener->onError(error);
      if (deleteListener) {
        delete listener;
      }
    }
  }
  else {
    const float contentWidth  = maxWidth;
    const float contentHeight = maxHeight;
    
    ICanvas* canvas = context->getFactory()->createCanvas(true);
    const Vector2F contentPos = _background->initializeCanvas(canvas,
                                                              contentWidth,
                                                              contentHeight);
    
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const float imageWidth  = image->getWidth()  / pixelRatio;
      const float imageHeight = image->getHeight() / pixelRatio;
      
      const float top  = contentPos._y + ((contentHeight - imageHeight) / 2.0f);
      const float left = contentPos._x + ((contentWidth  - imageWidth ) / 2.0f);
      canvas->drawImage(image, left, top, imageWidth, imageHeight);
    }

    canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                            new StackLayoutImageFactory_ImageListener(imageName,
                                                                                                      listener,
                                                                                                      deleteListener),
                                                            true),
                        true);
  }
  
  for (int i = 0; i < resultsSize; i++) {
    ChildResult* result = results[i];
#ifdef C_CODE
    delete result;
#endif
#ifdef JAVA_CODE
    result.dispose();
#endif
  }
  
}
