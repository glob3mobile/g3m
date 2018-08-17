package org.glob3.mobile.generated;import java.util.*;

public class MarkWidget
{
  private GLState _glState;
  private Geometry2DGLFeature _geo2Dfeature;
  private ViewportExtentGLFeature _viewportExtentGLFeature;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _image;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _image = new internal();
//#endif
  private String _imageName;
  private IImageBuilder _imageBuilder;
  private TexturesHandler _texHandler;

  private IFloatBuffer _vertices;
  private SimpleTextureMapping _textureMapping;

  private float _halfWidth;
  private float _halfHeight;

  // Screen position
  private float _x;
  private float _y;

  private static class WidgetImageListener implements IImageBuilderListener
  {
	private MarkWidget _widget;
	public WidgetImageListener(MarkWidget widget)
	{
		_widget = widget;
	}

	public void dispose()
	{
	}

	public final void imageCreated(IImage image, String imageName)
	{
	  _widget.prepareWidget(image, imageName);
	}

	public final void onError(String error)
	{
	  ILogger.instance().logError(error);
	}
  }

  private void prepareWidget(IImage image, String imageName)
  {
	_image = image;
	_imageName = imageName;
  
	_halfWidth = (float) image.getWidth() / 2.0f;
	_halfHeight = (float) image.getHeight() / 2.0f;
  
	if (_vertices != null)
	{
	  if (_vertices != null)
		  _vertices.dispose();
	}
  
	FloatBufferBuilderFromCartesian2D pos2D = new FloatBufferBuilderFromCartesian2D();
	// #warning TODO: share vertices for marks of the same size?
	pos2D.add(-_halfWidth, -_halfHeight); // vertex 1
	pos2D.add(_halfWidth, -_halfHeight); // vertex 2
	pos2D.add(-_halfWidth, _halfHeight); // vertex 3
	pos2D.add(_halfWidth, _halfHeight); // vertex 4
  
	_vertices = pos2D.create();
  
	_geo2Dfeature = new Geometry2DGLFeature(_vertices, 2, 0, true, 0, 3.0f, true, 1.0f, new Vector2F(_x, _y)); // translation -  pointSize -  needsPointSize -  lineWidth -  stride -  normalized -  index -  arrayElementSize -  buffer
  
	_glState.addGLFeature(_geo2Dfeature, false);
  
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
	texCoords.add(0.0f, 1.0f); // vertex 1
	texCoords.add(1.0f, 1.0f); // vertex 2
	texCoords.add(0.0f, 0.0f); // vertex 3
	texCoords.add(1.0f, 0.0f); // vertex 4
  
	final TextureIDReference textureID = _texHandler.getTextureIDReference(_image, GLFormat.rgba(), _imageName, false);
  
	// #warning TODO: share unit texCoords
	if (_textureMapping != null)
	{
	  if (_textureMapping != null)
		  _textureMapping.dispose();
	}
	_textureMapping = new SimpleTextureMapping(textureID, texCoords.create(), true, true);
  
	tangible.RefObject<GLState> tempRef__glState = new tangible.RefObject<GLState>(_glState);
	_textureMapping.modifyGLState(tempRef__glState);
	_glState = tempRef__glState.argvalue;
  }


  //C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
  public MarkWidget(IImageBuilder imageBuilder)
  {
	  _image = null;
	  _imageBuilder = imageBuilder;
	  _viewportExtentGLFeature = null;
	  _geo2Dfeature = null;
	  _glState = null;
	  _x = NANF;
	  _y = NANF;
	  _halfHeight = 0F;
	  _halfWidth = 0F;
	  _vertices = null;
	  _textureMapping = null;
  }

  public void dispose()
  {
	if (_image != null)
		_image.dispose();
	if (_imageBuilder != null)
		_imageBuilder.dispose();
  
	if (_vertices != null)
		_vertices.dispose();
	if (_textureMapping != null)
		_textureMapping.dispose();
  
	if (_glState != null)
	{
	  _glState._release();
	}
  }

  public final void init(G3MRenderContext rc)
  {
	if (_glState == null)
	{
	  _glState = new GLState();
	  _viewportExtentGLFeature = new ViewportExtentGLFeature(rc.getCurrentCamera(), rc.getViewMode());
  
	  _texHandler = rc.getTexturesHandler();
	  _imageBuilder.build(rc, new WidgetImageListener(this), true);
  
	  _glState.addGLFeature(_viewportExtentGLFeature, false);
	}
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
	rc.getGL().drawArrays(GLPrimitive.triangleStrip(), 0, 4, _glState, rc.getGPUProgramManager()); // count -  first
  }
//              float x,
//              float y

  public final void setAndClampScreenPos(float x, float y, int viewportWidth, int viewportHeight, float margin)
  {
	final IMathUtils mu = IMathUtils.instance();
	final float xx = mu.clamp(x, _halfWidth + margin, viewportWidth - _halfWidth - margin);
	final float yy = mu.clamp(y, _halfHeight + margin, viewportHeight - _halfHeight - margin);
  
	if (_geo2Dfeature != null)
	{
	  _geo2Dfeature.setTranslation(xx, yy);
	}
	_x = xx;
	_y = yy;
  }

  public final void setScreenPos(float x, float y)
  {
	if (_geo2Dfeature != null)
	{
	  _geo2Dfeature.setTranslation(x, y);
	}
	_x = x;
	_y = y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F getScreenPos() const
  public final Vector2F getScreenPos()
  {
	  return new Vector2F(_x, _y);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void getScreenPosition(MutableVector2F& result) const
  public final void getScreenPosition(tangible.RefObject<MutableVector2F> result)
  {
	result.argvalue.set(_x, _y);
  }

  public final void resetPosition()
  {
	if (_geo2Dfeature != null)
	{
	  _geo2Dfeature.setTranslation(0, 0);
	}
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
	_x = NANF;
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
	_y = NANF;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
	if (_viewportExtentGLFeature != null)
	{
	  int logicWidth = width;
	  if (ec.getViewMode() == ViewMode.STEREO)
	  {
		logicWidth /= 2;
	  }
  
	  _viewportExtentGLFeature.changeExtent(logicWidth, height);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady() const
  public final boolean isReady()
  {
	return _image != null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	return _image == null ? 0 : _image.getWidth();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	return _image == null ? 0 : _image.getHeight();
  }

}
