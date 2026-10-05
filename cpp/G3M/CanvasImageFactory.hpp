//
//  CanvasImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/10/14.
//
//

#ifndef __G3M__CanvasImageFactory__
#define __G3M__CanvasImageFactory__

#include "AbstractImageFactory.hpp"
class ICanvas;
#include <string>

class CanvasImageFactory : public AbstractImageFactory {
private:
  const int  _width;
  const int  _height;
  const bool _retina;

protected:

  CanvasImageFactory(int width,
                     int height,
                     bool retina) :
  _width(width),
  _height(height),
  _retina(retina)
  {
  }

  const std::string getResolutionID(const G3MContext* context) const;

  virtual ~CanvasImageFactory();

  virtual void buildOnCanvas(const G3MContext* context,
                             ICanvas* canvas) = 0;

  virtual const std::string getImageName(const G3MContext* context) const = 0;

public:
  void create(const G3MContext* context,
             IImageFactoryListener* listener,
             bool deleteListener);
  
};

#endif
