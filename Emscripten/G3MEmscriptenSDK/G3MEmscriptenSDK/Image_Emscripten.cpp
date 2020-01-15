

#include "Image_Emscripten.hpp"

#include <emscripten.h>

#include "IImageListener.hpp"
#include "IStringBuilder.hpp"
#include "MutableColor255.hpp"

#include "EMStorage.hpp"


using namespace emscripten;


Image_Emscripten::Image_Emscripten(const val& domImage) :
_domImage(domImage),
_imageData( val::null() )
{

}

const val Image_Emscripten::getDOMImage() const {
  return _domImage;
}

int Image_Emscripten::getWidth() const {
  return _domImage["width"].as<int>();
}

int Image_Emscripten::getHeight() const {
  return _domImage["height"].as<int>();
}

const Vector2I Image_Emscripten::getExtent() const {
  return Vector2I(getWidth(), getHeight());
}

const std::string Image_Emscripten::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Image_Emscripten ");
  isb->addInt(getWidth());
  isb->addString("x");
  isb->addInt(getHeight());
  isb->addString(", _image=(");
  isb->addString( _domImage.call<std::string>("toString") );
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

bool Image_Emscripten::isPremultiplied() const {
  return false;
}

IImage* Image_Emscripten::shallowCopy() const {
  return new Image_Emscripten(_domImage);
}

val Image_Emscripten::createImageData() const {
  val document = val::global("document");

  val canvas        = document.call<val>("createElement", val("canvas"));
  val canvasContext = canvas.call<val>("getContext", val("2d"));

  const int w = getWidth();
  const int h = getHeight();
  canvas.set("width",  w);
  canvas.set("height", h);

  canvasContext.call<void>("drawImage", _domImage, 0, 0, w, h);

  return canvasContext.call<val>("getImageData", 0, 0, w, h);
}

void Image_Emscripten::getPixel(int x, int y,
                                MutableColor255& pixel) const {
  if (!_imageData.as<bool>()) {
    _imageData = createImageData();
  }
  const int i = (y * getWidth()) + x;
  val data = _imageData["data"];
  pixel._red   = data[i    ].as<unsigned char>();
  pixel._green = data[i + 1].as<unsigned char>();
  pixel._blue  = data[i + 2].as<unsigned char>();
  pixel._alpha = data[i + 3].as<unsigned char>();
}


extern "C" {

EMSCRIPTEN_KEEPALIVE
void Image_Emscripten_imageCreated(int domImageID, void* voidListener, bool autodelete) {
  IImageListener* listener = (IImageListener*) voidListener;
  val domImage = EMStorage::take(domImageID);

  IImage* image = new Image_Emscripten(domImage);
  listener->imageCreated(image);
  if (autodelete) {
    delete listener;
  }
}
};

void Image_Emscripten::createFromURL(const std::string& url,
                                     IImageListener* listener,
                                     bool autodelete) {

  const int urlID = EMStorage::put( val(url) );

  EM_ASM({
    var img = new Image();

    img.onload = function() {
      Module.ccall('Image_Emscripten_imageCreated', 'void', ['int', 'number', 'bool'], [ document.EMStorage.put(img), $1, $2 ]);
    };

    var url = document.EMStorage.take($0);
    img.src = url;

  }, urlID, listener, autodelete);
}
