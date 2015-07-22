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

  private MutableVector3D _camDir = new MutableVector3D();
  private MutableVector3D _up = new MutableVector3D();


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
    final Camera camera = rc.getCurrentCamera();
  
    camera.getViewDirectionInto(_camDir);
    camera.getUpMutable(_up);
  
    if ((_cameraDirX == _camDir.x()) && (_cameraDirY == _camDir.y()) && (_cameraDirZ == _camDir.z()) && (_upX == _up.x()) && (_upY == _up.y()) && (_upZ == _up.z()))
    {
      return;
    }
  
    final MutableVector3D cameraVector = _camDir.times(-1);
  
    //Light slightly different of camera position
    final MutableVector3D rotationLightDirAxis = _up.cross(cameraVector);
    final MutableVector3D lightDir = cameraVector.rotateAroundAxis(rotationLightDirAxis, Angle.fromDegrees(45.0));
  
    DirectionLightGLFeature f = (DirectionLightGLFeature) glState.getGLFeature(GLFeatureID.GLF_DIRECTION_LIGTH);
    if (f == null)
    {
      glState.clearGLFeatureGroup(GLFeatureGroupName.LIGHTING_GROUP);
      glState.addGLFeature(new DirectionLightGLFeature(lightDir.asVector3D(), _diffuseColor, _ambientColor), false);
    }
    else
    {
      f.setLightDirection(lightDir.asVector3D());
    }
  
    //ADD MESH
    if (_meshRenderer != null)
    {
      Vector3D lastCamDir = new Vector3D(_cameraDirX, _cameraDirY, _cameraDirZ);
  
      if (lastCamDir.angleBetween(lightDir.asVector3D())._degrees > 10)
      {
  
        FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
        vertices.add(camera.getCartesianPosition());
        vertices.add(camera.getCartesianPosition().add(lightDir.times(1000).asVector3D()));
  
        DirectMesh mesh = new DirectMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), (float)3.0, (float)1.0, new Color(Color.red()));
        _meshRenderer.addMesh(mesh);
      }
    }
  
    //SAVING STATE
    _cameraDirX = _camDir.x();
    _cameraDirY = _camDir.y();
    _cameraDirZ = _camDir.z();
  
    _upX = _up.x();
    _upY = _up.y();
    _upZ = _up.z();
  }

  public final void setLightDirectionsMeshRenderer(MeshRenderer meshRenderer)
  {
    _meshRenderer = meshRenderer;
  }

}