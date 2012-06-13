//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Color_hpp
#define G3MiOSSDK_Color_hpp

class Color {
private:
  const float _red;
  const float _green;
  const float _blue;

  Color(const float red,
        const float green,
        const float blue) :
  _red(red),
  _green(green),
  _blue(blue)
  {
    
  }
  
public:
  
  static Color fromRGB(const float red,
                       const float green,
                       const float blue) {
    return Color(red, green, blue);
  }
  
  static Color black() {
    return Color::fromRGB(0, 0, 0);
  }
  
  static Color white() {
    return Color::fromRGB(1, 1, 1);
  }
  
  
  float getRed() const {
    return _red;
  }
  
  float getGreen() const {
    return _green;
  }
  
  float getBlue() const {
    return _blue;
  }
  
};

#endif
