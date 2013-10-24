//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 22/10/13.
//
//

#include "OrientedBox.hpp"
#include "Quadric.hpp"





const std::vector<Vector3D> OrientedBox::getCorners() const
{
  int __TODO_GUS_implement_OrientedBox_empty_methods;

  int __TODO_convert_to_java_this_code_as_in_Box_class;
  
  const Vector3D corners[8] = {
    Vector3D(_halfExtentX,  _halfExtentY,   _halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(-_halfExtentX, _halfExtentY,   _halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_halfExtentX,  -_halfExtentY,  _halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(-_halfExtentX, -_halfExtentY,  _halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_halfExtentX,  _halfExtentY,   -_halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(-_halfExtentX, _halfExtentY,   -_halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_halfExtentX,  -_halfExtentY,  -_halfExtentZ).transformedBy(*_transformMatrix, 1),
    Vector3D(-_halfExtentX, -_halfExtentY,  -_halfExtentZ).transformedBy(*_transformMatrix, 1)
  };
  
  return std::vector<Vector3D>(corners, corners+8);
}


std::vector<double> OrientedBox::intersectionsDistances(const Vector3D& origin,
                                                        const Vector3D& direction) const {
  std::vector<double> distances;
  
  double tmin=-1e10, tmax=1e10;
  double t1, t2;
  
  // create quadrics for the 6 sides
  // QUESTION: CREATE 6 MATRICES EVERYTIME OR SAVE THEM INSIDE THE CLASS??
  MutableMatrix44D inverse = _transformMatrix->inversed();
  MutableMatrix44D transpose = inverse.transposed();
  Quadric front = Quadric::fromPlane(1, 0, 0, -_halfExtentX).transformBy(inverse, transpose);
  Quadric back = Quadric::fromPlane(-1, 0, 0, -_halfExtentX).transformBy(inverse, transpose);
  Quadric left = Quadric::fromPlane(0, -1, 0, -_halfExtentY).transformBy(inverse, transpose);
  Quadric right = Quadric::fromPlane(0, 1, 0, -_halfExtentY).transformBy(inverse, transpose);
  Quadric top = Quadric::fromPlane(0, 0, 1, -_halfExtentZ).transformBy(inverse, transpose);
  Quadric bottom = Quadric::fromPlane(0, 0, -1, -_halfExtentZ).transformBy(inverse, transpose);
  
  // ALL THIS CODE COULD BE OPTIMIZED
  // FOR EXAMPLE, WHEN CUADRICS ARE PLANES, MATH EXPRESSIONS ARE SIMPLER
  
  // intersecction with X planes
  std::vector<double> frontDistance = front.intersectionsDistances(origin, direction);
  std::vector<double> backDistance = back.intersectionsDistances(origin, direction);
  if (frontDistance.size()==1 && backDistance.size()==1) {
    if (frontDistance[0] < backDistance[0]) {
      t1 = frontDistance[0];
      t2 = backDistance[0];
    } else {
      t2 = frontDistance[0];
      t1 = backDistance[0];
    }
    if (t1 > tmin)
      tmin = t1;
    if (t2 < tmax)
      tmax = t2;
  }
  
  // intersections with Y planes
  std::vector<double> leftDistance = left.intersectionsDistances(origin, direction);
  std::vector<double> rightDistance = right.intersectionsDistances(origin, direction);
  if (leftDistance.size()==1 && rightDistance.size()==1) {
    if (leftDistance[0] < rightDistance[0]) {
      t1 = leftDistance[0];
      t2 = rightDistance[0];
    } else {
      t2 = leftDistance[0];
      t1 = rightDistance[0];
    }
    if (t1 > tmin)
      tmin = t1;
    if (t2 < tmax)
      tmax = t2;
  }
  
  // intersections with Z planes
  std::vector<double> topDistance = top.intersectionsDistances(origin, direction);
  std::vector<double> bottomDistance = bottom.intersectionsDistances(origin, direction);
  if (topDistance.size()==1 && bottomDistance.size()==1) {
    if (topDistance[0] < bottomDistance[0]) {
      t1 = topDistance[0];
      t2 = bottomDistance[0];
    } else {
      t2 = topDistance[0];
      t1 = bottomDistance[0];
    }
    if (t1 > tmin)
      tmin = t1;
    if (t2 < tmax)
      tmax = t2;
  }
  
  if (tmin < tmax) {
    distances.push_back(tmin);
    //distances.push_back(tmax);
  }
  
  return distances;
}
