//
//  MapBooOLDBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#ifndef __G3MiOSSDK__MapBooOLDBuilder_iOS__
#define __G3MiOSSDK__MapBooOLDBuilder_iOS__

#include "MapBooOLDBuilder.hpp"
#include "G3MWidget_iOS.h"

class GPUProgramSources;

class MapBooOLDBuilder_iOS : public MapBooOLDBuilder {
private:
  G3MWidget_iOS* _nativeWidget;

  GPUProgramSources loadDefaultGPUProgramSources(const std::string& name);

protected:

  IStorage* createStorage();

  IDownloader* createDownloader();

  IThreadUtils* createThreadUtils();

  GPUProgramManager* createGPUProgramManager();

public:
  MapBooOLDBuilder_iOS(G3MWidget_iOS* nativeWidget,
                    const URL& serverURL,
                    const URL& tubesURL,
                    const std::string& applicationId,
                    MapBooOLD_ViewType viewType,
                    MapBooOLDApplicationChangeListener* applicationListener,
                    bool enableNotifications,
                    const std::string& token);

  void initializeWidget();
  
};

#endif
