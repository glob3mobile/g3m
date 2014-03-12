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

  CGMutablePathRef _path;
  CGAffineTransform _transform;

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

  void _setLineColor(const Color& color);

  void _setLineWidth(float width);

  void _setLineCap(StrokeCap cap);

  void _setLineJoin(StrokeJoin join);

  void _setLineMiterLimit(float limit);

  void _setLineDash(float lengths[],
                    int count,
                    int phase);

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

  void _drawImage(const IImage* image,
                  float srcLeft, float srcTop, float srcWidth, float srcHeight,
                  float destLeft, float destTop, float destWidth, float destHeight,
                  float transparency);


  void _beginPath();

  void _closePath();

  void _stroke();

  void _fill();

  void _fillAndStroke();

  void _moveTo(float x, float y);

  void _lineTo(float x, float y);


public:
  Canvas_iOS() :
  _context(NULL),
  _currentUIFont(nil),
  _path(NULL),
  _transform()
  {
  }

  virtual ~Canvas_iOS();
  
};

#endif
