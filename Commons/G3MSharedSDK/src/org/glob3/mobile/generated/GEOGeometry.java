package org.glob3.mobile.generated;import java.util.*;

//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOSymbol;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOFeature;

public abstract class GEOGeometry extends GEOObject
{
  private GEOFeature _feature;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const = 0;
  protected abstract java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer);

  public GEOGeometry()
  {
	  _feature = null;

  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setFeature(GEOFeature* feature) const
  public final void setFeature(GEOFeature feature)
  {
	if (_feature != null)
	{
	  THROW_EXCEPTION("Logic error");
	}
	_feature = feature;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GEOFeature* getFeature() const
  public final GEOFeature getFeature()
  {
	return _feature;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	java.util.ArrayList<GEOSymbol> symbols = createSymbols(symbolizer);
	if (symbols != null)
	{
  
	  final int symbolsSize = symbols.size();
	  for (int i = 0; i < symbolsSize; i++)
	  {
		final GEOSymbol symbol = symbols.get(i);
		if (symbol != null)
		{
		  final boolean deleteSymbol = symbol.symbolize(rc, symbolizer, meshRenderer, shapesRenderer, marksRenderer, geoVectorLayer);
		  if (deleteSymbol)
		  {
			if (symbol != null)
				symbol.dispose();
		  }
		}
	  }
  
	  symbols = null;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual GEOGeometry* deepCopy() const = 0;
  public abstract GEOGeometry deepCopy();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet, const VectorStreamingRenderer::Node* node) const
  public long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
	return 0;
  }

}
