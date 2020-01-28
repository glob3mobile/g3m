
#ifndef Canvas_Emscripten_hpp
#define Canvas_Emscripten_hpp

#include "G3M/ICanvas.hpp"

#include <emscripten/val.h>


class Canvas_Emscripten : public ICanvas {
private:
  emscripten::val _domCanvas;
  emscripten::val _domCanvasContext;

  int _width;
  int _height;

  std::string _currentDOMFont;
  int         _currentFontSize;

  void tryToSetCurrentFontToContext();

  void roundRect(float x, float y,
                 float width, float height,
                 float radius,
                 bool fill,
                 bool stroke);

  void drawEllipse(float x, float y,
                   float w, float h,
                   bool fill,
                   bool stroke);

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
                    float phase);

  void _setShadow(const Color& color,
                  float blur,
                  float offsetX,
                  float offsetY);

  void _removeShadow();

  void _clearRect(float left, float top,
                  float width, float height);


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
                  float destLeft, float destTop,
                  float transparency);

  void _drawImage(const IImage* image,
                  float destLeft, float destTop, float destWidth, float destHeight);


  void _drawImage(const IImage* image,
                  float destLeft, float destTop, float destWidth, float destHeight,
                  float transparency);

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

  void _fillEllipse(float left, float top,
                    float width, float height);

  void _strokeEllipse(float left, float top,
                      float width, float height);

  void _fillAndStrokeEllipse(float left, float top,
                             float width, float height);


public:
  Canvas_Emscripten(bool retina);

  virtual ~Canvas_Emscripten();

};

#endif
