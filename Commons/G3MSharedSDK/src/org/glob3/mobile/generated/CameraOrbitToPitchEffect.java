package org.glob3.mobile.generated; 
//
//  CameraOrbitToPitchEffect.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 30/3/15.
//
//


public class CameraOrbitToPitchEffect extends EffectWithDuration
{

  private MutableVector3D _pivotPoint = new MutableVector3D();
  private Camera _camera0;
  private double _finalPitch;


  public CameraOrbitToPitchEffect(Angle pitch, TimeInterval duration)
  {
     super(duration, true);
     _camera0 = null;
     _finalPitch = pitch._degrees;
  }

  public void dispose()
  {
    if (_camera0 != null)
       _camera0.dispose();
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);
      _pivotPoint = rc.getWidget().getFirstValidScenePositionForCentralColumn().asMutableVector3D();
    Camera nextCamera = rc.getNextCamera();
      _camera0 = new Camera(nextCamera);

      /*Geodetic3D geo = rc.getPlanet().toGeodetic3D(_pivotPoint.asVector3D());
     ILogger.instance().logInfo("pivot point =%f %f %f",
     geo._latitude._degrees, geo._longitude._degrees, geo._height);
     ILogger.instance().logInfo("initial pitch=%f",  _initialPitch);*/
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    double alpha = getAlpha(when);
    IMathUtils mu = IMathUtils.instance();

    // reset camera to camera0
    Camera camera = rc.getNextCamera();
    camera.copyFrom(_camera0);
    double initialPitch = rc.getNextCamera().getPitch()._degrees;

    // compute angle between normal and view direction
    Vector3D view = camera.getViewDirection();
    Vector3D normal = rc.getPlanet().geodeticSurfaceNormal(_pivotPoint);
    double dot = normal.normalized().dot(view.normalized().times(-1));
    //double initialAngle = mu->acos(dot) / 3.14159 * 180;

    // horizontal rotation over the original camera horizontal axix
    Vector3D u = camera.getHorizontalVector();
    double delta = alpha * (_finalPitch-initialPitch);
    camera.rotateWithAxisAndPoint(u, _pivotPoint.asVector3D(), Angle.fromDegrees(delta));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

}