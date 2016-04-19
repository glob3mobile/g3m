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
#include "ErrorHandling.hpp"

class ColorLegend{
  
  
public:
  
  class ColorAndValue{
  public:
    double _value;
    Color _color;
    ColorAndValue(const Color& color, double value):_value(value), _color(color){}
  };
  
  ColorLegend(std::vector<ColorAndValue*> legend):_legend(legend){
  
    for (size_t i = 0; i < _legend.size() -1; i++) {
      if (_legend[i]->_value >= _legend[i+1]->_value){
        THROW_EXCEPTION("ColorLegend -> List of colors must be passed in ascendant order.");
      }
    }
  
  }
  
  ~ColorLegend(){
    for (size_t i = 0; i < _legend.size(); i++) {
      delete _legend[i];
    }
  }
  
  
  Color getColor(double value) const{
    
    ColorAndValue* inf= NULL, *sup = NULL;
    
    for (size_t i = 0; i < _legend.size(); i++) {
      if (_legend[i]->_value <= value){
        inf = _legend[i];
        if (i < _legend.size() -1){
          sup = _legend[i+1];
        }
      }
    }
    
    if (inf == NULL){
      return _legend[0]->_color;
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
