//
//  IGLUniformID.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 19/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IGLUniformID_hpp
#define G3MiOSSDK_IGLUniformID_hpp

class IGLUniformID {
public:
  virtual bool isValid() const = 0;
  
#ifdef C_CODE
  virtual ~IGLUniformID() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
};

#endif
