package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEO2DPointGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//




public class GEO2DPointGeometry extends GEOGeometry2D
{
  private final Geodetic2D _position = new Geodetic2D();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const
  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
	return symbolizer.createSymbols(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const
  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
	return symbolizer.createSymbols(this);
  }


  public GEO2DPointGeometry(Geodetic2D position)
  {
	  _position = new Geodetic2D(position);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getPosition() const
  public final Geodetic2D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getCoordinatesCount() const
  public final long getCoordinatesCount()
  {
	return 1;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GEO2DPointGeometry* deepCopy() const
  public final GEO2DPointGeometry deepCopy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new GEO2DPointGeometry(_position);
	return new GEO2DPointGeometry(new Geodetic2D(_position));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet, const VectorStreamingRenderer::Node* node) const
  public final long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
	return vectorSet.createFeatureMark(node, this);
  }

}
