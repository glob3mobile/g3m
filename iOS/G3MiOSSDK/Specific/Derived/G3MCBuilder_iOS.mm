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
