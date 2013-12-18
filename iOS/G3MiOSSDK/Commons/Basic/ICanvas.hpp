//
//  ICanvas.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#ifndef __G3MiOSSDK__ICanvas__
#define __G3MiOSSDK__ICanvas__

class Color;
class IImageListener;
class GFont;
class IImage;
#include <string>

#include "Vector2F.hpp"

enum StrokeCap {
  CAP_BUTT,
  CAP_ROUND,
  CAP_SQUARE
};

enum StrokeJoin {
  JOIN_MITER,
  JOIN_ROUND,
  JOIN_BEVEL
};

class ICanvas {
protected:
  int _canvasWidth;
  int _canvasHeight;
  GFont* _currentFont;

  bool isInitialized() const {
    return (_canvasWidth > 0) && (_canvasHeight > 0);
  }

  inline void checkInitialized() const;
  inline void checkCurrentFont() const;

  virtual void _initialize(int width, int height) = 0;

  virtual void _setFillColor(const Color& color) = 0;

  virtual void _setLineColor(const Color& color) = 0;

  virtual void _setLineWidth(float width) = 0;

  virtual void _setLineCap(StrokeCap cap) = 0;

  virtual void _setLineJoin(StrokeJoin join) = 0;

  virtual void _setLineMiterLimit(float limit) = 0;

  virtual void _setLineDash(float lengths[],
                            int count,
                            int phase) = 0;

  virtual void _fillRectangle(float left, float top,
                              float width, float height) = 0;

  virtual void _strokeRectangle(float left, float top,
                                float width, float height) = 0;

  virtual void _fillAndStrokeRectangle(float left, float top,
                                       float width, float height) = 0;


  virtual void _fillRoundedRectangle(float left, float top,
                                     float width, float height,
                                     float radius) = 0;

  virtual void _strokeRoundedRectangle(float left, float top,
                                       float width, float height,
                                       float radius) = 0;
  virtual void _fillAndStrokeRoundedRectangle(float left, float top,
                                              float width, float height,
                                              float radius) = 0;

  virtual void _setShadow(const Color& color,
                          float blur,
                          float offsetX,
                          float offsetY) = 0;

  virtual void _removeShadow() = 0;


  virtual void _createImage(IImageListener* listener,
                            bool autodelete) = 0;

  virtual void _setFont(const GFont& font) = 0;

  virtual const Vector2F _textExtent(const std::string& text) = 0;

  virtual void _fillText(const std::string& text,
                         float left, float top) = 0;

  virtual void _drawImage(const IImage* image,
                          float destLeft, float destTop) = 0;

  virtual void _drawImage(const IImage* image,
                          float destLeft, float destTop, float destWidth, float destHeight) = 0;

  virtual void _drawImage(const IImage* image,
                          float srcLeft, float srcTop, float srcWidth, float srcHeight,
                          float destLeft, float destTop, float destWidth, float destHeight) = 0;

  virtual void _drawImage(const IImage* image,
                          float srcLeft, float srcTop, float srcWidth, float srcHeight,
                          float destLeft, float destTop, float destWidth, float destHeight,
                          float transparency) = 0;


  virtual void _beginPath() = 0;

  virtual void _closePath() = 0;

  virtual void _stroke() = 0;

  virtual void _fill() = 0;

  virtual void _fillAndStroke() = 0;

  virtual void _moveTo(float x, float y) = 0;

  virtual void _lineTo(float x, float y) = 0;


public:
  ICanvas() :
  _canvasWidth(-1),
  _canvasHeight(-1),
  _currentFont(NULL)
  {
  }

  virtual ~ICanvas();

  /**
   Initialize the Canvas, must be called just one time before calling most methods.
   */
  void initialize(int width, int height);


  /**
   Returns the size of the text if it were to be rendered with the actual font on a single line.

   NOTE: The current font has to be set before calling this method.
   NOTE: No need to initialize the canvas before calling this method.
   */
  const Vector2F textExtent(const std::string& text);

  /**
   Set the actual font.

   NOTE: No need to initialize the canvas before calling this method.
   */
  void setFont(const GFont& font);

  void setFillColor(const Color& color);

  void setLineColor(const Color& color);

  void setLineWidth(float width);

  void setLineCap(StrokeCap cap);

  void setLineJoin(StrokeJoin join);

  void setLineMiterLimit(float limit);

  void setLineDash(float lengths[],
                   int count,
                   int phase);

#ifdef JAVA_CODE
  void setLineDash(float[] lengths,
                   int phase) {
    setLineDash(lengths, lengths.length, phase);
  }
#endif

  void setShadow(const Color& color,
                 float blur,
                 float offsetX,
                 float offsetY);

  void removeShadow();


  void fillRectangle(float left, float top,
                     float width, float height);

  void strokeRectangle(float left, float top,
                       float width, float height);

  void fillAndStrokeRectangle(float left, float top,
                              float width, float height);

  void fillRoundedRectangle(float left, float top,
                            float width, float height,
                            float radius);

  void strokeRoundedRectangle(float left, float top,
                              float width, float height,
                              float radius);

  void fillAndStrokeRoundedRectangle(float left, float top,
                                     float width, float height,
                                     float radius);

  void createImage(IImageListener* listener,
                   bool autodelete);

  void fillText(const std::string& text,
                float left, float top);

  void drawImage(const IImage* image,
                 float destLeft, float destTop);

  void drawImage(const IImage* image,
                 float destLeft, float destTop, float destWidth, float destHeight);

  void drawImage(const IImage* image,
                 float srcLeft, float srcTop, float srcWidth, float srcHeight,
                 float destLeft, float destTop, float destWidth, float destHeight);

  void drawImage(const IImage* image,
                 float srcLeft, float srcTop, float srcWidth, float srcHeight,
                 float destLeft, float destTop, float destWidth, float destHeight,
                 float transparency);

  int getWidth() const {
    return _canvasWidth;
  }

  int getHeight() const {
    return _canvasHeight;
  }

  void beginPath();

  void closePath();

  void stroke();

  void fill();

  void fillAndStroke();

  void moveTo(float x, float y);

  void moveTo(const Vector2F& position) {
    moveTo(position._x, position._y);
  }

  void lineTo(float x, float y);

  void lineTo(const Vector2F& position) {
    lineTo(position._x, position._y);
  }
  
};

#endif
