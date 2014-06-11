package org.glob3.mobile.generated; 
public class DTT_TileTextureBuilderStartTask extends FrameTask
{
  private DTT_TileTextureBuilder _builder;

  public DTT_TileTextureBuilderStartTask(DTT_TileTextureBuilder builder)
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