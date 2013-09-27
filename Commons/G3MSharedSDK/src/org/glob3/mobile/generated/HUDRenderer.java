package org.glob3.mobile.generated; 
//
//  HUDRendererer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

//
//  HUDRendererer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//


//class Mesh;

public class HUDRenderer extends LeafRenderer
{

  private static class ShownImage
  {
    private final String _name;
    private IImage _image;
    private final Vector2D _size ;
    private final Vector2D _position ;

    private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE
    private Mesh _mesh;
    private Mesh createMesh(G3MRenderContext rc)
    {
      //TEXTURED
      IGLTextureId texId = null;
    
      _factory = rc.getFactory();
    
      texId = rc.getTexturesHandler().getGLTextureId(_image, GLFormat.rgba(), _name, false);
    
      if (texId == null)
      {
        rc.getLogger().logError("Can't upload texture to GPU");
        return null;
      }
    
      final int viewportWidth = rc.getCurrentCamera().getWidth();
      final int viewportHeight = rc.getCurrentCamera().getHeight();
    
      final Vector3D halfViewportAndPosition = new Vector3D(viewportWidth / 2 - _position._x, viewportHeight / 2 - _position._y, 0);
    
      final double w = _size._x;
      final double h = _size._y;
    
      FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      vertices.add(new Vector3D(0, h, 0).sub(halfViewportAndPosition));
      vertices.add(new Vector3D(0, 0, 0).sub(halfViewportAndPosition));
      vertices.add(new Vector3D(w, h, 0).sub(halfViewportAndPosition));
      vertices.add(new Vector3D(w, 0, 0).sub(halfViewportAndPosition));
    
      FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
      texCoords.add(0, 0);
      texCoords.add(0, 1);
      texCoords.add(1, 0);
      texCoords.add(1, 1);
    
      DirectMesh im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);
    
      TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, false);
    
      return new TexturedMesh(im, true, texMap, true, true);
    }

    public ShownImage(String name, IImage image, Vector2D size, Vector2D position)
    {
       _name = name;
       _image = image;
       _size = new Vector2D(size);
       _position = new Vector2D(position);
       _mesh = null;
       _factory = null;
    }

    public final Mesh getMesh(G3MRenderContext rc)
    {
      if (_mesh == null)
      {
        _mesh = createMesh(rc);
      }
      return _mesh;
    }

    public void dispose()
    {
      _factory.deleteImage(_image);
    //  delete _image;
      if (_mesh != null)
         _mesh.dispose();
    }

    public final void clearMesh()
    {
      if (_mesh != null)
         _mesh.dispose();
      _mesh = null;
    }

  }


  private GLState _glState;
  private java.util.ArrayList<ShownImage> _images = new java.util.ArrayList<ShownImage>();

  public HUDRenderer()
  {
     _glState = new GLState();
  }

  public final void addImage(String name, IImage image, Vector2D size, Vector2D position)
  {
    _images.add(new ShownImage(name, image, size, position));
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
    final int size = _images.size();
    for (int i = 0; i < size; i++)
    {
      ShownImage image = _images.get(i);
      Mesh mesh = image.getMesh(rc);
      if (mesh != null)
      {
        mesh.render(rc, _glState);
      }
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
  
    final int size = _images.size();
    for (int i = 0; i < size; i++)
    {
      _images.get(i).clearMesh();
    }
  }

  public void dispose()
  {
    _glState._release();
  
    final int size = _images.size();
    for (int i = 0; i < size; i++)
    {
      if (_images.get(i) != null)
         _images.get(i).dispose();
    }
  
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