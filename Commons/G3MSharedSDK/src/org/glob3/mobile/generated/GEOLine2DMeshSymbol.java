package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOLine2DStyle;


public class GEOLine2DMeshSymbol extends GEOMeshSymbol
{
  private final java.util.ArrayList<Geodetic2D> _coordinates;

  private final Color _lineColor = new Color();
  private final float _lineWidth;

  private double _deltaHeight;


  public GEOLine2DMeshSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEOLine2DStyle style)
  {
	  this(coordinates, style, 0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GEOLine2DMeshSymbol(const java.util.ArrayList<Geodetic2D*>* coordinates, const GEOLine2DStyle& style, double deltaHeight = 0.0) : _coordinates(coordinates), _lineColor(style.getColor()), _lineWidth(style.getWidth()), _deltaHeight(deltaHeight)
  public GEOLine2DMeshSymbol(java.util.ArrayList<Geodetic2D> coordinates, GEOLine2DStyle style, double deltaHeight)
  {
	  _coordinates = coordinates;
	  _lineColor = new Color(style.getColor());
	  _lineWidth = style.getWidth();
	  _deltaHeight = deltaHeight;
  
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createMesh(const G3MRenderContext* rc) const
  public final Mesh createMesh(G3MRenderContext rc)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return createLine2DMesh(_coordinates, _lineColor, _lineWidth, _deltaHeight, rc->getPlanet());
	return createLine2DMesh(_coordinates, new Color(_lineColor), _lineWidth, _deltaHeight, rc.getPlanet());
  }

}
