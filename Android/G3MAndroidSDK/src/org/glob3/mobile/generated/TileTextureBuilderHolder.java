package org.glob3.mobile.generated; 
public class TileTextureBuilderHolder implements ITexturizerData
{
  private TileTextureBuilder _builder;

  public TileTextureBuilderHolder(TileTextureBuilder builder)
  {
	  _builder = builder;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileTextureBuilder* get() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTexturizerData() const
  public final boolean isTexturizerData()
  {
	return true;
  }
}