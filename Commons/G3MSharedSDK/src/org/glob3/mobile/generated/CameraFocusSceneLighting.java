package org.glob3.mobile.generated; 
public class CameraFocusSceneLighting extends SceneLighting
{

  public final void modifyGLState(GLState glState, G3MRenderContext rc)
  {
  
    final Vector3D cameraVector = rc.getCurrentCamera().getCartesianPosition();
    final Vector3D lightDir = cameraVector;
  
    DirectionLightGLFeature f = (DirectionLightGLFeature) glState.getGLFeature(GLFeatureID.GLF_DIRECTION_LIGTH);
    if (f == null)
    {
      glState.clearGLFeatureGroup(GLFeatureGroupName.LIGHTING_GROUP);
      glState.addGLFeature(new DirectionLightGLFeature(lightDir, Color.yellow(), (float)0.4), false);
    }
    else
    {
      f.setLightDirection(lightDir);
    }
  
  }

}