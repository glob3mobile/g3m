package org.glob3.mobile.generated; 
public class ChessboardTileImageProvider_IImageListener extends IImageListener
{
  private ChessboardTileImageProvider _parent;
  private final Tile _tile;
  private TileImageListener _listener;
  private final boolean _deleteListener;


  public ChessboardTileImageProvider_IImageListener(ChessboardTileImageProvider parent, Tile tile, TileImageListener listener, boolean deleteListener)
  {
     _parent = parent;
     _tile = tile;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public final void imageCreated(IImage image)
  {
    _parent.imageCreated(image, _tile, _listener, _deleteListener);
  }
}