package org.glob3.mobile.generated;import java.util.*;

//
//  GEOMarkSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

//
//  GEOMarkSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mark;


public class GEOMarkSymbol extends GEOSymbol
{
  private Mark _mark;

  public GEOMarkSymbol(Mark mark)
  {
	  _mark = mark;

  }

  public void dispose()
  {
	if (_mark != null)
		_mark.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	if (_mark != null)
	{
	  if (marksRenderer == null)
	  {
		ILogger.instance().logError("Can't symbolize with Mark, MarksRenderer was not set");
		if (_mark != null)
			_mark.dispose();
	  }
	  else
	  {
		marksRenderer.addMark(_mark);
	  }
	  _mark = null;
	}
	return true;
  }

}
