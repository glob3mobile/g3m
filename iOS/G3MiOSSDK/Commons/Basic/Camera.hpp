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
  
  int getWidth() const{ return _width;}
  int getHeight() const{ return _height;}
  Vector3D getPos() const { return _pos;}
  Vector3D getCenter() const { return _center;}
  Vector3D getUp() const { return _up;}
  
  void dragCamera(Vector3D p0, Vector3D p1);
  
  void print() const
  {
    printf("LOOKAT: \n");
    for (int k = 0; k < 16; k++) printf("%f ",  _lookAt.get(k) );
    printf("\n");
    printf("PROJECTION: \n");
    for (int k = 0; k < 16; k++) printf("%f ", _projection.get(k) );
    printf("\n");
    printf("VIEWPORT: \n");
    for (int k = 0; k < 4; k++) printf("%d ",  _viewport[k] );
    printf("\n\n");
  }
  
private:
  int _width, _height;
  
  int _viewport[4];
  
  MutableMatrix44D _lookAt;           // gluLookAt matrix, computed in CPU in double precision
  MutableMatrix44D _projection;        // opengl projection matrix
  
  MutableVector3D _pos;             // position
  MutableVector3D _center;          // center of view
  MutableVector3D _up;              // vertical vector
  
  void applyTransform(MutableMatrix44D mat);
  
};

#endif
