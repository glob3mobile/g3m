

#include "Canvas_Emscripten.hpp"

using namespace emscripten;

#include "G3M/IMathUtils.hpp"
#include "G3M/IStringUtils.hpp"
#include "G3M/Color.hpp"
#include "G3M/GFont.hpp"
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

  const int w = (int) mu->ceil(width * ratio);
  const int h = (int) mu->ceil(height * ratio);

  _width = w;
  _height = h;

  _domCanvas.set("width",  w);
  _domCanvas.set("height", h);

  if (ratio != 1) {
    _domCanvasContext.call<void>("scale", val(ratio), val(ratio));
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
  _domCanvasContext.call<void>("setLineDash", val(lengths));
  _domCanvasContext.set("lineDashOffset", val(phase));
}

void Canvas_Emscripten::_setShadow(const Color& color,
                                   float blur,
                                   float offsetX,
                                   float offsetY) {
  _domCanvasContext.set("shadowColor",   createDOMColor(color));
  _domCanvasContext.set("shadowBlur",    val(blur));
  _domCanvasContext.set("shadowOffsetX", val(offsetX));
  _domCanvasContext.set("shadowOffsetY", val(offsetY));
}

void Canvas_Emscripten::_removeShadow() {
  _domCanvasContext.set("shadowColor",   std::string("rgba(0,0,0,0)"));
  _domCanvasContext.set("shadowBlur",    val(0));
  _domCanvasContext.set("shadowOffsetX", val(0));
  _domCanvasContext.set("shadowOffsetY", val(0));
}

void Canvas_Emscripten::_clearRect(float left, float top,
                                   float width, float height) {
  _domCanvasContext.call<void>("clearRect", val(left), val(top), val(width), val(height));
}


void Canvas_Emscripten::_fillRectangle(float left, float top,
                                       float width, float height) {
  _domCanvasContext.call<void>("fillRect", val(left), val(top), val(width), val(height));
}

void Canvas_Emscripten::_strokeRectangle(float left, float top,
                                         float width, float height) {
  _domCanvasContext.call<void>("strokeRect", val(left), val(top), val(width), val(height));
}

void Canvas_Emscripten::_fillAndStrokeRectangle(float left, float top,
                                                float width, float height) {
  _domCanvasContext.call<void>("fillRect", val(left), val(top), val(width), val(height));
  _domCanvasContext.call<void>("strokeRect", val(left), val(top), val(width), val(height));
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
  _domCanvasContext.call<void>("moveTo", val(x + radius), val(y));
  _domCanvasContext.call<void>("lineTo", val(x + width - radius), val(y));
  _domCanvasContext.call<void>("quadraticCurveTo", val(x + width), val(y), val(x + width), val(y + radius));
  _domCanvasContext.call<void>("lineTo", val(x + width), val(y + height - radius));
  _domCanvasContext.call<void>("quadraticCurveTo", val(x + width), val(y + height), val(x + width - radius), val(y + height));
  _domCanvasContext.call<void>("lineTo", val(x + radius), val(y + height));
  _domCanvasContext.call<void>("quadraticCurveTo", val(x), val(y + height), val(x), val(y + height - radius));
  _domCanvasContext.call<void>("lineTo", val(x), val(y + radius));
  _domCanvasContext.call<void>("quadraticCurveTo", val(x), val(y), val(x + radius), val(y));
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
  const std::string imageDataURL = _domCanvas.call<std::string>("toDataURL");
  Image_Emscripten::createFromURL(imageDataURL,
                                  listener,
                                  autodelete);
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
  const int width  = (int) mu->ceil( textMetrics["width"].as<float>() );
  const int height = _currentFontSize;
  return Vector2F(width, height);
}

void Canvas_Emscripten::_fillText(const std::string& text,
                                  float left, float top) {
  _domCanvasContext.set("textBaseline", "top");
  _domCanvasContext.call<void>("fillText", text, val(left), val(top - 1));
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               val(destLeft),
                               val(destTop),
                               domImage["width"],
                               domImage["height"]);
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop,
                                   float transparency) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  const float currentGlobalAlpha = _domCanvasContext["globalAlpha"].as<float>();
  _domCanvasContext.set("globalAlpha", val(transparency));
  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               val(destLeft),
                               val(destTop),
                               domImage["width"],
                               domImage["height"]);
  _domCanvasContext.set("globalAlpha", val(currentGlobalAlpha));
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop, float destWidth, float destHeight) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               val(destLeft),
                               val(destTop),
                               val(destWidth),
                               val(destHeight));
}


void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float destLeft, float destTop, float destWidth, float destHeight,
                                   float transparency) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  const float currentGlobalAlpha = _domCanvasContext["globalAlpha"].as<float>();
  _domCanvasContext.set("globalAlpha", val(transparency));
  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               val(destLeft),
                               val(destTop),
                               val(destWidth),
                               val(destHeight));
  _domCanvasContext.set("globalAlpha", val(currentGlobalAlpha));
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float srcLeft, float srcTop, float srcWidth, float srcHeight,
                                   float destLeft, float destTop, float destWidth, float destHeight) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               val(srcLeft),  val(srcTop),  val(srcWidth),  val(srcHeight),
                               val(destLeft), val(destTop), val(destWidth), val(destHeight));
}

void Canvas_Emscripten::_drawImage(const IImage* image,
                                   float srcLeft, float srcTop, float srcWidth, float srcHeight,
                                   float destLeft, float destTop, float destWidth, float destHeight,
                                   float transparency) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  const float currentGlobalAlpha = _domCanvasContext["globalAlpha"].as<float>();
  _domCanvasContext.set("globalAlpha", val(transparency));
  _domCanvasContext.call<void>("drawImage",
                               domImage,
                               val(srcLeft),  val(srcTop),  val(srcWidth),  val(srcHeight),
                               val(destLeft), val(destTop), val(destWidth), val(destHeight));
  _domCanvasContext.set("globalAlpha", val(currentGlobalAlpha));
}


void Canvas_Emscripten::_beginPath() {
  _domCanvasContext.call<void>("beginPath");
}

void Canvas_Emscripten::_closePath() {
  _domCanvasContext.call<void>("closePath");
}

void Canvas_Emscripten::_stroke() {
  _domCanvasContext.call<void>("stroke");
}

void Canvas_Emscripten::_fill() {
  _domCanvasContext.call<void>("fill");
}

void Canvas_Emscripten::_fillAndStroke() {
  _domCanvasContext.call<void>("fill");
  _domCanvasContext.call<void>("stroke");
}

void Canvas_Emscripten::_moveTo(float x, float y) {
  _domCanvasContext.call<void>("moveTo", val(x), val(y));
}

void Canvas_Emscripten::_lineTo(float x, float y) {
  _domCanvasContext.call<void>("lineTo", val(x), val(y));
}

void Canvas_Emscripten::_fillEllipse(float left, float top,
                                     float width, float height) {
  drawEllipse(left, top, width, height, true, false);
}

void Canvas_Emscripten::_strokeEllipse(float left, float top,
                                       float width, float height) {
  drawEllipse(left, top, width, height, false, true);
}

void Canvas_Emscripten::_fillAndStrokeEllipse(float left, float top,
                                              float width, float height) {
  drawEllipse(left, top, width, height, true, true);
}

void Canvas_Emscripten::drawEllipse(float x, float y,
                                    float w, float h,
                                    bool fill,
                                    bool stroke) {
  const float kappa = 0.5522848f;
  const float ox = (w / 2) * kappa;  // control point offset horizontal
  const float oy = (h / 2) * kappa;  // control point offset vertical
  const float xe = x + w;            // x-end
  const float ye = y + h;            // y-end
  const float xm = x + w / 2;        // x-middle
  const float ym = y + h / 2;        // y-middle

  _domCanvasContext.call<void>("beginPath");
  _domCanvasContext.call<void>("moveTo", val(x), val(ym));
  _domCanvasContext.call<void>("bezierCurveTo", val(x), val(ym - oy), val(xm - ox), val(y), val(xm), val(y));
  _domCanvasContext.call<void>("bezierCurveTo", val(xm + ox), val(y), val(xe), val(ym - oy), val(xe), val(ym));
  _domCanvasContext.call<void>("bezierCurveTo", val(xe), val(ym + oy), val(xm + ox), val(ye), val(xm), val(ye));
  _domCanvasContext.call<void>("bezierCurveTo", val(xm - ox), val(ye), val(x), val(ym + oy), val(x), val(ym));
  if (fill) {
    _domCanvasContext.call<void>("fill");
  }
  if (stroke) {
    _domCanvasContext.call<void>("stroke");
  }
}
