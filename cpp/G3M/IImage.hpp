//
//  IImage.hpp
//  G3M
//
//  Created by José Miguel S N on 01/06/12.
//

#ifndef G3M_IImage
#define G3M_IImage

#include "Vector2I.hpp"

class MutableColor255;


class IImage {
public:
  virtual ~IImage() {
  }

  virtual int getWidth() const = 0;
  virtual int getHeight() const = 0;
  virtual const Vector2I getExtent() const = 0;

  virtual const std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  virtual bool isPremultiplied() const = 0;

  virtual IImage* shallowCopy() const = 0;

  virtual void getPixel(int x, int y,
                        MutableColor255& pixel) const = 0;
};

#endif
