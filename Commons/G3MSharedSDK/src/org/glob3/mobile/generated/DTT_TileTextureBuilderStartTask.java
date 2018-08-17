package org.glob3.mobile.generated;public class DTT_TileTextureBuilderStartTask extends FrameTask
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
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
