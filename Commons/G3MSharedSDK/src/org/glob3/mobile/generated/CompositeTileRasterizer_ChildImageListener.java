package org.glob3.mobile.generated; 
public class CompositeTileRasterizer_ChildImageListener extends IImageListener
{
  private final int _childIndex;
  private final java.util.ArrayList<TileRasterizer> _children;
  private final TileRasterizerContext _trc;
  private IImageListener _listener;
  private boolean _autodeleteListener;

  public CompositeTileRasterizer_ChildImageListener(int childIndex, java.util.ArrayList<TileRasterizer> children, TileRasterizerContext trc, IImageListener listener, boolean autodeleteListener)
  {
     _childIndex = childIndex;
     _children = children;
     _trc = trc;
     _listener = listener;
     _autodeleteListener = autodeleteListener;
  }

  public final void imageCreated(IImage image)
  {
    TileRasterizer child = _children.get(_childIndex);

    final int nextChildIndex = _childIndex + 1;

    if (nextChildIndex > _children.size()-1)
    {
      child.rasterize(image, _trc, _listener, _autodeleteListener);
    }
    else
    {
      child.rasterize(image, _trc, new CompositeTileRasterizer_ChildImageListener(nextChildIndex, _children, _trc, _listener, _autodeleteListener), true);
    }
  }

}