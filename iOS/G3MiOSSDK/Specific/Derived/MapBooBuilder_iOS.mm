//
//  MapBooBuilder_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "MapBooBuilder_iOS.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "ThreadUtils_iOS.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "GPUProgramManager.hpp"
#include "BasicShadersGL2.hpp"

MapBooBuilder_iOS::MapBooBuilder_iOS(G3MWidget_iOS* nativeWidget,
                                     const URL& serverURL,
                                     const URL& tubesURL,
                                     const std::string& applicationId,
                                     MapBoo_ViewType viewType,
                                     MapBooApplicationChangeListener* applicationListener,
                                     bool enableNotifications) :
MapBooBuilder(serverURL,
              tubesURL,
              applicationId,
              viewType,
              applicationListener,
              enableNotifications),
_nativeWidget(nativeWidget)
{
  [_nativeWidget initSingletons];
}

void MapBooBuilder_iOS::initializeWidget() {
  setGL([_nativeWidget getGL]);

  [_nativeWidget setWidget: create()];
}

IStorage* MapBooBuilder_iOS::createStorage() {
  return new SQLiteStorage_iOS("g3m.cache");
}

IDownloader* MapBooBuilder_iOS::createDownloader() {
  const bool saveInBackground = true;
  return new CachedDownloader(new Downloader_iOS(8),
                                                 getStorage(),
                                                 saveInBackground);
}

IThreadUtils* MapBooBuilder_iOS::createThreadUtils() {
  return new ThreadUtils_iOS();
}

GPUProgramSources MapBooBuilder_iOS::loadDefaultGPUProgramSources(const std::string& name) {
  
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

GPUProgramManager* MapBooBuilder_iOS::createGPUProgramManager() {
  GPUProgramFactory * gpuProgramFactory = new BasicShadersGL2();
  return new GPUProgramManager(gpuProgramFactory);
}
