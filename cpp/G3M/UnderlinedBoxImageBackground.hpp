//
//  UnderlinedBoxImageBackground.hpp
//  G3M
//

#ifndef UnderlinedBoxImageBackground_hpp
#define UnderlinedBoxImageBackground_hpp

#include "ImageBackground.hpp"

#include "Vector2F.hpp"
#include "Color.hpp"


/*
 ..............................................
 . margin                                     .
 .  /--------------------------------------\  .
 .  | padding                              |  .
 .  |  #content##########################  |  .
 .  |  ##################################  |  .
 .  |                                      |  .
 .  ========================================  .
 .                                            .
 ..............................................
 a box with rounded top corners, square bottom corners and a line along its
 bottom, inside the box: the image keeps the size of a BoxImageBackground's
 */


class UnderlinedBoxImageBackground : public ImageBackground {
private:
  UnderlinedBoxImageBackground(const UnderlinedBoxImageBackground& that);

  const Vector2F _margin;
  const Vector2F _padding;
  const Color    _backgroundColor;
  const float    _cornerRadius;
  const Color    _lineColor;
  const float    _lineWidth;

public:
  UnderlinedBoxImageBackground(const Vector2F& margin,
                               const Vector2F& padding,
                               const Color&    backgroundColor,
                               const float     cornerRadius,
                               const Color&    lineColor,
                               const float     lineWidth);

  const Vector2F initializeCanvas(ICanvas* canvas,
                                  const float contentWidth,
                                  const float contentHeight) const;

  const std::string description() const;

  UnderlinedBoxImageBackground* copy() const;

};

#endif
