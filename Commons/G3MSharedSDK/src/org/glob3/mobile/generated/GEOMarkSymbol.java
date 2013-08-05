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
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
  
    MarksRenderer marksRenderer = sc.getMarksRenderer();
    if (marksRenderer == null)
    {
      ILogger.instance().logError("Can't simbolize with Mark, MarksRenderer was not set");
      if (_mark != null)
         _mark.dispose();
    }
    else
    {
      marksRenderer.addMark(_mark);
    }
    _mark = null;
  }

}