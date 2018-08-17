package org.glob3.mobile.generated;import java.util.*;

//
//  GEOFeatureCollection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOFeatureCollection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOFeature;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLGlobalState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOSymbolizer;

public class GEOFeatureCollection extends GEOObject
{
  private java.util.ArrayList<GEOFeature> _features = new java.util.ArrayList<GEOFeature>();

  private static java.util.ArrayList<GEOFeature> copy(java.util.ArrayList<GEOFeature> features)
  {
	java.util.ArrayList<GEOFeature> result = new java.util.ArrayList<GEOFeature>();
	final int size = features.size();
	for (int i = 0; i < size; i++)
	{
	  GEOFeature feature = features.get(i);
	  result.add((feature == null) ? null : feature.deepCopy());
	}
	return result;
  }

  public GEOFeatureCollection(java.util.ArrayList<GEOFeature> features)
  {
	  _features = features;
  }

  public void dispose()
  {
	final int featuresCount = _features.size();
	for (int i = 0; i < featuresCount; i++)
	{
	  GEOFeature feature = _features.get(i);
	  if (feature != null)
		  feature.dispose();
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	final int featuresCount = _features.size();
	for (int i = 0; i < featuresCount; i++)
	{
	  GEOFeature feature = _features.get(i);
	  feature.symbolize(rc, symbolizer, meshRenderer, shapesRenderer, marksRenderer, geoVectorLayer);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeature* get(int i) const
  public final GEOFeature get(int i)
  {
	return _features.get(i);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _features.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterize(const GEORasterSymbolizer* symbolizer, ICanvas* canvas, const GEORasterProjection* projection, int tileLevel) const
  public final void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
	final int featuresCount = _features.size();
	for (int i = 0; i < featuresCount; i++)
	{
	  GEOFeature feature = _features.get(i);
	  if (feature != null)
	  {
		feature.rasterize(symbolizer, canvas, projection, tileLevel);
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	long result = 0;
	final int featuresCount = _features.size();
	for (int i = 0; i < featuresCount; i++)
	{
	  GEOFeature feature = _features.get(i);
	  result += feature.getCoordinatesCount();
	}
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeatureCollection* deepCopy() const
  public final GEOFeatureCollection deepCopy()
  {
	return new GEOFeatureCollection(copy(_features));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet, const VectorStreamingRenderer::Node* node) const
  public final long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
	long result = 0;
	final int featuresCount = _features.size();
	for (int i = 0; i < featuresCount; i++)
	{
	  GEOFeature feature = _features.get(i);
	  if (feature != null)
	  {
		result += feature.createFeatureMarks(vectorSet, node);
	  }
	}
	return result;
  }

}
