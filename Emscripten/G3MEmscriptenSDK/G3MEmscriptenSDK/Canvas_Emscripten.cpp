

#include "Canvas_Emscripten.hpp"

using namespace emscripten;

#include "IMathUtils.hpp"
#include "IStringUtils.hpp"
#include "Color.hpp"


Canvas_Emscripten::Canvas_Emscripten(bool retina) :
ICanvas(retina),
_domCanvas( val::global("document").call<val>("createElement", val("canvas")) ),
_domCanvasContext( _domCanvas.call<val>("getContext", val("2d")) ),
_width(0),
_height(0),
_currentDOMFont("")
{
}

Canvas_Emscripten::~Canvas_Emscripten() {
  
}

void Canvas_Emscripten::_initialize(int width, int height) {
  val window = val::global("window");
  val valDevicePixelRatio = window["devicePixelRatio"];
  const float ratio = ( _retina && valDevicePixelRatio.as<bool>() ) ? valDevicePixelRatio.as<float>() : 1;

  const IMathUtils*   mu = IMathUtils::instance();

  int w = mu->ceil(width * ratio);
  int h = mu->ceil(height * ratio);

  _width = w;
  _height = h;

  _domCanvas.set("width",  w);
  _domCanvas.set("height", h);

  if (ratio != 1) {
    _domCanvasContext.call<void>("scale", ratio, ratio);
  }

  tryToSetCurrentFontToContext();
}

void Canvas_Emscripten::tryToSetCurrentFontToContext() {
  if (!_currentDOMFont.empty()) {
    _domCanvasContext.set("font", _currentDOMFont);
  }
}

const std::string createDOMColor(const Color& color) {
  const IMathUtils*   mu = IMathUtils::instance();
  const IStringUtils* su = IStringUtils::instance();

  const int   r = mu->round(255 * color._red);
  const int   g = mu->round(255 * color._green);
  const int   b = mu->round(255 * color._blue);
  const float a = color._alpha;

  return ("rgba(" +
          su->toString(r) + "," +
          su->toString(g) + "," +
          su->toString(b) + "," +
          su->toString(a) +
          ")");
}

void Canvas_Emscripten::_setFillColor(const Color& color) {
  _domCanvasContext.set("fillStyle", createDOMColor(color));
}

void Canvas_Emscripten::_setLineColor(const Color& color) {
  _domCanvasContext.set("strokeStyle", createDOMColor(color));
}

void Canvas_Emscripten::_setLineWidth(float width) {
  _domCanvasContext.set("lineWidth", width);
}

void Canvas_Emscripten::_setLineCap(StrokeCap cap) {
#error TODO
}

void Canvas_Emscripten::_setLineJoin(StrokeJoin join) {
#error TODO
}

void Canvas_Emscripten::_setLineMiterLimit(float limit) {
#error TODO
}

void Canvas_Emscripten::_setLineDash(float lengths[],
                                     int count,
                                     float phase) {
#error TODO
}

void Canvas_Emscripten::_setShadow(const Color& color,
                                   float blur,
                                   float offsetX,
                                   float offsetY) {
#error TODO
}

void Canvas_Emscripten::_removeShadow() {
#error TODO
}

void Canvas_Emscripten::_clearRect(float left, float top,
                                   float width, float height) {
#error TODO
}


void Canvas_Emscripten::_fillRectangle(float left, float top,
                                       float width, float height) {
#error TODO
}

void Canvas_Emscripten::_strokeRectangle(float left, float top,
                                         float width, float height) {
#error TODO
}

void Canvas_Emscripten::_fillAndStrokeRectangle(float left, float top,
                                                float width, float height) {
#error TODO
}


void Canvas_Emscripten::_fillRoundedRectangle(float left, float top,
                                              float width, float height,
                                              float radius) {
#error TODO
}

void Canvas_Emscripten::_strokeRoundedRectangle(float left, float top,
                                                float width, float height,
                                                float radius) {
#error TODO
}

void Canvas_Emscripten::_fillAndStrokeRoundedRectangle(float left, float top,
                                                       float width, float height,
                                                       float radius) {
#error TODO
}


void Canvas_Emscripten::_createImage(IImageListener* listener,
                                     bool autodelete) {
#error TODO
}

void Canvas_Emscripten::_setFont(const GFont& font) {
#error TODO
}

const Vector2F Canvas_Emscripten::_textExtent(const std::string& text) {
#error TODO
}

void Canvas_Emscripten::_fillText(const std::string& text,
                                  float left, float top) {
#error TODO
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop) {
#error TODO
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop,
                                   float transparency) {
#error TODO
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop, float destWidth, float destHeight) {
#error TODO
}


void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop, float destWidth, float destHeight,
                                   float transparency) {
#error TODO
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float srcLeft, float srcTop, float srcWidth, float srcHeight,
                                   float destLeft, float destTop, float destWidth, float destHeight) {
#error TODO
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float srcLeft, float srcTop, float srcWidth, float srcHeight,
                                   float destLeft, float destTop, float destWidth, float destHeight,
                                   float transparency) {
#error TODO
}


void Canvas_Emscripten::_beginPath() {
#error TODO
}

void Canvas_Emscripten::_closePath() {
#error TODO
}

void Canvas_Emscripten::_stroke() {
#error TODO
}

void Canvas_Emscripten::_fill() {
#error TODO
}

void Canvas_Emscripten::_fillAndStroke() {
#error TODO
}

void Canvas_Emscripten::_moveTo(float x, float y) {
#error TODO
}

void Canvas_Emscripten::_lineTo(float x, float y) {
#error TODO
}

void Canvas_Emscripten::_fillEllipse(float left, float top,
                                     float width, float height) {
#error TODO
}

void Canvas_Emscripten::_strokeEllipse(float left, float top,
                                       float width, float height) {
#error TODO
}

void Canvas_Emscripten::_fillAndStrokeEllipse(float left, float top,
                                              float width, float height) {
#error TODO
}
