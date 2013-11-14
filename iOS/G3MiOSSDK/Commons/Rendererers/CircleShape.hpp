//
//  CircleShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#ifndef __G3MiOSSDK__CircleShape__
#define __G3MiOSSDK__CircleShape__

#include "AbstractMeshShape.hpp"
#include "Color.hpp"

class CircleShape : public AbstractMeshShape {
private:
  float  _radius;
  int    _steps;
  Color* _color;

  const bool _useNormals;

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  CircleShape(Geodetic3D* position,
              AltitudeMode altitudeMode,
              float radius,
              const Color& color,
              int steps = 64,
              bool useNormals = true) :
  AbstractMeshShape(position, altitudeMode),
  _radius(radius),
  _color(new Color(color)),
  _steps(steps),
  _useNormals(useNormals)
  {

  }

  ~CircleShape() {
    delete _color;
    
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  void setRadius(float radius) {
    if (_radius != radius) {
      _radius = radius;
      cleanMesh();
    }
  }

  void setColor(Color* color) {
    if (_color != color) {
      delete _color;
      _color = color;
      cleanMesh();
    }
  }
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const {
    std::vector<double> intersections;
    return intersections;
  }


};

#endif
