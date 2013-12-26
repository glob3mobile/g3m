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



//class HUDPosition;
//class HUDSize;
//class IImage;
//class Mesh;
//class SimpleTextureMapping;

public class HUDQuadWidget extends HUDWidget
{
  private final URL _imageURL;
  private final HUDPosition _xPosition;
  private final HUDPosition _yPosition;
  private final HUDSize _widthSize;
  private final HUDSize _heightSize;

  private float _texCoordsTranslationU;
  private float _texCoordsTranslationV;
  private float _texCoordsScaleU;
  private float _texCoordsScaleV;
  private float _texCoordsRotationInRadians;
  private float _texCoordsRotationCenterU;
  private float _texCoordsRotationCenterV;

  private IImage _image;
  private int _imageWidth;
  private int _imageHeight;

  private boolean _downloadingImage;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private Mesh _mesh;
  private SimpleTextureMapping _simpleTextureMapping;
  private Mesh createMesh(G3MRenderContext rc)
  {
    if (_image == null)
    {
      return null;
    }
  
    final TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(_image, GLFormat.rgba(), _imageURL.getPath(), false);
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
    final Camera camera = rc.getCurrentCamera();
    final int viewPortWidth = camera.getWidth();
    final int viewPortHeight = camera.getHeight();
  
    final float width = _widthSize.getSize(viewPortWidth, viewPortHeight, _imageWidth, _imageHeight);
    final float height = _heightSize.getSize(viewPortWidth, viewPortHeight, _imageWidth, _imageHeight);
  
    final float x = _xPosition.getPosition(viewPortWidth, viewPortHeight, width, height);
    final float y = _yPosition.getPosition(viewPortWidth, viewPortHeight, width, height);
  
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
  
    _simpleTextureMapping = new SimpleTextureMapping(texId, texCoords.create(), true, true);
    _simpleTextureMapping.setTranslation(_texCoordsTranslationU, _texCoordsTranslationV);
  
    _simpleTextureMapping.setScale(_texCoordsScaleU, _texCoordsScaleV);
  
    _simpleTextureMapping.setRotation(_texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
  
    return new TexturedMesh(dm, true, _simpleTextureMapping, true, true);
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
    _simpleTextureMapping = null;
  
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

  public HUDQuadWidget(URL imageURL, HUDPosition xPosition, HUDPosition yPosition, HUDSize widthSize, HUDSize heightSize)
  {
     _imageURL = imageURL;
     _xPosition = xPosition;
     _yPosition = yPosition;
     _widthSize = widthSize;
     _heightSize = heightSize;
     _mesh = null;
     _simpleTextureMapping = null;
     _image = null;
     _imageWidth = 0;
     _imageHeight = 0;
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
  
    if (_simpleTextureMapping != null)
    {
      _simpleTextureMapping.setTranslation(_texCoordsTranslationU, _texCoordsTranslationV);
    }
  }

  public final void setTexCoordsScale(float u, float v)
  {
    _texCoordsScaleU = u;
    _texCoordsScaleV = v;
  
    if (_simpleTextureMapping != null)
    {
      _simpleTextureMapping.setScale(_texCoordsScaleU, _texCoordsScaleV);
    }
  }

  public final void setTexCoordsRotation(float angleInRadians, float centerU, float centerV)
  {
    _texCoordsRotationInRadians = angleInRadians;
    _texCoordsRotationCenterU = centerU;
    _texCoordsRotationCenterV = centerV;
  
    if (_simpleTextureMapping != null)
    {
      _simpleTextureMapping.setRotation(_texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
    }
  }

  public final void setTexCoordsRotation(Angle angle, float centerU, float centerV)
  {
    setTexCoordsRotation((float) angle._radians, centerU, centerV);
  }

  public void dispose()
  {
    if (_image != null)
       _image.dispose();
    if (_mesh != null)
       _mesh.dispose();
  
    if (_xPosition != null)
       _xPosition.dispose();
    if (_yPosition != null)
       _yPosition.dispose();
  
    if (_widthSize != null)
       _widthSize.dispose();
    if (_heightSize != null)
       _heightSize.dispose();
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
  public final void onImageDownload(IImage image)
  {
    _downloadingImage = false;
    _image = image;
    _imageWidth = _image.getWidth();
    _imageHeight = _image.getHeight();
  }

  /** private, do not call */
  public final void onImageDownloadError(URL url)
  {
    _errors.add("HUDQuadWidget: Error downloading \"" + url.getPath() + "\"");
  }

}