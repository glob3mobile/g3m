//
//  ChangedListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/27/13.
//
//

#ifndef __G3M__ChangedListener__
#define __G3M__ChangedListener__

class ChangedListener {
public:
#ifdef C_CODE
  virtual ~ChangedListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void changed() = 0;
};

#endif
