package org.glob3.mobile.generated; 
public class BILDownloader_ParserAsyncTask extends GAsyncTask
{
  private BILDownloader.Handler _handler;
  private final boolean _deleteHandler;
  private IByteBuffer _buffer;
  private final Sector _sector;
  private final Vector2I _extent;
  private final short _noDataValue;
  private final double _deltaHeight;

  private ShortBufferTerrainElevationGrid _result;

  public BILDownloader_ParserAsyncTask(IByteBuffer buffer, Sector sector, Vector2I extent, short noDataValue, double deltaHeight, BILDownloader.Handler handler, boolean deleteHandler)
  {
     _buffer = buffer;
     _sector = new Sector(sector);
     _extent = new Vector2I(extent);
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
      _handler.onParseError();
    }
    else
    {
      _handler.onBIL(_result);
      _result = null; // moves _result ownership to _handler
    }
  }

}