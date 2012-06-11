/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef CAMERA
#define CAMERA

#include "MutableVector3D.hpp"

#include "Context.hpp"
#include "IFactory.hpp"

#include "Geodetic3D.hpp"

#include "Vector2D.hpp"

#include "MutableMatrix44D.hpp"

#define AUTO_DRAG_FRICTION              0.985
#define AUTO_DRAG_MIN                   1e-7
#define AUTO_ZOOM_FRICTION              0.850
#define AUTO_ZOOM_MIN                   1e-7


/**
 * Class to control the camera.
 */
class Camera{
  
public:
  
  Camera(const Camera &c);
  
  Camera(int width, int height);
  
  void copyFrom(const Camera &c);
  
  void resizeViewport(int width, int height);
  
  void draw(const RenderContext &rc);
  
  Vector3D pixel2Vector(const Vector2D& pixel) const;
  
  int getWidth() const{ return _width; }
  int getHeight() const{ return _height; }
  Vector3D getPos() const { return _pos.asVector3D(); }
  Vector3D getCenter() const { return _center.asVector3D(); }
  Vector3D getUp() const { return _up.asVector3D(); }
  
  //Dragging camera
  void dragCamera(const Vector3D& p0, const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis, const Angle& delta);
  
  //Zoom
  void zoom(double factor);
  
  //Pivot
  void pivotOnCenter(const Angle& a);
  
  //Rotate
  void rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta);
  
  
  void print() const;

private:
  int _width, _height;
  
  int _viewport[4];
  
  MutableMatrix44D _model;              // Model matrix, computed in CPU in double precision
  MutableMatrix44D _projection;        // opengl projection matrix
  
  MutableVector3D _pos;             // position
  MutableVector3D _center;          // center of view
  MutableVector3D _up;              // vertical vector
  
  void applyTransform(const MutableMatrix44D& mat);
  
};

#endif
