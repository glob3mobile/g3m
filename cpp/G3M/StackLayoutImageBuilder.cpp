//
//  StackLayoutImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "StackLayoutImageBuilder.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImageListener.hpp"
#include "ImageBackground.hpp"


StackLayoutImageBuilder::StackLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                                                 const ImageBackground*             background) :
LayoutImageBuilder(children,
                   background)
{
  
}

StackLayoutImageBuilder::StackLayoutImageBuilder(IImageBuilder*         child0,
                                                 IImageBuilder*         child1,
                                                 const ImageBackground* background) :
LayoutImageBuilder(child0,
                   child1,
                   background)
{
  
}

class StackLayoutImageBuilder_IImageListener : public IImageListener {
private:
  IImageBuilderListener* _listener;
  bool _deleteListener;
  
  const std::string _imageName;
  
public:
  StackLayoutImageBuilder_IImageListener(const std::string& imageName,
                                         IImageBuilderListener* listener,
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

void StackLayoutImageBuilder::doLayout(const G3MContext* context,
                                       IImageBuilderListener* listener,
                                       bool deleteListener,
                                       const std::vector<ChildResult*>& results)
{
  bool anyError = false;
  std::string error = "";
  std::string imageName = "Stack";
  
  int maxWidth  = 0;
  int maxHeight = 0;
  
  const size_t resultsSize = results.size();
  for (size_t i = 0; i < resultsSize; i++) {
    ChildResult* result = results[i];
    const IImage* image = result->_image;
    
    if (image == NULL) {
      anyError = true;
      error += result->_error + " ";
    }
    else {
      if (image->getWidth() > maxWidth) {
        maxWidth = image->getWidth();
      }
      if (image->getHeight() > maxHeight) {
        maxHeight = image->getHeight();
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
    
    ICanvas* canvas = context->getFactory()->createCanvas(false);
    const Vector2F contentPos = _background->initializeCanvas(canvas,
                                                              contentWidth,
                                                              contentHeight);
    
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const int imageWidth  = image->getWidth();
      const int imageHeight = image->getHeight();
      
      const float top  = contentPos._y + ((contentHeight - imageHeight) / 2.0f);
      const float left = contentPos._x + ((contentWidth  - imageWidth ) / 2.0f);
      canvas->drawImage(image, left, top);
    }
    
    canvas->createImage(new StackLayoutImageBuilder_IImageListener(imageName,
                                                                   listener,
                                                                   deleteListener),
                        true);
    delete canvas;
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
