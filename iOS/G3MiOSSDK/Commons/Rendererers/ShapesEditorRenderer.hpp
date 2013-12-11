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
  ShapesRenderer* _vertexRenderer;
  std::vector<RasterShapes> _rasterShapes;
  
public:
  ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer,
                       ShapesRenderer* vertexRenderer);
  
  void addShape(Shape* shape);
  
  void selectShape(Shape* shape);
  
  void cleanVertexRenderer() const {
    _vertexRenderer->removeAllShapes();
  }
};



#endif /* defined(__G3MiOSSDK__ShapesEditorRenderer__) */
