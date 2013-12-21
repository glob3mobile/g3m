package org.glob3.mobile.generated; 
//
//  ShapesEditorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

//
//  ShapesEditorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//






//class GEOTileRasterizer;
//class PointShape;
//class PlanetRenderer;
//class MyShapeSelectionListener;


public class RasterShapes
{
  public Shape _shape;
  public java.util.ArrayList<Geodetic2D> _coordinates = new java.util.ArrayList<Geodetic2D>();
  public float _borderWidth;
  public Color _borderColor;
  public Color _surfaceColor;

  public RasterShapes(Shape shape, java.util.ArrayList<Geodetic2D> coordinates, float borderWidth, Color borderColor, Color surfaceColor)
  {
     _shape = shape;
     _coordinates = coordinates;
     _borderWidth = borderWidth;
     _borderColor = new Color(borderColor);
     _surfaceColor = new Color(surfaceColor);
  }

  public RasterShapes(float borderWidth, Color borderColor, Color surfaceColor)
  {
     _borderWidth = borderWidth;
     _borderColor = new Color(borderColor);
     _surfaceColor = new Color(surfaceColor);
    _coordinates.clear();
  }

  public RasterShapes()
  {
  }
}