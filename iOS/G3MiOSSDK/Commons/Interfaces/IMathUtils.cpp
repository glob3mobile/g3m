//
//  IMathUtils.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 29/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "IMathUtils.hpp"
#include "Vector2D.hpp"
#include "MutableVector2D.hpp"

IMathUtils* IMathUtils::_instance = NULL;

Vector2D IMathUtils::solveSecondDegreeEquation(double A, double B, double C) const {
  
  double x = B*B - 4*A*C;
  if (x < 0){
    return Vector2D::nan();
  }
  
  double squareRoot = this->sqrt(x);
  double A2 = 2*A;
  
  return Vector2D((-B + squareRoot) / A2,
                  (-B - squareRoot) / A2);
}

void IMathUtils::solveSecondDegreeEquation(double A, double B, double C,
                                           MutableVector2D& result) const {
  
  double x = B*B - 4*A*C;
  if (x < 0){
    result.setNan();
    return;
  }
  
  double squareRoot = this->sqrt(x);
  double A2 = 2*A;
  result.setValues((-B + squareRoot) / A2, (-B - squareRoot) / A2);
}

