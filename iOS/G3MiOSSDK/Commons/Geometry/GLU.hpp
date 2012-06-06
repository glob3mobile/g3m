/*
 *  GLU.h
 *  Terrenos
 *
 *  Created by Agustín Trujillo Pino on 23/11/10.
 *  Copyright 2010 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef GLU
#define GLU

#include "Vector3D.hpp"

#include "MutableMatrix44D.hpp"

class Glu {
private:

    static void transform_point(double out[4], const double m[16], const double in[4]);
  
public:

  static Vector3D *unproject(double winx, double winy, double winz, 
                           MutableMatrix44D model, MutableMatrix44D proj,
                      const int viewport[4]);

  static MutableMatrix44D translationMatrix(Vector3D t);
  
  static MutableMatrix44D rotationMatrix(double angle_rad, Vector3D p);

  static MutableMatrix44D lookAtMatrix(Vector3D pos, Vector3D center, Vector3D up);

  static MutableMatrix44D projectionMatrix(double left, double right, double bottom, double top,
                                    double near, double far);

};

#endif