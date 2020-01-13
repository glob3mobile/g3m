

#include "TextUtils_Emscripten.hpp"

#include "ILogger.hpp"
#include "IImageListener.hpp"
#include "IStringUtils.hpp"
#include "IMathUtils.hpp"
#include "Color.hpp"

#include <emscripten/val.h>

using namespace emscripten;


void TextUtils_Emscripten::labelImage(const IImage* image,
                                      const std::string& label,
                                      const LabelPosition labelPosition,
                                      int separation,
                                      float fontSize,
                                      const Color* color,
                                      const Color* shadowColor,
                                      IImageListener* listener,
                                      bool autodelete)
{
  bool labelBottom;
  if (labelPosition == LabelPosition::Bottom) {
    labelBottom = true;
  }
  else if (labelPosition == LabelPosition::Right) {
    labelBottom = false;
  }
  else {
    ILogger::instance()->logError("Unsupported LabelPosition");

    listener->imageCreated(NULL);
    return;
  }

  if (image == NULL) {
    createLabelImage(label, fontSize, color, shadowColor, listener, autodelete);
  }
  else {
    nativeLabelImage(image, label, labelBottom, separation, fontSize, color, shadowColor, listener);
  }
}

const std::string toJSColor(const Color* color) {
  if (color == NULL) {
     return NULL;
  }

  const IMathUtils* mu = IMathUtils::instance();
  const IStringUtils* su = IStringUtils::instance();

  const int   r = mu->round(255 * color->_red);
  const int   g = mu->round(255 * color->_green);
  const int   b = mu->round(255 * color->_blue);
  const float a = color->_alpha;

  return ("rgba(" +
          su->toString(r) + "," +
          su->toString(g) + "," +
          su->toString(b) + "," +
          su->toString(a) +
          ")");
}

void TextUtils_Emscripten::createLabelImage(const std::string& label,
                                            float fontSize,
                                            const Color* color,
                                            const Color* shadowColor,
                                            IImageListener* listener,
                                            bool autodelete)
{
  const val document = val::global("document");
  val canvas = document.call<val>("createElement", val("canvas"));
  val context = canvas.call<val>("getContext", val("2d"));

  const IStringUtils* su = IStringUtils::instance();
  const IMathUtils*   mu = IMathUtils::instance();

  const std::string font = su->toString( mu->round(fontSize) ) + "px sans-serif";
  context.set("font", font);

  const val textMetrics = context.call<val>("measureText", label);

  int width  = textMetrics["width"].as<int>();
  int height = mu->ceil(fontSize);
  if (shadowColor) {
    width  += 2;
    height += 2;
  }

  canvas.set("width",  width);
  canvas.set("height", height);
  context.set("font", font); // set font as the width/height change resets the context

  context.set("fillStyle", toJSColor(color));

  if (shadowColor) {
    context.set("shadowColor",   toJSColor(shadowColor));
    context.set("shadowBlur",    2);
    context.set("shadowOffsetX", 2);
    context.set("shadowOffsetY", 2);
  }

  context.set("textAlign", "left");
  context.set("textBaseline", "top");
  context.call<void>("fillText", label, 0, 0);

  const std::string imageDataURL = canvas.call<std::string>("toDataURL");

//  EM_ASM({
//    console.log('I received: ' + $0);
//  }, imageDataURL);


  val jsResult = val::global("Image").new_();
  jsResult.onload = function() {
    var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
    listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
  };
  jsResult.set("src", imageDataURL);
}
