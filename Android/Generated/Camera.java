package org.glob3.mobile.generated; 
/*
*  Camera.cpp
*  Prueba Opengl iPad
*
*  Created by AgustÃ­n Trujillo Pino on 24/01/11.
*  Copyright 2011 Universidad de Las Palmas. All rights reserved.
*
*/




/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */







//#define AUTO_DRAG_FRICTION 0.985
//#define AUTO_DRAG_MIN 1e-7
//#define AUTO_ZOOM_FRICTION 0.850
//#define AUTO_ZOOM_MIN 1e-7


/**
 * Class to control the camera.
 */
public class Camera
{


  public Camera(Camera c)
  {
	  _pos = new MutableVector3D(c._pos);
	  _center = new MutableVector3D(c._center);
	  _up = new MutableVector3D(_up);
	  _lookAt = new MutableMatrix44D(c._lookAt);
	  _projection = new MutableMatrix44D(c._projection);
	  _rotationAxis = new MutableVector3D(c._rotationAxis);
	  _rotationDelta = c._rotationDelta;
	  _zoomFactor = c._zoomFactor;
	resizeViewport(c.getWidth(), c.getHeight());
  }

  public Camera(int width, int height)
  {
	  _pos = new MutableVector3D(63650000.0, 0.0, 0.0);
	  _center = new MutableVector3D(0.0, 0.0, 0.0);
	  _up = new MutableVector3D(0.0, 0.0, 1.0);
	  _rotationAxis = new MutableVector3D(0.0, 0.0, 0.0);
	  _rotationDelta = 0.0;
	  _zoomFactor = 1.0;
	resizeViewport(width, height);
  }

  public final void copyFrom(Camera c)
  {
	_pos = c.getPos();
	_center = c.getCenter();
	_up = c.getUp();
	_lookAt = c._lookAt;
	_projection = c._projection;
	_rotationAxis = c._rotationAxis;
	_rotationDelta = c._rotationDelta;
	_zoomFactor = c._zoomFactor;
  }

  public final void resizeViewport(int width, int height)
  {
	_width = width;
	_height = height;
  
	  _viewport[0] = _viewport[1] = 0;
	  _viewport[2] = width;
	  _viewport[3] = height;
  }

  public final void draw(RenderContext rc)
  {
	  double znear;
  
	  // update znear
	  //double height = GetPosGeo3D().height();
	double height = _pos.length();
  
	  if (height > 1273000.0)
		  znear = 636500.0;
	  else if (height > 12730.0)
		  znear = 6365.0;
	  else if (height > 3182.5)
		  znear = 63.65;
	  else
		  znear = 19.095;
  
	  // compute projection matrix
	  double ratioScreen = (double) _viewport[3] / _viewport[2];
	  _projection = GLU.projectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear);
  
	//_projection.print();
  
	  // obtaing gl object reference
	  IGL gl = rc.getGL();
	  gl.setProjection(_projection);
  
	  // make the lookat
	  _lookAt = GLU.lookAtMatrix(_pos, _center, _up);
	  gl.loadMatrixf(_lookAt);
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D pixel2Vector(const Vector2D& pixel) const
  public final Vector3D pixel2Vector(Vector2D pixel)
  {
	double py = (int) pixel.y();
	double px = (int) pixel.x();
  
	py = _viewport[3] - py;
	Vector3D obj = GLU.unproject(px, py, 0, _lookAt, _projection, _viewport);
	if (obj == null)
		return new Vector3D(0.0,0.0,0.0);
  
	Vector3D v = obj.sub(_pos);
	if (obj != null)
		obj.dispose();
	return v;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	  return _width;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	  return _height;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D getPos() const
  public final MutableVector3D getPos()
  {
	  return _pos;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D getCenter() const
  public final MutableVector3D getCenter()
  {
	  return _center;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D getUp() const
  public final MutableVector3D getUp()
  {
	  return _up;
  }

  //Dragging camera
  public final void dragCamera(Vector3D p0, Vector3D p1)
  {
	// compute the rotation axe
	_rotationAxis = p0.cross(p1);
  
	// compute the angle
	_rotationDelta = - Math.acos(p0.normalized().dot(p1.normalized()));
	if (isnan(_rotationDelta))
		return;
  
	dragCamera(_rotationAxis, _rotationDelta);
  
	//Inertia
	_rotationDelta /= 10.0; //Rotate much less with inertia
	if (Math.abs(_rotationDelta) < DefineConstants.AUTO_DRAG_MIN * 3.0)
		_rotationDelta = 0.0;
  }
  public final void dragCamera(Vector3D axis, double delta)
  {
	// update the camera
	MutableMatrix44D rot = GLU.rotationMatrix(Angle.fromRadians(delta), axis);
	applyTransform(rot);
  }

  //Zoom
  public final void zoom(double factor)
  {
  
	if (factor != 1.0)
	{
	  Vector3D w = _pos.sub(_center);
	  _pos = _center.add(w.times(factor));
	  _zoomFactor = factor;
	}
  }

  //Camera inertia
  public final void applyInertia()
  {
	//DRAGGING
	if (Math.abs(_rotationDelta) > DefineConstants.AUTO_DRAG_MIN)
	{
	  _rotationDelta *= DefineConstants.AUTO_DRAG_FRICTION;
	  dragCamera(_rotationAxis, _rotationDelta);
	}
	else
	{
	  _rotationDelta = 0.0;
	}
  
	//ZOOMING
	if ((Math.abs(_zoomFactor) - 1.0) > DefineConstants.AUTO_ZOOM_MIN)
	{
	  _zoomFactor = (_zoomFactor - 1.0) *DefineConstants.AUTO_ZOOM_FRICTION +1.0;
	  zoom(_zoomFactor);
	}
  
  }
  public final void stopInertia()
  {
	_rotationDelta = 0.0;
	_zoomFactor = 0.0;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print() const
  public final void print()
  {
	System.out.print("LOOKAT: \n");
	_lookAt.print();
	System.out.print("\n");
	System.out.print("PROJECTION: \n");
	_projection.print();
	System.out.print("\n");
	System.out.print("VIEWPORT: \n");
	for (int k = 0; k < 4; k++)
		System.out.printf("%d ", _viewport[k]);
	System.out.print("\n\n");
  }

  private int _width;
  private int _height;

  private int[] _viewport = new int[4];

  private MutableMatrix44D _lookAt = new MutableMatrix44D(); // gluLookAt matrix, computed in CPU in double precision
  private MutableMatrix44D _projection = new MutableMatrix44D(); // opengl projection matrix

  private MutableVector3D _pos = new MutableVector3D(); // position
  private MutableVector3D _center = new MutableVector3D(); // center of view
  private MutableVector3D _up = new MutableVector3D(); // vertical vector

  //Camera Inertia
  private MutableVector3D _rotationAxis = new MutableVector3D(); // Rotation Axis
  private double _rotationDelta; // Rotation Delta
  private double _zoomFactor;

  private void applyTransform(MutableMatrix44D M)
  {
	_pos = _pos.applyTransform(M);
	_center = _center.applyTransform(M);
	_up = _up.applyTransform(M);
  }

}