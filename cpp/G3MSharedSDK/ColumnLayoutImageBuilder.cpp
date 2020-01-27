//
//  ColumnLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "ColumnLayoutImageBuilder.hpp"

#include "IImageListener.hpp"
#include "ImageBackground.hpp"
#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"


ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                                                   const ImageBackground*             background,
                                                   const int                          childrenSeparation) :
LayoutImageBuilder(children,
                   background),
_childrenSeparation(childrenSeparation)
{

}

ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(IImageBuilder*         child0,
                                                   IImageBuilder*         child1,
                                                   const ImageBackground* background,
                                                   const int              childrenSeparation) :
LayoutImageBuilder(child0,
                   child1,
                   background),
_childrenSeparation(childrenSeparation)
{

}

ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(IImageBuilder*         child0,
                                                   const ImageBackground* background,
                                                   const int              childrenSeparation) :
LayoutImageBuilder(child0,
                   background),
_childrenSeparation(childrenSeparation)
{

}

class ColumnLayoutImageBuilder_IImageListener : public IImageListener {
private:
  IImageBuilderListener* _listener;
  bool _deleteListener;

  const std::string _imageName;

public:
  ColumnLayoutImageBuilder_IImageListener(const std::string& imageName,
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

void ColumnLayoutImageBuilder::doLayout(const G3MContext* context,
                                        IImageBuilderListener* listener,
                                        bool deleteListener,
                                        const std::vector<ChildResult*>& results)
{
  bool anyError = false;
  std::string error = "";
  std::string imageName = "Col";

  int maxWidth = 0;
  int accumulatedHeight = 0;

  const size_t resultsSize = results.size();
  for (size_t i = 0; i < resultsSize; i++) {
    ChildResult* result = results[i];
    const IImage* image = result->_image;

    if (image == NULL) {
      anyError = true;
      error += result->_error + " ";
    }
    else {
      accumulatedHeight += image->getHeight();
      if (image->getWidth() > maxWidth) {
        maxWidth = image->getWidth();
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
    const float contentHeight = accumulatedHeight + ((resultsSize - 1) * _childrenSeparation);

    ICanvas* canvas = context->getFactory()->createCanvas(false);

    const Vector2F contentPos = _background->initializeCanvas(canvas,
                                                              contentWidth,
                                                              contentHeight);

    float cursorTop = contentPos._y;
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const int imageWidth  = image->getWidth();
      const int imageHeight = image->getHeight();

      const float left = contentPos._x + ((contentWidth - imageWidth) / 2.0f);
      canvas->drawImage(image, left, cursorTop);
      cursorTop += imageHeight + _childrenSeparation;
    }

    canvas->createImage(new ColumnLayoutImageBuilder_IImageListener(imageName,
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
