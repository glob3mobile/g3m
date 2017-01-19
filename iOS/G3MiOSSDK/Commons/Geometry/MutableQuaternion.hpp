//
//  MutableQuaternion.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/17.
//
//

#ifndef MutableQuaternion_hpp
#define MutableQuaternion_hpp

class MutableQuaternion {
private:
  float _x;
  float _y;
  float _z;
  float _w;

  mutable MutableQuaternion* _temp;

public:
  MutableQuaternion();

  MutableQuaternion(float x, float y, float z, float w);

  ~MutableQuaternion();

  void setXYZW(float x, float y, float z, float w);
  void setX(float x);
  void setY(float y);
  void setZ(float z);
  void setW(float w);

  float getX() const;
  float getY() const;
  float getZ() const;
  float getW() const;

  void copyFrom(const MutableQuaternion& that);

  float dot(const MutableQuaternion& that) const;

  void multiplyBy(const MutableQuaternion& that,
                  MutableQuaternion& output) const;

  void slerp(const MutableQuaternion& that,
             MutableQuaternion& output,
             float t) const;
  
};

#endif
