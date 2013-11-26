//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 22/10/13.
//
//

#include "OrientedBox.hpp"
#include "Quadric.hpp"
#include "Mesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"


OrientedBox::~OrientedBox() {
  delete _transformMatrix;
  if (_mesh) delete _mesh;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const std::vector<Vector3D> OrientedBox::getCorners() const
{
  int __TODO_GUS_implement_all_the_OrientedBox_empty_methods;
  
#ifdef C_CODE
  const Vector3D corners[8] = {
    Vector3D(_upperX, _upperY, _upperZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_lowerX, _upperY, _upperZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_upperX, _lowerY, _upperZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_lowerX, _lowerY, _upperZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_upperX, _upperY, _lowerZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_lowerX, _upperY, _lowerZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_upperX, _lowerY, _lowerZ).transformedBy(*_transformMatrix, 1),
    Vector3D(_lowerX, _lowerY, _lowerZ).transformedBy(*_transformMatrix, 1)
  };
  
  return std::vector<Vector3D>(corners, corners+8);
#endif

#ifdef JAVA_CODE
  if (_cornersD == null) {
    _cornersD = new java.util.ArrayList<Vector3D>(8);
    _cornersD.add(new Vector3D(_upperX, _upperY, _upperZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_lowerX, _upperY, _upperZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_upperX, _lowerY, _upperZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_lowerX, _lowerY, _upperZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_upperX, _upperY, _lowerZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_lowerX, _upperY, _lowerZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_upperX, _lowerY, _lowerZ).transformedBy(_transformMatrix, 1));
    _cornersD.add(new Vector3D(_lowerX, _lowerY, _lowerZ).transformedBy(_transformMatrix, 1));
  }
  return _cornersD;
#endif

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
  Quadric front   = Quadric::fromPlane(1, 0, 0,   -_upperX).transformBy(inverse, transpose);
  Quadric back    = Quadric::fromPlane(-1, 0, 0,  _lowerX).transformBy(inverse, transpose);
  Quadric left    = Quadric::fromPlane(0, -1, 0,  _lowerY).transformBy(inverse, transpose);
  Quadric right   = Quadric::fromPlane(0, 1, 0,   -_upperY).transformBy(inverse, transpose);
  Quadric top     = Quadric::fromPlane(0, 0, 1,   -_upperZ).transformBy(inverse, transpose);
  Quadric bottom  = Quadric::fromPlane(0, 0, -1,  _lowerZ).transformBy(inverse, transpose);
  
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


void OrientedBox::render(const G3MRenderContext* rc, const GLState& parentState) const{
  if (_mesh == NULL) {
    createMesh(Color::newFromRGBA(1.0f, 0.0f, 1.0f, 1.0f));
  }
  _mesh->render(rc, &parentState);
}


void OrientedBox::createMesh(Color* color) const {
  float v[] = {
    (float)_upperX, (float)_upperY, (float)_upperZ,
    (float)_lowerX, (float)_upperY, (float)_upperZ,
    (float)_upperX, (float)_lowerY, (float)_upperZ,
    (float)_lowerX, (float)_lowerY, (float)_upperZ,
    (float)_upperX, (float)_upperY, (float)_lowerZ,
    (float)_lowerX, (float)_upperY, (float)_lowerZ,
    (float)_upperX, (float)_lowerY, (float)_lowerZ,
    (float)_lowerX, (float)_lowerY, (float)_lowerZ
  };
  
  short i[] = {
    0, 1, 1, 3, 3, 2, 2, 0,
    0, 4, 1, 5, 3, 7, 2, 6,
    4, 5, 5, 7, 7, 6, 6, 4
  };
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  ShortBufferBuilder indices;
  
  const unsigned int numVertices = 8;
  for (unsigned int n=0; n<numVertices; n++) {
    vertices->add(v[n*3], v[n*3+1], v[n*3+2]);
  }
  
  const int numIndices = 24;
  for (unsigned int n=0; n<numIndices; n++) {
    indices.add(i[n]);
  }
  
  _mesh = new IndexedMesh(GLPrimitive::lines(),
                          true,
                          vertices->getCenter(),
                          vertices->create(),
                          indices.create(),
                          1,
                          1,
                          color);
  delete vertices;
}

