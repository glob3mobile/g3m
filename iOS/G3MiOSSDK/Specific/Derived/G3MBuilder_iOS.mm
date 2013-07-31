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

G3MBuilder_iOS::G3MBuilder_iOS(G3MWidget_iOS* nativeWidget) {
  _nativeWidget = nativeWidget;

  [_nativeWidget initSingletons];
}

void G3MBuilder_iOS::initializeWidget() {
  setGL([_nativeWidget getGL]);

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
