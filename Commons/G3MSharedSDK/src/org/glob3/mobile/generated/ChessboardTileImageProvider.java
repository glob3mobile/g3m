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

  public void dispose()
  {
    if (_image != null)
       _image.dispose();
    super.dispose();
  }

  public ChessboardTileImageProvider()
  {
     this(8);
  }
  public ChessboardTileImageProvider(int splits)
  {
     _splits = splits;
     _image = null;
  }

  public final TileImageContribution contribution(Tile tile)
  {
    return TileImageContribution.fullCoverageOpaque();
  }

  public final void create(Tile tile, TileImageContribution contribution, Vector2I resolution, long tileDownloadPriority, boolean logDownloadActivity, TileImageListener listener, boolean deleteListener, FrameTasksExecutor frameTasksExecutor)
  {
    if (_image == null)
    {
      final int width = resolution._x;
      final int height = resolution._y;
  
      ICanvas canvas = IFactory.instance().createCanvas();
      canvas.initialize(width, height);
  
      canvas.setFillColor(Color.white());
      canvas.fillRectangle(0.0f, 0.0f, width, height);
  
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
  
          canvas.fillRoundedRectangle(x + 2, y + 2, xInterval - 4, yInterval - 4, 4);
          canvas.fillRoundedRectangle(x2 + 2, y2 + 2, xInterval - 4, yInterval - 4, 4);
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
      listener.imageCreated(tile._id, image, "ChessboardTileImageProvider_image", contribution);
      if (deleteListener)
      {
        if (listener != null)
           listener.dispose();
      }
    }
  }

  public final void cancel(String tileId)
  {
    // do nothing, can't cancel
  }

  public final void imageCreated(IImage image, Tile tile, TileImageListener listener, boolean deleteListener)
  {
    _image = image.shallowCopy();
  
    listener.imageCreated(tile._id, image, "ChessboardTileImageProvider_image", TileImageContribution.fullCoverageOpaque());
  
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

}