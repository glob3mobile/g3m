//
//  HUDSize.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//

#ifndef __G3MiOSSDK__HUDSize__
#define __G3MiOSSDK__HUDSize__

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
