//
//  DEMListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef DEMListener_hpp
#define DEMListener_hpp

class DEMGrid;


class DEMListener {
public:
#ifdef C_CODE
  virtual ~DEMListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void onGrid(DEMGrid* grid) = 0;

};

#endif
