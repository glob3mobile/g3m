//
//  GLProgramId_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 19/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GLProgramId_iOS_hpp
#define G3MiOSSDK_GLProgramId_iOS_hpp

#include "IGLProgramId.hpp"

class GLProgramId_iOS{
private:
  const int _id;
public:
  GLProgramId_iOS(int id):_id(id){}
  
  int getID(){ return _id;}
  
  bool isValid() const { return _id > -1;}
  
};


#endif
