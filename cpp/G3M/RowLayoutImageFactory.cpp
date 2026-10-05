//
//  RowLayoutImageFactory.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/20/19.
//

#include "RowLayoutImageFactory.hpp"


#include "IImageListener.hpp"
#include "ImageBackground.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"


RowLayoutImageFactory::RowLayoutImageFactory(const std::vector<IImageFactory*>& children,
                                             const ImageBackground*             background,
                                             const int                          childrenSeparation) :
LayoutImageFactory(children,
                   background),
_childrenSeparation(childrenSeparation)
{
  
}

RowLayoutImageFactory::RowLayoutImageFactory(IImageFactory*         child0,
                                             IImageFactory*         child1,
                                             const ImageBackground* background,
                                             const int              childrenSeparation) :
LayoutImageFactory(child0,
                   child1,
                   background),
_childrenSeparation(childrenSeparation)
{
  
}

RowLayoutImageFactory::RowLayoutImageFactory(IImageFactory*         child0,
                                             const ImageBackground* background,
                                             const int              childrenSeparation) :
LayoutImageFactory(child0,
                   background),
_childrenSeparation(childrenSeparation)
{
  
}

class RowLayoutImageFactory_ImageListener : public IImageListener {
private:
  const std::string _imageName;
  
  IImageFactoryListener* _listener;
  const bool             _deleteListener;
  
public:
  RowLayoutImageFactory_ImageListener(const std::string& imageName,
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

void RowLayoutImageFactory::doLayout(const G3MContext* context,
                                     IImageFactoryListener* listener,
                                     bool deleteListener,
                                     const std::vector<ChildResult*>& results)
{
  bool anyError = false;
  std::string error = "";
  std::string imageName = "Row";
  
  int maxHeight = 0;
  int accumulatedWidth = 0;
  
  const size_t resultsSize = results.size();
  for (size_t i = 0; i < resultsSize; i++) {
    ChildResult* result = results[i];
    const IImage* image = result->_image;
    
    if (image == NULL) {
      anyError = true;
      error += result->_error + " ";
    }
    else {
      accumulatedWidth += image->getWidth();
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
    const float contentWidth  = accumulatedWidth + ((resultsSize - 1) * _childrenSeparation);
    const float contentHeight = maxHeight;
    
    ICanvas* canvas = context->getFactory()->createCanvas(false);
    
    const Vector2F contentPos = _background->initializeCanvas(canvas,
                                                              contentWidth,
                                                              contentHeight);
    
    float cursorLeft = contentPos._x;
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const int imageWidth  = image->getWidth();
      const int imageHeight = image->getHeight();
      
      const float top = contentPos._y + ((contentHeight - imageHeight) / 2.0f);
      canvas->drawImage(image, cursorLeft, top);
      cursorLeft += imageWidth + _childrenSeparation;
    }

    canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                            new RowLayoutImageFactory_ImageListener(imageName,
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
