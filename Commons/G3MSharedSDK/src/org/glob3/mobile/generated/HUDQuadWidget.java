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



///#include "URL.hpp"
//class HUDPosition;
//class HUDSize;
//class IImage;
//class Mesh;
//class TransformableTextureMapping;
//class IImageBuilder;


public class HUDQuadWidget extends HUDWidget implements ChangedListener
{
  private IImageBuilder _imageBuilder;
  private IImageBuilder _backgroundImageBuilder;

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
  private IImage _backgroundImage;

  private String _imageName;
  private int _imageWidth;
  private int _imageHeight;

  private String _backgroundImageName;

  private boolean _buildingImage;
  private boolean _buildingBackgroundImage;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private Mesh _mesh;
  private TransformableTextureMapping _textureMapping;
  private Mesh createMesh(G3MRenderContext rc)
  {
    if (_image == null)
    {
      return null;
    }
  
    final boolean hasBackground = (_backgroundImageBuilder != null);
  
    if (hasBackground && (_backgroundImage == null))
    {
      return null;
    }
  
    TexturesHandler texturesHandler = rc.getTexturesHandler();
  
    final TextureIDReference textureID = texturesHandler.getTextureIDReference(_image, GLFormat.rgba(), _imageName, false);
    if (textureID == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
  
    TextureIDReference backgroundTextureID = null;
    if (hasBackground)
    {
      backgroundTextureID = texturesHandler.getTextureIDReference(_backgroundImage, GLFormat.rgba(), _backgroundImageName, false);
  
      if (backgroundTextureID == null)
      {
        if (textureID != null)
           textureID.dispose();
  
        rc.getLogger().logError("Can't background upload texture to GPU");
        return null;
      }
    }
  
    final Camera camera = rc.getCurrentCamera();
    final int viewPortWidth = camera.getViewPortWidth();
    final int viewPortHeight = camera.getViewPortHeight();
  
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
  
    if (hasBackground)
    {
      _textureMapping = new MultiTextureMapping(textureID, texCoords.create(), true, true, backgroundTextureID, texCoords.create(), true, true, _texCoordsTranslationU, _texCoordsTranslationV, _texCoordsScaleU, _texCoordsScaleV, _texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
    }
    else
    {
      _textureMapping = new SimpleTextureMapping(textureID, texCoords.create(), true, true, _texCoordsTranslationU, _texCoordsTranslationV, _texCoordsScaleU, _texCoordsScaleV, _texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
    }
  
    return new TexturedMesh(dm, true, _textureMapping, true, true);
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
    _textureMapping = null; // just nullify the pointer, the instance will be deleted from the Mesh destructor
  
    if (_mesh != null)
       _mesh.dispose();
    _mesh = null;
  }

  private G3MContext _context;

  protected final void rawRender(G3MRenderContext rc, GLState glState)
  {
    Mesh mesh = getMesh(rc);
    if (mesh != null)
    {
      mesh.render(rc, glState);
    }
  }

  public HUDQuadWidget(IImageBuilder imageBuilder, HUDPosition xPosition, HUDPosition yPosition, HUDSize widthSize, HUDSize heightSize)
  {
     this(imageBuilder, xPosition, yPosition, widthSize, heightSize, null);
  }
  public HUDQuadWidget(IImageBuilder imageBuilder, HUDPosition xPosition, HUDPosition yPosition, HUDSize widthSize, HUDSize heightSize, IImageBuilder backgroundImageBuilder)
  {
     _imageBuilder = imageBuilder;
     _xPosition = xPosition;
     _yPosition = yPosition;
     _widthSize = widthSize;
     _heightSize = heightSize;
     _backgroundImageBuilder = backgroundImageBuilder;
     _mesh = null;
     _textureMapping = null;
     _image = null;
     _imageWidth = 0;
     _imageHeight = 0;
     _buildingImage = false;
     _backgroundImage = null;
     _buildingBackgroundImage = false;
     _texCoordsTranslationU = 0F;
     _texCoordsTranslationV = 0F;
     _texCoordsScaleU = 1F;
     _texCoordsScaleV = 1F;
     _texCoordsRotationInRadians = 0F;
     _texCoordsRotationCenterU = 0F;
     _texCoordsRotationCenterV = 0F;
     _context = null;
  }

  public final void setTexCoordsTranslation(float u, float v)
  {
    _texCoordsTranslationU = u;
    _texCoordsTranslationV = v;
  
    if (_textureMapping != null)
    {
      _textureMapping.setTranslation(_texCoordsTranslationU, _texCoordsTranslationV);
    }
  }

  public final void setTexCoordsScale(float u, float v)
  {
    _texCoordsScaleU = u;
    _texCoordsScaleV = v;
  
    if (_textureMapping != null)
    {
      _textureMapping.setScale(_texCoordsScaleU, _texCoordsScaleV);
    }
  }

  public final void setTexCoordsRotation(float angleInRadians, float centerU, float centerV)
  {
    _texCoordsRotationInRadians = angleInRadians;
    _texCoordsRotationCenterU = centerU;
    _texCoordsRotationCenterV = centerV;
  
    if (_textureMapping != null)
    {
      _textureMapping.setRotation(_texCoordsRotationInRadians, _texCoordsRotationCenterU, _texCoordsRotationCenterV);
    }
  }

  public final void setTexCoordsRotation(Angle angle, float centerU, float centerV)
  {
    setTexCoordsRotation((float) angle._radians, centerU, centerV);
  }

  public void dispose()
  {
    if (_imageBuilder != null)
       _imageBuilder.dispose();
    if (_backgroundImageBuilder != null)
       _backgroundImageBuilder.dispose();
  
    _image = null;
    _backgroundImage = null;
  
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
    _context = context;
  
    if (!_buildingImage && (_image == null))
    {
      _buildingImage = true;
      _imageBuilder.build(context, new HUDQuadWidget_ImageBuilderListener(this, 0), true);
      if (_imageBuilder.isMutable())
      {
        _imageBuilder.setChangeListener(this);
      }
    }
  
    if (_backgroundImageBuilder != null)
    {
      if (!_buildingBackgroundImage && (_backgroundImage == null))
      {
        _buildingBackgroundImage = true;
        _backgroundImageBuilder.build(context, new HUDQuadWidget_ImageBuilderListener(this, 1), true);
        if (_backgroundImageBuilder.isMutable())
        {
          _backgroundImageBuilder.setChangeListener(this);
        }
      }
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
    else if (_buildingImage || _buildingBackgroundImage)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  /** private, do not call */
  public final void imageCreated(IImage image, String imageName, int imageRole)
  {
  
    if (imageRole == 0)
    {
      _buildingImage = false;
      _image = image;
      _imageName = imageName;
      _imageWidth = _image.getWidth();
      _imageHeight = _image.getHeight();
    }
    else if (imageRole == 1)
    {
      _buildingBackgroundImage = false;
      _backgroundImage = image;
      _backgroundImageName = imageName;
    }
  
    //  delete _imageBuilder;
    //  _imageBuilder = NULL;
  }

  /** private, do not call */
  public final void onImageBuildError(String error, int imageRole)
  {
    _errors.add("HUDQuadWidget: " + error);
  
    //  if (imageRole == 0) {
    //    _buildingImage0 = false;
    //  }
    //  else if (imageRole == 1) {
    //    _buildingBackgroundImage = false;
    //  }
  
    //  delete _imageBuilder;
    //  _imageBuilder = NULL;
  }

  public final void changed()
  {
  ///#warning Diego at work!
    cleanMesh();
  
    _image = null;
    _image = null;
    _imageName = "";
    _imageWidth = 0;
    _imageHeight = 0;
  
    _buildingImage = true;
    _imageBuilder.build(_context, new HUDQuadWidget_ImageBuilderListener(this, 0), true);
  
    _backgroundImage = null;
    _backgroundImage = null;
    _backgroundImageName = "";
  
    if (_backgroundImageBuilder != null)
    {
      _buildingBackgroundImage = true;
      _backgroundImageBuilder.build(_context, new HUDQuadWidget_ImageBuilderListener(this, 1), true);
    }
  }

}