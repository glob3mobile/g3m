//
//  RampColorizer.cpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

#include <vector>
#include <iostream>
#include "RampColorizer.hpp"
#include "Color.hpp"


RampColorizer::RampColorizer(const std::vector<Color> colors,
                             const int                colorsLength,
                             const std::vector<float> steps) :
_colors(colors),
_colorsLength(colorsLength),
_steps(steps)
{
  
}


RampColorizer::RampColorizer(const std::vector<Color> colors) :
_colors(colors),
_colorsLength(colors.size()),
_steps(createDefaultSteps(colors.size()))
{
  
}


RampColorizer::RampColorizer(const std::vector<Color> colors,
                             const std::vector<float> steps) :
_colors(colors),
_colorsLength(_colors.size()),
_steps(steps)
{
  
}


std::vector<float> RampColorizer::createDefaultSteps(int length) {
  std::vector<float> result;
  float step = 1 / (length - 1);
  for (int i = 0; i < length; i++) {
    result.push_back(step * i);
  }
  return result;
}


Color RampColorizer::getColor(const float alpha) {
  if (_colorsLength == 1) {
    return _colors.at(0);
  }
  
  if (alpha <= 0) {
    return _colors.at(0);
  }
  if (alpha >= 1) {
    return _colors.at(_colorsLength - 1);
  }
  
  int baseColorIndex = -1;
  float baseStep = 0;
  
  for (int i = _steps.size() - 1; i >= 0; i--) {
    const  float step = _steps.at(i);
    if (alpha <= step) {
      baseStep = step;
      baseColorIndex = i;
    }
  }
  const float deltaStep = baseStep - alpha;
  
  if (deltaStep <= 0.0001) {
    return _colors.at(baseColorIndex);
  }
  
  const float localAlpha = 1 - (deltaStep * (_colorsLength - 1));
  
  return _colors.at(baseColorIndex - 1).mixedWith(_colors.at(baseColorIndex), localAlpha);
}
