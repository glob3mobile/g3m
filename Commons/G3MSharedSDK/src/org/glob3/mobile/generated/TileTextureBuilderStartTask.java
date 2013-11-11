package org.glob3.mobile.generated; 
public class TileTextureBuilderStartTask extends FrameTask
{
  private TileTextureBuilder _builder;

  public TileTextureBuilderStartTask(TileTextureBuilder builder)
  {
     _builder = builder;
    _builder._retain();
  }

  public void dispose()
  {
    _builder._release();
  super.dispose();
  }

  public final void execute(G3MRenderContext rc)
  {
    _builder.start();
  }

  public final boolean isCanceled(G3MRenderContext rc)
  {
    return _builder.isCanceled();
  }
}