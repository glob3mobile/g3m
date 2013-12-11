//
//  ShapesEditorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#ifndef __G3MiOSSDK__ShapesEditorRenderer__
#define __G3MiOSSDK__ShapesEditorRenderer__


#include "ShapesRenderer.hpp"


class GEOTileRasterizer;
class PointShape;


struct RasterShapes {
  Shape* _shape;
  std::vector<Geodetic2D*> _coordinates;
  
  RasterShapes(Shape* shape, std::vector<Geodetic2D*> coordinates):
  _shape(shape),
  _coordinates(coordinates)
  {}
};


class ShapesEditorRenderer: public ShapesRenderer {
private:
  std::vector<RasterShapes> _rasterShapes;
  std::vector<PointShape*> _vertexShapes;
  
public:
  ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer);
  
  void addShape(Shape* shape);
  
  void selectShape(Shape* shape);
  
  void removeVertexShapes();
  
};



#endif /* defined(__G3MiOSSDK__ShapesEditorRenderer__) */
