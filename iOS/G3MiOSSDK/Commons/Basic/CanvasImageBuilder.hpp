//
//  CanvasImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

#ifndef __G3MiOSSDK__CanvasImageBuilder__
#define __G3MiOSSDK__CanvasImageBuilder__

#include "AbstractImageBuilder.hpp"
class ICanvas;
#include <string>

class CanvasImageBuilder : public AbstractImageBuilder {
private:
  ICanvas* _canvas;
  int      _canvasWidth;
  int      _canvasHeight;

  ICanvas* getCanvas(const G3MContext* context);

  static long long _counter;

protected:
  const int _width;
  const int _height;

  CanvasImageBuilder(int width, int height) :
  _width(width),
  _height(height),
  _canvas(NULL),
  _canvasWidth(0),
  _canvasHeight(0)
  {
  }

  virtual ~CanvasImageBuilder();

  virtual void buildOnCanvas(const G3MContext* context,
                             ICanvas* canvas) = 0;

  virtual std::string getImageName(const G3MContext* context) const;

public:
  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
