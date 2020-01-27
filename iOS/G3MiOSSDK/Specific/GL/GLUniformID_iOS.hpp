//
//  GLUniformID_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 19/09/12.
//

#ifndef G3MiOSSDK_GLUniformID_iOS
#define G3MiOSSDK_GLUniformID_iOS

#include "G3MSharedSDK/IGLUniformID.hpp"


class GLUniformID_iOS: public IGLUniformID {
private:
  const int _id;

public:
  GLUniformID_iOS(int id) :
  _id(id)
  {

  }
  
  int getID() const {
    return _id;

  }
  
  bool isValid() const {
    return _id > -1;
  }
  
};


#endif
