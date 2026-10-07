package org.glob3.mobile.generated;
//
//  AtmosphereRenderer.cpp
//  G3M
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

//
//  AtmosphereRenderer.hpp
//  G3M
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//




//class DirectMesh;
//class IFloatBuffer;
//class CameraPositionGLFeature;
//class Camera;
//class Color;

public class AtmosphereRenderer extends DefaultRenderer
{
  private final boolean _groundHazePass;
  private GLState _glState;
  private DirectMesh _directMesh;
  private IFloatBuffer _vertices;
  private CameraPositionGLFeature _camPosGLF;

  private void updateGLState(Camera camera, Color spaceColor)
  {
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else
    {
      f.setMatrix(camera.getModelViewMatrix44D());
    }
  
    // The quad is placed relative to the camera, so the shader gets small numbers
    // and the ray directions keep their float precision at low altitude
    updateVerticesOfZNearPlaneRelativeToCamera(camera);
    _directMesh.setUserTransformMatrix(new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(camera.getCartesianPosition())));
  
    //CamPos
    _camPosGLF.update(camera, spaceColor);
  }

  private void updateVerticesOfZNearPlaneRelativeToCamera(Camera camera)
  {
    final FrustumData frustumData = camera.getFrustumData();
  
    final Vector3D viewDirection = camera.getViewDirection().normalized();
    final Vector3D center = viewDirection.times(frustumData._zNear);
    final Vector3D up = camera.getUp().normalized().times(frustumData._top * 2.0);
    final Vector3D right = viewDirection.cross(up).normalized().times(frustumData._right * 2.0);
  
    _vertices.putVector3D(0, center.sub(up).sub(right));
    _vertices.putVector3D(1, center.add(up).sub(right));
    _vertices.putVector3D(2, center.sub(up).add(right));
    _vertices.putVector3D(3, center.add(right).add(up));
  }

  private AtmosphereRenderer(boolean groundHazePass)
  {
     _groundHazePass = groundHazePass;
     _glState = null;
     _directMesh = null;
     _vertices = null;
     _camPosGLF = null;
  }

  // the sky and the space, drawn before the PlanetRenderer
  public static AtmosphereRenderer createSky()
  {
    return new AtmosphereRenderer(false);
  }

  // the air in front of the ground, drawn after the PlanetRenderer
  public static AtmosphereRenderer createGroundHaze()
  {
    return new AtmosphereRenderer(true);
  }

  public void dispose()
  {
    if (_directMesh != null)
       _directMesh.dispose();
    _glState._release();
  }

  public final void start(G3MRenderContext rc)
  {
    if (_glState == null)
    {
      _glState = new GLState();
  
      FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
      fbb.add(0.0, 0.0, 0.0);
      fbb.add(0.0, 0.0, 0.0);
      fbb.add(0.0, 0.0, 0.0);
      fbb.add(0.0, 0.0, 0.0);
  
      _vertices = fbb.create();
  
      _directMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, fbb.getCenter(), _vertices, 10.0f, 10.0f, null, null, false);
  
      if (fbb != null)
         fbb.dispose();
      fbb = null;
  
      //CamPos
      // the background colour is the colour of space, seen through the air
      _camPosGLF = new CameraPositionGLFeature(rc.getCurrentCamera(), _groundHazePass, rc.getWidget().getBackgroundColor());
      _glState.addGLFeature(_camPosGLF, false);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    final Sector rSector = rc.getWidget().getPlanetRenderer().getRenderedSector();
    if (rc.getPlanet().getType().compareTo("Flat") == 0 || (rSector != null && !rSector.fullContains(Sector.fullSphere())))
    {
      return;
    }
  
    updateGLState(rc.getCurrentCamera(), rc.getWidget().getBackgroundColor());
    _glState.setParent(glState);
  
    _directMesh.render(rc, _glState);
  }

}