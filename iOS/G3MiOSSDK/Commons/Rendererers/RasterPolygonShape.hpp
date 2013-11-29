//
//  RasterPolygonShape.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 28/11/13.
//
//

#ifndef __G3MiOSSDK__RasterPolygonShape__
#define __G3MiOSSDK__RasterPolygonShape__

#include "Shape.hpp"
#include "GEORasterSymbol.hpp"


class OrientedBox;


class RasterPolygonShape : public Shape {
private:
  
  OrientedBox* _boundingVolume;
  
  float _borderWidth;
  
  Color* _borderColor;
  Color* _surfaceColor;
  
  OrientedBox* computeOrientedBox(const Planet* planet,
                                  const Camera* camera);
  
  Vector3D*   _cartesianStartPos;
  std::vector<Geodetic2D*>* _coordinates;
  
  void computeOrientationParams(const Planet* planet);
  
  double _minX, _minY, _minZ;
  double _maxX, _maxY, _maxZ;
  
  
protected:
  BoundingVolume* getBoundingVolume(const G3MRenderContext *rc);
  
public:
  
#ifdef C_CODE
  RasterPolygonShape(std::vector<Geodetic2D*>* coordinates,
                     float borderWidth,
                     const Color& borderColor,
                     const Color& surfaceColor) :
  Shape(new Geodetic3D(*coordinates->at(0), 0), RELATIVE_TO_GROUND),
  _coordinates(GEORasterSymbol::copyCoordinates(coordinates)),
  _cartesianStartPos(NULL),
  _boundingVolume(NULL),
  _borderWidth(borderWidth),
  _borderColor(new Color(borderColor)),
  _surfaceColor(new Color(surfaceColor))
  {
  }
#endif
  
#ifdef JAVA_CODE
  public RasterPolygonShape(java.util.ArrayList<Geodetic2D> coordinates, float borderWidth, Color borderColor, Color surfaceColor)
  {
    super(new Geodetic3D(coordinates.get(0), 0), AltitudeMode.RELATIVE_TO_GROUND);
    _coordinates = GEORasterSymbol.copyCoordinates(coordinates);
    _cartesianStartPos = null;
    _boundingVolume = null;
    _borderWidth = borderWidth;
    _borderColor = new Color(borderColor);
    _surfaceColor = new Color(surfaceColor);
  }
#endif
  
  ~RasterPolygonShape();
  
  
  void setColor(Color* color) {
   /* delete _color;
    _color = color;*/
  }
  
  void setWidth(float width) {
   /* if (_width != width) {
      _width = width;
    }*/
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
  
};


#endif /* defined(__G3MiOSSDK__RasterPolygonShape__) */
