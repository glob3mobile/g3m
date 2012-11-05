package org.glob3.mobile.generated; 
//
//  Shape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  Shape.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



public abstract class Shape
{
  protected Geodetic3D _position;
  protected Angle _heading;
  protected Angle _pitch;

  public Shape(Geodetic3D position, Angle heading, Angle pitch)
  {
	  _position = new Geodetic3D(position);
	  _heading = new Angle(heading);
	  _pitch = new Angle(pitch);

  }

  public void dispose()
  {
	if (_position != null)
		_position.dispose();
	if (_heading != null)
		_heading.dispose();
	if (_pitch != null)
		_pitch.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getHeading() const
  public final Angle getHeading()
  {
	return _heading;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getPitch() const
  public final Angle getPitch()
  {
	return _pitch;
  }

  public final void setPosition(Geodetic3D position)
  {
	if (_position != null)
		_position.dispose();
	_position = new Geodetic3D(position);
  }

  public final void render(RenderContext rc)
  {
	int __diego_at_work;
	if (isReadyToRender(rc))
	{
	  GL gl = rc.getGL();
  
	  gl.pushMatrix();
  
	  final Planet planet = rc.getPlanet();
  
  
	  final Vector3D cartesianPosition = planet.toCartesian(_position);
	  final MutableMatrix44D translationMatrix = MutableMatrix44D.createTranslationMatrix(cartesianPosition);
	  gl.multMatrixf(translationMatrix);
  
	  final MutableMatrix44D rotationMatrix = planet.orientationMatrix(_position, _heading, _pitch);
	  gl.multMatrixf(rotationMatrix);
  
  
	  rawRender(rc);
  
	  gl.popMatrix();
	}
  
  }

  public abstract boolean isReadyToRender(RenderContext rc);

  public abstract void rawRender(RenderContext rc);

}