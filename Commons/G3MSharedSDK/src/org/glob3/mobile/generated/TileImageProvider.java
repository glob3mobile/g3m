package org.glob3.mobile.generated;
//
//  TileImageProvider.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//
//  TileImageProvider.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//class TileImageContribution;
//class Tile;
//class Vector2S;
//class TileImageListener;


//class FrameTasksExecutor;

public abstract class TileImageProvider extends RCObject
{
  public void dispose()
  {
    super.dispose();
  }


  public abstract TileImageContribution contribution(Tile tile);

  public abstract void create(Tile tile, TileImageContribution contribution, Vector2S resolution, long tileTextureDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor);

  public abstract void cancel(String tileID);

}