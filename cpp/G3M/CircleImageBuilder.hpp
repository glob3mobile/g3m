//
//  CircleImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

#ifndef __G3M__CircleImageBuilder__
#define __G3M__CircleImageBuilder__


class Color;
class G3MContext;
class ICanvas;

#include "CanvasImageBuilder.hpp"
#include "Color.hpp"

class CircleImageBuilder : public CanvasImageBuilder {
  
private:
  const Color _color;
  const int   _radius;
  
protected:
  void buildOnCanvas(const G3MContext* context,
                     ICanvas* canvas);
  
  const std::string getImageName(const G3MContext* context) const;
  
public:
  CircleImageBuilder(const Color& color,
                     int radius);
  
  bool isMutable() const {
    return false;
  }
  
};

#endif
