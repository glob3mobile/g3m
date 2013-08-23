package org.glob3.mobile.generated; 
//
//  GEOLine2DMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

//
//  GEOLine2DMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//



//class GEOLine2DStyle;


public class GEOLine2DMeshSymbol extends GEOMeshSymbol
{
  private final java.util.ArrayList<Geodetic2D> _coordinates;

  private final Color _lineColor ;
  private final float _lineWidth;

  private double _deltaHeight;


  public GEOLine2DMeshSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEOLine2DStyle style)
  {
     this(coordinates, style, 0.0);
  }
  public GEOLine2DMeshSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEOLine2DStyle style, double deltaHeight)
  {
     _coordinates = coordinates;
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
    return createLine2DMesh(_coordinates, _lineColor, _lineWidth, _deltaHeight, rc.getPlanet());
  }

}