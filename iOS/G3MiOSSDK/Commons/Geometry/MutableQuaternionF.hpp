//
//  MutableQuaternionF.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/17.
//
//

#ifndef MutableQuaternionF_hpp
#define MutableQuaternionF_hpp

class MutableQuaternionF {
private:
  float _x;
  float _y;
  float _z;
  float _w;

  mutable MutableQuaternionF* _temp;

public:
  MutableQuaternionF();

  MutableQuaternionF(float x, float y, float z, float w);

  ~MutableQuaternionF();

  void setXYZW(float x, float y, float z, float w);
  void setX(float x);
  void setY(float y);
  void setZ(float z);
  void setW(float w);

  float getX() const;
  float getY() const;
  float getZ() const;
  float getW() const;

  void copyFrom(const MutableQuaternionF& that);

  float dot(const MutableQuaternionF& that) const;

  void multiplyBy(const MutableQuaternionF& that,
                  MutableQuaternionF& output) const;

  void slerp(const MutableQuaternionF& that,
             MutableQuaternionF& output,
             float t) const;
  
};

#endif
