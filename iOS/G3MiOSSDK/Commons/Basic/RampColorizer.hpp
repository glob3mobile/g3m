//
//  RampColorizer.hpp
//  G3MiOSSDK
//
//  Created by Nico on 21/02/2019.
//

#ifndef RampColorizer_hpp
#define RampColorizer_hpp

#include <stdio.h>
#include <vector>
#include "Color.hpp"



class RampColorizer {
private:
#ifdef C_CODE
  const std::vector<Color>   _colors;
#else
  std::vector<Color>   _colors;
#endif
  
  const size_t               _colorsLength;
#ifdef C_CODE
  const std::vector<float>   _steps;
#else
  std::vector<float>   _steps;
#endif
  
  static const std::vector<float> createDefaultSteps(size_t length);
  
  RampColorizer(const std::vector<Color>& colors,
                const std::vector<float>& steps);
  
public:
  
  static RampColorizer createRampColorizer(const std::vector<Color>& colors,
                                           const std::vector<float>& steps);
  
  static RampColorizer createRampColorizer(const std::vector<Color>& colors);
  
  const Color getColor(const float alpha) const;
  
  ~RampColorizer();
};

#endif /* RampColorizer_hpp */
