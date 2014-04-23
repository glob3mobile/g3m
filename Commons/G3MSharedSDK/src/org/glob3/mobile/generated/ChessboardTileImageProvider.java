package org.glob3.mobile.generated; 
//
//  ChessboardTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//

//
//  ChessboardTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/23/14.
//
//



//class IImage;

public class ChessboardTileImageProvider extends TileImageProvider
{
  private final int _splits;

  private IImage _image;

  public ChessboardTileImageProvider()
  {
     this(8);
  }
  public ChessboardTileImageProvider(int splits)
  {
     _splits = splits;
     _image = null;
  }

  public void dispose()
  {
    if (_image != null)
       _image.dispose();
  }

  public final TileImageContribution contribution(Tile tile)
  {
    //return FULL_COVERAGE_TRANSPARENT;
    return TileImageContribution.fullCoverageTransparent(1);
  }

  public final void create(Tile tile, Vector2I resolution, long tileDownloadPriority, TileImageListener listener, boolean deleteListener)
  {
    if (_image == null)
    {
      final int width = resolution._x;
      final int height = resolution._y;
  
      ICanvas canvas = IFactory.instance().createCanvas();
      canvas.initialize(width, height);
  
      canvas.setFillColor(Color.white());
      canvas.fillRectangle(0, 0, width, height);
  
  
  //    canvas->setFillColor(Color::gray());
  
      canvas.setFillColor(Color.fromRGBA(0.9f, 0.9f, 0.35f, 1));
  
      final float xInterval = (float) width / _splits;
      final float yInterval = (float) height / _splits;
  
  //    for (int col = 0; col <= _splits; col += 2) {
  //      const float x  = col * xInterval;
  //      const float x2 = (col + 1) * xInterval;
  //      for (int row = 0; row <= _splits; row += 2) {
  //        const float y  = row * yInterval;
  //        const float y2 = (row + 1) * yInterval;
  //
  //        canvas->fillRectangle(x - (xInterval / 2),
  //                              y - (yInterval / 2),
  //                              xInterval,
  //                              yInterval);
  //        canvas->fillRectangle(x2 - (xInterval / 2),
  //                              y2 - (yInterval / 2),
  //                              xInterval,
  //                              yInterval);
  //      }
  //    }
  
      for (int col = 0; col < _splits; col += 2)
      {
        final float x = col * xInterval;
        final float x2 = (col + 1) * xInterval;
        for (int row = 0; row < _splits; row += 2)
        {
          final float y = row * yInterval;
          final float y2 = (row + 1) * yInterval;
  
          canvas.fillRoundedRectangle(x + 1, y + 1, xInterval - 2, yInterval - 2, 4);
          canvas.fillRoundedRectangle(x2 + 1, y2 + 1, xInterval - 2, yInterval - 2, 4);
        }
      }
  
  //    canvas->setLineColor(Color::magenta());
  //    canvas->strokeRectangle(0, 0, width, height);
  
      canvas.createImage(new ChessboardTileImageProvider_IImageListener(this, tile, listener, deleteListener), true);
  
      if (canvas != null)
         canvas.dispose();
    }
    else
    {
      IImage image = _image.shallowCopy();
      listener.imageCreated(tile, image, "ChessboardTileImageProvider_image", tile._sector, new RectangleF(0, 0, image.getWidth(), image.getHeight()), 1);
      if (deleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
    }
  }

  public final void cancel(Tile tile)
  {
    // do nothing, can't cancel
  }

  public final void imageCreated(IImage image, Tile tile, TileImageListener listener, boolean deleteListener)
  {
    _image = image.shallowCopy();
  
    listener.imageCreated(tile, image, "ChessboardTileImageProvider_image", tile._sector, new RectangleF(0, 0, image.getWidth(), image.getHeight()), 1);
  
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

}