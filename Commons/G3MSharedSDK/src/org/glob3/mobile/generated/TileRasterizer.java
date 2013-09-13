package org.glob3.mobile.generated; 
public abstract class TileRasterizer
{
  private ChangedListener _listener;
  private boolean _enable;

  protected TileRasterizer()
  {
     _enable = true;
     _listener = null;

  }


  public void dispose()
  {
  }

  public abstract String getId();

  public final void rasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodelete)
  {
    if (_enable)
    {
      rawRasterize(image, trc, listener, autodelete);
    }
    else
    {
      listener.imageCreated(image);
      if (autodelete)
      {
        if (listener != null)
           listener.dispose();
      }
    }
  }

  public abstract void rawRasterize(IImage image, TileRasterizerContext trc, IImageListener listener, boolean autodelete);

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

  public final boolean isEnable()
  {
    return _enable;
  }

  public final void setEnable(boolean enable)
  {
    if (enable != _enable)
    {
      _enable = enable;
      notifyChanges();
    }
  }

}