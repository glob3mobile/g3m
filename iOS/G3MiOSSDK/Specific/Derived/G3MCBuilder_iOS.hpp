//
//  G3MCBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#ifndef __G3MiOSSDK__G3MCBuilder_iOS__
#define __G3MiOSSDK__G3MCBuilder_iOS__

#include "G3MCBuilder.hpp"
#include "G3MWidget_iOS.h"



class G3MCBuilder_iOS : public G3MCBuilder {
private:
  G3MWidget_iOS* _nativeWidget;

protected:

  IStorage* createStorage();

  IDownloader* createDownloader();

  IThreadUtils* createThreadUtils();

public:
  G3MCBuilder_iOS(G3MWidget_iOS* nativeWidget,
                  const URL& serverURL,
                  const URL& tubesURL,
                  bool useWebSockets,
                  const std::string& sceneId,
                  G3MCSceneChangeListener* sceneListener);

  void initializeWidget();
  
};

#endif
