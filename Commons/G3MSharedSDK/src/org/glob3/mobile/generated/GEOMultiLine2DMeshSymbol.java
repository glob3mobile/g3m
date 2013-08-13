package org.glob3.mobile.generated; 
//
//  GEOMultiLine2DMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

//
//  GEOMultiLine2DMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//


//class GEOLine2DStyle;

public class GEOMultiLine2DMeshSymbol extends GEOMeshSymbol
{
  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;

  private final Color _lineColor ;
  private final float _lineWidth;

  private double _deltaHeight;

  public GEOMultiLine2DMeshSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEOLine2DStyle style)
  {
     this(coordinatesArray, style, 0.0);
  }
  public GEOMultiLine2DMeshSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, GEOLine2DStyle style, double deltaHeight)
  {
     _coordinatesArray = coordinatesArray;
     _lineColor = new Color(style.getColor());
     _lineWidth = style.getWidth();
     _deltaHeight = deltaHeight;
  
  }

  public void dispose()
  {
  super.dispose();

  }

  public final Mesh createMesh(G3MRenderContext rc)
  {
    return createLines2DMesh(_coordinatesArray, _lineColor, _lineWidth, _deltaHeight, rc.getPlanet());
  }

}