//
//  Mat4.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Mat4_h
#define G3MiOSSDK_Mat4_h

class Vector3D;
class Angle;
class MutableVector3D;


// class to keep a 4x4 matrix
class MutableMatrix44D {
private:
  double _m[16];
    
  static bool invert_matrix(const double m[16], double out[16]);
  
  void transformPoint(double out[4], const double in[4]);
  
  
public:
  
  MutableMatrix44D() {
    for (int i = 0; i < 16; i++) { _m[i] = 0.0;}
  }
  
  MutableMatrix44D(const MutableMatrix44D &m){
    for (int i = 0; i < 16; i++) { _m[i] = m._m[i];}
  }
  
  MutableMatrix44D(const double M[16]){
    for (int i = 0; i < 16; i++) { _m[i] = M[i];}
  }
  
  MutableMatrix44D(const float M[16]){
    for (int i = 0; i < 16; i++) { _m[i] = M[i];}
  }
  
  MutableMatrix44D multMatrix(const MutableMatrix44D& m) const;
  
  MutableMatrix44D inverse() const;
  
  double get(int i) const { return _m[i];}
    
  //const double * getMatrix() const { return _m;}
  
  void copyToFloatMatrix(float M[16]) const { 
    for (int i = 0; i < 16; i++) { M[i] = (float) _m[i];}
  }
  
  void print() const;
  
  Vector3D unproject(const Vector3D& pixel3D, const int viewport[4]) const;
  
  static MutableMatrix44D createTranslationMatrix(const Vector3D& t);
  
  static MutableMatrix44D createRotationMatrix(const Angle& angle, const Vector3D& p);
  
  static MutableMatrix44D createModelMatrix(const MutableVector3D& pos, 
                                            const MutableVector3D& center,
                                            const MutableVector3D& up);
  
  static MutableMatrix44D createProjectionMatrix(double left, double right, double bottom, double top, 
                                                 double near, double far);
  
};


#endif
