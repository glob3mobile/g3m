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
  protected Geodetic3D _position;
  protected Angle _heading;
  protected Angle _pitch;


  protected MutableMatrix44D _transformationMatrix;
  protected final MutableMatrix44D createTransformationMatrix(Planet planet)
  {
	final MutableMatrix44D geodeticTranslation = MutableMatrix44D.createTranslationMatrix(planet.toCartesian(_position));
	final MutableMatrix44D geodeticRotation = planet.orientationMatrix(_position);
	final MutableMatrix44D geodeticTransform = geodeticTranslation.multiply(geodeticRotation);
  
	final MutableMatrix44D headingRotation = MutableMatrix44D.createRotationMatrix(_heading, Vector3D.downZ());
	final MutableMatrix44D pitchRotation = MutableMatrix44D.createRotationMatrix(_pitch, Vector3D.upX());
	final MutableMatrix44D localTransform = headingRotation.multiply(pitchRotation);
  
	return new MutableMatrix44D(geodeticTransform.multiply(localTransform));
  }
  protected final MutableMatrix44D getTransformMatrix(Planet planet)
  {
	if (_transformationMatrix == null)
	{
	  _transformationMatrix = createTransformationMatrix(planet);
	}
	return _transformationMatrix;
  }

  protected void cleanTransformationMatrix()
  {
	if (_transformationMatrix != null)
		_transformationMatrix.dispose();
	_transformationMatrix = null;
  }

  public Shape(Geodetic3D position)
  {
	  _position = new Geodetic3D(position);
	  _heading = new Angle(Angle.zero());
	  _pitch = new Angle(Angle.zero());
	  _transformationMatrix = null;

  }

  public void dispose()
  {
	if (_position != null)
		_position.dispose();
	if (_heading != null)
		_heading.dispose();
	if (_pitch != null)
		_pitch.dispose();
	if (_transformationMatrix != null)
		_transformationMatrix.dispose();
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
	cleanTransformationMatrix();
  }

  public final void setHeading(Angle heading)
  {
	if (_heading != null)
		_heading.dispose();
	_heading = new Angle(heading);
	cleanTransformationMatrix();
  }

  public final void setPitch(Angle pitch)
  {
	if (_pitch != null)
		_pitch.dispose();
	_pitch = new Angle(pitch);
	cleanTransformationMatrix();
  }

  public final void render(RenderContext rc)
  {
	int __diego_at_work;
	if (isReadyToRender(rc))
	{
	  GL gl = rc.getGL();
  
	  gl.pushMatrix();
  
	  final Planet planet = rc.getPlanet();
  
  //    const MutableMatrix44D geodeticTranslation = MutableMatrix44D::createTranslationMatrix( planet->toCartesian(*_position) );
  //    const MutableMatrix44D geodeticRotation    = planet->orientationMatrix(*_position);
  //    const MutableMatrix44D geodeticTransform   = geodeticTranslation.multiply(geodeticRotation);
  //
  //    const MutableMatrix44D headingRotation  = MutableMatrix44D::createRotationMatrix(*_heading, Vector3D::downZ());
  //    const MutableMatrix44D pitchRotation    = MutableMatrix44D::createRotationMatrix(*_pitch, Vector3D::upX());
  //    const MutableMatrix44D localTransform   = headingRotation.multiply(pitchRotation);
  //
  //    gl->multMatrixf(geodeticTransform.multiply(localTransform));
  
	  gl.multMatrixf(getTransformMatrix(planet));
  
	  rawRender(rc);
  
	  gl.popMatrix();
	}
  
  }

  public abstract boolean isReadyToRender(RenderContext rc);

  public abstract void rawRender(RenderContext rc);

}