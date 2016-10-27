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
  private GLState _glState;

  private Mesh _directMesh;

  private IFloatBuffer _vertices;
  private CameraPositionGLFeature _camPosGLF;

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

  private Color _blueSky ;
  private Color _darkSpace ;
  private boolean _overPresicionThreshold;
  private final double _minHeight;

  public AtmosphereRenderer()
  {
     _blueSky = new Color(Color.fromRGBA((32.0f / 2.0f + 128.0f) / 256.0f, (173.0f / 2.0f + 128.0f) / 256.0f, (249.0f / 2.0f + 128.0f) / 256.0f, 1.0f));
     _darkSpace = new Color(Color.fromRGBA(0.0f, 0.0f, 0.0f, 0.0f));
     _overPresicionThreshold = true;
     _minHeight = 8000.0;
  
  }

  public final void start(G3MRenderContext rc)
  {
    _glState = new GLState();
  
    FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    fbb.add(0.0, 0.0, 0.0);
    fbb.add(0.0, 0.0, 0.0);
    fbb.add(0.0, 0.0, 0.0);
    fbb.add(0.0, 0.0, 0.0);
  
    _vertices = fbb.create();
  
    _directMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, fbb.getCenter(), _vertices, 10.0f, 10.0f, null, null, 0.0f, false); //new Color(Color::green()),
  
    //CamPos
    _camPosGLF = new CameraPositionGLFeature(rc.getCurrentCamera());
    _glState.addGLFeature(_camPosGLF, false);
  
    //Computing background color
    final double camHeigth = rc.getCurrentCamera().getGeodeticPosition()._height;
    _overPresicionThreshold = camHeigth < _minHeight * 1.2;
    if (_overPresicionThreshold)
    {
      rc.getWidget().setBackgroundColor(_blueSky);
    }
    else
    {
      rc.getWidget().setBackgroundColor(_darkSpace);
    }
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void removeAllTrails(boolean deleteTrails);

  public void dispose()
  {
    if (_directMesh != null)
       _directMesh.dispose();
    _glState._release();
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
    final double camHeigth = rc.getCurrentCamera().getGeodeticPosition()._height;
    if (camHeigth > _minHeight)
    {
      updateGLState(rc.getCurrentCamera());
      _glState.setParent(glState);
  
      _directMesh.render(rc, _glState);
    }
  
    final boolean nowIsOverPresicionThreshold = (camHeigth < _minHeight * 1.2);
  
    if (_overPresicionThreshold != nowIsOverPresicionThreshold)
    {
      //Changing background color
      _overPresicionThreshold = nowIsOverPresicionThreshold;
  
      if (_overPresicionThreshold)
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