package org.glob3.mobile.generated; 
public class FixedFocusSceneLighting extends SceneLighting
{

  public final void modifyGLState(GLState glState, G3MRenderContext rc)
  {
    final Vector3D lightDir = new Vector3D(1, 0,0);
  
    //STATIC LIGHT
    DirectionLightGLFeature f = (DirectionLightGLFeature) glState.getGLFeature(GLFeatureID.GLF_DIRECTION_LIGTH);
    if (f == null)
    {
      glState.clearGLFeatureGroup(GLFeatureGroupName.LIGHTING_GROUP);
      glState.addGLFeature(new DirectionLightGLFeature(lightDir, Color.yellow(), Color.white()), false);
    }
    /* //Add this to obtain a rotating "sun"
     else {
  
     ITimer *timer = IFactory::instance()->createTimer();
     double sec = timer->now().milliseconds();
     delete timer;
     double angle = ((int)sec % 36000) / 100.0;
  
     MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle),
     Vector3D::upZ(),
     Vector3D::zero);
  
     f->setLightDirection(lightDir.transformedBy(m, 1.0));
  
     }
     */
  
  }

}