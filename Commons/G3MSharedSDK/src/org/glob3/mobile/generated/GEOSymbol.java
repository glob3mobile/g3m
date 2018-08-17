package org.glob3.mobile.generated;//
//  GEOSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOSymbolizer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MeshRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ShapesRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarksRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOVectorLayer;

public abstract class GEOSymbol
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const = 0;
  public abstract boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer);

}
