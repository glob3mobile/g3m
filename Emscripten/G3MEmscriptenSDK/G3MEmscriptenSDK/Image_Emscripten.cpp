

#include "Image_Emscripten.hpp"

#include <emscripten.h>


using namespace emscripten;


Image_Emscripten::Image_Emscripten(const val& domImage) :
_domImage(domImage)
{

}

const val Image_Emscripten::getDOMImage() const {
  return _domImage;
}



void Image_Emscripten::createFromURL(const std::string& url,
                                     IImageListener* listener,
                                     bool autodelete) {

  val domImage = val::global("Image").new_();

  EM_ASM( {

    $0.onload = function() {

    };

  }, url);


  jsResult.onload = function() {
    var result = @org.glob3.mobile.specific.Image_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(jsResult);
    listener.@org.glob3.mobile.generated.IImageListener::imageCreated(Lorg/glob3/mobile/generated/IImage;)(result);
  };
  jsResult.set("src", imageDataURL);

}
