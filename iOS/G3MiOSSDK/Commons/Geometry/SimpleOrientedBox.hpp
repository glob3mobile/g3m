//
//  SimpleOrientedBox.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 16/07/12.
//

#ifndef G3MiOSSDK_SimpleOrientedBox
#define G3MiOSSDK_SimpleOrientedBox

#include "BoundingVolume.hpp"
#include "Vector3D.hpp"
#include "Vector3F.hpp"
#include "Frustum.hpp"
#include "Box.hpp"

#include "GLState.hpp"

class Vector2D;
class Mesh;
class Color;

class SimpleOrientedBox: public BoundingVolume {
private:

  mutable Mesh* _mesh;

public:
  const Vector3D _lower;
  const Vector3D _upper;
  const MutableMatrix44D _matrix;

#ifdef JAVA_CODE
  private java.util.ArrayList<Vector3D> _cornersD = null; // cache for getCorners() method
  private java.util.ArrayList<Vector3F> _cornersF = null; // cache for getCornersF() method
#endif

  SimpleOrientedBox(const Vector3D& lower,
                    const Vector3D& upper,
                    const MutableMatrix44D& matrix):
  _lower(lower),
  _upper(upper),
  _matrix(matrix),
  _mesh(NULL)
  {
  }
  
  SimpleOrientedBox(const Vector3D& lower,
                    const Vector3D& xAxis,
                    const Vector3D& yAxis,
                    const Vector3D& zAxis):
  _lower(lower),
  _upper(lower.add(Vector3D(1,0,0).times(xAxis.length())).add(Vector3D(0,1,0).times((yAxis.length()))).add(Vector3D(0,0,1).times(zAxis.length()))),
  _matrix(MutableMatrix44D::createGeneralRotationMatrix(xAxis, yAxis, zAxis, lower)),
  _mesh(NULL)
  {
  }

  ~SimpleOrientedBox();
  
  bool touchesFrustum(const Frustum* frustum) const {
    return frustum->touchesWithSimpleOrientedBox(this);
  };

  Vector3D getLower() const { return _lower; }
  Vector3D getUpper() const { return _upper; }

  inline const std::vector<Vector3D> getCorners() const;
  inline const std::vector<Vector3F> getCornersF() const;


  double projectedArea(const G3MRenderContext* rc) const;
  Vector2F projectedExtent(const G3MRenderContext* rc) const;


  Vector3D intersectionWithRay(const Vector3D& origin,
                               const Vector3D& direction) const;

  void render(const G3MRenderContext* rc,
              const GLState* parentState,
              const Color& color) const;

  bool touches(const BoundingVolume* that) const {
    /*if (that == NULL) {
      return false;
    }
    return that->touchesSimpleOrientedBox(this);*/
  }

  bool touchesBox(const Box* that) const;
  bool touchesSphere(const Sphere* that) const;

  BoundingVolume* mergedWith(const BoundingVolume* that) const {
    if (that == NULL) {
      return NULL;
    }
    //return that->mergedWithSimpleOrientedBox(this);
  }

  SimpleOrientedBox* mergedWithSimpleOrientedBox(const SimpleOrientedBox* that) const;
  BoundingVolume* mergedWithSphere(const Sphere* that) const;
  
  // TODO for Agustin
  SimpleOrientedBox* mergedWithOrientedBox(const OrientedBox* that) const;

  Box* mergedWithBox(const Box* that) const;
  
  Vector3D closestPoint(const Vector3D& point) const;

  bool contains(const Vector3D& p) const;

  bool fullContains(const BoundingVolume* that) const {
  //  return that->fullContainedInSimpleOrientedBox(this);
  }
  
  bool fullContainedInBox(const Box* that) const;
  bool fullContainedInSphere(const Sphere* that) const;
  bool fullContainedInOrientedBox(const OrientedBox* that) const;
  bool fullContainedInSimpleOrientedBox(const SimpleOrientedBox* that) const;

  Sphere* createSphere() const;

#ifdef JAVA_CODE
  private Vector3F[] _cornersArray = null;
  public Vector3F[] getCornersArray() {
    if (_cornersArray == null) {
      _cornersArray = new Vector3F[8];

      _cornersArray[0] = new Vector3F((float) _lower._x, (float) _lower._y, (float) _lower._z);
      _cornersArray[1] = new Vector3F((float) _lower._x, (float) _lower._y, (float) _upper._z);
      _cornersArray[2] = new Vector3F((float) _lower._x, (float) _upper._y, (float) _lower._z);
      _cornersArray[3] = new Vector3F((float) _lower._x, (float) _upper._y, (float) _upper._z);
      _cornersArray[4] = new Vector3F((float) _upper._x, (float) _lower._y, (float) _lower._z);
      _cornersArray[5] = new Vector3F((float) _upper._x, (float) _lower._y, (float) _upper._z);
      _cornersArray[6] = new Vector3F((float) _upper._x, (float) _upper._y, (float) _lower._z);
      _cornersArray[7] = new Vector3F((float) _upper._x, (float) _upper._y, (float) _upper._z);
    }
    return _cornersArray;
  }
#endif

  const std::string description() const;
  
  Mesh* createMesh(const Color& color,
                   float lineWidth=2) const;
  Mesh* getMesh() const {
    if (_mesh == NULL)
      _mesh = createMesh(Color::fromRGBA(1, 1, 0, 1), 4);
    return _mesh;
  }

};

#endif
