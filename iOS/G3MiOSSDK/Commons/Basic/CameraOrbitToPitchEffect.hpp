//
//  CameraOrbitToPitchEffect.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 30/3/15.
//
//

#ifndef G3MiOSSDK_CameraOrbitToPitchEffect_hpp
#define G3MiOSSDK_CameraOrbitToPitchEffect_hpp

class CameraOrbitToPitchEffect : public EffectWithDuration {

private:
  MutableVector3D _pivotPoint;
  Camera* _camera0;
  double _finalPitch;
  
public:

  CameraOrbitToPitchEffect(Angle pitch,
                           TimeInterval duration):
  EffectWithDuration(duration, true),
  _camera0(NULL),
  _finalPitch(pitch._degrees)
  {
  }
  
  ~CameraOrbitToPitchEffect() {
    delete _camera0;
  }

  void start(const G3MRenderContext* rc,
             const TimeInterval& when) {
    EffectWithDuration::start(rc, when);
		_pivotPoint = rc->getWidget()->getFirstValidScenePositionForCentralColumn().asMutableVector3D();
		_camera0 = new Camera(*(rc->getNextCamera()));
		
		/*Geodetic3D geo = rc.getPlanet().toGeodetic3D(_pivotPoint.asVector3D());
     ILogger.instance().logInfo("pivot point =%f %f %f",
     geo._latitude._degrees, geo._longitude._degrees, geo._height);
     ILogger.instance().logInfo("initial pitch=%f",  _initialPitch);*/
  }

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when) {
    double alpha = getAlpha(when);
    IMathUtils* mu = IMathUtils::instance();
    
    // reset camera to camera0
    Camera* camera = rc->getNextCamera();
    camera->copyFrom(*_camera0);
    double initialPitch = rc->getNextCamera()->getPitch()._degrees;
    
    // compute angle between normal and view direction
    Vector3D view = camera->getViewDirection();
    Vector3D normal = rc->getPlanet()->geodeticSurfaceNormal(_pivotPoint);
    double dot = normal.normalized().dot(view.normalized().times(-1));
    //double initialAngle = mu->acos(dot) / 3.14159 * 180;
    
    // horizontal rotation over the original camera horizontal axix
    Vector3D u = camera->getHorizontalVector();
    double delta = alpha * (_finalPitch-initialPitch);
    camera->rotateWithAxisAndPoint(u, _pivotPoint.asVector3D(), Angle::fromDegrees(delta));
  }

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
  void cancel(const TimeInterval& when) {
    // do nothing, just leave the effect in the intermediate state
  }
  
};


#endif
