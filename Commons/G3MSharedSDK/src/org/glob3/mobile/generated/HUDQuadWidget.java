package org.glob3.mobile.generated;
//
//  HUDQuadWidget.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

//
//  HUDQuadWidget.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//



///#include "URL.hpp"
//class HUDPosition;
//class HUDSize;
//class Mesh;
//class TransformableTextureMapping;
//class IImageFactory;


public class HUDQuadWidget extends HUDWidget implements ChangedListener
{
  private HUDWidgetImage _image;
  private HUDWidgetImage _backgroundImage; // NULL when the widget has no background

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

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private Mesh _mesh;
  private TransformableTextureMapping _textureMapping;
  private Mesh createMesh(G3MRenderContext rc)
  {
    if (_image.getImage() == null)
    {
      return null;
    }
  
    final boolean hasBackground = (_backgroundImage != null);
  
    if (hasBackground && (_backgroundImage.getImage() == null))
    {
      return null;
    }
  
    TexturesHandler texturesHandler = rc.getTexturesHandler();
  
    final TextureIDReference textureID = texturesHandler.getTextureIDReference(_image.getImage(), GLFormat.rgba(), _image.getImageName(), false, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge());
    if (textureID == null)
    {
      rc.getLogger().logError("Can't upload texture to GPU");
      return null;
    }
  
    TextureIDReference backgroundTextureID = null;
    if (hasBackground)
    {
      backgroundTextureID = texturesHandler.getTextureIDReference(_backgroundImage.getImage(), GLFormat.rgba(), _backgroundImage.getImageName(), false, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge());
  
      if (backgroundTextureID == null)
      {
        if (textureID != null)
           textureID.dispose();
        rc.getLogger().logError("Can't upload background texture to GPU");
        return null;
      }
    }
  
    final Camera camera = rc.getCurrentCamera();
    int viewPortWidth = camera.getViewPortWidth();
    if (rc.getViewMode() == ViewMode.STEREO)
    {
      viewPortWidth /= 2;
    }
    final int viewPortHeight = camera.getViewPortHeight();
  
    final int imageWidth = _image.getImageWidth();
    final int imageHeight = _image.getImageHeight();
  
    final float width = _widthSize.getSize(viewPortWidth, viewPortHeight, imageWidth, imageHeight);
    final float height = _heightSize.getSize(viewPortWidth, viewPortHeight, imageWidth, imageHeight);
  
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

  private void addErrors(HUDWidgetImage widgetImage)
  {
    final java.util.ArrayList<String> errors = widgetImage.getErrors();
    for (int i = 0; i < errors.size(); i++)
    {
      _errors.add("HUDQuadWidget: " + errors.get(i));
    }
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

  public HUDQuadWidget(IImageFactory imageFactory, HUDPosition xPosition, HUDPosition yPosition, HUDSize widthSize, HUDSize heightSize)
  {
     this(imageFactory, xPosition, yPosition, widthSize, heightSize, null);
  }
  public HUDQuadWidget(IImageFactory imageFactory, HUDPosition xPosition, HUDPosition yPosition, HUDSize widthSize, HUDSize heightSize, IImageFactory backgroundImageFactory)
  {
     _image = new HUDWidgetImage(imageFactory);
     _backgroundImage = (backgroundImageFactory == null) ? null : new HUDWidgetImage(backgroundImageFactory);
     _xPosition = xPosition;
     _yPosition = yPosition;
     _widthSize = widthSize;
     _heightSize = heightSize;
     _mesh = null;
     _textureMapping = null;
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
    if (_image != null)
       _image.dispose();
    if (_backgroundImage != null)
       _backgroundImage.dispose();
  
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
  
    _image.initialize(context, this);
    if (_backgroundImage != null)
    {
      _backgroundImage.initialize(context, this);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    cleanMesh();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    addErrors(_image);
    if (_backgroundImage != null)
    {
      addErrors(_backgroundImage);
    }
  
    if (!_errors.isEmpty())
    {
      return RenderState.error(_errors);
    }
    else if (_image.isBuilding() || ((_backgroundImage != null) && _backgroundImage.isBuilding()))
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final void changed()
  {
    cleanMesh();
  
    _image.rebuild(_context);
    if (_backgroundImage != null)
    {
      _backgroundImage.rebuild(_context);
    }
  }

}