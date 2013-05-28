//
//  CameraActivityListener_iOS.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 28/05/13.
//
//

#ifndef __G3MiOSSDK__CameraActivityListener_iOS__
#define __G3MiOSSDK__CameraActivityListener_iOS__

#include "ICameraActivityListener.hpp"
#include "ILogger.hpp"


class CameraActivityListener_iOS : public ICameraActivityListener {
public:
  
  CameraActivityListener_iOS() {}
  
  ~CameraActivityListener_iOS() {}
  
  void touchEventHandled(){
    ILogger::instance()->logInfo("Touch Event in CameraActivityListener");
  }
 
  
};

#endif /* defined(__G3MiOSSDK__CameraActivityListener_iOS__) */
