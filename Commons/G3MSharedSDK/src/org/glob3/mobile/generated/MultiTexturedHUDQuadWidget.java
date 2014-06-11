package org.glob3.mobile.generated; 
//
//  MultiTexturedHUDQuadWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

//
//  MultiTexturedHUDQuadWidget.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//



//class HUDPosition;
//class IImage;
//class Mesh;
//class SimpleTextureMapping;

public class MultiTexturedHUDQuadWidget extends HUDWidget
{
  private final URL _imageURL1;
  private final URL _imageURL2;

  private final HUDPosition _x;
  private final HUDPosition _y;
  private final float _width;
  private final float _height;

  private float _texCoordsTranslationU;
  private float _texCoordsTranslationV;
  private float _texCoordsScaleU;
  private float _texCoordsScaleV;
  private float _texCoordsRotationInRadians;
  private float _texCoordsRotationCenterU;
  private float _texCoordsRotationCenterV;

  private IImage _image1;
  private IImage _image2;
  private boolean _downloadingImage;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private Mesh _mesh;
  private MultiTextureMapping _mtMapping;

  private Mesh createMesh(G3MRenderContext rc)
  {
    if ((_image1 == null) || (_image2 == null))
    {
      return null;
    }
  
    final TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(_image1, GLFormat.rgba(), _imageURL1._path, false);
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
  
    final TextureIDReference texId2 = rc.getTexturesHandler().getTextureIDReference(_image2, GLFormat.rgba(), _imageURL2._path, false);
  
    if (texId2 == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
  
    final Camera camera = rc.getCurrentCamera();
    final int viewPortWidth = camera.getViewPortWidth();
    final int viewPortHeight = camera.getViewPortHeight();
  
    final float width = _width;
    final float height = _height;
    final float x = _x.getPosition(viewPortWidth, viewPortHeight, width, height);
    final float y = _y.getPosition(viewPortWidth, viewPortHeight, width, height);
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    vertices.add(x, height+y, 0);
    vertices.add(x, y, 0);
    vertices.add(width+x, height+y, 0);
    vertices.add(width+x, y, 0);
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0, 0);
    texCoords.add(0, 1);
    texCoords.add(1, 0);
    texCoords.add(1, 1);
  
    DirectMesh dm = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);
  
    if (vertices != null)
       vertices.dispose();
  
    _mtMapping = new MultiTextureMapping(texId, texCoords.create(), true, true, texId2, texCoords.create(), true, true, _texCoordsTranslationU, _texCoordsTranslationV, _texCoordsScaleU, _texCoordsScaleV, _texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
  
    return new TexturedMesh(dm, true, _mtMapping, true, true);
  }
  private Mesh getMesh(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      _mesh = createMesh(rc);
    }
    return _mesh;
  }

  private void cleanMesh()
  {
    _mtMapping = null;
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  }

  protected final void rawRender(G3MRenderContext rc, GLState glState)
  {
    Mesh mesh = getMesh(rc);
    if (mesh != null)
    {
      mesh.render(rc, glState);
    }
  }

  public MultiTexturedHUDQuadWidget(URL imageURL1, URL imageURL2, HUDPosition x, HUDPosition y, float width, float height)
  {
     _imageURL1 = imageURL1;
     _imageURL2 = imageURL2;
     _x = x;
     _y = y;
     _width = width;
     _height = height;
     _mesh = null;
     _mtMapping = null;
     _image1 = null;
     _image2 = null;
     _downloadingImage = false;
     _texCoordsTranslationU = 0F;
     _texCoordsTranslationV = 0F;
     _texCoordsScaleU = 1F;
     _texCoordsScaleV = 1F;
     _texCoordsRotationInRadians = 0F;
     _texCoordsRotationCenterU = 0F;
     _texCoordsRotationCenterV = 0F;
  }

  public final void setTexCoordsTranslation(float u, float v)
  {
    _texCoordsTranslationU = u;
    _texCoordsTranslationV = v;
  
    if (_mtMapping != null)
    {
      _mtMapping.setTranslation(_texCoordsTranslationU, _texCoordsTranslationV);
    }
  }

  public final void setTexCoordsScale(float u, float v)
  {
    _texCoordsScaleU = u;
    _texCoordsScaleV = v;
  
    if (_mtMapping != null)
    {
      _mtMapping.setScale(_texCoordsScaleU, _texCoordsScaleV);
    }
  }

  public final void setTexCoordsRotation(float angleInRadians, float centerU, float centerV)
  {
    _texCoordsRotationInRadians = angleInRadians;
    _texCoordsRotationCenterU = centerU;
    _texCoordsRotationCenterV = centerV;
  
    if (_mtMapping != null)
    {
      _mtMapping.setRotation(_texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
    }
  }

  public final void setTexCoordsRotation(Angle angle, float centerU, float centerV)
  {
    setTexCoordsRotation((float) angle._radians, centerU, centerV);
  }

  public void dispose()
  {
    if (_image1 != null)
       _image1.dispose();
    if (_image2 != null)
       _image2.dispose();
  
    if (_mesh != null)
       _mesh.dispose();
  
    if (_x != null)
       _x.dispose();
    if (_y != null)
       _y.dispose();
  }

  public final void initialize(G3MContext context)
  {
    if (!_downloadingImage && (_image1 == null) && (_image2 == null))
    {
      _downloadingImage = true;
      IDownloader downloader = context.getDownloader();
      downloader.requestImage(_imageURL1, 1000000, TimeInterval.fromDays(30), true, new MultiTexturedHUDQuadWidget_ImageDownloadListener(this), true); // readExpired -  priority
  
      downloader.requestImage(_imageURL2, 1000000, TimeInterval.fromDays(30), true, new MultiTexturedHUDQuadWidget_ImageDownloadListener(this), true); // readExpired -  priority
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    cleanMesh();
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

  /** private, do not call */
  public final void onImageDownload(IImage image, URL url)
  {
    if (url.isEquals(_imageURL1))
    {
      _image1 = image;
    }
    if (url.isEquals(_imageURL2))
    {
      _image2 = image;
    }
  
    if ((_image1 != null) && (_image2 != null))
    {
      _downloadingImage = false;
    }
  }

  /** private, do not call */
  public final void onImageDownloadError(URL url)
  {
    _errors.add("MultiTexturedHUDQuadWidget: Error downloading \"" + url._path + "\"");
  }

}