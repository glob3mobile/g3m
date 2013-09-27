package org.glob3.mobile.generated; 
//
//  HUDImageRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

//
//  HUDImageRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//


//class Mesh;
//class ImageFactory;

public class HUDImageRenderer extends LeafRenderer
{

  private static class ImageListener extends IImageListener
  {
    private HUDImageRenderer _hudImageRenderer;

    public ImageListener(HUDImageRenderer hudImageRenderer)
    {
       _hudImageRenderer = hudImageRenderer;
    }

    public final void imageCreated(IImage image)
    {
      _hudImageRenderer.setImage(image);
    }

  }

  private GLState _glState;
  private Mesh _mesh;
  private ImageFactory _imageFactory;
  private boolean _creatingMesh;

  private Mesh getMesh(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      if (!_creatingMesh)
      {
        if (_image == null)
        {
          _creatingMesh = true;
  
          final Camera camera = rc.getCurrentCamera();
  
          final int width = camera.getWidth();
          final int height = camera.getHeight();
  
          _imageFactory.create(rc, width, height, new HUDImageRenderer.ImageListener(this), true);
  
          return null;
        }
      }
  
      if (_image != null)
      {
        _mesh = createMesh(rc);
      }
    }
  
    return _mesh;
  }
  private Mesh createMesh(G3MRenderContext rc)
  {
    _creatingMesh = false;
  
  ///#ifdef C_CODE
  //  const IGLTextureId* texId = NULL;
  ///#endif
  ///#ifdef JAVA_CODE
  //  IGLTextureId texId = null;
  ///#endif
  
    int __TODO_create_unique_name;
    final IGLTextureId texId = rc.getTexturesHandler().getGLTextureId(_image, GLFormat.rgba(), "HUDImageRenderer", false);
  
    _image = null;
    _image = null;
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
  
    if (_mesh != null)
    {
      if (_mesh != null)
         _mesh.dispose();
      _mesh = null;
    }
  
    final Camera camera = rc.getCurrentCamera();
  
    final double halfWidth = camera.getWidth() / 2.0;
    final double halfHeight = camera.getHeight() / 2.0;
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    vertices.add(-halfWidth, halfHeight, 0);
    vertices.add(-halfWidth, -halfHeight, 0);
    vertices.add(halfWidth, halfHeight, 0);
    vertices.add(halfWidth, -halfHeight, 0);
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0, 0);
    texCoords.add(0, 1);
    texCoords.add(1, 0);
    texCoords.add(1, 1);
  
    DirectMesh mesh = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);
  
    TextureMapping textureMapping = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
    return new TexturedMesh(mesh, true, textureMapping, true, true);
  }

  private IImage _image;
  private void setImage(IImage image)
  {
    _image = image;
  }

  public HUDImageRenderer(ImageFactory imageFactory)
  {
     _imageFactory = imageFactory;
     _glState = new GLState();
     _creatingMesh = false;
     _image = null;
     _mesh = null;
  }

  public final void initialize(G3MContext context)
  {
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    Mesh mesh = getMesh(rc);
    if (mesh != null)
    {
      mesh.render(rc, _glState);
    }
  }


  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    final int halfWidth = width / 2;
    final int halfHeight = height / 2;
    MutableMatrix44D projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
  
    ProjectionGLFeature pr = (ProjectionGLFeature) _glState.getGLFeature(GLFeatureID.GLF_PROJECTION);
    if (pr == null)
    {
      _glState.addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()), false);
    }
    else
    {
      pr.setMatrix(projectionMatrix.asMatrix44D());
    }
  
    _creatingMesh = false;
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  
    _image = null;
    _image = null;
  }

  public void dispose()
  {
    _glState._release();
  
    if (_mesh != null)
       _mesh.dispose();
    _image = null;
  
    if (_imageFactory != null)
       _imageFactory.dispose();
  
    super.dispose();
  }

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
  {
  }

  public final void onResume(G3MContext context)
  {
  }

  public final void onPause(G3MContext context)
  {
  }

  public final void onDestroy(G3MContext context)
  {
  }

}