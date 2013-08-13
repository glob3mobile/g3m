package org.glob3.mobile.generated; 
public class GEORenderer_ObjectSymbolizerPair
{
  public final GEOObject _geoObject;
  public final GEOSymbolizer _symbolizer;

  public GEORenderer_ObjectSymbolizerPair(GEOObject geoObject, GEOSymbolizer symbolizer)
  {
     _geoObject = geoObject;
     _symbolizer = symbolizer;

  }

  public void dispose()
  {
    if (_geoObject != null)
       _geoObject.dispose();
    if (_symbolizer != null)
       _symbolizer.dispose();
  }
}