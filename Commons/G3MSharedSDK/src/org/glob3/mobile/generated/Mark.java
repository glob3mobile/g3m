package org.glob3.mobile.generated; 
public class Mark
{
  private final String _label;
  private LabelPosition _labelPosition = new LabelPosition();

  private URL _iconURL = new URL();
  private final Geodetic3D _position ;
  private final double _minDistanceToCamera;

  private MarkUserData _userData;
  private final boolean _autoDeleteUserData;

  private MarkTouchListener _listener;
  private final boolean _autoDeleteListener;

  private IGLTextureId _textureId;

  private Vector3D _cartesianPosition;

  private IFloatBuffer _vertices;
  private IFloatBuffer getVertices(Planet planet)
  {
	if (_vertices == null)
	{
	  final Vector3D pos = getCartesianPosition(planet);
  
	  FloatBufferBuilderFromCartesian3D vertex = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	  vertex.add(pos);
	  vertex.add(pos);
	  vertex.add(pos);
	  vertex.add(pos);
  
	  _vertices = vertex.create();
	}
	return _vertices;
  }

  private boolean _textureSolved;
  private IImage _textureImage;
  private int _textureWidth;
  private int _textureHeight;

  private boolean _renderedMark;

  public Mark(String label, URL iconURL, Geodetic3D position, LabelPosition labelPosition, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(label, iconURL, position, labelPosition, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, LabelPosition labelPosition, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(label, iconURL, position, labelPosition, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, LabelPosition labelPosition, double minDistanceToCamera, MarkUserData userData)
  {
	  this(label, iconURL, position, labelPosition, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, LabelPosition labelPosition, double minDistanceToCamera)
  {
	  this(label, iconURL, position, labelPosition, minDistanceToCamera, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, LabelPosition labelPosition)
  {
	  this(label, iconURL, position, labelPosition, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& label, const URL iconURL, const Geodetic3D position, const LabelPosition labelPosition, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _label(label), _iconURL(iconURL), _position(position), _labelPosition(labelPosition), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(String label, URL iconURL, Geodetic3D position, LabelPosition labelPosition, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _label = label;
	  _iconURL = new URL(iconURL);
	  _position = new Geodetic3D(position);
	  _labelPosition = labelPosition;
	  _textureId = null;
	  _cartesianPosition = null;
	  _vertices = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0;
	  _textureHeight = 0;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
  
  }

  public Mark(String label, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(label, position, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(label, position, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, MarkUserData userData)
  {
	  this(label, position, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(String label, Geodetic3D position, double minDistanceToCamera)
  {
	  this(label, position, minDistanceToCamera, null, true, null, false);
  }
  public Mark(String label, Geodetic3D position)
  {
	  this(label, position, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& label, const Geodetic3D position, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _label(label), _labelPosition(Bottom), _iconURL("", false), _position(position), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _label = label;
	  _labelPosition = Bottom;
	  _iconURL = new URL("", false);
	  _position = new Geodetic3D(position);
	  _textureId = null;
	  _cartesianPosition = null;
	  _vertices = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0;
	  _textureHeight = 0;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
  
  }

  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(iconURL, position, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(iconURL, position, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData)
  {
	  this(iconURL, position, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera)
  {
	  this(iconURL, position, minDistanceToCamera, null, true, null, false);
  }
  public Mark(URL iconURL, Geodetic3D position)
  {
	  this(iconURL, position, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const URL iconURL, const Geodetic3D position, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _label(""), _labelPosition(Bottom), _iconURL(iconURL), _position(position), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _label = "";
	  _labelPosition = Bottom;
	  _iconURL = new URL(iconURL);
	  _position = new Geodetic3D(position);
	  _textureId = null;
	  _cartesianPosition = null;
	  _vertices = null;
	  _textureSolved = false;
	  _textureImage = null;
	  _renderedMark = false;
	  _textureWidth = 0;
	  _textureHeight = 0;
	  _userData = userData;
	  _autoDeleteUserData = autoDeleteUserData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
  
  }

  public void dispose()
  {
	if (_cartesianPosition != null)
		_cartesianPosition.dispose();
	if (_vertices != null)
		_vertices.dispose();
	if (_autoDeleteListener)
	{
	  if (_listener != null)
		  _listener.dispose();
	}
	if (_autoDeleteUserData)
	{
	  if (_userData != null)
		  _userData.dispose();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getLabel() const
  public final String getLabel()
  {
	return _label;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

  public final void initialize(G3MContext context)
  {
	if (!_textureSolved)
	{
	  final boolean hasLabel = (_label.length() != 0);
	  final boolean hasIconURL = (_iconURL.getPath().length() != 0);
  
	  if (hasIconURL)
	  {
		IDownloader downloader = context.getDownloader();
  
		downloader.requestImage(_iconURL, 1000000, TimeInterval.fromDays(30), new IconDownloadListener(this, _label, _labelPosition), true);
	  }
	  else
	  {
		if (hasLabel)
		{
		  ITextUtils.instance().createLabelImage(_label, new MarkLabelImageListener(this), true);
		}
		else
		{
		  ILogger.instance().logWarning("Marker created without label nor icon");
		}
	  }
	}
  }

  public final void render(G3MRenderContext rc)
  {
	final Camera camera = rc.getCurrentCamera();
	final Planet planet = rc.getPlanet();
  
	final Vector3D cameraPosition = camera.getCartesianPosition();
	final Vector3D markPosition = getCartesianPosition(planet);
  
	final Vector3D markCameraVector = markPosition.sub(cameraPosition);
	final double distanceToCamera = markCameraVector.length();
  
	_renderedMark = (_minDistanceToCamera == 0) || (distanceToCamera <= _minDistanceToCamera);
  
	if (_renderedMark)
	{
	  final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
  
	  if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > IMathUtils.instance().halfPi())
	  {
  
		if (_textureId == null)
		{
		  if (_textureImage != null)
		  {
			_textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _iconURL.getPath() + "_" + _label, false);
  
			rc.getFactory().deleteImage(_textureImage);
			_textureImage = null;
		  }
		}
  
		if (_textureId != null)
		{
		  GL gl = rc.getGL();
  
		  gl.drawBillBoard(_textureId, getVertices(planet), _textureWidth, _textureHeight);
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady() const
  public final boolean isReady()
  {
	return _textureSolved;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isRendered() const
  public final boolean isRendered()
  {
	return _renderedMark;
  }

  public final void onTextureDownloadError()
  {
	_textureSolved = true;
  
	ILogger.instance().logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")", _iconURL.getPath(), _label);
  }

  public final void onTextureDownload(IImage image)
  {
	_textureSolved = true;
	_textureImage = image.shallowCopy();
	_textureWidth = _textureImage.getWidth();
	_textureHeight = _textureImage.getHeight();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getTextureWidth() const
  public final int getTextureWidth()
  {
	return _textureWidth;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getTextureHeight() const
  public final int getTextureHeight()
  {
	return _textureHeight;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2I getTextureExtent() const
  public final Vector2I getTextureExtent()
  {
	return new Vector2I(_textureWidth, _textureHeight);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Object* getUserData() const
  public final Object getUserData()
  {
	return _userData;
  }

  public final void setUserData(MarkUserData userData)
  {
	if (_autoDeleteUserData)
	{
	  if (_userData != null)
		  _userData.dispose();
	}
	_userData = userData;
  }

  public final boolean touched()
  {
	if (_listener == null)
	{
	  return false;
	}
	return _listener.touchedMark(this);
  }

  public final Vector3D getCartesianPosition(Planet planet)
  {
	if (_cartesianPosition == null)
	{
	  _cartesianPosition = new Vector3D(planet.toCartesian(_position));
	}
	return _cartesianPosition;
  }

}