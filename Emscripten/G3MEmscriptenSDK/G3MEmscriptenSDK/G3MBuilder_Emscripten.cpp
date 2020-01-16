
#include "G3MBuilder_Emscripten.hpp"

#include "ThreadUtils_Emscripten.hpp"
#include "Downloader_Emscripten.hpp"
#include "BasicShadersGL2.hpp"
#include "G3MWidget_Emscripten.hpp"

//#include <emscripten/bind.h>
#include <emscripten/html5.h>


G3MBuilder_Emscripten::G3MBuilder_Emscripten() {
}

G3MBuilder_Emscripten::~G3MBuilder_Emscripten() {
}

IThreadUtils* G3MBuilder_Emscripten::createDefaultThreadUtils() {
  const int delayMillis = 10;
  return new ThreadUtils_Emscripten(delayMillis);
}

IStorage* G3MBuilder_Emscripten::createDefaultStorage() {
#warning TODO: Storage_Emscripten 
  return NULL;
}

IDownloader* G3MBuilder_Emscripten::createDefaultDownloader() {
  const int  maxConcurrentOperationCount = 8;
  const int  delayMillis                 = 10;
  return new Downloader_Emscripten(maxConcurrentOperationCount, delayMillis);
}

void G3MBuilder_Emscripten::addGPUProgramSources() {
  const BasicShadersGL2 basicShaders;
  for (int i = 0; i < basicShaders.size(); i++) {
    IG3MBuilder::addGPUProgramSources(basicShaders.get(i));
  }
}

G3MWidget_Emscripten* G3MBuilder_Emscripten::createWidget() {
  emscripten_console_log("G3MBuilder_Emscripten::createWidget() 1");
  G3MWidget_Emscripten* nativeWidget = new G3MWidget_Emscripten();
  if (nativeWidget->isWebGLSupported()) {
    emscripten_console_log("G3MBuilder_Emscripten::createWidget() 2");
    addGPUProgramSources();

    emscripten_console_log("G3MBuilder_Emscripten::createWidget() 3");
    nativeWidget->initSingletons();

    emscripten_console_log("G3MBuilder_Emscripten::createWidget() 4");
    setGL(nativeWidget->getGL());

    emscripten_console_log("G3MBuilder_Emscripten::createWidget() 5");
    nativeWidget->setG3MWidget(create());
    emscripten_console_log("G3MBuilder_Emscripten::createWidget() 6");
    nativeWidget->startWidget();
  }

  return nativeWidget;
}

// class BaseClass {
// public:
//   BaseClass() {}
//
//   void basePrint() {
//     printf("base print\n");
//   }
//
//   virtual void subPrint() = 0;
// };
//
//
// class SubClass : public BaseClass {
// public:
//   SubClass() : BaseClass() {}
//
//   void subPrint() {
//     printf("sub print\n");
//   }
// };




// https://github.com/emscripten-core/emscripten/issues/627

// using namespace emscripten;

// EMSCRIPTEN_BINDINGS() {
//   class_<IG3MBuilder>("IG3MBuilder")
//     //.function("basePrint", &IG3MBuilder::basePrint)
//     ;

//   class_<G3MBuilder_Emscripten, base<IG3MBuilder>>("G3MBuilder_Emscripten")
//     .constructor()
//     .function("createWidget", &G3MBuilder_Emscripten::createWidget, allow_raw_pointers())
//     ;
// }
