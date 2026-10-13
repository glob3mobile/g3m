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
private:
  MutableColor255& operator=(const MutableColor255& that);


public:
  // Channels are 0-255 held in int: cpp2java turns unsigned char into Java's signed byte.
  int _red;
  int _green;
  int _blue;
  int _alpha;

  MutableColor255(const MutableColor255& that):
  _red(that._red),
  _green(that._green),
  _blue(that._blue),
  _alpha(that._alpha)
  {
  }

  MutableColor255(int red,
                  int green,
                  int blue,
                  int alpha):
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
