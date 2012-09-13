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
//class IFloatBuffer;

public class Mark
{
  private final String _name;
  private final String _textureFilename;
  private final Geodetic3D _position ;

  private GLTextureId _textureId = new GLTextureId();

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
  
	  FloatBufferBuilderFromCartesian3D vertex = new FloatBufferBuilderFromCartesian3D(CenterStrategy.NoCenter, Vector3D.zero());
	  vertex.add(pos);
	  vertex.add(pos);
	  vertex.add(pos);
	  vertex.add(pos);
  
	  _vertices = vertex.create();
	}
	return _vertices;
  }

  public Mark(String name, String textureFilename, Geodetic3D position)
  {
	  _name = name;
	  _textureFilename = textureFilename;
	  _position = new Geodetic3D(position);
	  _textureId = new GLTextureId(GLTextureId.invalid());
	  _cartesianPosition = null;
	  _vertices = null;

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

  public final void render(RenderContext rc, double minDistanceToCamera)
  {
	final Camera camera = rc.getCurrentCamera();
	final Planet planet = rc.getPlanet();
  
	final Vector3D cameraPosition = camera.getCartesianPosition();
	//  const Vector3D markPosition = planet->toCartesian(_position);
	final Vector3D markPosition = getCartesianPosition(planet);
  
	final Vector3D markCameraVector = markPosition.sub(cameraPosition);
	final double distanceToCamera = markCameraVector.length();
  
	if (distanceToCamera <= minDistanceToCamera || true)
	{
	  final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
  
	  if (normalAtMarkPosition.angleBetween(markCameraVector).radians() > IMathUtils.instance().pi() / 2)
	  {
		GL gl = rc.getGL();
  
		Vector2D tr = new Vector2D(0.0,0.0);
		Vector2D scale = new Vector2D(1.0,1.0);
		gl.transformTexCoords(scale, tr);
  
		if (!_textureId.isValid())
		{
		  IImage image = rc.getFactory().createImageFromFileName(_textureFilename);
  
		  _textureId = rc.getTexturesHandler().getGLTextureId(image, GLFormat.RGBA, _textureFilename, false);
  
		  rc.getFactory().deleteImage(image);
		}
  
		if (!_textureId.isValid())
		{
		  rc.getLogger().logError("Can't load file %s", _textureFilename);
		  return;
		}
  
		//    rc->getLogger()->logInfo(" Visible   << %f %f", minDist, distanceToCamera);
		gl.drawBillBoard(_textureId, getVertices(planet), camera.getViewPortRatio());
	  }
  
	}
  
  }

}