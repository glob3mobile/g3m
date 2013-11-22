package org.glob3.mobile.generated; 
//
//  ElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//


//class Sector;
//class Vector2I;
//class ElevationData;
//class G3MContext;
//class G3MRenderContext;



public interface IElevationDataListener
{
  void dispose();

  /**
   Callback method for ElevationData creation. Pointer is owned by Listener
   */
  void onData(Sector sector, Vector2I extent, ElevationData elevationData);

  void onError(Sector sector, Vector2I extent);

  void onCancel(Sector sector, Vector2I extent);

}