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
  const std::vector<Color>   _colors;
  const size_t               _colorsLength;
  const std::vector<float>   _steps;
  
  static const std::vector<float> createDefaultSteps(size_t length);
  
  RampColorizer(const std::vector<Color>& colors,
                const std::vector<float>& steps);
  
  RampColorizer(const std::vector<Color>& colors);
public:
  
  static RampColorizer initializeRampColorizer(const std::vector<Color>& colors,
                                               const std::vector<float>& steps);
  
  static RampColorizer initializeRampColorizer(const std::vector<Color>& colors);
  
  const Color getColor(const float alpha) const;
  
  ~RampColorizer();
};

#endif /* RampColorizer_hpp */
