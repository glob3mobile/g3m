

#include "Canvas_Emscripten.hpp"

using namespace emscripten;

#include "IMathUtils.hpp"
#include "IStringUtils.hpp"
#include "Color.hpp"
#include "GFont.hpp"
#include "Image_Emscripten.hpp"

#include <sstream>


Canvas_Emscripten::Canvas_Emscripten(bool retina) :
ICanvas(retina),
_domCanvas( val::global("document").call<val>("createElement", val("canvas")) ),
_domCanvasContext( _domCanvas.call<val>("getContext", val("2d")) ),
_width(0),
_height(0),
_currentDOMFont(""),
_currentFontSize(0)
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
  switch (cap) {
    case CAP_BUTT:
      _domCanvasContext.set("lineCap", "butt");
      break;
    case CAP_ROUND:
      _domCanvasContext.set("lineCap", "round");
      break;
    case CAP_SQUARE:
      _domCanvasContext.set("lineCap", "square");
      break;
  }
}

void Canvas_Emscripten::_setLineJoin(StrokeJoin join) {
  switch (join) {
    case JOIN_MITER:
      _domCanvasContext.set("lineJoin", "miter");
      break;
    case JOIN_ROUND:
      _domCanvasContext.set("lineJoin", "round");
      break;
    case JOIN_BEVEL:
      _domCanvasContext.set("lineJoin", "bevel");
      break;
  }
}

void Canvas_Emscripten::_setLineMiterLimit(float limit) {
  _domCanvasContext.set("miterLimit", limit);
}

void Canvas_Emscripten::_setLineDash(float lengths[],
                                     int count,
                                     float phase) {
  val jsArray = val::array();
  for (int i = 0; i < count; i++) {
    jsArray.set(i, lengths[i]);
  }
  _domCanvasContext.call<void>("setLineDash", lengths);
  _domCanvasContext.set("lineDashOffset", phase);
}

void Canvas_Emscripten::_setShadow(const Color& color,
                                   float blur,
                                   float offsetX,
                                   float offsetY) {
  _domCanvasContext.set("shadowColor",   createDOMColor(color));
  _domCanvasContext.set("shadowBlur",    blur);
  _domCanvasContext.set("shadowOffsetX", offsetX);
  _domCanvasContext.set("shadowOffsetY", offsetY);
}

void Canvas_Emscripten::_removeShadow() {
  _domCanvasContext.set("shadowColor",   "rgba(0,0,0,0)");
  _domCanvasContext.set("shadowBlur",    0);
  _domCanvasContext.set("shadowOffsetX", 0);
  _domCanvasContext.set("shadowOffsetY", 0);
}

void Canvas_Emscripten::_clearRect(float left, float top,
                                   float width, float height) {
  _domCanvasContext.call<void>("clearRect", left, top, width, height);
}


void Canvas_Emscripten::_fillRectangle(float left, float top,
                                       float width, float height) {
  _domCanvasContext.call<void>("fillRect", left, top, width, height);
}

void Canvas_Emscripten::_strokeRectangle(float left, float top,
                                         float width, float height) {
  _domCanvasContext.call<void>("strokeRect", left, top, width, height);
}

void Canvas_Emscripten::_fillAndStrokeRectangle(float left, float top,
                                                float width, float height) {
  _domCanvasContext.call<void>("fillRect", left, top, width, height);
  _domCanvasContext.call<void>("strokeRect", left, top, width, height);
}

void Canvas_Emscripten::_fillRoundedRectangle(float left, float top,
                                              float width, float height,
                                              float radius) {
  roundRect(left, top, width, height, radius, true, false);
}

void Canvas_Emscripten::_strokeRoundedRectangle(float left, float top,
                                                float width, float height,
                                                float radius) {
  roundRect(left, top, width, height, radius, false, true);
}

void Canvas_Emscripten::_fillAndStrokeRoundedRectangle(float left, float top,
                                                       float width, float height,
                                                       float radius) {
  roundRect(left, top, width, height, radius, true, true);
}

void Canvas_Emscripten::roundRect(float x, float y,
                                  float width, float height,
                                  float radius,
                                  bool fill,
                                  bool stroke) {
  _domCanvasContext.call<void>("beginPath");
  _domCanvasContext.call<void>("moveTo", x + radius, y);
  _domCanvasContext.call<void>("lineTo", x + width - radius, y);
  _domCanvasContext.call<void>("quadraticCurveTo", x + width, y, x + width, y + radius);
  _domCanvasContext.call<void>("lineTo", x + width, y + height - radius);
  _domCanvasContext.call<void>("quadraticCurveTo", x + width, y + height, x + width - radius, y + height);
  _domCanvasContext.call<void>("lineTo", x + radius, y + height);
  _domCanvasContext.call<void>("quadraticCurveTo", x, y + height, x, y + height - radius);
  _domCanvasContext.call<void>("lineTo", x, y + radius);
  _domCanvasContext.call<void>("quadraticCurveTo", x, y, x + radius, y);
  _domCanvasContext.call<void>("closePath");
  if (fill) {
    _domCanvasContext.call<void>("fill");
  }
  if (stroke) {
    _domCanvasContext.call<void>("stroke");
  }
}

void Canvas_Emscripten::_createImage(IImageListener* listener,
                                     bool autodelete) {
#error TODO
}

const std::string createDOMFont(const GFont& font,
                                int fontSize) {
  std::ostringstream builder;

  if (font.isItalic()) {
    builder << "italic ";
  }
  if (font.isBold()) {
    builder << "bold ";
  }

  builder << fontSize;
  builder << "px ";

  if (font.isSerif()) {
    builder << "serif";
  }
  else if (font.isSansSerif()) {
    builder << "sans-serif";
  }
  else if (font.isMonospaced()) {
    builder << "monospace";
  }

  return builder.str();
}

void Canvas_Emscripten::_setFont(const GFont& font) {
  const IMathUtils* mu = IMathUtils::instance();

  _currentFontSize = mu->round(font.getSize());
  _currentDOMFont = createDOMFont(font, _currentFontSize);

  tryToSetCurrentFontToContext();
}

const Vector2F Canvas_Emscripten::_textExtent(const std::string& text) {
  const IMathUtils* mu = IMathUtils::instance();

  _domCanvasContext.set("textBaseline", "top");

  const val textMetrics = _domCanvasContext.call<val>("measureText", text);
  const int width  = mu->ceil( textMetrics["width"].as<float>() );
  const int height = _currentFontSize;
  return Vector2F(width, height);
}

void Canvas_Emscripten::_fillText(const std::string& text,
                                  float left, float top) {
  _domCanvasContext.set("textBaseline", "top");
  _domCanvasContext.call<void>("fillText", text, left, top - 1);
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               destLeft,
                               destTop,
                               domImage["width"].as<float>(),
                               domImage["height"].as<float>());
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop,
                                   float transparency) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  const float currentGlobalAlpha = _domCanvasContext["globalAlpha"].as<float>();
  _domCanvasContext.set("globalAlpha", transparency);
  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               destLeft,
                               destTop,
                               domImage["width"].as<float>(),
                               domImage["height"].as<float>());
  _domCanvasContext.set("globalAlpha", currentGlobalAlpha);
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop, float destWidth, float destHeight) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               destLeft,
                               destTop,
                               destWidth,
                               destHeight);
}


void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop, float destWidth, float destHeight,
                                   float transparency) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  const float currentGlobalAlpha = _domCanvasContext["globalAlpha"].as<float>();
  _domCanvasContext.set("globalAlpha", transparency);
  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               destLeft,
                               destTop,
                               destWidth,
                               destHeight);
  _domCanvasContext.set("globalAlpha", currentGlobalAlpha);
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float srcLeft, float srcTop, float srcWidth, float srcHeight,
                                   float destLeft, float destTop, float destWidth, float destHeight) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               srcLeft,  srcTop,  srcWidth,  srcHeight,
                               destLeft, destTop, destWidth, destHeight);
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float srcLeft, float srcTop, float srcWidth, float srcHeight,
                                   float destLeft, float destTop, float destWidth, float destHeight,
                                   float transparency) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  const float currentGlobalAlpha = _domCanvasContext["globalAlpha"].as<float>();
  _domCanvasContext.set("globalAlpha", transparency);
  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               srcLeft,  srcTop,  srcWidth,  srcHeight,
                               destLeft, destTop, destWidth, destHeight);
  _domCanvasContext.set("globalAlpha", currentGlobalAlpha);
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
