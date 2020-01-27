//
//  GLTextureID.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#ifndef __G3MiOSSDK__GLTextureID__
#define __G3MiOSSDK__GLTextureID__

#include <string>

class IGLTextureID {
public:

  virtual bool isEquals(const IGLTextureID* that) const = 0;
  
  virtual const std::string description() const = 0;
  
#ifdef C_CODE
  virtual ~IGLTextureID() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
};

#endif
