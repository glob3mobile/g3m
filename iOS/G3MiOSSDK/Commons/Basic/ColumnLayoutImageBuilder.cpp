//
//  ColumnLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "ColumnLayoutImageBuilder.hpp"

#include "G3MContext.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImageListener.hpp"
#include "IMathUtils.hpp"


ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                                                   const Vector2F&                    margin,
                                                   float                              borderWidth,
                                                   const Color&                       borderColor,
                                                   const Vector2F&                    padding,
                                                   const Color&                       backgroundColor,
                                                   float                              cornerRadius,
                                                   int                                childrenSeparation) :
LayoutImageBuilder(children,
                   margin,
                   borderWidth,
                   borderColor,
                   padding,
                   backgroundColor,
                   cornerRadius,
                   childrenSeparation)
{

}

ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(IImageBuilder*  child0,
                                                   IImageBuilder*  child1,
                                                   const Vector2F& margin,
                                                   float           borderWidth,
                                                   const Color&    borderColor,
                                                   const Vector2F& padding,
                                                   const Color&    backgroundColor,
                                                   float           cornerRadius,
                                                   int             childrenSeparation) :
LayoutImageBuilder(child0,
                   child1,
                   margin,
                   borderWidth,
                   borderColor,
                   padding,
                   backgroundColor,
                   cornerRadius,
                   childrenSeparation)
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

  if (anyError) {
    if (listener != NULL) {
      listener->onError(error);
      if (deleteListener) {
        delete listener;
      }
    }
  }
  else {
    const Vector2F margin2  = _margin.times(2);
    const Vector2F padding2 = _padding.times(2);

    const IMathUtils* mu = context->getMathUtils();

    const int canvasWidth  = (int) mu->ceil(maxWidth          + margin2._x + padding2._x);
    const int canvasHeight = (int) mu->ceil(accumulatedHeight + margin2._y + padding2._y + ((int)resultsSize-1)*_childrenSeparation);

    ICanvas* canvas = context->getFactory()->createCanvas(false);
    canvas->initialize(canvasWidth, canvasHeight);

    //#warning remove debug code
    //    canvas->setFillColor(Color::red());
    //    canvas->fillRectangle(0, 0, width, height);

    if (!_backgroundColor.isFullTransparent()) {
      canvas->setFillColor(_backgroundColor);
      if (_cornerRadius > 0) {
        canvas->fillRoundedRectangle(_margin._x,
                                     _margin._y,
                                     canvasWidth  - margin2._x,
                                     canvasHeight - margin2._y,
                                     _cornerRadius);
      }
      else {
        canvas->fillRectangle(_margin._x,
                              _margin._y,
                              canvasWidth  - margin2._x,
                              canvasHeight - margin2._y);
      }
    }

    if (_borderWidth > 0 && !_borderColor.isFullTransparent()) {
      canvas->setLineColor(_borderColor);
      canvas->setLineWidth(_borderWidth);
      if (_cornerRadius > 0) {
        canvas->strokeRoundedRectangle(_margin._x,
                                       _margin._y,
                                       canvasWidth  - margin2._x,
                                       canvasHeight - margin2._y,
                                       _cornerRadius);
      }
      else {
        canvas->strokeRectangle(_margin._x,
                                _margin._y,
                                canvasWidth  - margin2._x,
                                canvasHeight - margin2._y);
      }
    }

    float cursorTop = _margin._y + _padding._y;
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const int imageWidth  = image->getWidth();
      const int imageHeight = image->getHeight();

      const float left = ((float) (canvasWidth - imageWidth) / 2.0f);
      canvas->drawImage(image, left, cursorTop);
      //canvas->strokeRectangle(left, cursorTop, imageWidth, imageHeight);
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
