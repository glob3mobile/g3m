//
//  ColumnLayoutImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "ColumnLayoutImageFactory.hpp"

#include "IImageListener.hpp"
#include "ImageBackground.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "IDeviceInfo.hpp"
#include "ICanvas.hpp"
#include "CanvasOwnerImageListenerWrapper.hpp"
#include "IStringUtils.hpp"


ColumnLayoutImageFactory::ColumnLayoutImageFactory(const std::vector<IImageFactory*>& children,
                                                   const ImageBackground*             background,
                                                   const int                          childrenSeparation,
                                                   const HorizontalAlignment          childrenAlignment) :
LayoutImageFactory(children,
                   background),
_childrenSeparation(childrenSeparation),
_childrenAlignment(childrenAlignment)
{

}

ColumnLayoutImageFactory::ColumnLayoutImageFactory(IImageFactory*         child0,
                                                   IImageFactory*         child1,
                                                   const ImageBackground* background,
                                                   const int              childrenSeparation,
                                                   const HorizontalAlignment childrenAlignment) :
LayoutImageFactory(child0,
                   child1,
                   background),
_childrenSeparation(childrenSeparation),
_childrenAlignment(childrenAlignment)
{

}

ColumnLayoutImageFactory::ColumnLayoutImageFactory(IImageFactory*         child0,
                                                   const ImageBackground* background,
                                                   const int              childrenSeparation,
                                                   const HorizontalAlignment childrenAlignment) :
LayoutImageFactory(child0,
                   background),
_childrenSeparation(childrenSeparation),
_childrenAlignment(childrenAlignment)
{

}

class ColumnLayoutImageFactory_ImageListener : public IImageListener {
private:
  IImageFactoryListener* _listener;
  bool _deleteListener;

  const std::string _imageName;

public:
  ColumnLayoutImageFactory_ImageListener(const std::string& imageName,
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

void ColumnLayoutImageFactory::doLayout(const G3MContext* context,
                                        IImageFactoryListener* listener,
                                        bool deleteListener,
                                        const std::vector<ChildResult*>& results)
{
  bool anyError = false;
  std::string error = "";
  // the textures are cached by image name: each alignment needs its own
  std::string imageName;
  switch (_childrenAlignment) {
    case Left:
      imageName = "ColLeft";
      break;
    case Right:
      imageName = "ColRight";
      break;
    default:
      imageName = "Col";
      break;
  }

  // the children are measured in points, as the retina canvas below draws in points
  const float pixelRatio = context->getFactory()->getDeviceInfo()->getDevicePixelRatio();

  float maxWidth = 0;
  float accumulatedHeight = 0;

  const size_t resultsSize = results.size();
  for (size_t i = 0; i < resultsSize; i++) {
    ChildResult* result = results[i];
    const IImage* image = result->_image;

    if (image == NULL) {
      anyError = true;
      error += result->_error + " ";
    }
    else {
      accumulatedHeight += image->getHeight() / pixelRatio;
      if ((image->getWidth() / pixelRatio) > maxWidth) {
        maxWidth = image->getWidth() / pixelRatio;
      }
      imageName += result->_imageName + "/";
    }
  }

  // the separation changes the pixels, so it must be part of the texture name
  imageName += "sep=" + IStringUtils::instance()->toString(_childrenSeparation) + "/";
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
    const float contentHeight = accumulatedHeight + ((resultsSize - 1) * _childrenSeparation);

    ICanvas* canvas = context->getFactory()->createCanvas(true);

    const Vector2F contentPos = _background->initializeCanvas(canvas,
                                                              contentWidth,
                                                              contentHeight);

    float cursorTop = contentPos._y;
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const float imageWidth  = image->getWidth()  / pixelRatio;
      const float imageHeight = image->getHeight() / pixelRatio;
      
      float left;
      switch (_childrenAlignment) {
        case Left:
          left = contentPos._x;
          break;
        case Right:
          left = contentPos._x + (contentWidth - imageWidth);
          break;
        default:
          left = contentPos._x + ((contentWidth - imageWidth) / 2.0f);
          break;
      }
      canvas->drawImage(image, left, cursorTop, imageWidth, imageHeight);
      cursorTop += imageHeight + _childrenSeparation;
    }
    
    canvas->createImage(new CanvasOwnerImageListenerWrapper(canvas,
                                                            new ColumnLayoutImageFactory_ImageListener(imageName,
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
