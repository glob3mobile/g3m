package org.glob3.mobile.generated;
//
//  AtmosphereRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

//
//  AtmosphereRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//





//class Mesh;
//class IFloatBuffer;
//class CameraPositionGLFeature;
//class Camera;

public class AtmosphereRenderer extends DefaultRenderer
{
  private final Color _blueSky ;
  private final Color _darkSpace ;
  private final double _minHeight;

  private GLState _glState;
  private Mesh _directMesh;
  private IFloatBuffer _vertices;
  private CameraPositionGLFeature _camPosGLF;
  private Color _previousBackgroundColor;
  private boolean _overPrecisionThreshold;

  private void updateGLState(Camera camera)
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
  
    //Updating ZNEAR plane
    camera.getVerticesOfZNearPlane(_vertices);
  
    //CamPos
    _camPosGLF.update(camera);
  }

  public AtmosphereRenderer()
  //_blueSky(Color::fromRGBA(( 32.0f / 2.0f + 128.0f) / 256.0f,
  //                         (173.0f / 2.0f + 128.0f) / 256.0f,
  //                         (249.0f / 2.0f + 128.0f) / 256.0f,
  //                         1.0f)),
  {
     _blueSky = new Color(Color.fromRGBA255(135, 206, 235, 255));
     _darkSpace = new Color(Color.BLACK);
     _minHeight = 8000.0;
     _previousBackgroundColor = null;
     _overPrecisionThreshold = true;
     _glState = null;
     _directMesh = null;
     _vertices = null;
     _camPosGLF = null;
  }

  public void dispose()
  {
    if (_previousBackgroundColor != null)
       _previousBackgroundColor.dispose();
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
      _camPosGLF = new CameraPositionGLFeature(rc.getCurrentCamera());
      _glState.addGLFeature(_camPosGLF, false);
    }
  
    if (_previousBackgroundColor != null)
       _previousBackgroundColor.dispose();
    _previousBackgroundColor = new Color(rc.getWidget().getBackgroundColor());
  
    //Computing background color
    final double camHeight = rc.getCurrentCamera().getGeodeticHeight();
    _overPrecisionThreshold = (camHeight < _minHeight * 1.2);
    if (_overPrecisionThreshold)
    {
      rc.getWidget().setBackgroundColor(_blueSky);
    }
    else
    {
      rc.getWidget().setBackgroundColor(_darkSpace);
    }
  }

  public final void stop(G3MRenderContext rc)
  {
    if (_previousBackgroundColor != null)
    {
      rc.getWidget().setBackgroundColor(_previousBackgroundColor);
      if (_previousBackgroundColor != null)
         _previousBackgroundColor.dispose();
      _previousBackgroundColor = null;
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
  
    //Rendering
    final double camHeigth = rc.getCurrentCamera().getGeodeticHeight();
    if (camHeigth > _minHeight)
    {
      updateGLState(rc.getCurrentCamera());
      _glState.setParent(glState);
  
      _directMesh.render(rc, _glState);
    }
  
    final boolean nowIsOverPrecisionThreshold = (camHeigth < _minHeight * 1.2);
  
    if (_overPrecisionThreshold != nowIsOverPrecisionThreshold)
    {
      //Changing background color
      _overPrecisionThreshold = nowIsOverPrecisionThreshold;
  
      if (_overPrecisionThreshold)
      {
        rc.getWidget().setBackgroundColor(_blueSky);
      }
      else
      {
        rc.getWidget().setBackgroundColor(_darkSpace);
      }
    }
  }

}
