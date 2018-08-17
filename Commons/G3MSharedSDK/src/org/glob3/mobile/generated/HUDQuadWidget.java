package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class HUDPosition;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class HUDSize;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TransformableTextureMapping;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageBuilder;


//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class HUDQuadWidget extends HUDWidget, ChangedListener
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _image;
  private final IImage _backgroundImage;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _image = new internal();
  public IImage _backgroundImage = new internal();
//#endif

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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final TextureIDReference backgroundTextureID = null;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	TextureIDReference backgroundTextureID = null;
//#endif
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
	int viewPortWidth = camera.getViewPortWidth();
	if (rc.getViewMode() == ViewMode.STEREO)
	{
	  viewPortWidth /= 2;
	}
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final G3MContext _context;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public G3MContext _context = new internal();
//#endif

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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: HUDQuadWidget(IImageBuilder* imageBuilder, HUDPosition* xPosition, HUDPosition* yPosition, HUDSize* widthSize, HUDSize* heightSize, IImageBuilder* backgroundImageBuilder = null) : _imageBuilder(imageBuilder), _xPosition(xPosition), _yPosition(yPosition), _widthSize(widthSize), _heightSize(heightSize), _backgroundImageBuilder(backgroundImageBuilder), _mesh(null), _textureMapping(null), _image(null), _imageWidth(0), _imageHeight(0), _buildingImage(false), _backgroundImage(null), _buildingBackgroundImage(false), _texCoordsTranslationU(0), _texCoordsTranslationV(0), _texCoordsScaleU(1), _texCoordsScaleV(1), _texCoordsRotationInRadians(0), _texCoordsRotationCenterU(0), _texCoordsRotationCenterV(0), _context(null)
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
  
	if (_image != null)
		_image.dispose();
	_image = null;
	_imageName = "";
	_imageWidth = 0;
	_imageHeight = 0;
  
	_buildingImage = true;
	_imageBuilder.build(_context, new HUDQuadWidget_ImageBuilderListener(this, 0), true);
  
	if (_backgroundImage != null)
		_backgroundImage.dispose();
	_backgroundImage = null;
	_backgroundImageName = "";
  
	if (_backgroundImageBuilder != null)
	{
	  _buildingBackgroundImage = true;
	  _backgroundImageBuilder.build(_context, new HUDQuadWidget_ImageBuilderListener(this, 1), true);
	}
  }

}
