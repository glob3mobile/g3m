package org.glob3.mobile.generated; 
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

public class Mark
{
  private final String _name;
  private URL _textureURL = new URL();
  private final Geodetic3D _position ;
  private Object _userData;
  private double _minDistanceToCamera;
  private MarkTouchListener _listener;
  private boolean _autoDeleteListener;

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

  public Mark(String name, URL textureURL, Geodetic3D position, double minDistanceToCamera, Object userData, MarkTouchListener listener)
  {
	  this(name, textureURL, position, minDistanceToCamera, userData, listener, false);
  }
  public Mark(String name, URL textureURL, Geodetic3D position, double minDistanceToCamera, Object userData)
  {
	  this(name, textureURL, position, minDistanceToCamera, userData, null, false);
  }
  public Mark(String name, URL textureURL, Geodetic3D position, double minDistanceToCamera)
  {
	  this(name, textureURL, position, minDistanceToCamera, null, null, false);
  }
  public Mark(String name, URL textureURL, Geodetic3D position)
  {
	  this(name, textureURL, position, 4.5e+06, null, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& name, const URL textureURL, const Geodetic3D position, double minDistanceToCamera =4.5e+06, Object* userData =null, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _name(name), _textureURL(textureURL), _position(position), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(false), _textureImage(null), _renderedMark(false), _textureWidth(0), _textureHeight(0), _userData(userData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(String name, URL textureURL, Geodetic3D position, double minDistanceToCamera, Object userData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _name = name;
	  _textureURL = new URL(textureURL);
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
	  _minDistanceToCamera = minDistanceToCamera;
	  _listener = listener;
	  _autoDeleteListener = autoDeleteListener;
  
  }

  public Mark(String name, IImage textureImage, Geodetic3D position, double minDistanceToCamera, Object userData, MarkTouchListener listener)
  {
	  this(name, textureImage, position, minDistanceToCamera, userData, listener, false);
  }
  public Mark(String name, IImage textureImage, Geodetic3D position, double minDistanceToCamera, Object userData)
  {
	  this(name, textureImage, position, minDistanceToCamera, userData, null, false);
  }
  public Mark(String name, IImage textureImage, Geodetic3D position, double minDistanceToCamera)
  {
	  this(name, textureImage, position, minDistanceToCamera, null, null, false);
  }
  public Mark(String name, IImage textureImage, Geodetic3D position)
  {
	  this(name, textureImage, position, 4.5e+06, null, null, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Mark(const String& name, IImage* textureImage, const Geodetic3D position, double minDistanceToCamera =4.5e+06, Object* userData =null, MarkTouchListener* listener =null, boolean autoDeleteListener =false) : _name(name), _textureURL("", false), _position(position), _textureId(null), _cartesianPosition(null), _vertices(null), _textureSolved(true), _textureImage(textureImage), _renderedMark(false), _textureWidth(textureImage->getWidth()), _textureHeight(textureImage->getHeight()), _userData(userData), _minDistanceToCamera(minDistanceToCamera), _listener(listener), _autoDeleteListener(autoDeleteListener)
  public Mark(String name, IImage textureImage, Geodetic3D position, double minDistanceToCamera, Object userData, MarkTouchListener listener, boolean autoDeleteListener)
  {
	  _name = name;
	  _textureURL = new URL("", false);
	  _position = new Geodetic3D(position);
	  _textureId = null;
	  _cartesianPosition = null;
	  _vertices = null;
	  _textureSolved = true;
	  _textureImage = textureImage;
	  _renderedMark = false;
	  _textureWidth = textureImage.getWidth();
	  _textureHeight = textureImage.getHeight();
	  _userData = userData;
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
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getName() const
  public final String getName()
  {
	return _name;
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
	  IDownloader downloader = context.getDownloader();
  
	  downloader.requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), new TextureDownloadListener(this), true);
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
			_textureId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _textureURL.getPath(), false);
  
			rc.getFactory().deleteImage(_textureImage);
			_textureImage = null;
		  }
		}
  
		if (_textureId != null)
		{
		  GL gl = rc.getGL();
  
		  // static Vector2D textureTranslation(0.0, 0.0);
		  // static Vector2D textureScale(1.0, 1.0);
		  // gl->transformTexCoords(textureScale, textureTranslation);
  
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
  
	ILogger.instance().logError("Can't load image \"%s\"", _textureURL.getPath());
	//=======
	//    //  todo;
	//    if (!_textureSolved) {
	//        IDownloader* downloader = context->getDownloader();
	//
	//        downloader->requestImage(_textureURL,
	//                                 1000000,
	//                                 TimeInterval::fromDays(30),
	//                                 new TextureDownloadListener(this),
	//                                 true);
	//    }
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

  public final void setUserData(Object userData)
  {
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