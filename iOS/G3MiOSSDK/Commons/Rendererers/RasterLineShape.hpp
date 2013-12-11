//
//  RasterLineShape.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 27/11/13.
//
//

#ifndef __G3MiOSSDK__RasterLineShape__
#define __G3MiOSSDK__RasterLineShape__


#include "Shape.hpp"

class OrientedBox;


class RasterLineShape : public Shape {
private:
  
  OrientedBox* _boundingVolume;
  
  float _width;
  
  Color* _color;
  //Color* _originalColor;
  
  OrientedBox* computeOrientedBox(const Planet* planet,
                                  const Camera* camera);
  
  Geodetic2D* _geodeticStartPos;
  Geodetic2D* _geodeticEndPos;
  Vector3D*   _cartesianStartPos;
  
  void computeOrientationParams(const Planet* planet);
  
  
protected:
  BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);
  
public:
  RasterLineShape(Geodetic2D* startPosition,
                  Geodetic2D* endPosition,
                  float width,
                  const Color& color) :
  Shape(new Geodetic3D(*startPosition, 0), RELATIVE_TO_GROUND),
  _geodeticStartPos(startPosition),
  _geodeticEndPos(endPosition),
  _cartesianStartPos(NULL),
  _boundingVolume(NULL),
  _width(width),
  //_originalColor(new Color(color)),
  _color(new Color(color))
  {
  }
  
  ~RasterLineShape();
  
  
  void setColor(Color* color) {
    delete _color;
    _color = color;
  }
  
  void setWidth(float width) {
    if (_width != width) {
      _width = width;
    }
  }
  
  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Camera* camera,
                                             const Vector3D& origin,
                                             const Vector3D& direction);
  
  bool isVisible(const G3MRenderContext *rc);
  
  void setSelectedDrawMode(bool mode) {
  /*  if (mode) {
      setColor(Color::newFromRGBA(1, 0, 0, 1));
    } else {
      setColor(new Color(*_originalColor));
    }*/
  }
  
  bool isTransparent(const G3MRenderContext* rc) {
    return false;
  }
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  void rawRender(const G3MRenderContext* rc,
                 GLState* parentState,
                 bool renderNotReadyShapes)
  {}

  GEORasterSymbol* createRasterSymbolIfNeeded() const;
  
  std::vector<Geodetic2D*> getCopyRasterCoordinates() const {
    std::vector<Geodetic2D*> coordinates;
    coordinates.push_back(new Geodetic2D(*_geodeticStartPos));
    coordinates.push_back(new Geodetic2D(*_geodeticEndPos));
    return coordinates;
  }

};


#endif /* defined(__G3MiOSSDK__RasterLineShape__) */
