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
  
    super.dispose();
  
  }

}