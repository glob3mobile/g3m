package org.glob3.mobile.generated; 
public abstract class DTT_TileTextureBuilderHolder implements ITexturizerData
{
  private DTT_TileTextureBuilder _builder;

  public DTT_TileTextureBuilderHolder(DTT_TileTextureBuilder builder)
  {
     _builder = builder;
  }

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

  ///#ifndef C_CODE
  //  void unusedMethod() { }
  ///#endif
}