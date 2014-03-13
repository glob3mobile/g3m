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

  // useless, it's here only to make the C++ => Java translator creates an interface intead of an empty class
  public final void unusedMethod()
  {

  }

}