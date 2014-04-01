package org.glob3.mobile.generated; 
public class TileTextureBuilderHolder implements ITexturizerData
{
  private TileTextureBuilder _builder;

  public TileTextureBuilderHolder(TileTextureBuilder builder)
  {
     _builder = builder;

  }

  public final TileTextureBuilder get()
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