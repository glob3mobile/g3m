package org.glob3.mobile.generated; 
//
//  TerrainElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//

//
//  TerrainElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/13/16.
//
//



//class RenderState;
//class G3MContext;
//class Sector;
//class Vector2I;
//class TerrainElevationListener;


public abstract class TerrainElevationProvider extends RCObject
{
  public void dispose()
  {
    super.dispose();
  }


  public abstract RenderState getRenderState();

  public abstract void initialize(G3MContext context);

  public abstract void cancel();

  public abstract long subscribe(Sector sector, Vector2I resolution, TerrainElevationListener listener);

  public abstract void unsubscribe(long subscriptionID, boolean deleteListener);

}