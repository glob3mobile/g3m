//
//  ColumnLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "ColumnLayoutImageBuilder.hpp"

#include "Context.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImageListener.hpp"

ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                                                   int margin,
                                                   int borderWidth,
                                                   const Color& borderColor,
                                                   int padding) :
LayoutImageBuilder(children, margin, borderWidth, borderColor, padding)
{

}

ColumnLayoutImageBuilder::ColumnLayoutImageBuilder(IImageBuilder* child0,
                                                   IImageBuilder* child1,
                                                   int margin ,
                                                   int borderWidth ,
                                                   const Color& borderColor,
                                                   int padding) :
LayoutImageBuilder(child0, child1, margin, borderWidth, borderColor, padding)
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
  std::string imageName = "";

  int maxWidth = 0;
  int accumulatedHeight = 0;

  const int resultsSize = results.size();
  for (int i = 0; i < resultsSize; i++) {
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
    ICanvas* canvas = context->getFactory()->createCanvas();

    const int width = maxWidth;
    const int height = accumulatedHeight;
    canvas->initialize(width, height);
#warning remove debug code
    canvas->setFillColor(Color::red());
    canvas->fillRectangle(0, 0, width, height);

#warning TODO : margin, border, padding, background color?, borderRadius?
    float cursorTop = height;
    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;

      const float destLeft = ((float) (width - image->getWidth()) / 2.0f);
      cursorTop -= image->getHeight();
      canvas->drawImage(image,
                        destLeft,
                        cursorTop);
    }

    canvas->createImage(new ColumnLayoutImageBuilder_IImageListener(imageName,
                                                                    listener,
                                                                    deleteListener),
                        true);
    delete canvas;
  }

#warning delete images??
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
