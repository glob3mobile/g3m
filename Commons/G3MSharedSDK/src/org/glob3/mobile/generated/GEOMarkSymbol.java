package org.glob3.mobile.generated; 
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
  
    super.dispose();
  
  }

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