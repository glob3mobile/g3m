package org.glob3.mobile.generated;
//
//  HUDBillboardWidget.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//

//
//  HUDBillboardWidget.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//




//class IImageFactory;
//class HUDWidgetImage;
//class GLState;
//class TextureIDReference;
//class BillboardGLFeature;
//class ModelTransformGLFeature;
//class TextureGLFeature;
//class ViewportExtentGLFeature;
//class IFloatBuffer;


// An image drawn with the Billboard shader at a pixel position: position, size, alpha and mirror can change every frame
public class HUDBillboardWidget extends HUDWidget implements ChangedListener
{
  private HUDWidgetImage _image;

  // the point of the image (as drawn, not mirrored) placed at the position, in UV: (0,0) top-left; may lie outside the image
  private float _anchorU;
  private float _anchorV;

  // pixels, (0,0) top-left, the frame Camera::point2Pixel uses
  private float _x;
  private float _y;

  // pixels
  private float _width;
  private float _height;

  private float _alpha;

  // flipped left to right, around the anchor
  private boolean _mirrored;

  private TextureIDReference _textureID;
  private G3MContext         _context;

  private GLState _glState;
  private BillboardGLFeature _billboardGLF;
  private ModelTransformGLFeature _modelTransformGLF;
  private TextureGLFeature _textureGLF;
  private ViewportExtentGLFeature _viewportExtentGLF;
  private IFloatBuffer _texCoords;

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private boolean createGLState(G3MRenderContext rc)
  {
    _textureID = rc.getTexturesHandler().getTextureIDReference(_image.getImage(), GLFormat.rgba(), _image.getImageName(), false, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge());
    if (_textureID == null)
    {
      rc.getLogger().logError("HUDBillboardWidget: can't upload texture to GPU");
      return false;
    }
  
    if (_texCoords == null)
    {
      // the corners in the order the Billboard shader expects (as MarksRenderer::getBillboardTexCoords)
      FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
      texCoords.add(1, 1);
      texCoords.add(1, 0);
      texCoords.add(0, 1);
      texCoords.add(0, 0);
      _texCoords = texCoords.create();
    }
  
    _glState = new GLState();
  
    _billboardGLF = new BillboardGLFeature(_width, _height, _anchorU, _anchorV, _textureID.isPremultiplied());
    _glState.addGLFeature(_billboardGLF, false);
  
    _modelTransformGLF = new ModelTransformGLFeature(MutableMatrix44D.identity().asMatrix44D());
    _glState.addGLFeature(_modelTransformGLF, false);
  
    _viewportExtentGLF = new ViewportExtentGLFeature(rc.getCurrentCamera(), rc.getViewMode());
    _glState.addGLFeature(_viewportExtentGLF, false);
  
    _textureGLF = new TextureGLFeature(_textureID.getID(), _texCoords, 2, 0, false, 0, true, _textureID.isPremultiplied() ? GLBlendFactor.one() : GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha(), 0, 0, 1, 1, 0, 0, 0); // rotation -  scale -  translation
    _glState.addGLFeature(_textureGLF, false);
  
    return true;
  }

  private void releaseGLState()
  {
    if (_glState != null)
    {
      _glState._release(); // releases its features
      _glState = null;
    }
    _billboardGLF = null;
    _modelTransformGLF = null;
    _textureGLF = null;
    _viewportExtentGLF = null;
  
    _textureID = null;
    _textureID = null;
  }

  private void updateGLState(G3MRenderContext rc)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    // the HUD projection has y up
    final float hudY = rc.getCurrentCamera().getViewPortHeight() - _y;
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(new Vector3D(_x, hudY, 0));
    _modelTransformGLF.setMatrix(translation.asMatrix44D());
  
    _billboardGLF.changeSize(mu.round(_width), mu.round(_height));
    _billboardGLF.changeAlpha(_alpha);
  
    // the shader places the quad from its texture coordinates, so the mirror flips the image inside the quad and moves the anchor
    if (_mirrored)
    {
      _billboardGLF.changeAnchor(1 - _anchorU, _anchorV);
      _textureGLF.setTranslation(1, 0);
      _textureGLF.setScale(-1, 1);
    }
    else
    {
      _billboardGLF.changeAnchor(_anchorU, _anchorV);
      _textureGLF.setTranslation(0, 0);
      _textureGLF.setScale(1, 1);
    }
  }

  protected final void rawRender(G3MRenderContext rc, GLState glState)
  {
    if (_image.getImage() == null)
    {
      return;
    }
    if ((_alpha <= 0) || (_width <= 0) || (_height <= 0))
    {
      return;
    }
  
    if (_glState == null)
    {
      if (!createGLState(rc))
      {
        return;
      }
    }
  
    updateGLState(rc);
    _glState.setParent(glState);
  
    rc.getGL().drawArrays(GLPrimitive.triangleStrip(), 0, 4, _glState, rc.getGPUProgramManager());
  }

  public HUDBillboardWidget(IImageFactory imageFactory, float anchorU, float anchorV)
  {
     _image = new HUDWidgetImage(imageFactory);
     _anchorU = anchorU;
     _anchorV = anchorV;
     _x = 0F;
     _y = 0F;
     _width = 0F;
     _height = 0F;
     _alpha = 1F;
     _mirrored = false;
     _textureID = null;
     _context = null;
     _glState = null;
     _billboardGLF = null;
     _modelTransformGLF = null;
     _textureGLF = null;
     _viewportExtentGLF = null;
     _texCoords = null;
  }

  public void dispose()
  {
    releaseGLState();
    if (_texCoords != null)
       _texCoords.dispose();
    if (_image != null)
       _image.dispose();
  }

  public final void setPosition(float x, float y)
  {
    _x = x;
    _y = y;
  }

  public final void setSize(float width, float height)
  {
    _width = width;
    _height = height;
  }

  public final void setAnchor(float anchorU, float anchorV)
  {
    _anchorU = anchorU;
    _anchorV = anchorV;
  }

  public final void setAlpha(float alpha)
  {
    _alpha = alpha;
  }

  public final void setMirrored(boolean mirrored)
  {
    _mirrored = mirrored;
  }

  /** the image's own size in pixels, zero until it is built */
  public final int getImageWidth()
  {
    return _image.getImageWidth();
  }

  public final int getImageHeight()
  {
    return _image.getImageHeight();
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
    _image.initialize(context, this);
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    // the viewport extent is read when the GLState is created
    releaseGLState();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    final java.util.ArrayList<String> imageErrors = _image.getErrors();
    for (int i = 0; i < imageErrors.size(); i++)
    {
      _errors.add("HUDBillboardWidget: " + imageErrors.get(i));
    }
  
    if (!_errors.isEmpty())
    {
      return RenderState.error(_errors);
    }
    else if (_image.isBuilding())
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
    releaseGLState();
    _image.rebuild(_context);
  }

}