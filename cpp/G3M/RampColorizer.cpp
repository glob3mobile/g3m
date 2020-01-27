//
//  RampColorizer.cpp
//  G3M
//
//  Created by Nico on 21/02/2019.
//

#include "ErrorHandling.hpp"
#include <vector>
#include "RampColorizer.hpp"
#include "Color.hpp"


const RampColorizer* RampColorizer::createRampColorizer(const std::vector<Color>& colors,
                                                        const std::vector<float>& steps) {
  return new RampColorizer(colors, steps);
}

const RampColorizer* RampColorizer::createRampColorizer(const std::vector<Color>& colors) {
  return new RampColorizer(colors, createDefaultSteps(colors.size()));
}

RampColorizer::RampColorizer(const std::vector<Color>& colors,
                             const std::vector<float>& steps) :
_colors(colors),
_colorsLength(_colors.size()),
_steps(steps)
{
  if (colors.empty()) {
    THROW_EXCEPTION("Colors is empty.");
  }
  if (steps.size() != colors.size()) {
    THROW_EXCEPTION("Steps size is not equal as colors size.");
  }
}

RampColorizer::~RampColorizer() {

}

const std::vector<float> RampColorizer::createDefaultSteps(const size_t length) {
  std::vector<float> result;
  const float step = 1.0f / (length - 1);
  for (size_t i = 0; i < length; i++) {
    result.push_back(step * i);
  }
  return result;
}

const Color RampColorizer::getColor(const float alpha) const {
  if (_colorsLength == 1) {
    return _colors[0];
  }
  
  if (alpha <= 0) {
    return _colors[0];
  }
  if (alpha >= 1) {
    return _colors[_colorsLength - 1];
  }
  
  int baseColorIndex = -1;
  float baseStep = 0;
  
  for (int i = _steps.size() - 1; i >= 0; i--) {
    const  float step = _steps[i];
    if (alpha <= step) {
      baseStep = step;
      baseColorIndex = i;
    }
  }
  const float deltaStep = baseStep - alpha;
  
  if (deltaStep <= 0.0001) {
    return _colors[baseColorIndex];
  }
  
  const float localAlpha = 1 - (deltaStep * (_colorsLength - 1));
  
  return _colors[baseColorIndex - 1].mixedWith((_colors[baseColorIndex]), localAlpha);
}

