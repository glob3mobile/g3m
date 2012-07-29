//
//  Plane.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Plane.hpp"

Plane Plane::transformedBy(const MutableMatrix44D& M) const
{
  double a = _normal.x()*M.get(0) + _normal.y()*M.get(4) + _normal.z()*M.get(8) + _d*M.get(12);
  double b = _normal.x()*M.get(1) + _normal.y()*M.get(5) + _normal.z()*M.get(9) + _d*M.get(13);
  double c = _normal.x()*M.get(2) + _normal.y()*M.get(6) + _normal.z()*M.get(10) + _d*M.get(14);
  double d = _normal.x()*M.get(3) + _normal.y()*M.get(7) + _normal.z()*M.get(11) + _d*M.get(15);
  
  return Plane(a,b,c,d);
}
