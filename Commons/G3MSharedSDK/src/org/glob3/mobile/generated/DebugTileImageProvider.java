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
    private final Tile _tile;
    private TileImageListener _listener;
    private boolean _deleteListener;

    private String getImageId(Tile tile)
    {
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("DebugTileImageProvider/");
      isb.addInt(tile._level);
      isb.addString("/");
      isb.addInt(tile._column);
      isb.addString("/");
      isb.addInt(tile._row);
      final String s = isb.getString();
      if (isb != null)
         isb.dispose();
      return s;
    }

    public ImageListener(Tile tile, TileImageListener listener, boolean deleteListener)
    {
       _tile = tile;
       _listener = listener;
       _deleteListener = deleteListener;
    }

    public final void imageCreated(IImage image)
    {
      final String imageId = getImageId(_tile);
      _listener.imageCreated(_tile, image, imageId, _tile._sector, new RectangleF(0, 0, image.getWidth(), image.getHeight()), 1); // alpha
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
    //return FULL_COVERAGE_TRANSPARENT;
    return TileImageContribution.fullCoverageTransparent(1);
  }

  public final void create(Tile tile, Vector2I resolution, long tileDownloadPriority, TileImageListener listener, boolean deleteListener)
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