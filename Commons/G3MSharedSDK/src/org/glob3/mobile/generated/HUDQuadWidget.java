package org.glob3.mobile.generated; 
//
//  HUDQuadWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

//
//  HUDQuadWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//



//class IImage;
//class Mesh;

public class HUDQuadWidget extends HUDWidget
{
  private final URL _imageURL;
  private final float _x;
  private final float _y;
  private final float _width;
  private final float _height;

  private IImage _image;
  private boolean _downloadingImage;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private Mesh _mesh;
  private Mesh createMesh(G3MRenderContext rc)
  {
    if (_image == null)
    {
      return null;
    }
    TextureIDReference texId;
  
    texId = rc.getTexturesHandler().getTextureIDReference(_image, GLFormat.rgba(), _imageURL.getPath(), false);
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
  
    final Camera camera = rc.getCurrentCamera();
    final int viewportWidth = camera.getWidth();
    final int viewportHeight = camera.getHeight();
  
    final Vector3D halfViewportAndPosition = new Vector3D(viewportWidth / 2 - _x, viewportHeight / 2 - _y, 0);
  
    final double w = _width;
    final double h = _height;
  
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
  
    DirectMesh dm = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);
  
    if (vertices != null)
       vertices.dispose();
  
    TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
    return new TexturedMesh(dm, true, texMap, true, true);
  }
  private Mesh getMesh(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      _mesh = createMesh(rc);
    }
    return _mesh;
  }

  public HUDQuadWidget(URL imageURL, float x, float y, float width, float height)
  {
     _imageURL = imageURL;
     _x = x;
     _y = y;
     _width = width;
     _height = height;
     _mesh = null;
     _image = null;
     _downloadingImage = false;
  }

  public void dispose()
  {
    if (_image != null)
       _image.dispose();
    if (_mesh != null)
       _mesh.dispose();
  }

  public final void initialize(G3MContext context)
  {
    if (!_downloadingImage && (_image == null))
    {
      _downloadingImage = true;
      IDownloader downloader = context.getDownloader();
      downloader.requestImage(_imageURL, 1000000, TimeInterval.fromDays(30), true, new HUDQuadWidget_ImageDownloadListener(this), true); // readExpired -  priority
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (!_errors.isEmpty())
    {
      return RenderState.error(_errors);
    }
    else if (_downloadingImage)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    Mesh mesh = getMesh(rc);
    if (mesh != null)
    {
      mesh.render(rc, glState);
    }
  }


  /** private, do not call */
  public final void onImageDownload(IImage image)
  {
    _downloadingImage = false;
    _image = image;
  }

  /** private, do not call */
  public final void onImageDownloadError(URL url)
  {
    _errors.add("HUDQuadWidget: Error downloading \"" + url.getPath() + "\"");
  }

}