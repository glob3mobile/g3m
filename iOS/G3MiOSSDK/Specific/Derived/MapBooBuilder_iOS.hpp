//
//  MapBooBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#ifndef __G3MiOSSDK__MapBooBuilder_iOS__
#define __G3MiOSSDK__MapBooBuilder_iOS__

#include "MapBooBuilder.hpp"
#include "G3MWidget_iOS.h"

class GPUProgramSources;

class MapBooBuilder_iOS : public MapBooBuilder {
private:
  G3MWidget_iOS* _nativeWidget;

  GPUProgramSources loadDefaultGPUProgramSources(const std::string& name);

protected:

  IStorage* createStorage();

  IDownloader* createDownloader();

  IThreadUtils* createThreadUtils();

  GPUProgramManager* createGPUProgramManager();

public:
  MapBooBuilder_iOS(G3MWidget_iOS* nativeWidget,
                    const URL& serverURL,
                    const URL& tubesURL,
                    const std::string& applicationId,
                    MapBoo_ViewType viewType,
                    MapBooApplicationChangeListener* applicationListener,
                    bool enableNotifications);

  void initializeWidget();
  
};

#endif
