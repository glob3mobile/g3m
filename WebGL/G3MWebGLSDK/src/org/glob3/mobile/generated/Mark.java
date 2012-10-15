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

public class Mark
{

  private final String _name;
  private URL _textureURL = new URL();
  private final Geodetic3D _position ;

  private IGLTextureId _textureId;

  private Vector3D _cartesianPosition;
  private Vector3D getCartesianPosition(Planet planet)
  {
	if (_cartesianPosition == null)
	{
	  _cartesianPosition = new Vector3D(planet.toCartesian(_position));
	}
	return _cartesianPosition;
  }

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

  public Mark(String name, URL textureURL, Geodetic3D position)
  {
	  _name = name;
	  _textureURL = new URL(textureURL);
	  _position = new Geodetic3D(position);
	  _textureId = null;
	  _cartesianPosition = null;
	  _vertices = null;
	  _textureSolved = false;
	  _textureImage = null;

  }

  public void dispose()
  {
	if (_cartesianPosition != null)
	{
	  if (_cartesianPosition != null)
		  _cartesianPosition.dispose();
	}
	if (_vertices != null)
	{
	  if (_vertices != null)
		  _vertices.dispose();
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

  public final void initialize(InitializationContext ic)
  {
	//  todo;
	if (!_textureSolved)
	{
	  IDownloader downloader = ic.getDownloader();
  
	  downloader.requestImage(_textureURL, 1000000, new TextureDownloadListener(this), true);
	}
  }

//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private Vector2D render_textureTranslation = new Vector2D(0.0, 0.0);
//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private Vector2D render_textureScale = new Vector2D(1.0, 1.0);
  public final void render(RenderContext rc, double minDistanceToCamera)
  {
	final Camera camera = rc.getCurrentCamera();
	final Planet planet = rc.getPlanet();
  
	final Vector3D cameraPosition = camera.getCartesianPosition();
	final Vector3D markPosition = getCartesianPosition(planet);
  
	final Vector3D markCameraVector = markPosition.sub(cameraPosition);
	//  const double distanceToCamera = markCameraVector.length();
	//  const bool renderMark = distanceToCamera <= minDistanceToCamera;
	final boolean renderMark = true;
  
	if (renderMark)
	{
	  final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
  
	  if (normalAtMarkPosition.angleBetween(markCameraVector).radians() > IMathUtils.instance().halfPi())
	  {
		GL gl = rc.getGL();
  
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//		static Vector2D textureTranslation(0.0, 0.0);
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//		static Vector2D textureScale(1.0, 1.0);
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
  
			rc.getFactory().deleteImage(_textureImage);
			_textureImage = null;
		  }
		}
  
		if (_textureId != null)
		{
		  gl.drawBillBoard(_textureId, getVertices(planet), camera.getViewPortRatio());
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


  public final void onTextureDownloadError()
  {
	_textureSolved = true;
  
	ILogger.instance().logError("Can't load image \"%s\"", _textureURL.getPath());
  }

  public final void onTextureDownload(IImage image)
  {
	_textureSolved = true;
	_textureImage = image.shallowCopy();
  }

}