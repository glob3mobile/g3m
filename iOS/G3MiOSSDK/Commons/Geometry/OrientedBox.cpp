//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 22/10/13.
//
//

#include "OrientedBox.hpp"


int __TODO_GUS_implement_OrientedBox_empty_methods;



const std::vector<Vector3D> OrientedBox::getCorners() const
{
  int __TODO_convert_to_java_this_code_as_in_Box_class;
  
  const Vector3D corners[8] = {
    Vector3D(_halfExtent._x,  _halfExtent._y,   _halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(-_halfExtent._x, _halfExtent._y,   _halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(_halfExtent._x,  -_halfExtent._y,  _halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(-_halfExtent._x, -_halfExtent._y,  _halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(_halfExtent._x,  _halfExtent._y,   -_halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(-_halfExtent._x, _halfExtent._y,   -_halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(_halfExtent._x,  -_halfExtent._y,  -_halfExtent._z).transformedBy(_transformMatrix, 1),
    Vector3D(-_halfExtent._x, -_halfExtent._y,  -_halfExtent._z).transformedBy(_transformMatrix, 1)
  };
  
  return std::vector<Vector3D>(corners, corners+8);
}

