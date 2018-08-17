package org.glob3.mobile.generated;import java.util.*;

//
//  GEOFeature.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOFeature.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLGlobalState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOSymbolizer;

public class GEOFeature extends GEOObject
{
  private final JSONBaseObject _id;
  private final GEOGeometry _geometry;
  private final JSONObject _properties;


  public GEOFeature(JSONBaseObject id, GEOGeometry geometry, JSONObject properties)
  {
	  _id = id;
	  _geometry = geometry;
	  _properties = properties;
	if (_geometry != null)
	{
	  _geometry.setFeature(this);
	}
  }

  public void dispose()
  {
	if (_id != null)
		_id.dispose();
	if (_geometry != null)
		_geometry.dispose();
	if (_properties != null)
		_properties.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const JSONObject* getProperties() const
  public final JSONObject getProperties()
  {
	return _properties;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GEOGeometry* getGeometry() const
  public final GEOGeometry getGeometry()
  {
	return _geometry;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	if (_geometry != null)
	{
	  _geometry.symbolize(rc, symbolizer, meshRenderer, shapesRenderer, marksRenderer, geoVectorLayer);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterize(const GEORasterSymbolizer* symbolizer, ICanvas* canvas, const GEORasterProjection* projection, int tileLevel) const
  public final void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
	if (_geometry != null)
	{
	  _geometry.rasterize(symbolizer, canvas, projection, tileLevel);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	return (_geometry == null) ? 0 : _geometry.getCoordinatesCount();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEOFeature* deepCopy() const
  public final GEOFeature deepCopy()
  {
	return new GEOFeature((_id == null) ? null : _id.deepCopy(), (_geometry == null) ? null : _geometry.deepCopy(), (_properties == null) ? null : _properties.deepCopy());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet, const VectorStreamingRenderer::Node* node) const
  public final long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
	if (_geometry != null)
	{
	  return _geometry.createFeatureMarks(vectorSet, node);
	}
	return 0;
  }

}
