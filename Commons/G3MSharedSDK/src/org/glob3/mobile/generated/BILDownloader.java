package org.glob3.mobile.generated; 
//
//  BILDownloader.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/16.
//
//

//
//  BILDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/6/16.
//
//


//class G3MContext;
//class URL;
//class Sector;
//class Vector2I;
//class TimeInterval;
//class ShortBufferTerrainElevationGrid;


public class BILDownloader
{
  private BILDownloader()
  {
  }


  public abstract static class Handler
  {
    public void dispose()
    {
    }

    public abstract void onDownloadError(G3MContext context, URL url);

    public abstract void onParseError(G3MContext context);

    public abstract void onBIL(G3MContext context, ShortBufferTerrainElevationGrid result);
  }

  public static void request(G3MContext context, URL url, long priority, TimeInterval timeToCache, boolean readExpired, Sector sector, Vector2I extent, double deltaHeight, short noDataValue, BILDownloader.Handler handler, boolean deleteHandler)
  {
  
  
    context.getDownloader().requestBuffer(url, priority, timeToCache, readExpired, new BILDownloader_BufferDownloadListener(sector, extent, noDataValue, deltaHeight,handler, deleteHandler, context), true);
  }


}