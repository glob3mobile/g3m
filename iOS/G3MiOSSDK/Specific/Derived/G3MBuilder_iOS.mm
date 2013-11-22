//
//  G3MBuilder_iOS.mm
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#import "G3MBuilder_iOS.hpp"
#include "Factory_iOS.hpp"
#include "ThreadUtils_iOS.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "BasicShadersGL2.hpp"

G3MBuilder_iOS::G3MBuilder_iOS(G3MWidget_iOS* nativeWidget) {
  _nativeWidget = nativeWidget;

  [_nativeWidget initSingletons];
}

void G3MBuilder_iOS::initializeWidget() {
  setGL([_nativeWidget getGL]);

  BasicShadersGL2 basicShaders;
  for (int i = 0; i < basicShaders.size(); i++) {
    addGPUProgramSources(basicShaders.get(i));
  }
  
  [_nativeWidget setWidget: create()];
}

IThreadUtils* G3MBuilder_iOS::createDefaultThreadUtils() {
  return new ThreadUtils_iOS();
}

IStorage* G3MBuilder_iOS::createDefaultStorage() {
  return new SQLiteStorage_iOS("g3m.cache");
}

IDownloader* G3MBuilder_iOS::createDefaultDownloader() {
  const bool saveInBackground = true;
  return new CachedDownloader(new Downloader_iOS(8),
                              getStorage(),
                              saveInBackground);
}

GPUProgramSources G3MBuilder_iOS::loadGPUProgramSources(const std::string& name) {

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
