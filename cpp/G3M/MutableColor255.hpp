//
//  MutableColor255.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

#ifndef MutableColor255_hpp
#define MutableColor255_hpp

class MutableColor255 {
public:
  unsigned char _red;
  unsigned char _green;
  unsigned char _blue;
  unsigned char _alpha;

  MutableColor255(const MutableColor255& that):
  _red(that._red),
  _green(that._green),
  _blue(that._blue),
  _alpha(that._alpha)
  {
  }

  MutableColor255(unsigned char red,
                  unsigned char green,
                  unsigned char blue,
                  unsigned char alpha):
  _red(red),
  _green(green),
  _blue(blue),
  _alpha(alpha)
  {
  }

  ~MutableColor255() {

  }

};

#endif
