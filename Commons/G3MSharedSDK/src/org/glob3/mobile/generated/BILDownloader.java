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
//class TimeInterval;
//class ShortBufferTerrainElevationGrid;
//class IByteBuffer;



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
  
  
    context.getDownloader().requestBuffer(url, priority, timeToCache, readExpired, new BILDownloader.BufferDownloadListener(sector, extent, noDataValue, deltaHeight,handler, deleteHandler, context), true);
  }



  public static class ParserAsyncTask extends GAsyncTask
  {
    private BILDownloader.Handler _handler;
    private final boolean _deleteHandler;
    private IByteBuffer _buffer;
    private final Sector _sector ;
    private final Vector2I  _extent;
    private final short _noDataValue;
    private final double _deltaHeight;

    private ShortBufferTerrainElevationGrid _result;

    public ParserAsyncTask(IByteBuffer buffer, Sector sector, Vector2I extent, short noDataValue, double deltaHeight, BILDownloader.Handler handler, boolean deleteHandler)
    {
       _buffer = buffer;
       _sector = new Sector(sector);
       _extent = extent;
       _noDataValue = noDataValue;
       _deltaHeight = deltaHeight;
       _handler = handler;
       _deleteHandler = deleteHandler;
       _result = null;
    }

    public void dispose()
    {
      if (_buffer != null)
         _buffer.dispose();
      if (_result != null)
         _result.dispose();
      if (_deleteHandler)
      {
        if (_handler != null)
           _handler.dispose();
      }
      super.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      _result = BILParser.parseBIL16(_sector, _extent, _buffer, _noDataValue, _deltaHeight);
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    }

    public final void onPostExecute(G3MContext context)
    {
      if (_result == null)
      {
        _handler.onParseError(context);
      }
      else
      {
        _handler.onBIL(context, _result);
        _result = null; // moves _result ownership to _handler
      }
    }

  }




  public static class BufferDownloadListener extends IBufferDownloadListener
  {
    private final Sector _sector ;
    private final Vector2I  _extent;
    private final short _noDataValue;
    private final double _deltaHeight;
    private BILDownloader.Handler _handler;
    private final boolean _deleteHandler;
    private final G3MContext _context;

    public BufferDownloadListener(Sector sector, Vector2I extent, short noDataValue, double deltaHeight, BILDownloader.Handler handler, boolean deleteHandler, G3MContext context)
    {
       _sector = new Sector(sector);
       _extent = extent;
       _noDataValue = noDataValue;
       _deltaHeight = deltaHeight;
       _handler = handler;
       _deleteHandler = deleteHandler;
       _context = context;
    }


    public void dispose()
    {
      if (_deleteHandler)
      {
        if (_handler != null)
           _handler.dispose();
      }
      super.dispose();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      GAsyncTask parserTask = new BILDownloader.ParserAsyncTask(buffer, _sector, _extent, _noDataValue, _deltaHeight, _handler, _deleteHandler);
      _context.getThreadUtils().invokeAsyncTask(parserTask, true);
    
      _handler = null; // moves _handler ownership to ParserAsyncTask
    }

    public final void onError(URL url)
    {
      _handler.onDownloadError(_context, url);
    }

    public final void onCancel(URL url)
    {
      // do nothing!
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing!
    }

  }



}