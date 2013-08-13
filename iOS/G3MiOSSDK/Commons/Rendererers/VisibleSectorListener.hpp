//
//  VisibleSectorListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/17/13.
//
//

#ifndef __G3MiOSSDK__VisibleSectorListener__
#define __G3MiOSSDK__VisibleSectorListener__

#include "Sector.hpp"

class VisibleSectorListener : public Disposable {
public:
  virtual ~VisibleSectorListener() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  virtual void onVisibleSectorChange(const Sector& visibleSector,
                                     const Geodetic3D& cameraPosition) = 0;

};

#endif
