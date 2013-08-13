package org.glob3.mobile.generated; 
public abstract class TileRasterizer extends Disposable
{
  private ChangedListener _listener;


  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract String getId();

  public abstract void rasterize(TileRasterizerContext trc, IImageListener listener, boolean autodelete);

  public final void setChangeListener(ChangedListener listener)
  {
    if (_listener != null)
    {
      ILogger.instance().logError("Listener already set");
    }
    _listener = listener;
  }

  public final void notifyChanges()
  {
    if (_listener != null)
    {
      _listener.changed();
    }
  }

}