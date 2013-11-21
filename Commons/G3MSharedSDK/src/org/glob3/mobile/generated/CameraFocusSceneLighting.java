package org.glob3.mobile.generated; 
public class CameraFocusSceneLighting extends SceneLighting
{

  private Color _ambientColor ;
  private Color _diffuseColor ;

  private double _cameraDirX;
  private double _cameraDirY;
  private double _cameraDirZ;


  public CameraFocusSceneLighting(Color ambient, Color diffuse)
  {
     _ambientColor = new Color(ambient);
     _diffuseColor = new Color(diffuse);
     _cameraDirX = 0;
     _cameraDirY = 0;
     _cameraDirZ = 0;
  
  }

  public void dispose()
  {
  }

  public final void modifyGLState(GLState glState, G3MRenderContext rc)
  {
  
    Vector3D camDir = rc.getCurrentCamera().getViewDirection();
    if (_cameraDirX == camDir._x && _cameraDirY == camDir._y && _cameraDirZ == camDir._z)
    {
      return;
    }
    _cameraDirX = camDir._x;
    _cameraDirY = camDir._y;
    _cameraDirZ = camDir._z;
  
    final Vector3D cameraVector = camDir.times(-1);
  
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
  
      System.out.printf("LD: %f, %f, %f\n", lightDir._x, lightDir._y, lightDir._z);
    }
  
  }

}