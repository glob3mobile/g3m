//
//  G3MCBuilder_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "G3MCBuilder_iOS.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "ThreadUtils_iOS.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "GPUProgramManager.hpp"

G3MCBuilder_iOS::G3MCBuilder_iOS(G3MWidget_iOS* nativeWidget,
                                 const URL& serverURL,
                                 const URL& tubesURL,
                                 bool useWebSockets,
                                 const std::string& sceneId,
                                 G3MCSceneChangeListener* sceneListener) :
G3MCBuilder(serverURL, tubesURL, useWebSockets, sceneId, sceneListener),
_nativeWidget(nativeWidget)
{
  [_nativeWidget initSingletons];
}

void G3MCBuilder_iOS::initializeWidget() {
  setGL([_nativeWidget getGL]);

  [_nativeWidget setWidget: create()];
}

IStorage* G3MCBuilder_iOS::createStorage() {
  return new SQLiteStorage_iOS("g3m.cache");
}

IDownloader* G3MCBuilder_iOS::createDownloader() {
  const bool saveInBackground = true;
  IDownloader* downloader = new CachedDownloader(new Downloader_iOS(8),
                                                 getStorage(),
                                                 saveInBackground);

  return downloader;
}

IThreadUtils* G3MCBuilder_iOS::createThreadUtils() {
  return new ThreadUtils_iOS();
}

GPUProgramSources G3MCBuilder_iOS::loadDefaultGPUProgramSources(const std::string& name){
  
  NSString* nsName = [[NSString alloc] initWithUTF8String:name.c_str()];
  
  //GPU Program Sources
  NSString* vertShaderPathname = [[NSBundle mainBundle] pathForResource: nsName
                                                                 ofType: @"vsh"];
  if (!vertShaderPathname) {
    NSLog(@"Can't load Shader.vsh");
  }
  const std::string vertexSource ([[NSString stringWithContentsOfFile: vertShaderPathname
                                                             encoding: NSUTF8StringEncoding
                                                                error: nil] UTF8String]);
  
  NSString* fragShaderPathname = [[NSBundle mainBundle] pathForResource: nsName
                                                                 ofType: @"fsh"];
  if (!fragShaderPathname) {
    NSLog(@"Can't load Shader.fsh");
  }
  
  const std::string fragmentSource ([[NSString stringWithContentsOfFile: fragShaderPathname
                                                               encoding: NSUTF8StringEncoding
                                                                  error: nil] UTF8String]);
  
  return GPUProgramSources(name, vertexSource, fragmentSource);
}

GPUProgramManager* G3MCBuilder_iOS::createGPUProgramManager(){
  GPUProgramFactory * gpuProgramFactory = new GPUProgramFactory();
  
  gpuProgramFactory->add(loadDefaultGPUProgramSources("Billboard"));
  gpuProgramFactory->add(loadDefaultGPUProgramSources("Default"));
  gpuProgramFactory->add(loadDefaultGPUProgramSources("FlatColorMesh"));
  gpuProgramFactory->add(loadDefaultGPUProgramSources("TexturedMesh"));
  gpuProgramFactory->add(loadDefaultGPUProgramSources("ColorMesh"));
  gpuProgramFactory->add(loadDefaultGPUProgramSources("TransformedTexCoorTexturedMesh"));
  gpuProgramFactory->add(loadDefaultGPUProgramSources("TexturedMesh+PointLight"));

  return new GPUProgramManager(gpuProgramFactory);
}
