//
//  DefaultChessCanvasImageFactory.hpp
//  G3M
//
//  Created by Vidal Toboso on 21/08/14.
//
//

#ifndef __G3M__DefaultChessCanvasImageFactory__
#define __G3M__DefaultChessCanvasImageFactory__

class Color;
class G3MContext;
class ICanvas;

#include "CanvasImageFactory.hpp"
#include "Color.hpp"

class DefaultChessCanvasImageFactory : public CanvasImageFactory {
  
private:
  const Color _backgroundColor;
  const Color _boxColor;
  const int _splits;
  
protected:
  ~DefaultChessCanvasImageFactory() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
  void buildOnCanvas(const G3MContext* context,
                     ICanvas* canvas);
  
  const std::string getImageName(const G3MContext* context) const;
  
public:
  DefaultChessCanvasImageFactory(int width,
                                 int height,
                                 const Color& backgroundColor,
                                 const Color& boxColor,
                                 int splits);
  
  bool isMutable() const {
    return false;
  }
  
};

#endif
