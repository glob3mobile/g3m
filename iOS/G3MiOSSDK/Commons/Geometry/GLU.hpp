/*
 *  GLU.h
 *  Terrenos
 *
 *  Created by Agustín Trujillo Pino on 23/11/10.
 *  Copyright 2010 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef G3MiOSSDK_GLU_hpp
#define G3MiOSSDK_GLU_hpp

#include "Vector3D.hpp"
#include "MutableVector3D.hpp"

#include "MutableMatrix44D.hpp"

#include "Angle.hpp"

class GLU {
private:
  static void transform_point(double out[4], const double m[16], const double in[4]);
  
public:
  
  static Vector3D *unproject(double winx, double winy, double winz, 
                             const MutableMatrix44D& model,
                             const MutableMatrix44D& proj,
                             const int viewport[4]);
  
  static MutableMatrix44D translationMatrix(const Vector3D& t);
  
  static MutableMatrix44D rotationMatrix(const Angle& angle,
                                         const Vector3D& p);
  
  static MutableMatrix44D lookAtMatrix(const MutableVector3D& pos,
                                       const MutableVector3D& center,
                                       const MutableVector3D& up);
  
  static MutableMatrix44D projectionMatrix(double left, double right,
                                           double bottom, double top,
                                           double near, double far);
  
};

#endif
