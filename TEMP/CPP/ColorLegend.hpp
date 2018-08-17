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
    
    if (_legend.size() == 0){
      return;
    }
    
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
    if (value <= _legend[0]->_value){
      return _legend[0]->_color;
    } else{
      
      for (size_t i = 0; i < _legend.size(); i++) {
        if (i == _legend.size()-1 ||
            _legend[i]->_value == value){
          return _legend[i]->_color;
        } else{
          
          if (_legend[i]->_value > value){
            ColorAndValue* inf= _legend[i-1];
            ColorAndValue* sup =  _legend[i];
            
            double x = (value - inf->_value) / (sup->_value - inf->_value);
            return Color::interpolateColor(inf->_color, sup->_color, (float)x);
          }
        }
      }
    }
    
    return Color::transparent();
  }
  
private:
  
  std::vector<ColorAndValue*> _legend;
  
};

#endif /* ColorLegend_hpp */
