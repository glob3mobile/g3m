package org.glob3.mobile.generated; 
<<<<<<< HEAD
//
//  Mark.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Mark.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarkTouchListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;

=======
>>>>>>> webgl-port
public class Mark
{
  private final String _label;
  private final boolean _labelBottom;

<<<<<<< HEAD
	private final String _name;
	private URL _textureURL = new URL();
	private final Geodetic3D _position ;
	private Object _userData;
	private double _minDistanceToCamera;
	private MarkTouchListener _listener;
=======
  private URL _iconURL = new URL();
  private final Geodetic3D _position ;
  private final double _minDistanceToCamera;

  private MarkUserData _userData;
  private final boolean _autoDeleteUserData;

  private MarkTouchListener _listener;
  private final boolean _autoDeleteListener;
>>>>>>> webgl-port

	private IGLTextureId _textureId;

<<<<<<< HEAD
	private Vector3D _cartesianPosition;
	private Vector3D getCartesianPosition(Planet planet)
	{
		if (_cartesianPosition == null)
		{
			_cartesianPosition = new Vector3D(planet.toCartesian(_position));
		}
		return _cartesianPosition;
	}
=======
  private Vector3D _cartesianPosition;
>>>>>>> webgl-port

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

<<<<<<< HEAD
  public Mark(String name, URL textureURL, Geodetic3D position, Object userData, double minDistanceToCamera)
  {
	  this(name, textureURL, position, userData, minDistanceToCamera, null);
  }
  public Mark(String name, URL textureURL, Geodetic3D position, Object userData)
  {
	  this(name, textureURL, position, userData, 0, null);
  }
  public Mark(String name, URL textureURL, Geodetic3D position)
  {
	  this(name, textureURL, position, null, 0, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String name, const URL textureURL, const Geodetic3D position, Object* userData=null, double minDistanceToCamera=0, MarkTouchListener* listener=null) : _name(name), _textureURL(textureURL), _position(position), _userData(userData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0)
  public Mark(String name, URL textureURL, Geodetic3D position, Object userData, double minDistanceToCamera, MarkTouchListener listener)
  {
	  _name = name;
	  _textureURL = new URL(textureURL);
	  _position = new Geodetic3D(position);
	  _userData = userData;
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
=======
  public Mark(String label, URL iconURL, Geodetic3D position, boolean labelBottom, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener)
  {
	  this(label, iconURL, position, labelBottom, minDistanceToCamera, userData, autoDeleteUserData, listener, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, boolean labelBottom, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData)
  {
	  this(label, iconURL, position, labelBottom, minDistanceToCamera, userData, autoDeleteUserData, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, boolean labelBottom, double minDistanceToCamera, MarkUserData userData)
  {
	  this(label, iconURL, position, labelBottom, minDistanceToCamera, userData, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, boolean labelBottom, double minDistanceToCamera)
  {
	  this(label, iconURL, position, labelBottom, minDistanceToCamera, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position, boolean labelBottom)
  {
	  this(label, iconURL, position, labelBottom, 4.5e+06, null, true, null, false);
  }
  public Mark(String label, URL iconURL, Geodetic3D position)
  {
	  this(label, iconURL, position, true, 4.5e+06, null, true, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& label, const URL iconURL, const Geodetic3D position, const boolean labelBottom =true, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _label(label), _iconURL(iconURL), _position(position), _labelBottom(labelBottom), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(String label, URL iconURL, Geodetic3D position, boolean labelBottom, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _label = label;
	  _iconURL = new URL(iconURL);
	  _position = new Geodetic3D(position);
	  _labelBottom = labelBottom;
>>>>>>> webgl-port
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
//ORIGINAL LINE: Mark(const String& label, const Geodetic3D position, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _label(label), _labelBottom(true), _iconURL("", false), _position(position), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(String label, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _label = label;
	  _labelBottom = true;
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
//ORIGINAL LINE: Mark(const URL iconURL, const Geodetic3D position, double minDistanceToCamera =4.5e+06, MarkUserData* userData =null, boolean autoDeleteUserData =true, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _label(""), _labelBottom(true), _iconURL(iconURL), _position(position), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _autoDeleteUserData(autoDeleteUserData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(URL iconURL, Geodetic3D position, double minDistanceToCamera, MarkUserData userData, boolean autoDeleteUserData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _label = "";
	  _labelBottom = true;
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
<<<<<<< HEAD
	  if (_cartesianPosition != null)
		  _cartesianPosition.dispose();
	  if (_vertices != null)
		  _vertices.dispose();
=======
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
>>>>>>> webgl-port
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
  
		downloader.requestImage(_iconURL, 1000000, TimeInterval.fromDays(30), new IconDownloadListener(this, _label, _labelBottom), true);
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

<<<<<<< HEAD
//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private Vector2D render_textureTranslation = new Vector2D(0.0, 0.0);
//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private Vector2D render_textureScale = new Vector2D(1.0, 1.0);
  public final void render(G3MRenderContext rc, GLState parentState)
=======
  public final void render(G3MRenderContext rc)
>>>>>>> webgl-port
  {
	final Camera camera = rc.getCurrentCamera();
	final Planet planet = rc.getPlanet();
  
	final Vector3D cameraPosition = camera.getCartesianPosition();
	final Vector3D markPosition = getCartesianPosition(planet);
  
	final Vector3D markCameraVector = markPosition.sub(cameraPosition);
	final double distanceToCamera = markCameraVector.length();
<<<<<<< HEAD
	//_renderedMark = distanceToCamera <= _minDistanceToCamera;
	//const bool renderMark = true;
=======
  
	_renderedMark = (_minDistanceToCamera == 0) || (distanceToCamera <= _minDistanceToCamera);
>>>>>>> webgl-port
  
	//if (_renderedMark) {
	//const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);
  
	  if (_minDistanceToCamera!=0)
	  {
		  _renderedMark = distanceToCamera <= _minDistanceToCamera;
	  }
	  else
	  {
		  final Vector3D radius = rc.getPlanet().getRadii();
		  final double minDistanceToCamera = (radius._x + radius._y + radius._z) / 3 * 0.75;
  
		  _renderedMark = distanceToCamera <= minDistanceToCamera;
	  }
	  //  const bool renderMark = true;
  
	  if (_renderedMark)
	  {
<<<<<<< HEAD
		  final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
  
		  if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > IMathUtils.instance().halfPi())
		  {
			  GL gl = rc.getGL();
  
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//			  static Vector2D textureTranslation(0.0, 0.0);
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//			  static Vector2D textureScale(1.0, 1.0);
			  gl.transformTexCoords(render_textureScale, render_textureTranslation);
  
			  if (_textureId == null)
			  {
				  //        IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);
				  //
				  //        _textureId = rc->getTexturesHandler()->getGLTextureId(image,
				  //                                                              GLFormat::rgba(),
				  //                                                              _textureFilename,
				  //                                                              false);
				  //
				  //        rc->getFactory()->deleteImage(image);
  
				  if (_textureImage != null)
				  {
					  _textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _textureURL.getPath(), false);
=======
  
		if (_textureId == null)
		{
		  if (_textureImage != null)
		  {
			_textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _iconURL.getPath() + "_" + _label, false);
>>>>>>> webgl-port
  
					  rc.getFactory().deleteImage(_textureImage);
					  _textureImage = null;
				  }
			  }
  
<<<<<<< HEAD
			  if (_textureId != null)
			  {
				  gl.drawBillBoard(_textureId, getVertices(planet), camera.getViewPortRatio());
			  }
		  }
	  }
	//}
=======
		if (_textureId != null)
		{
		  GL gl = rc.getGL();
  
		  gl.drawBillBoard(_textureId, getVertices(planet), _textureWidth, _textureHeight);
		}
	  }
	}
>>>>>>> webgl-port
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
  
<<<<<<< HEAD
	  ILogger.instance().logError("Can't load image \"%s\"", _textureURL.getPath());
=======
	ILogger.instance().logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")", _iconURL.getPath(), _label);
>>>>>>> webgl-port
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
<<<<<<< HEAD
	  //  return (_textureImage == NULL) ? 0 : _textureImage->getWidth();
	  return _textureWidth;
=======
	return _textureWidth;
>>>>>>> webgl-port
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getTextureHeight() const
  public final int getTextureHeight()
  {
<<<<<<< HEAD
	  //  return (_textureImage == NULL) ? 0 : _textureImage->getHeight();
	  return _textureHeight;
=======
	return _textureHeight;
>>>>>>> webgl-port
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2I getTextureExtent() const
  public final Vector2I getTextureExtent()
  {
<<<<<<< HEAD
	  //  return (_textureImage == NULL) ? Vector2I::zero() : _textureImage->getExtent();
	  return new Vector2I(_textureWidth, _textureHeight);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Object* getUserData() const
  public final Object getUserData()
  {
	return _userData;
  }

  public final void setUserData(Object userData)
  {
	  _userData = userData;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MarkTouchListener* getListener() const
  public final MarkTouchListener getListener()
  {
	return _listener;
=======
	return new Vector2I(_textureWidth, _textureHeight);
>>>>>>> webgl-port
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