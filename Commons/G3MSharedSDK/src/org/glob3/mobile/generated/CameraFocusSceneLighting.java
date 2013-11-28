package org.glob3.mobile.generated; 
public class CameraFocusSceneLighting extends SceneLighting
{

  private Color _ambientColor ;
  private Color _diffuseColor ;

  private MeshRenderer _meshRenderer;

  private double _cameraDirX;
  private double _cameraDirY;
  private double _cameraDirZ;
  private double _upX;
  private double _upY;
  private double _upZ;


  public CameraFocusSceneLighting(Color ambient, Color diffuse)
  {
     _ambientColor = new Color(ambient);
     _diffuseColor = new Color(diffuse);
     _cameraDirX = 0;
     _cameraDirY = 0;
     _cameraDirZ = 0;
     _meshRenderer = null;
  
  }

  public void dispose()
  {
  }

  public final void modifyGLState(GLState glState, G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    final Vector3D camDir = cam.getViewDirection();
    final Vector3D up = cam.getUp();
    if (_cameraDirX == camDir._x && _cameraDirY == camDir._y && _cameraDirZ == camDir._z && _upX == up._x && _upY == up._y && _upZ == up._z)
    {
      return;
    }
  
    final Vector3D cameraVector = camDir.times(-1);
  
    //Light slightly different of camera position
    final Vector3D rotationLightDirAxis = up.cross(cameraVector);
    final Vector3D lightDir = cameraVector.rotateAroundAxis(rotationLightDirAxis, Angle.fromDegrees(45.0));
  
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
  
    //ADD MESH
    if (_meshRenderer != null)
    {
      Vector3D lastCamDir = new Vector3D(_cameraDirX, _cameraDirY, _cameraDirZ);
  
      if (lastCamDir.angleBetween(lightDir)._degrees > 10)
      {
  
        FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
        vertices.add(cam.getCartesianPosition());
        vertices.add(cam.getCartesianPosition().add(lightDir.times(1000)));
  
        DirectMesh mesh = new DirectMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), (float)3.0, (float)1.0, new Color(Color.red()));
        _meshRenderer.addMesh(mesh);
      }
    }
  
    //SAVING STATE
    _cameraDirX = camDir._x;
    _cameraDirY = camDir._y;
    _cameraDirZ = camDir._z;
  
    _upX = up._x;
    _upY = up._y;
    _upZ = up._z;
  
  }

  public final void setLightDirectionsMeshRenderer(MeshRenderer meshRenderer)
  {
    _meshRenderer = meshRenderer;
  }

}