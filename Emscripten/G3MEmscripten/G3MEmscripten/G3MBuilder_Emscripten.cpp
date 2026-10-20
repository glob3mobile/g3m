
#include "G3MBuilder_Emscripten.hpp"

#include "G3M/BasicShadersGL2.hpp"

#include "ThreadUtils_Emscripten.hpp"
#include "Downloader_Emscripten.hpp"
#include "G3MWidget_Emscripten.hpp"

//#include <emscripten/bind.h>
#include <emscripten/html5.h>


// as G3MBuilder_WebGL: the singletons and the GL exist from the builder's creation,
// so what is created before createWidget() (e.g. a StarsRenderer, which reads GLType) sees them
G3MBuilder_Emscripten::G3MBuilder_Emscripten() :
_nativeWidget(NULL)
{
  G3MWidget_Emscripten::initSingletons();
  _nativeWidget = new G3MWidget_Emscripten();
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
  if (_nativeWidget->isWebGLSupported()) {
    addGPUProgramSources();

    setGL(_nativeWidget->getGL());

    _nativeWidget->setG3MWidget(create());
    
    // nativeWidget->startWidget();
  }

  return _nativeWidget;
}
