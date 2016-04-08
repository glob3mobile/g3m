//
//  ColorLegend.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/4/16.
//
//

#ifndef ColorLegend_hpp
#define ColorLegend_hpp

#include <vector>
#include "Color.hpp"

class ColorLegend{
  
  
public:
  
  class ColorAndValue{
  public:
    double _value;
    Color _color;
    ColorAndValue(const Color& color, double value):_value(value), _color(color){}
  };
  
  ColorLegend(std::vector<ColorAndValue*> legend):_legend(legend){}
  
  ~ColorLegend(){
    for (int i = 0; i < _legend.size(); i++) {
      delete _legend[i];
    }
  }
  
  
  Color getColor(double value) const{
    
    ColorAndValue* inf= NULL, *sup = NULL;
    
    for (int i = 0; i < _legend.size(); i++) {
      ColorAndValue* cv = _legend[i];
      if (cv->_value == value){
        return cv->_color;
      }
      if (cv->_value <= value &&
          (inf == NULL || (cv->_value > inf->_value))){
        inf = cv;
      }
      if (cv->_value >= value &&
          ((sup == NULL) || (cv->_value < sup->_value))){
        sup = cv;
      }
    }
    
    if (inf == NULL){
      return sup->_color;
    }
    if (sup == NULL){
      return inf->_color;
    }
    
    double x = (value - inf->_value) / (sup->_value - inf->_value);
    return Color::interpolateColor(inf->_color, sup->_color, (float)x);
  }
  
private:
  
  std::vector<ColorAndValue*> _legend;
  
};

#endif /* ColorLegend_hpp */
