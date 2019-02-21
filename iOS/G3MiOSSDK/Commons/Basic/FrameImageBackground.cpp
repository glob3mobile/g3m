//
//  FrameImageBackground.cpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

#include "FrameImageBackground.hpp"

//#include "Vector2I.hpp"
#include "IMathUtils.hpp"
#include "ICanvas.hpp"
#include "IStringUtils.hpp"


FrameImageBackground::FrameImageBackground(const float topFrameHeight,
                                           const float bottomFrameHeight,
                                           const float leftFrameWidth,
                                           const float rightFrameWidth,
                                           const Color& color) :
_topFrameHeight(topFrameHeight),
_bottomFrameHeight(bottomFrameHeight),
_leftFrameWidth(leftFrameWidth),
_rightFrameWidth(rightFrameWidth),
_color(color)
{
  
}


const Vector2F FrameImageBackground::initializeCanvas(ICanvas* canvas,
                                                      const float contentWidth,
                                                      const float contentHeight) const {
  
  
  const IMathUtils* mu = IMathUtils::instance();
  
  const float canvasWidth  = contentWidth  + _leftFrameWidth + _rightFrameWidth;
  const float canvasHeight = contentHeight + _topFrameHeight + _bottomFrameHeight;
  
  canvas->initialize((int) mu->ceil(canvasWidth),
                     (int) mu->ceil(canvasHeight));
  
  
  if (!_color.isTransparent()) {
    canvas->setFillColor(_color);
    if (_topFrameHeight > 0) {
      canvas->fillRectangle(0,
                            0,
                            canvasWidth,
                            _topFrameHeight);
    }
    if (_bottomFrameHeight > 0) {
      canvas->fillRectangle(0,
                            canvasHeight - _bottomFrameHeight,
                            canvasWidth,
                            _bottomFrameHeight);
    }
    
    if (_leftFrameWidth > 0) {
      canvas->fillRectangle(0,
                            _topFrameHeight,
                            _leftFrameWidth,
                            canvasHeight - _topFrameHeight - _bottomFrameHeight);
      
    }
    if (_rightFrameWidth > 0) {
      canvas->fillRectangle(canvasWidth - _rightFrameWidth,
                            _topFrameHeight,
                            _rightFrameWidth,
                            canvasHeight - _topFrameHeight - _bottomFrameHeight);
    }
  }
  
  
  const Vector2F contentPosition(_leftFrameWidth,
                                 _topFrameHeight);
  return contentPosition;
}

const std::string FrameImageBackground::description() const {
  const IStringUtils* su = IStringUtils::instance();
  return ("Frame/"                               +
          su->toString(_topFrameHeight)    + "/" +
          su->toString(_bottomFrameHeight) + "/" +
          su->toString(_leftFrameWidth)    + "/" +
          su->toString(_rightFrameWidth)   + "/" +
          _color.id());
}

FrameImageBackground* FrameImageBackground::copy() const {
  return new FrameImageBackground(_topFrameHeight,
                                  _bottomFrameHeight,
                                  _leftFrameWidth,
                                  _rightFrameWidth,
                                  _color);
  
}
