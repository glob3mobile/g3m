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

  void drawRoundedRectangle(float x, float y,
                            float width, float height,
                            float radius,
                            CGPathDrawingMode mode);

  UIFont* createUIFont(const GFont& font);

  const Vector2F textExtent(const std::string& text,
                            UIFont* uiFont);

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


  void _fillRectangle(float x, float y,
                      float width, float height);

  void _strokeRectangle(float x, float y,
                        float width, float height);

  void _fillAndStrokeRectangle(float x, float y,
                               float width, float height);


  void _fillRoundedRectangle(float x, float y,
                             float width, float height,
                             float radius);

  void _strokeRoundedRectangle(float x, float y,
                               float width, float height,
                               float radius);

  void _fillAndStrokeRoundedRectangle(float x, float y,
                                      float width, float height,
                                      float radius);


  void _createImage(IImageListener* listener,
                    bool autodelete);

  void _setFont(const GFont& font);

  const Vector2F _textExtent(const std::string& text);

  void _fillText(const std::string& text,
                 float x, float y);


public:
  Canvas_iOS() :
  _context(NULL),
  _currentUIFont(nil)
  {
  }

  virtual ~Canvas_iOS();
  
};

#endif
