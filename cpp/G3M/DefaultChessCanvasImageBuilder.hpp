//
//  DefaultChessCanvasImageBuilder.hpp
//  G3M
//
//  Created by Vidal Toboso on 21/08/14.
//
//

#ifndef __G3M__DefaultChessCanvasImageBuilder__
#define __G3M__DefaultChessCanvasImageBuilder__

class Color;
class G3MContext;
class ICanvas;

#include "CanvasImageBuilder.hpp"
#include "Color.hpp"

class DefaultChessCanvasImageBuilder : public CanvasImageBuilder {
  
private:
  const Color _backgroundColor;
  const Color _boxColor;
  const int _splits;
  
protected:
  ~DefaultChessCanvasImageBuilder() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
  void buildOnCanvas(const G3MContext* context,
                     ICanvas* canvas);
  
  const std::string getImageName(const G3MContext* context) const;
  
public:
  DefaultChessCanvasImageBuilder(int width,
                                 int height,
                                 const Color& backgroundColor,
                                 const Color& boxColor,
                                 int splits);
  
  bool isMutable() const {
    return false;
  }
  
};

#endif
