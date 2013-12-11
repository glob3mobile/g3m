//
//  ShapesEditorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#include "ShapesEditorRenderer.hpp"
#include "PointShape.hpp"





class MyShapeSelectionListener : public SimpleShapeSelectionListener {
private:
  ShapesEditorRenderer* _renderer;
  
public:
  
  MyShapeSelectionListener(ShapesEditorRenderer* renderer):
  _renderer(renderer)
  {}
  
  bool touchedShape(Shape* shape) {
    SimpleShapeSelectionListener::touchedShape(shape);
    _renderer->cleanVertexRenderer();
    if (getSelectedShape() != NULL)
      _renderer->selectShape(shape);
    return true;
  }
};



ShapesEditorRenderer::ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer,
                                           ShapesRenderer* vertexRenderer):
ShapesRenderer(geoTileRasterizer),
_vertexRenderer(vertexRenderer)
{
  setShapeTouchListener(new MyShapeSelectionListener(this), true);
}


void ShapesEditorRenderer::selectShape(Shape* shape)
{
  /*// if shape is not a raster shape, return
  std::vector<Geodetic2D*> coordinates = shape->getCopyRasterCoordinates();
  if (coordinates.empty()) return;*/
  
  // if shape is not a raster shape, return
  int pos = -1;
  const int size = _rasterShapes.size();
  for (int i = 0; i < size; i++) {
    if (_rasterShapes[i]._shape == shape) {
      pos = i;
      break;
    }
  }
  if (pos<0) return;

  // creamos nuevos points para el vertexRenderer
  std::vector<Geodetic2D*> coordinates = _rasterShapes[pos]._coordinates;
  for (int n=0; n<coordinates.size(); n++) {
    Geodetic3D* position = new Geodetic3D(*coordinates[n], 1);
    Shape* vertex = new PointShape(position,
                                   RELATIVE_TO_GROUND,
                                   20,
                                   Color::fromRGBA(0.6f, 0.4f, 0.4f, 1));
    _vertexRenderer->addShape(vertex);
  }
}


void ShapesEditorRenderer::addShape(Shape* shape)
{
  ShapesRenderer::addShape(shape);
  
  // if shape is raster, it is saved in the class
  std::vector<Geodetic2D*> coordinates = shape->getCopyRasterCoordinates();
  if (!coordinates.empty())
    _rasterShapes.push_back(RasterShapes(shape, coordinates));
}

