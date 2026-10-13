//
//  CircleImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

#ifndef __G3M__CircleImageFactory__
#define __G3M__CircleImageFactory__


class Color;
class G3MContext;
class ICanvas;

#include "CanvasImageFactory.hpp"
#include "Color.hpp"

class CircleImageFactory : public CanvasImageFactory {
  
private:
  const Color _color;
  const int   _radius;
  
protected:
  ~CircleImageFactory() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
  void buildOnCanvas(const G3MContext* context,
                     ICanvas* canvas);
  
  const std::string getImageName(const G3MContext* context) const;
  
public:
  CircleImageFactory(const Color& color,
                     int radius);
  
  bool isMutable() const {
    return false;
  }
  
};

#endif
