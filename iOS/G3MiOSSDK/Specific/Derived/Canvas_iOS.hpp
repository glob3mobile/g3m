//
//  Canvas_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#ifndef __G3MiOSSDK__Canvas_iOS__
#define __G3MiOSSDK__Canvas_iOS__

#include "ICanvas.hpp"

#import <UIKit/UIKit.h>

class Canvas_iOS : public ICanvas {
private:
  CGContextRef _context;
  UIFont*      _currentUIFont;
  
  CGColorRef createCGColor(const Color& color);
  
  void drawRoundedRectangle(float left, float top,
                            float width, float height,
                            float radius,
                            CGPathDrawingMode mode);
  
  UIFont* createUIFont(const GFont& font);
  
  void tryToSetCurrentFontToContext();
  
protected:
  void _initialize(int width, int height);
  
  
  void _setFillColor(const Color& color);
  
  void _setStrokeColor(const Color& color);
  
  void _setStrokeWidth(float width);
  
  
  void _setShadow(const Color& color,
                  float blur,
                  float offsetX,
                  float offsetY);
  
  void _removeShadow();
  
  
  void _fillRectangle(float left, float top,
                      float width, float height);
  
  void _strokeRectangle(float left, float top,
                        float width, float height);
  
  void _fillAndStrokeRectangle(float left, float top,
                               float width, float height);
  
  
  void _fillRoundedRectangle(float left, float top,
                             float width, float height,
                             float radius);
  
  void _strokeRoundedRectangle(float left, float top,
                               float width, float height,
                               float radius);
  
  void _fillAndStrokeRoundedRectangle(float left, float top,
                                      float width, float height,
                                      float radius);
  
  
  void _createImage(IImageListener* listener,
                    bool autodelete);
  
  void _setFont(const GFont& font);
  
  const Vector2F _textExtent(const std::string& text);
  
  void _fillText(const std::string& text,
                 float left, float top);
  
  void _drawImage(const IImage* image,
                  float destLeft, float destTop);
  
  void _drawImage(const IImage* image,
                  float destLeft, float destTop, float destWidth, float destHeight);
  
  void _drawImage(const IImage* image,
                  float srcLeft, float srcTop, float srcWidth, float srcHeight,
                  float destLeft, float destTop, float destWidth, float destHeight);
  
  
public:
  Canvas_iOS() :
  _context(NULL),
  _currentUIFont(nil)
  {
  }
  
  virtual ~Canvas_iOS();
  
};

#endif
