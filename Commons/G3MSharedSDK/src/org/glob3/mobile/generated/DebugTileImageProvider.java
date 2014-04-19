package org.glob3.mobile.generated; 
//
//  DebugTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//

//
//  DebugTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/18/14.
//
//




public class DebugTileImageProvider extends CanvasTileImageProvider
{
  private static class ImageListener extends IImageListener
  {
    private Tile _tile;
    private TileImageListener _listener;
    private boolean _deleteListener;

    public ImageListener(Tile tile, TileImageListener listener, boolean deleteListener)
    {
       _listener = listener;
       _deleteListener = deleteListener;
    }

    public final void imageCreated(IImage image)
    {
      _listener.imageCreated(_tile, image, _tile._sector, new RectangleF(0, 0, image.getWidth(), image.getHeight()), 1); // alpha
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
    }

  }

  public void dispose()
  {
    super.dispose();
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return TileImageContribution.FULL_COVERAGE_TRANSPARENT;
  }

  public final void create(Tile tile, Vector2I resolution, TileImageListener listener, boolean deleteListener)
  {
    final int width = resolution._x;
    final int height = resolution._y;
  
    ICanvas canvas = getCanvas(width, height);
  
    canvas.setLineColor(Color.green());
    canvas.setLineWidth(1);
    canvas.strokeRectangle(0, 0, width, height);
  
    canvas.createImage(new DebugTileImageProvider.ImageListener(tile, listener, deleteListener), true);
  }

  public final void cancel(Tile tile)
  {
    // do nothing, can't cancel
  }

}