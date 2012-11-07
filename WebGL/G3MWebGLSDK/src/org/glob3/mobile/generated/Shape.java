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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableMatrix44D;

public abstract class Shape
{
  private Geodetic3D _position;

  private Angle _heading;
  private Angle _pitch;

  private double _scaleX;
  private double _scaleY;
  private double _scaleZ;


  private MutableMatrix44D _transformMatrix;
  private MutableMatrix44D createTransformMatrix(Planet planet)
  {
  //  const MutableMatrix44D geodeticTranslation = MutableMatrix44D::createTranslationMatrix( planet->toCartesian(*_position) );
  //  const MutableMatrix44D geodeticRotation    = MutableMatrix44D::createGeodeticRotationMatrix(*_position);
  //  const MutableMatrix44D geodeticTransform   = geodeticTranslation.multiply(geodeticRotation);
  
	final MutableMatrix44D geodeticTransform = planet.createGeodeticTransformMatrix(_position);
  
  
	final MutableMatrix44D headingRotation = MutableMatrix44D.createRotationMatrix(_heading, Vector3D.downZ());
	final MutableMatrix44D pitchRotation = MutableMatrix44D.createRotationMatrix(_pitch, Vector3D.upX());
	final MutableMatrix44D scale = MutableMatrix44D.createScaleMatrix(_scaleX, _scaleY, _scaleZ);
	final MutableMatrix44D localTransform = headingRotation.multiply(pitchRotation).multiply(scale);
  
  
	return new MutableMatrix44D(geodeticTransform.multiply(localTransform));
  }
  private MutableMatrix44D getTransformMatrix(Planet planet)
  {
	if (_transformMatrix == null)
	{
	  _transformMatrix = createTransformMatrix(planet);
	}
	return _transformMatrix;
  }

  protected void cleanTransformMatrix()
  {
	if (_transformMatrix != null)
		_transformMatrix.dispose();
	_transformMatrix = null;
  }

  public Shape(Geodetic3D position)
  {
	  _position = new Geodetic3D(position);
	  _heading = new Angle(Angle.zero());
	  _pitch = new Angle(Angle.zero());
	  _scaleX = 1;
	  _scaleY = 1;
	  _scaleZ = 1;
	  _transformMatrix = null;

  }

  public void dispose()
  {
	if (_position != null)
		_position.dispose();
	if (_heading != null)
		_heading.dispose();
	if (_pitch != null)
		_pitch.dispose();
	if (_transformMatrix != null)
		_transformMatrix.dispose();
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
	cleanTransformMatrix();
  }

  public final void setHeading(Angle heading)
  {
	if (_heading != null)
		_heading.dispose();
	_heading = new Angle(heading);
	cleanTransformMatrix();
  }

  public final void setPitch(Angle pitch)
  {
	if (_pitch != null)
		_pitch.dispose();
	_pitch = new Angle(pitch);
	cleanTransformMatrix();
  }

  public final void render(RenderContext rc)
  {
	if (isReadyToRender(rc))
	{
	  GL gl = rc.getGL();
  
	  gl.pushMatrix();
  
	  final Planet planet = rc.getPlanet();
  
	  gl.multMatrixf(getTransformMatrix(planet));
  
	  rawRender(rc);
  
	  gl.popMatrix();
	}
  
  }

  public abstract boolean isReadyToRender(RenderContext rc);

  public abstract void rawRender(RenderContext rc);

  public final void setScale(double scaleX, double scaleY, double scaleZ)
  {
	_scaleX = scaleX;
	_scaleY = scaleY;
	_scaleZ = scaleZ;

	cleanTransformMatrix();
  }

  public final void setScale(Vector3D scale)
  {
	setScale(scale._x, scale._y, scale._z);
  }

}