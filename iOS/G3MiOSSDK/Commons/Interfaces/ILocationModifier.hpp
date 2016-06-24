//
//  ILocationModifier.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 28/10/15.
//
//

#ifndef ILocationModifier_h
#define ILocationModifier_h

class Geodetic3D;

/** Class used as modifier of GPS data from DeviceAttitudeCameraHandler**/

class ILocationModifier{
public:
#ifdef C_CODE
  virtual ~ILocationModifier() {}
#endif
  
  /** 
   Modifies the sensors position every frame 
   **/
  virtual Geodetic3D modify(const Geodetic3D& location) = 0;
  
};


#endif /* ILocationModifier_h */
