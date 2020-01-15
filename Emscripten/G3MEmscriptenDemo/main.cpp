
//#include <stdio.h>

#include <emscripten.h>
#include <emscripten/html5.h>
#include <emscripten/val.h>

//#include "G3MBuilder_Emscripten.hpp"

#include "EMStorage.hpp"
#include "Image_Emscripten.hpp"
#include "IImageListener.hpp"
#include "StringBuilder_Emscripten.hpp"
#include "TextUtils_Emscripten.hpp"
#include "Color.hpp"

#include "MathUtils_Emscripten.hpp"
#include "StringUtils_Emscripten.hpp"


using namespace emscripten;


extern "C" {

  EMSCRIPTEN_KEEPALIVE
  void invoke_function_pointer(void(*f)(int), int i) {
    (*f)(i);
  }

  EMSCRIPTEN_KEEPALIVE
  void processDOMImage(int domImageID) {
    val domImage = EMStorage::take(domImageID);
    
    val document = val::global("document");
    val body = document["body"].as<val>();
    
    body.call<void>("appendChild", domImage);
  };
  
}


EM_JS(void, createDOMImage, (const int urlID), {
    var img = new Image();
    
    img.onload = function() {
      console.log("** IMAGE LOADED!!!!");
      Module.ccall('processDOMImage', 'void', ['int'], [ document.EMStorage.put(img) ]);
    };
    img.onerror = function() {
      console.log("** ERRORR!!!!");
    };

    var url = document.EMStorage.take(urlID);
    img.src = url;
  });



class PvtListener : public IImageListener {
public:
  void imageCreated(const IImage* image) {
    emscripten_console_log( image->description().c_str() );

    val document = val::global("document");
    val body = document["body"].as<val>();
    
    body.call<void>("appendChild", ((Image_Emscripten*) image)->getDOMImage() );

    delete image;
  }

};



int main() {
  IStringBuilder::setInstance( new StringBuilder_Emscripten(IStringBuilder::DEFAULT_FLOAT_PRECISION) );
  IMathUtils::setInstance( new MathUtils_Emscripten() );
  IStringUtils::setInstance( new StringUtils_Emscripten() );

  
  EMStorage::initialize();
  
  //printf("hello, world!\n");

  //val domImage = val::global("Image").new_();
  //emscripten_console_log( domImage.call<std::string>("toString").c_str() );

  const int urlID = EMStorage::put( val("https://emscripten.org/_static/Emscripten_logo_full.png") );
  createDOMImage(urlID);

  emscripten_console_log("Hello from emscripten_console_log (1)");
  emscripten_console_warn("Hello from emscripten_console_warn (2)");
  //emscripten_console_error("Hello from emscripten_console_error (3)");
  
  // G3MBuilder_Emscripten builder;
  // G3MWidget_Emscripten* widget = builder.createWidget();

  // final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
  // g3mWidgetHolder.add(_widget);

  EM_ASM( {
      var pointer = addFunction(function(i) { 
	  console.log('I was called from C world! ' + i); 
	}, "vi");

      Module.ccall('invoke_function_pointer', 'void', ['number', 'int'], [pointer, 42]);

      var jsInvokeFunctionPointer = Module.cwrap('invoke_function_pointer', 'void', ['number', 'int']);
      jsInvokeFunctionPointer(pointer, 64);
      
      removeFunction(pointer);
    } );

  Image_Emscripten::createFromURL("https://emscripten.org/_static/Emscripten_logo_full.png",
                                  new PvtListener(),
                                  true);

  Image_Emscripten::createFromURL("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAZ0lEQVR42u3QgRAAQAgAsA/hQWKMOIc4ujaExc+ud1gIECBAgAABAgQIECBAgAABAgQIECBAgAABAgQIECBAgAABAgQIECBAgAABAgQIECBAgAABAgQIECBAgAABAgQIECBAgAAB+w2uJmrBDxPwrwAAAABJRU5ErkJggg==",
                                  new PvtListener(),
                                  true);


  TextUtils_Emscripten* tu = new TextUtils_Emscripten();

  tu->createLabelImage("Hello world from TextUtils!",
		       42, // fontSize,
		       Color::newFromRGBA(1,0,0,1),
		       NULL, // const Color* shadowColor,
		       new PvtListener(),
		       true);

  delete tu;
  
  return 0;
}
