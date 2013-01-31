//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Color_hpp
#define G3MiOSSDK_Color_hpp

#include <iostream>
#include <string>
#include <sstream>

#include "ILogger.hpp"
#include "IMathUtils.hpp"


class Color {
private:
  const float _red;
  const float _green;
  const float _blue;
  const float _alpha;

  Color(const float red,
        const float green,
        const float blue,
        const float alpha) :
  _red(red),
  _green(green),
  _blue(blue),
  _alpha(alpha)
  {

  }
    
  static bool isValidHex(const std::string &hex) {      
      if (hex[0] == '#') {
          if (hex.length() != 7) {
              return false;
          }
      }
      else {
          if (hex.length() != 6) {
              return false;
          }
      }
        
      #ifdef C_CODE
      if(hex.find_first_not_of("#0123456789abcdefABCDEF") != hex.npos){ return false;}
      #endif
      #ifdef JAVA_CODE
      if(!hex.matches("^#?([a-f]|[A-F]|[0-9]){3}(([a-f]|[A-F]|[0-9]){3})?$")){ return false;}
      #endif
    
      return true;
  }
  
  static Color* hexToRGB(std::string hex) {
      if (!isValidHex(hex)) {
          ILogger::instance()->logError("The value received is not avalid hex string!");
      }
      
      if (hex[0] == '#') {
          hex.erase(0,1);
      }
      
      std::string R = hex.substr(0, 2);
      std::string G = hex.substr(2, 2);
      std::string B = hex.substr(4, 2);
      
      return new Color((float)IMathUtils::instance()->parseIntHex(R)/255,(float)IMathUtils::instance()->parseIntHex(G)/255,(float)IMathUtils::instance()->parseIntHex(B)/255,1);
  }

public:
  Color(const Color& that):
  _red(that._red),
  _green(that._green),
  _blue(that._blue),
  _alpha(that._alpha) {
  }

  ~Color() {

  }

  static Color fromRGBA(const float red,
                        const float green,
                        const float blue,
                        const float alpha) {
    return Color(red, green, blue, alpha);
  }

  static Color* newFromRGBA(const float red,
                            const float green,
                            const float blue,
                            const float alpha) {
    return new Color(red, green, blue, alpha);
  }
    
  static Color* newFromHEX(const std::string hex){
      return hexToRGB(hex);
  }

  static Color black() {
    return Color::fromRGBA(0, 0, 0, 1);
  }

  static Color white() {
    return Color::fromRGBA(1, 1, 1, 1);
  }

  static Color yellow() {
    return Color::fromRGBA(1, 1, 0, 1);
  }
  
  static Color cyan() {
    return Color::fromRGBA(0, 1, 1, 1);
  }

  static Color magenta() {
    return Color::fromRGBA(1, 0, 1, 1);
  }
  
  static Color green() {
    return Color::fromRGBA(0, 1, 0, 1);
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

  float getAlpha() const {
    return _alpha;
  }

  Color mixedWith(const Color& that,
                  float factor) const {
    float frac1 = factor;
    if (frac1 < 0) frac1 = 0;
    if (frac1 > 1) frac1 = 1;

    const float frac2 = 1 - frac1;

    const float newRed   = (getRed()   * frac2) + (that.getRed()   * frac1);
    const float newGreen = (getGreen() * frac2) + (that.getGreen() * frac1);
    const float newBlue  = (getBlue()  * frac2) + (that.getBlue()  * frac1);
    const float newAlpha = (getAlpha() * frac2) + (that.getAlpha() * frac1);

    return Color::fromRGBA(newRed, newGreen, newBlue, newAlpha);
  }

  bool isTransparent() const {
    return (_alpha < 1);
  }
  
};

#endif
