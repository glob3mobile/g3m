//
//  BilinearInterpolator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#ifndef __G3MiOSSDK__BilinearInterpolator__
#define __G3MiOSSDK__BilinearInterpolator__

#include "Interpolator.hpp"

class BilinearInterpolator : public Interpolator {
public:

  double interpolation(double valueSW,
                       double valueSE,
                       double valueNE,
                       double valueNW,
                       double u,
                       double v) const;

  
  const std::string description() const {
    return "BilinearInterpolator";
  }
  
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
