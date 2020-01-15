

#include "TextUtils_Emscripten.hpp"

#include "ILogger.hpp"
#include "IImageListener.hpp"
#include "IStringUtils.hpp"
#include "IMathUtils.hpp"
#include "Color.hpp"

#include "Image_Emscripten.hpp"

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
                                      bool autodelete) {
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
    nativeLabelImage(image, label, labelBottom, separation, fontSize, color, shadowColor, listener, autodelete);
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
                                            bool autodelete) {
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
  Image_Emscripten::createFromURL(imageDataURL,
                                  listener,
                                  autodelete);
}

void TextUtils_Emscripten::nativeLabelImage(const IImage* image,
                                            const std::string& label,
                                            bool labelBottom,
                                            int separation,
                                            float fontSize,
                                            const Color* color,
                                            const Color* shadowColor,
                                            IImageListener* listener,
                                            bool autodelete) {
  const val document = val::global("document");
  val canvas = document.call<val>("createElement", val("canvas"));
  val context = canvas.call<val>("getContext", val("2d"));

  const IStringUtils* su = IStringUtils::instance();
  const IMathUtils*   mu = IMathUtils::instance();

  const std::string font = su->toString( mu->round(fontSize) ) + "px sans-serif";
  context.set("font", font);

  const val textMetrics = context.call<val>("measureText", label);

  int textWidth  = textMetrics["width"].as<int>();
  int textHeight = mu->ceil(fontSize);
  if (shadowColor) {
    textWidth  += 2;
    textHeight += 2;
  }

  const int imageWidth  = image->getWidth();
  const int imageHeight = image->getHeight();

  int resultWidth;
  int resultHeight;
  if (labelBottom) {
    resultWidth  = mu->max(textWidth, imageWidth);
    resultHeight = textHeight + separation + imageHeight;
  }
  else {
    resultWidth  = textWidth + separation + imageWidth;
    resultHeight = mu->max(textHeight, imageHeight);
  }

  canvas.set("width",  resultWidth);
  canvas.set("height", resultHeight);
  context.set("font", font); // set font as the width/height change resets the context

  //context.fillStyle = "green"; // for debug
  //context.fillRect(0, 0, resultWidth, resultHeight); // for debug

  val domImage = ((Image_Emscripten*) image)->getDOMImage();

  if (labelBottom) {
    context.call<void>("drawImage", domImage, (resultWidth - imageWidth) / 2, 0);
  }
  else {
    context.call<void>("drawImage", domImage, 0, (resultHeight - imageHeight) / 2);
  }

  context.set("fillStyle", toJSColor(color));

  if (shadowColor) {
    context.set("shadowColor",   toJSColor(shadowColor));
    context.set("shadowBlur",    2);
    context.set("shadowOffsetX", 2);
    context.set("shadowOffsetY", 2);
  }

  context.set("textAlign",    "left");
  context.set("textBaseline", "top");

  if (labelBottom) {
    context.call<void>("fillText", label, (resultWidth - textWidth) / 2, imageHeight + separation);
  }
  else {
    context.call<void>("fillText", label, imageWidth + separation, (resultHeight - textHeight) / 2);
  }

  const std::string imageDataURL = canvas.call<std::string>("toDataURL");
  Image_Emscripten::createFromURL(imageDataURL,
                                  listener,
                                  autodelete);
}
