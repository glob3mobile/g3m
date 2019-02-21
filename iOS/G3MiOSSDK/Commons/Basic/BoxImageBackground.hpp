//
//  BoxImageBackground.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#ifndef BoxImageBackground_hpp
#define BoxImageBackground_hpp


#include "ImageBackground.hpp"

#include "Vector2F.hpp"
#include "Color.hpp"


/*
 ..............................................
 . margin                                     .
 .  +-border-------------------------------+  .
 .  | padding                              |  .
 .  |  #content##########################  |  .
 .  |  ##################################  |  .
 .  |  ##################################  |  .
 .  |                                      |  .
 .  +--------------------------------------+  .
 .                                            .
 ..............................................
 */


class BoxImageBackground : public ImageBackground {
private:
  BoxImageBackground(const BoxImageBackground& that);

#ifdef C_CODE
  const Vector2F _margin;
#endif
#ifdef JAVA_CODE
  private final Vector2F _margin;
#endif
  const float _borderWidth;
  const Color _borderColor;
#ifdef C_CODE
  const Vector2F _padding;
#endif
#ifdef JAVA_CODE
  private final Vector2F _padding;
#endif
  const Color _backgroundColor;
  const float _cornerRadius;


public:
  BoxImageBackground(const Vector2F& margin,
                     const float     borderWidth,
                     const Color&    borderColor,
                     const Vector2F& padding,
                     const Color&    backgroundColor,
                     const float     cornerRadius);


  const Vector2F initializeCanvas(ICanvas* canvas,
                                  const float contentWidth,
                                  const float contentHeight) const;

  const std::string description() const;

  BoxImageBackground* copy() const;

};

#endif
