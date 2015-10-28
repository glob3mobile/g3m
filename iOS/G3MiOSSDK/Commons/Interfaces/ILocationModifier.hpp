//
//  ILocationModifier.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/10/15.
//
//

#ifndef ILocationModifier_h
#define ILocationModifier_h

/** Class used as modifier of GPS data from DeviceAttitudeCameraHandler**/

#include "Geodetic3D.hpp"

class ILocationModifier{
public:
  virtual ~ILocationModifier() {}
  
  /** Modifies the sensors position every frame **/
  virtual Geodetic3D modify(const Geodetic3D& location) = 0;
  
};


#endif /* ILocationModifier_h */
