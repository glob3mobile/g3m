

#include "G3MBuilder_Wasm.hpp"

#include "ThreadUtils_Wasm.hpp"
#include "Downloader_Wasm"
#include "BasicShadersGL2.hpp"


G3MBuilder_Wasm::~G3MBuilder_Wasm() {
}


IThreadUtils* G3MBuilder_Wasm::createDefaultThreadUtils() {
  const int delayMillis = 10;
  return new ThreadUtils_Wasm(delayMillis);
}


IStorage* G3MBuilder_Wasm::createDefaultStorage() {
#warning TODO: Storage_Wasm 
  return null;
}


IDownloader* G3MBuilder_Wasm::createDefaultDownloader() {
  const int maxConcurrentOperationCount = 8;
  const int delayMillis = 10;
  const boolean verboseErrors = true;
  const std::string proxy = "";
  return new Downloader_Wasm(maxConcurrentOperationCount, delayMillis, proxy, verboseErrors);
}

G3MBuilder_Wasm::G3MBuilder_Wasm() :
  _nativeWidget(new G3MWidget_WebGL());
{
}

G3MBuilder_Wasm::G3MBuilder_Wasm(G3MWidget_Wasm* nativeWidget) :
  _nativeWidget(nativeWidget)
{
}

void G3MBuilder_Wasm::addGPUProgramSources() {
  const BasicShadersGL2 basicShaders;
  for (int i = 0; i < basicShaders.size(); i++) {
    addGPUProgramSources(basicShaders.get(i));
  }
}

G3MWidget_Wasm* G3MBuilder_Wasm::createWidget() const {
  G3MWidget_Wasm* nativeWidget = new G3MWidget_Wasm();
  if (nativeWidget->isWebGLSupported()) {
    addGPUProgramSources();

    setGL(nativeWidget.getGL());

    nativeWidget.setG3MWidget(create());
    nativeWidget.startWidget();
  }

  return nativeWidget;
}
