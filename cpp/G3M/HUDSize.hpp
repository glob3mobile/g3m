//
//  HUDSize.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//

#ifndef __G3M__HUDSize__
#define __G3M__HUDSize__

class HUDSize {
protected:
  HUDSize() {

  }

public:
  virtual float getSize(int viewPortWidth,
                        int viewPortHeight,
                        int bitmapWidth,
                        int bitmapHeight) const = 0;

  virtual ~HUDSize() {
  }

};

#endif
