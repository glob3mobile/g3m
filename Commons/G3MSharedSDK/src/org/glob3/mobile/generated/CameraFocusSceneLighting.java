package org.glob3.mobile.generated; 
public class CameraFocusSceneLighting extends SceneLighting
{

  private Color _ambientColor ;
  private Color _diffuseColor ;


  public CameraFocusSceneLighting(Color ambient, Color diffuse)
  {
     _ambientColor = new Color(ambient);
     _diffuseColor = new Color(diffuse);
  
  }

  public void dispose()
  {
  }

  public final void modifyGLState(GLState glState, G3MRenderContext rc)
  {
  
    final Vector3D cameraVector = rc.getCurrentCamera().getViewDirection().times(-1);
    //const Vector3D lightDir = cameraVector; //Light from camera
  
    //Light slightly different of camera position
    final Vector3D rotationLightDirAxis = rc.getCurrentCamera().getUp().cross(cameraVector);
    final Vector3D lightDir = cameraVector.rotateAroundAxis(rotationLightDirAxis, Angle.fromDegrees(10.0));
  
    DirectionLightGLFeature f = (DirectionLightGLFeature) glState.getGLFeature(GLFeatureID.GLF_DIRECTION_LIGTH);
    if (f == null)
    {
      glState.clearGLFeatureGroup(GLFeatureGroupName.LIGHTING_GROUP);
      glState.addGLFeature(new DirectionLightGLFeature(lightDir, _diffuseColor, _ambientColor), false);
    }
    else
    {
      f.setLightDirection(lightDir);
    }
  
  }

}