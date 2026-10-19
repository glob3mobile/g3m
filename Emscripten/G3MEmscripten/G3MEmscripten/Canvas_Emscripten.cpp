

#include "Canvas_Emscripten.hpp"

using namespace emscripten;

#include "G3M/IMathUtils.hpp"
#include "G3M/IStringUtils.hpp"
#include "G3M/Color.hpp"
#include "G3M/GFont.hpp"
#include "Image_Emscripten.hpp"

#include <map>
#include <sstream>


Canvas_Emscripten::Canvas_Emscripten(bool retina,
                                     const int maxSize) :
ICanvas(retina),
_maxSize(maxSize),
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
  const float rawDevicePixelRatio = ( _retina && valDevicePixelRatio.as<bool>() ) ? valDevicePixelRatio.as<float>() : 1;

  float widthPixelRatio  = rawDevicePixelRatio;
  float heightPixelRatio = rawDevicePixelRatio;

  if (_maxSize > 0) {
    const float goalWidth  = (width * widthPixelRatio);
    if (goalWidth > _maxSize) {
      widthPixelRatio = (float) _maxSize / width;
    }

    const float goalHeight = (height * heightPixelRatio);
    if (goalHeight > _maxSize) {
      heightPixelRatio = (float) _maxSize / height;
    }
  }
  
  const IMathUtils* mu = IMathUtils::instance();

  const int w = (int) mu->ceil(width  * widthPixelRatio);
  const int h = (int) mu->ceil(height * heightPixelRatio);

  _width  = w;
  _height = h;

  _domCanvas.set("width",  w);
  _domCanvas.set("height", h);

  if ((widthPixelRatio != 1) || (heightPixelRatio != 1)) {
    _domCanvasContext.call<void>("scale", val(widthPixelRatio), val(heightPixelRatio));
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
  _domCanvasContext.call<void>("setLineDash", jsArray);
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
  // Every caller discards this ICanvas after createImage().
  Image_Emscripten::createFromCanvas(_domCanvas,
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

struct FontMetrics {
  int _ascent;
  int _height;
};

// as Canvas_WebGL.fontMetrics: ascent and height (ascent + descent) of the context's font, cached by font
static const FontMetrics& fontMetrics(val& context,
                                      const int fontSize) {
  static std::map<std::string, FontMetrics> cache;

  const std::string font = context["font"].as<std::string>();
  const std::map<std::string, FontMetrics>::const_iterator cached = cache.find(font);
  if (cached != cache.end()) {
    return cached->second;
  }

  context.set("textBaseline", "alphabetic");
  // accented capitals and descenders, as Canvas_WebGL ("\u00C1\u00C9gjpqy")
  const val m = context.call<val>("measureText", std::string("\xC3\x81\xC3\x89gjpqy"));

  double ascent;
  double descent;
  if (m["fontBoundingBoxAscent"].isNumber() && m["fontBoundingBoxDescent"].isNumber()) {
    ascent  = m["fontBoundingBoxAscent"].as<double>();
    descent = m["fontBoundingBoxDescent"].as<double>();
  }
  else if (m["actualBoundingBoxAscent"].isNumber() && m["actualBoundingBoxDescent"].isNumber()) {
    ascent  = m["actualBoundingBoxAscent"].as<double>();
    descent = m["actualBoundingBoxDescent"].as<double>();
  }
  else {
    // same fallback factors as Canvas_WebGL
    ascent  = fontSize * 0.95;
    descent = fontSize * 0.25;
  }

  const IMathUtils* mu = IMathUtils::instance();
  const int ceiledAscent = (int) mu->ceil(ascent);
  const FontMetrics metrics = { ceiledAscent, ceiledAscent + (int) mu->ceil(descent) };
  return cache[font] = metrics;
}

const Vector2F Canvas_Emscripten::_textExtent(const std::string& text) {
  const IMathUtils* mu = IMathUtils::instance();

  const FontMetrics& metrics = fontMetrics(_domCanvasContext, _currentFontSize);
  _domCanvasContext.set("textBaseline", "alphabetic");

  const val textMetrics = _domCanvasContext.call<val>("measureText", text);
  const int width = (int) mu->ceil( textMetrics["width"].as<float>() );
  return Vector2F(width, metrics._height);
}

void Canvas_Emscripten::_fillText(const std::string& text,
                                  float left, float top) {
  const FontMetrics& metrics = fontMetrics(_domCanvasContext, _currentFontSize);
  _domCanvasContext.set("textBaseline", "alphabetic");
  _domCanvasContext.call<void>("fillText", text, val(left), val(top + metrics._ascent));
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

void Canvas_Emscripten::_drawImageTinted(const IImage* image,
                                         const Color& color,
                                         float destLeft, float destTop, float destWidth, float destHeight) {
  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  // tinted in a canvas of its own: source-in on this one would also clear what is already drawn around the image
  val tintCanvas = val::global("document").call<val>("createElement", std::string("canvas"));
  tintCanvas.set("width", domImage["width"]);
  tintCanvas.set("height", domImage["height"]);
  val tintContext = tintCanvas.call<val>("getContext", std::string("2d"));
  tintContext.call<void>("drawImage", domImage, val(0), val(0));
  tintContext.set("globalCompositeOperation", std::string("source-in"));
  tintContext.set("fillStyle", createDOMColor(color));
  tintContext.call<void>("fillRect", val(0), val(0), tintCanvas["width"], tintCanvas["height"]);

  _domCanvasContext.call<void>("drawImage",
                               tintCanvas,
                               val(destLeft),
                               val(destTop),
                               val(destWidth),
                               val(destHeight));
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
  _domCanvasContext.call<void>("fill", val(std::string("evenodd")));
}

void Canvas_Emscripten::_fillAndStroke() {
  _domCanvasContext.call<void>("fill", val(std::string("evenodd")));
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
