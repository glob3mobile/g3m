
//#include <stdio.h>

#include <emscripten.h>
#include <emscripten/html5.h>

//#include "G3MBuilder_Emscripten.hpp"


extern "C" {

  void invoke_function_pointer(void(*f)(int), int i) {
    (*f)(i);
  }

}


int main() {
  //printf("hello, world!\n");

  emscripten_console_log("Hello from emscripten_console_log (1)");
  emscripten_console_warn("Hello from emscripten_console_warn (2)");
  //emscripten_console_error("Hello from emscripten_console_error (3)");
  
  //  G3MBuilder_Emscripten builder;
  //  G3MWidget_Emscripten* widget = builder.createWidget();

  // final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
  // g3mWidgetHolder.add(_widget);

  EM_ASM( {
      var pointer = addFunction(function(i) { 
	  console.log('I was called from C world! ' + i); 
	}, "vi");
      Module.ccall('invoke_function_pointer', 'void', ['number', 'int'], [pointer, 42]);
      removeFunction(pointer);
    } );
  
  return 0;
}
