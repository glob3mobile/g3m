//
//  StarDomeRenderer.hpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 19/3/15.
//
//

#ifndef __G3MiOSDemo__StarDomeRenderer__
#define __G3MiOSDemo__StarDomeRenderer__

#include "DefaultRenderer.hpp"
#include "GLState.hpp"

#include "MeshShape.hpp"
#include "Color.hpp"
#include <vector>

class StarsMeshShape: public MeshShape{
public:
  StarsMeshShape(Geodetic3D* position,
                 AltitudeMode altitudeMode,
                 Mesh* mesh):
  MeshShape(position,altitudeMode, mesh){
    
  }
  
  std::vector<double> intersectionsDistances(const Planet* planet,
                                             const Vector3D& origin,
                                             const Vector3D& direction) const{
    return std::vector<double>();
  }
};

class Star{
public:
  const double _trueNorthAzimuthInDegrees;
  const double _altitudeInDegrees;
  Color* _color;
  
  Star(double trueNorthAzimuthInDegrees,
       double altitudeInDegrees,
       const Color& color):
  _trueNorthAzimuthInDegrees(trueNorthAzimuthInDegrees),
  _altitudeInDegrees(altitudeInDegrees),
  _color(new Color(color)){
    
  }
  
  ~Star(){
    delete _color;
  }
  
  Star& operator=(const Star& s){
    return *(new Star(s._trueNorthAzimuthInDegrees, s._altitudeInDegrees, *s._color));
  }
  
  Star(const Star& s):
  _trueNorthAzimuthInDegrees(s._trueNorthAzimuthInDegrees),
  _altitudeInDegrees(s._altitudeInDegrees),
  _color(new Color(*s._color)){
    
  }
  
  void setColor(const Color& color){
    _color = new Color(color);
  }
  
  double distanceInDegrees(const Angle& trueNorthAzimuthInDegrees,
                           const Angle& altitudeInDegrees){
    return Angle::fromDegrees(_altitudeInDegrees).distanceTo(altitudeInDegrees)._degrees +
    Angle::fromDegrees(_trueNorthAzimuthInDegrees).distanceTo(trueNorthAzimuthInDegrees)._degrees;
  }
  
};

class StarDomeRenderer : public DefaultRenderer {
  
  StarsMeshShape* _starsShape;
  
  GLState* _glState;
  
  std::vector<Star> _stars;
  
  const Camera* _currentCamera;
  
public:
  
  StarDomeRenderer(std::vector<Star> stars):
  _starsShape(NULL), _glState(new GLState()), _stars(stars), _currentCamera(NULL)
  {
  }
  
  void initialize(const G3MContext* context);
  
  void render(const G3MRenderContext* rc,
              GLState* glState);
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
  }
  
  virtual ~StarDomeRenderer() {
  }
  
  void start(const G3MRenderContext* rc){}
  
  void stop(const G3MRenderContext* rc){}
  
  void onResume(const G3MContext* context) {
    
  }
  
  void onPause(const G3MContext* context) {
    
  }
  
  void onDestroy(const G3MContext* context) {
    
  }
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);
  
  void selectStar(const Angle& trueNorthAzimuthInDegrees,
                  const Angle& altitudeInDegrees);
  
};

#endif /* defined(__G3MiOSDemo__StarDomeRenderer__) */
