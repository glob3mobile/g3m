//
//  Disposable.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/13/13.
//
//

#ifndef G3MiOSSDK_Disposable_hpp
#define G3MiOSSDK_Disposable_hpp


#ifdef C_CODE
#define JAVA_POST_DISPOSE
#else
#define JAVA_POST_DISPOSE super.dispose();
#endif

class Disposable {
public:
  virtual ~Disposable() {

  }
};

#endif
