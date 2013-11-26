//
//  LineShape.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 26/11/13.
//
//

#ifndef __G3MiOSSDK__LineShape__
#define __G3MiOSSDK__LineShape__


#include "AbstractMeshShape.hpp"
#include "Color.hpp"

class OrientedBox;


class LineShape : public AbstractMeshShape {
private:
  
  OrientedBox* _boundingVolume;
  
  float _width;
  
  Color* _color;
  Color* _originalColor;
  
  OrientedBox* computeOrientedBox(const Planet* planet,
                                  const Camera* camera);
  
  Vector3D* _cartesianPosition;
  
protected:
  Mesh* createMesh(const G3MRenderContext* rc);
  BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);
  
public:
  LineShape(Geodetic3D* position0,
            Geodetic3D* position1,
            AltitudeMode altitudeMode,
            float width,
            const Color& color) :
  AbstractMeshShape(position0, altitudeMode),
  _cartesianPosition(NULL),
  _boundingVolume(NULL),
  _width(width),
  _color(new Color(color)),
  _originalColor(new Color(color))
  {
  }
  
  ~LineShape();
  
  
  void setColor(Color* color) {
    delete _color;
    _color = color;
    cleanMesh();
  }
  
  void setWidth(float width) {
    if (_width != width) {
      _width = width;
      cleanMesh();
    }
  }
  
  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Camera* camera,
                                             const Vector3D& origin,
                                             const Vector3D& direction);
  
  bool isVisible(const G3MRenderContext *rc);
  
  void setSelectedDrawMode(bool mode) {
    if (mode) {
      setColor(Color::newFromRGBA(1, 0, 0, 1));
    } else {
      setColor(new Color(*_originalColor));
    }
  }
  
};

#endif /* defined(__G3MiOSSDK__LineShape__) */
