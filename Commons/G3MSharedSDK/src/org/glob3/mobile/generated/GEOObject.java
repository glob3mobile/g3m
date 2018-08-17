package org.glob3.mobile.generated;//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterSymbolizer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterProjection;
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



public abstract class GEOObject
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void rasterize(const GEORasterSymbolizer* symbolizer, ICanvas* canvas, const GEORasterProjection* projection, int tileLevel) const = 0;
  public abstract void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const = 0;
  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long getCoordinatesCount() const = 0;
  public abstract long getCoordinatesCount();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual GEOObject* deepCopy() const = 0;
  public abstract GEOObject deepCopy();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet, const VectorStreamingRenderer::Node* node) const = 0;
  public abstract long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node);

}
