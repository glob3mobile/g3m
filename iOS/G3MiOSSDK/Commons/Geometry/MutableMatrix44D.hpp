//
//  Mat4.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Mat4_h
#define G3MiOSSDK_Mat4_h

// class to keep a 4x4 matrix
class MutableMatrix44D {
public:
  
  MutableMatrix44D(){
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
  
  float get(int i) const { return _m[i];}
  
  const double* getMatrix() const { return _m;}
  
  void copyToFloatMatrix(float M[16]) const { 
    for (int i = 0; i < 16; i++) { M[i] = _m[i];}
  }
  
private:
  double _m[16];
  
  static bool invert_matrix(const double m[16], double out[16]);
};


#endif
