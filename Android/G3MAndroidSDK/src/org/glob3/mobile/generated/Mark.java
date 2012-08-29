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




public class Mark
{
  private final String _name;
  private final String _textureFilename;
  private final Geodetic3D _position ;

  private GLTextureId _textureId = new GLTextureId();

  public Mark(String name, String textureFilename, Geodetic3D position)
  {
	  _name = name;
	  _textureFilename = textureFilename;
	  _position = new Geodetic3D(position);
	  _textureId = new GLTextureId(-1);

  }

  public void dispose()
  {
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
	final Vector3D markPosition = planet.toCartesian(_position);
  
	final Vector3D markCameraVector = markPosition.sub(cameraPosition);
	final double distanceToCamera = markCameraVector.length();
  
	if (distanceToCamera <= minDistanceToCamera || true)
	{
	  final Vector3D normalAtMarkPosition = planet.geodeticSurfaceNormal(markPosition);
  
	  if (normalAtMarkPosition.angleBetween(markCameraVector).radians() > Math.PI / 2)
	  {
		GL gl = rc.getGL();
  
		Vector2D tr = new Vector2D(0.0,0.0);
		Vector2D scale = new Vector2D(1.0,1.0);
		gl.transformTexCoords(scale, tr);
  
		if (!_textureId.isValid())
		{
		  _textureId = rc.getTexturesHandler().getGLTextureIdFromFileName(_textureFilename, 128, 128, false);
		}
  
		if (!_textureId.isValid())
		{
		  rc.getLogger().logError("Can't load file %s", _textureFilename);
		  return;
		}
  
  //    rc->getLogger()->logInfo(" Visible   << %f %f", minDist, distanceToCamera);
		gl.drawBillBoard(_textureId, markPosition, camera.getViewPortRatio());
	  }
  
	}
  
  }

}