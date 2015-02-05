//
//  ITexturizerData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/7/13.
//
//

#ifndef __G3MiOSSDK__ITexturizerData__
#define __G3MiOSSDK__ITexturizerData__

class ITexturizerData {
public:
#ifdef C_CODE
  virtual ~ITexturizerData() { }
#else
  // useless, it's here only to make the C++ => Java translator creates an interface intead of an empty class
  virtual void unusedMethod() const = 0;
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
};

#endif
