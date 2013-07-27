package org.glob3.mobile.generated; 
//
//  TileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

//
//  TileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//



//class IImage;
//class Tile;
//class IImageListener;
//class ChangedListener;

public abstract class TileRasterizer
{
  private ChangedListener _listener;


  public void dispose()
  {

  }

  public abstract String getId();

  public abstract void rasterize(IImage image, Tile tile, boolean mercator, IImageListener listener, boolean autodelete);

  public final void setChangeListener(ChangedListener listener)
  {
    if (_listener != null)
    {
      ILogger.instance().logError("Listener already set");
    }
    _listener = listener;
  }

  public final void notifyChanges()
  {
    if (_listener != null)
    {
      _listener.changed();
    }
  }

}