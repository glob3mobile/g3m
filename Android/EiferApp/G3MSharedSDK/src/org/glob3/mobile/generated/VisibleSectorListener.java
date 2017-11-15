package org.glob3.mobile.generated; 
//
//  VisibleSectorListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/17/13.
//
//



public abstract class VisibleSectorListener
{
  public void dispose()
  {
  }

  public abstract void onVisibleSectorChange(Sector visibleSector, Geodetic3D cameraPosition);

}