package org.glob3.mobile.generated; 
//class Tile;
//class TileImageListener;


public abstract class TileImageProvider
{
  public void dispose()
  {
  }

  public abstract TileImageContribution contribution(Tile tile);

  public abstract void create(Tile tile, TileImageListener listener, boolean deleteListener);

  public abstract void cancel(Tile tile);

}