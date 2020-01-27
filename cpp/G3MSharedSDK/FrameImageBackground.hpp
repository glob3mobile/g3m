//
//  FrameImageBackground.hpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

#ifndef FrameImageBackground_hpp
#define FrameImageBackground_hpp

#include "ImageBackground.hpp"

#include "Color.hpp"


class FrameImageBackground : public ImageBackground {
private:
  FrameImageBackground(const FrameImageBackground& that);
  
  const float _topFrameHeight;
  const float _bottomFrameHeight;
  const float _leftFrameWidth;
  const float _rightFrameWidth;
  
  const Color _color;
  
public:
  FrameImageBackground(const float topFrameHeight,
                       const float bottomFrameHeight,
                       const float leftFrameWidth,
                       const float rightFrameWidth,
                       const Color& color);
  
  const Vector2F initializeCanvas(ICanvas* canvas,
                                  const float contentWidth,
                                  const float contentHeight) const;
  
  const std::string description() const;
  
  FrameImageBackground* copy() const;
  
};

#endif
