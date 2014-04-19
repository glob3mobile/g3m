package org.glob3.mobile.generated; 
//class Tile;
//class TileImageListener;
//class Vector2I;

public abstract class TileImageProvider
{
  public void dispose()
  {
  }

  public abstract TileImageContribution contribution(Tile tile);

  public abstract void create(Tile tile, Vector2I resolution, TileImageListener listener, boolean deleteListener);

  public abstract void cancel(Tile tile);

}