//
//  XPCPointCloudUpdateListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 3/13/21.
//

#ifndef XPCPointCloudUpdateListener_hpp
#define XPCPointCloudUpdateListener_hpp

#include <string>


class XPCPointCloudUpdateListener {

public:
  virtual ~XPCPointCloudUpdateListener() {

  }

  virtual void onPointCloudUpdateSuccess(const long long updatedPoints) = 0;

  virtual void onPointCloudUpdateFail(const std::string& errorMessage) = 0;

};

#endif
