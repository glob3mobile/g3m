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
#include <string>

#include "IFactory.hpp"
#include "ITimer.hpp"
#include "Geodetic3D.hpp"

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
  
  const double _ascencion;
  const double _declination;
  
public:
  Color* _color;
  
  Star(double ascencion,
       double declination,
       const Color& color):
  _ascencion(ascencion),
  _declination(declination),
  _color(new Color(color)){
    
  }
  
  double getTrueNorthAzimuthInDegrees(double siderealTime) const{
#warning TODO
    
    const IMathUtils* mu = IMathUtils::instance();
    
    double delta = TO_RADIANS( _declination );
    double alpha = TO_RADIANS( _ascencion );
    
    double azimuth = mu->atan2(-(mu->cos(delta) * mu->cos(90 - phi)*Cos(Degree*(-alpha + tetha))) + Sin(Degree*delta)*Sin(Degree*(90 - phi)),
                               <#double v#>)
    
    
    //ArcTan(-(Cos(Degree*delta)*Cos(Degree*(90 - phi))*Cos(Degree*(-alpha + tetha))) + Sin(Degree*delta)*Sin(Degree*(90 - phi)),-(Cos(Degree*delta)*Sin(Degree*(-alpha + tetha))))
    
    return _declination;
  }
  
  double getAltitude(double siderealTime) const{
#warning TODO
    return _ascencion;
  }
  
  ~Star(){
    delete _color;
  }
  
  Star& operator=(const Star& s){
    return *(new Star(s._ascencion, s._declination, *s._color));
  }
  
  Star(const Star& s):
  _ascencion(s._ascencion),
  _declination(s._declination),
  _color(new Color(*s._color)){
    
  }
  
  void setColor(const Color& color){
    _color = new Color(color);
  }
  
  double distanceInDegrees(const Angle& trueNorthAzimuthInDegrees,
                           const Angle& altitudeInDegrees,
                           double siderealTime){
    return Angle::fromDegrees(getAltitude(siderealTime)).distanceTo(altitudeInDegrees)._degrees +
    Angle::fromDegrees(getTrueNorthAzimuthInDegrees(siderealTime)).distanceTo(trueNorthAzimuthInDegrees)._degrees;
  }
  
};

class StarDomeRenderer : public DefaultRenderer {
  
  StarsMeshShape* _starsShape;
  
  GLState* _glState;
  
  std::vector<Star> _stars;
  
  const Camera* _currentCamera;
  
  const std::string _name;
  
  Geodetic3D* _position;
  double _clockTimeInDegrees;
  double _siderealTimeOffset;
  
public:
  
  StarDomeRenderer(std::string& name, std::vector<Star> stars,
                   const Geodetic3D& position,
                   double clockTimeInDegrees,
                   double siderealTimeOffset):
  _starsShape(NULL),
  _glState(new GLState()),
  _stars(stars),
  _currentCamera(NULL),
  _name(name),
  _position(new Geodetic3D(position)),
  _clockTimeInDegrees(clockTimeInDegrees),
  _siderealTimeOffset(siderealTimeOffset)
  {
  }
  
  static double getSiderealTime(double placeLongitudeInDegrees, double clockTimeInDegrees, double thetaZero){
    
    double I = 366.2422 / 365.2422;
    double TU = clockTimeInDegrees - IMathUtils::instance()->round(placeLongitudeInDegrees);
    
    return thetaZero + TU * I + placeLongitudeInDegrees;
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
