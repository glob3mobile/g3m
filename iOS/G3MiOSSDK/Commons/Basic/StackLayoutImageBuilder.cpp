//
//  StackLayoutImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#include "StackLayoutImageBuilder.hpp"

#include "Context.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "IImageListener.hpp"

StackLayoutImageBuilder::StackLayoutImageBuilder(const std::vector<IImageBuilder*>& children) :
LayoutImageBuilder(children,
                   0,
                   0,
                   Color::transparent(),
                   0,
                   Color::transparent(),
                   0,
                   0)
{

}

StackLayoutImageBuilder::StackLayoutImageBuilder(IImageBuilder* child0,
                                                 IImageBuilder* child1) :
LayoutImageBuilder(child0,
                   child1,
                   0,
                   0,
                   Color::transparent(),
                   0,
                   Color::transparent(),
                   0,
                   0)
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

  if (anyError) {
    if (listener != NULL) {
      listener->onError(error);
      if (deleteListener) {
        delete listener;
      }
    }
  }
  else {
    const int canvasWidth  = maxWidth;
    const int canvasHeight = maxHeight;

    ICanvas* canvas = context->getFactory()->createCanvas();
    canvas->initialize(canvasWidth, canvasHeight);

    //#warning remove debug code
    //    canvas->setFillColor(Color::red());
    //    canvas->fillRectangle(0, 0, width, height);

    for (int i = 0; i < resultsSize; i++) {
      ChildResult* result = results[i];
      const IImage* image = result->_image;
      const int imageWidth  = image->getWidth();
      const int imageHeight = image->getHeight();

      const float top  = ((float) (canvasHeight - imageHeight) / 2.0f);
      const float left = ((float) (canvasWidth  - imageWidth) / 2.0f);
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
