//
//  MapBooOLDBuilder_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "MapBooOLDBuilder_iOS.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "ThreadUtils_iOS.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "GPUProgramManager.hpp"
#include "BasicShadersGL2.hpp"
#include "Info.hpp"

MapBooOLDBuilder_iOS::MapBooOLDBuilder_iOS(G3MWidget_iOS* nativeWidget,
                                     const URL& serverURL,
                                     const URL& tubesURL,
                                     const std::string& applicationId,
                                     MapBooOLD_ViewType viewType,
                                     MapBooOLDApplicationChangeListener* applicationListener,
                                     bool enableNotifications,
                                     const std::string& token) :
MapBooOLDBuilder(serverURL,
              tubesURL,
              applicationId,
              viewType,
              applicationListener,
              enableNotifications,
              token),
_nativeWidget(nativeWidget)
{
  [_nativeWidget initSingletons];
}

void MapBooOLDBuilder_iOS::initializeWidget() {
  setGL([_nativeWidget getGL]);

  [_nativeWidget setWidget: create()];
}

IStorage* MapBooOLDBuilder_iOS::createStorage() {
  return new SQLiteStorage_iOS("g3m.cache");
}

IDownloader* MapBooOLDBuilder_iOS::createDownloader() {
  const bool saveInBackground = true;
  return new CachedDownloader(new Downloader_iOS(8),
                                                 getStorage(),
                                                 saveInBackground);
}

IThreadUtils* MapBooOLDBuilder_iOS::createThreadUtils() {
  return new ThreadUtils_iOS();
}

GPUProgramSources MapBooOLDBuilder_iOS::loadDefaultGPUProgramSources(const std::string& name) {
  
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

GPUProgramManager* MapBooOLDBuilder_iOS::createGPUProgramManager() {
  GPUProgramFactory * gpuProgramFactory = new BasicShadersGL2();
  return new GPUProgramManager(gpuProgramFactory);
}
