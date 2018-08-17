package org.glob3.mobile.generated;import java.util.*;

//
//  GEOShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Shape;

public class GEOShapeSymbol extends GEOSymbol
{
  private Shape _shape;

  public GEOShapeSymbol(Shape shape)
  {
	  _shape = shape;

  }

  public void dispose()
  {
	if (_shape != null)
		_shape.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	if (_shape != null)
	{
	  if (shapesRenderer == null)
	  {
		ILogger.instance().logError("Can't symbolize with Shape, ShapesRenderer was not set");
		if (_shape != null)
			_shape.dispose();
	  }
	  else
	  {
		shapesRenderer.addShape(_shape);
	  }
	  _shape = null;
	}
	return true;
  }

}
