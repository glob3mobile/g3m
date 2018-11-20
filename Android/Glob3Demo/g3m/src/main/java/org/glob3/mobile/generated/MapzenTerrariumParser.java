package org.glob3.mobile.generated;
//
//  MapzenTerrariumParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

//
//  MapzenTerrariumParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//



//class FloatBufferDEMGrid;
//class IImage;


public class MapzenTerrariumParser
{
  private MapzenTerrariumParser()
  {
  }



  public abstract static class Listener
  {
    public void dispose()
    {

    }

    public abstract void onGrid(FloatBufferDEMGrid grid);
  }

  private static class ParserTask extends GAsyncTask
  {
    private final IImage _image;
    private final Sector _sector ;
    private final double _deltaHeight;
    private Listener _listener;
    private final boolean _deleteListener;

    private FloatBufferDEMGrid _result;

    public ParserTask(IImage image, Sector sector, double deltaHeight, Listener listener, boolean deleteListener)
    {
       _image = image;
       _sector = new Sector(sector);
       _deltaHeight = deltaHeight;
       _listener = listener;
       _deleteListener = deleteListener;
       _result = null;

    }

    public void dispose()
    {
      if (_result != null)
      {
        _result._release();
      }
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
      super.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      _result = MapzenTerrariumParser.parse(_image, _sector, _deltaHeight);
    }

    public final void onPostExecute(G3MContext context)
    {
      _listener.onGrid(_result);
      _result = null; // moved ownership to _listener
    }

  }


  public static FloatBufferDEMGrid parse(IImage image, Sector sector, double deltaHeight)
  {
    MutableColor255 pixel = new MutableColor255((byte) 0, (byte) 0, (byte) 0, (byte) 0);

    final int width = image.getWidth();
    final int height = image.getHeight();

    final int bufferSize = width * height;

    float[] buffer = new float[bufferSize];

    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        image.getPixel(x, y, pixel);
        final float elevation = ((pixel._red * 256.0f) + pixel._green + (pixel._blue / 256.0f)) - 32768.0f;
        final int index = ((height-1-y) * width) + x;
        buffer[index] = elevation;
      }
    }

    if (image != null)
       image.dispose();

    return new FloatBufferDEMGrid(WebMercatorProjection.instance(), sector, new Vector2I(width, height), buffer, bufferSize, deltaHeight);
  }

  public static void parse(G3MContext context, IImage image, Sector sector, double deltaHeight, Listener listener, boolean deleteListener)
  {
    final IThreadUtils threadUtils = context.getThreadUtils();
    GAsyncTask parserTask = new ParserTask(image, sector, deltaHeight, listener, deleteListener);
    threadUtils.invokeAsyncTask(parserTask, true);
  }

}
