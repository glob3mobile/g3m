//
//  CanvasImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

#ifndef __G3M__CanvasImageBuilder__
#define __G3M__CanvasImageBuilder__

#include "AbstractImageBuilder.hpp"
class ICanvas;
#include <string>

class CanvasImageBuilder : public AbstractImageBuilder {
private:
  const int  _width;
  const int  _height;
  const bool _retina;

  ICanvas* _canvas;
  int      _canvasWidth;
  int      _canvasHeight;
  bool     _canvasRetina;

  ICanvas* getCanvas(const G3MContext* context);

protected:

  CanvasImageBuilder(int width,
                     int height,
                     bool retina) :
  _width(width),
  _height(height),
  _retina(retina),
  _canvas(NULL),
  _canvasWidth(0),
  _canvasHeight(0),
  _canvasRetina(false)
  {
  }

  const std::string getResolutionID(const G3MContext* context) const;

  virtual ~CanvasImageBuilder();

  virtual void buildOnCanvas(const G3MContext* context,
                             ICanvas* canvas) = 0;

  virtual const std::string getImageName(const G3MContext* context) const = 0;

public:
  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
