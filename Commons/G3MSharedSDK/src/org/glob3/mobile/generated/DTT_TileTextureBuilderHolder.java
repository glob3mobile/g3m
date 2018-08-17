package org.glob3.mobile.generated;public class DTT_TileTextureBuilderHolder implements ITexturizerData
{
  private DTT_TileTextureBuilder _builder;

  public DTT_TileTextureBuilderHolder(DTT_TileTextureBuilder builder)
  {
	  _builder = builder;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: DTT_TileTextureBuilder* get() const
  public final DTT_TileTextureBuilder get()
  {
	return _builder;
  }

  public void dispose()
  {
	if (_builder != null)
	{
	  _builder._release();
	}
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if ! C_CODE
  public final void unusedMethod()
  {
  }
//#endif
}
