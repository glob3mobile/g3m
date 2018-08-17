package org.glob3.mobile.generated;import java.util.*;

//
//  TileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//
//  TileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImageContribution;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2I;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImageListener;


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrameTasksExecutor;

public abstract class TileImageProvider extends RCObject
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public abstract TileImageContribution contribution(Tile tile);

  public abstract void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor);

  public abstract void cancel(String tileId);

}
