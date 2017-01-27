//
//  QuaternionF.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/27/17.
//
//

#ifndef QuaternionF_hpp
#define QuaternionF_hpp

class QuaternionF {
public:
  const float _x;
  const float _y;
  const float _z;
  const float _w;

  QuaternionF(float x,
              float y,
              float z,
              float w);

  ~QuaternionF();

  float dot(const QuaternionF& that) const;

  QuaternionF multiplyBy(const QuaternionF& that) const;

  QuaternionF slerp(const QuaternionF& that,
                    float t) const;
  
};

#endif
