//
//  RampColorizer.hpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

#ifndef RampColorizer_hpp
#define RampColorizer_hpp

#include <stdio.h>
#include "Color.hpp"

#endif /* RampColorizer_hpp */

class RampColorizer {
private:
  const std::vector<Color>   _colors;
  const int                  _colorsLength;
  const std::vector<float>   _steps;
  
  std::vector<float> RampColorizer::createDefaultSteps(int length);
  
  Color RampColorizer::getColor(const float alpha);
  
public:
  RampColorizer::RampColorizer(const std::vector<Color> colors,
                               const int                colorsLength,
                               const std::vector<float> steps);
  
  
  RampColorizer::RampColorizer(const std::vector<Color> colors,
                               const std::vector<float> steps);
  
  RampColorizer::RampColorizer(const std::vector<Color> colors);
  
  
  }
