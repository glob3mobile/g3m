
#include "G3MBuilder_Wasm.hpp"

#include "ThreadUtils_Wasm.hpp"
#include "Downloader_Wasm.hpp"
#include "BasicShadersGL2.hpp"
#include "G3MWidget_Wasm.hpp"


G3MBuilder_Wasm::G3MBuilder_Wasm() {
}

G3MBuilder_Wasm::~G3MBuilder_Wasm() {
}

IThreadUtils* G3MBuilder_Wasm::createDefaultThreadUtils() {
  const int delayMillis = 10;
  return new ThreadUtils_Wasm(delayMillis);
}

IStorage* G3MBuilder_Wasm::createDefaultStorage() {
#warning TODO: Storage_Wasm 
  return NULL;
}

IDownloader* G3MBuilder_Wasm::createDefaultDownloader() {
  const int maxConcurrentOperationCount = 8;
  const int delayMillis = 10;
  const bool verboseErrors = true;
  const std::string proxy = "";
  return new Downloader_Wasm(maxConcurrentOperationCount, delayMillis, proxy, verboseErrors);
}

void G3MBuilder_Wasm::addGPUProgramSources() {
  const BasicShadersGL2 basicShaders;
  for (int i = 0; i < basicShaders.size(); i++) {
    IG3MBuilder::addGPUProgramSources(basicShaders.get(i));
  }
}

G3MWidget_Wasm* G3MBuilder_Wasm::createWidget() {
  G3MWidget_Wasm* nativeWidget = new G3MWidget_Wasm();
  if (nativeWidget->isWebGLSupported()) {
    addGPUProgramSources();

    nativeWidget->initSingletons();

    setGL(nativeWidget->getGL());

    nativeWidget->setG3MWidget(create());
    nativeWidget->startWidget();
  }

  return nativeWidget;
}
