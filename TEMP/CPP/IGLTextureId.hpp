//
//  GLTextureId.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#ifndef __G3MiOSSDK__GLTextureId__
#define __G3MiOSSDK__GLTextureId__

#include <string>

class IGLTextureId {
public:

  virtual bool isEquals(const IGLTextureId* that) const = 0;
  
  virtual const std::string description() const = 0;
  
#ifdef C_CODE
  virtual ~IGLTextureId() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
};

#endif
