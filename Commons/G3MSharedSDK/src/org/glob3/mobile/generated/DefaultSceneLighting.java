package org.glob3.mobile.generated; 
//class ITimer;

public class DefaultSceneLighting extends SceneLighting
{

  public final void modifyGLState(GLState glState)
  {
  
    Vector3D lightDir = new Vector3D(1, 0,0);
  
  
    if (false) //ROTATING LIGHT
    {
      ITimer timer = IFactory.instance().createTimer();
      double sec = timer.now().milliseconds();
      if (timer != null)
         timer.dispose();
      double angle = ((int)sec % 36000) / 100.0;
  
      MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(angle), Vector3D.upZ(), Vector3D.zero);
  
      glState.clearGLFeatureGroup(GLFeatureGroupName.LIGHTING_GROUP);
      glState.addGLFeature(new DirectionLightGLFeature(lightDir.transformedBy(m, 1.0), Color.yellow(), (float)0.2), false);
    } //STATIC LIGHT
    else
    {
  
      if (glState.getGLFeature(GLFeatureID.GLF_DIRECTION_LIGTH) == null)
      {
        glState.clearGLFeatureGroup(GLFeatureGroupName.LIGHTING_GROUP);
        glState.addGLFeature(new DirectionLightGLFeature(lightDir, Color.yellow(), (float)0.2), false);
      }
    }
  }

}