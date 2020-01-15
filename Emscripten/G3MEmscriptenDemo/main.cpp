
//#include <stdio.h>

#include <emscripten.h>
#include <emscripten/html5.h>
#include <emscripten/val.h>

//#include "G3MBuilder_Emscripten.hpp"

using namespace emscripten;

val takeoutFromStorage(int id) {
  val MyStorage =  val::global("document")["MyStorage"];

  val result = MyStorage[id];
  
  EM_ASM({ delete document.MyStorage[$0]; }, id);
  
  return result;
}

EM_JS(void, initStorage, (), {
    if (typeof document.MyStorage === 'undefined') {
      document.MyStorage = {
	"__idCounter" : 0
      };

      document.MyStorage["put"] = function(obj) {
	var self = document.MyStorage;
	var id = self.__idCounter + 1;
	self.__idCounter = id;
	self[id] = obj;
	return id;
      };
    }
});


extern "C" {

  void EMSCRIPTEN_KEEPALIVE invoke_function_pointer(void(*f)(int), int i) {
    (*f)(i);
  }

  void EMSCRIPTEN_KEEPALIVE processDOMImage(int domImageID) {
    val domImage = takeoutFromStorage(domImageID);
    
    val document = val::global("document");
    //emscripten_console_log( document.call<std::string>("toString").c_str() );
    val body = document["body"].as<emscripten::val>();
    
    body.call<void>("appendChild", domImage);
  };
  
}


EM_JS(void, createDOMImage, (), {
    var img = new Image();
    
    img.onload = function() {
      console.log("** IMAGE LOADED!!!!");
      Module.ccall('processDOMImage', 'void', ['int'], [ document.MyStorage.put(img) ]);
    };
    img.onerror = function() {
      console.log("** ERRORR!!!!");
    };
    
    img.src = "https://emscripten.org/_static/Emscripten_logo_full.png";
  });


int main() {
  initStorage();
  
  //printf("hello, world!\n");

  //val domImage = val::global("Image").new_();
  //emscripten_console_log( domImage.call<std::string>("toString").c_str() );

  createDOMImage();

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

  
  return 0;
}
