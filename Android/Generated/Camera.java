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
	  _up = new MutableVector3D(c._up);
	  _model = new MutableMatrix44D(c._model);
	  _projection = new MutableMatrix44D(c._projection);
	resizeViewport(c.getWidth(), c.getHeight());
  }

  public Camera(int width, int height)
  {
	  _pos = new MutableVector3D(63650000.0, 0.0, 0.0);
	  _center = new MutableVector3D(0.0, 0.0, 0.0);
	  _up = new MutableVector3D(0.0, 0.0, 1.0);
	resizeViewport(width, height);
  }

  public final void copyFrom(Camera c)
  {
	_pos = c._pos;
	_center = c._center;
	_up = c._up;
	_model = c._model;
	_projection = c._projection;
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
	_projection = MutableMatrix44D.createProjectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear);
  
	// obtaing gl object reference
	IGL gl = rc.getGL();
	gl.setProjection(_projection);
  
	// make the model
	_model = MutableMatrix44D.createModelMatrix(_pos, _center, _up);
	gl.loadMatrixf(_model);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D pixel2Vector(const Vector2D& pixel) const
  public final Vector3D pixel2Vector(Vector2D pixel)
  {
	double py = (int) pixel.y();
	double px = (int) pixel.x();
	py = _viewport[3] - py;
	Vector3D pixel3D = new Vector3D(px, py, 0);
  
	MutableMatrix44D modelView = _projection.multMatrix(_model);
	Vector3D obj = modelView.unproject(pixel3D, _viewport);
	if (obj.isNan())
		return obj;
  
	Vector3D v = obj.sub(_pos.asVector3D());
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
//ORIGINAL LINE: Vector3D getPos() const
  public final Vector3D getPos()
  {
	  return _pos.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCenter() const
  public final Vector3D getCenter()
  {
	  return _center.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getUp() const
  public final Vector3D getUp()
  {
	  return _up.asVector3D();
  }

  //Dragging camera
  public final void dragCamera(Vector3D p0, Vector3D p1)
  {
	// compute the rotation axe
	Vector3D rotationAxis = p0.cross(p1);
  
	// compute the angle
	Angle rotationDelta = Angle.fromRadians(- Math.acos(p0.normalized().dot(p1.normalized())));
  
	//if (isnan(rotationDelta.radians())) return;
	if (rotationDelta.isNan())
		return;
  
	rotateWithAxis(rotationAxis, rotationDelta);
  }
  public final void rotateWithAxis(Vector3D axis, Angle delta)
  {
	// update the camera
	MutableMatrix44D rot = MutableMatrix44D.createRotationMatrix(delta, axis);
	applyTransform(rot);
  }

  //Zoom
  public final void zoom(double factor)
  {
	if (factor != 1.0)
	{
	  MutableVector3D w = _pos.sub(_center);
	  _pos = _center.add(w.times(factor));
	}
  }

  //Pivot
  public final void pivotOnCenter(Angle a)
  {
	Vector3D rotationAxis = _pos.sub(_center).asVector3D();
	rotateWithAxis(rotationAxis, a);
  }

  //Rotate
  //void rotate(const Vector3D& axis, double angle)


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print() const
  public final void print()
  {
	System.out.print("MODEL: \n");
	_model.print();
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

  private MutableMatrix44D _model = new MutableMatrix44D(); // Model matrix, computed in CPU in double precision
  private MutableMatrix44D _projection = new MutableMatrix44D(); // opengl projection matrix

  private MutableVector3D _pos = new MutableVector3D(); // position
  private MutableVector3D _center = new MutableVector3D(); // center of view
  private MutableVector3D _up = new MutableVector3D(); // vertical vector

  private void applyTransform(MutableMatrix44D M)
  {
	_pos = _pos.applyTransform(M);
	_center = _center.applyTransform(M);
	_up = _up.applyTransform(M);
  }

}