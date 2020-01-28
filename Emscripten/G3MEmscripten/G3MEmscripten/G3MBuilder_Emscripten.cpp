
#include "G3MBuilder_Emscripten.hpp"

#include "G3M/BasicShadersGL2.hpp"

#include "ThreadUtils_Emscripten.hpp"
#include "Downloader_Emscripten.hpp"
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
  G3MWidget_Emscripten* nativeWidget = new G3MWidget_Emscripten();
  if (nativeWidget->isWebGLSupported()) {
    addGPUProgramSources();

    G3MWidget_Emscripten::initSingletons();

    setGL(nativeWidget->getGL());

    nativeWidget->setG3MWidget(create());
    
//    nativeWidget->startWidget();
  }

  return nativeWidget;
}
