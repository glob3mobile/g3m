package org.glob3.mobile.generated; 
//
//  Shape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  Shape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//


///#include <string>

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableMatrix44D;


public abstract class Shape implements EffectTarget
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
	final MutableMatrix44D geodeticTransform = (_position == null) ? MutableMatrix44D.identity() : planet.createGeodeticTransformMatrix(_position);
  
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

  private Effect _pendingEffect;

  protected void cleanTransformMatrix()
  {
	if (_transformMatrix != null)
		_transformMatrix.dispose();
	_transformMatrix = null;
  }

  public Shape(Geodetic3D position)
  {
	  _position = position;
	  _heading = new Angle(Angle.zero());
	  _pitch = new Angle(Angle.zero());
	  _scaleX = 1;
	  _scaleY = 1;
	  _scaleZ = 1;
	  _transformMatrix = null;
	  _pendingEffect = null;

  }

  public void dispose()
  {
	if (_pendingEffect != null)
		_pendingEffect.dispose();
  
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
//ORIGINAL LINE: const Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle getHeading() const
  public final Angle getHeading()
  {
	return _heading;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle getPitch() const
  public final Angle getPitch()
  {
	return _pitch;
  }

  public final void setPosition(Geodetic3D position)
  {
	if (_position != null)
		_position.dispose();
	_position = position;
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getScale() const
  public final Vector3D getScale()
  {
	return new Vector3D(_scaleX, _scaleY, _scaleZ);
  }

  public final void setAnimatedScale(TimeInterval duration, double scaleX, double scaleY, double scaleZ)
  {
	if (_pendingEffect != null)
		_pendingEffect.dispose();
  
	_pendingEffect = new ShapeScaleEffect(duration, this, _scaleX, _scaleY, _scaleZ, scaleX, scaleY, scaleZ);
  }

  public final void setAnimatedScale(double scaleX, double scaleY, double scaleZ)
  {
	setAnimatedScale(TimeInterval.fromSeconds(1), scaleX, scaleY, scaleZ);
  }

  public final void setAnimatedScale(Vector3D scale)
  {
	setAnimatedScale(scale._x, scale._y, scale._z);
  }

  public final void setAnimatedScale(TimeInterval duration, Vector3D scale)
  {
	setAnimatedScale(duration, scale._x, scale._y, scale._z);
  }

  public final void orbitCamera(TimeInterval duration, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude)
  {
	if (_pendingEffect != null)
		_pendingEffect.dispose();
  
	_pendingEffect = new ShapeOrbitCameraEffect(duration, this, fromDistance, toDistance, fromAzimuth, toAzimuth, fromAltitude, toAltitude);
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
	if (isReadyToRender(rc))
	{
	  if (_pendingEffect != null)
	  {
		EffectsScheduler effectsScheduler = rc.getEffectsScheduler();
  
		effectsScheduler.cancellAllEffectsFor(this);
		effectsScheduler.startEffect(_pendingEffect, this);
  
		_pendingEffect = null;
	  }
  
	  GL gl = rc.getGL();
  
	  gl.pushMatrix();
  
	  gl.multMatrixf(getTransformMatrix(rc.getPlanet()));
  
	  rawRender(rc, parentState);
  
	  gl.popMatrix();
	}
  }

  public void initialize(G3MContext context)
  {

  }

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void rawRender(G3MRenderContext rc, GLState parentState);

  public abstract boolean isTransparent(G3MRenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void unusedMethod() const
  public final void unusedMethod()
  {
  }

}