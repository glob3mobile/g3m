//
//  ShapesEditorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#include "ShapesEditorRenderer.hpp"
#include "PointShape.hpp"


class MyShapeTouchListener : public ShapeTouchListener {
private:
  Shape* _selectedShape;
  ShapesEditorRenderer* _renderer;
  
public:
  
  MyShapeTouchListener(ShapesEditorRenderer* renderer):
  _renderer(renderer),
  _selectedShape(NULL)
  {}
  
  bool touchedShape(Shape* shape) {
    if (_selectedShape == NULL) {
      shape->select();
      _selectedShape = shape;
    } else {
      if (_selectedShape==shape) {
        shape->unselect();
        _selectedShape = NULL;
      } else {
        _selectedShape->unselect();
        _selectedShape = shape;
        shape->select();
      }
    }
    if (_selectedShape != NULL)
      _renderer->selectShape(shape);
    return true;
  }
};



ShapesEditorRenderer::ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer,
                                           ShapesRenderer* vertexRenderer):
ShapesRenderer(geoTileRasterizer),
_vertexRenderer(vertexRenderer)
{
  setShapeTouchListener(new MyShapeTouchListener(this), true);
}


void ShapesEditorRenderer::selectShape(Shape* shape)
{
  // if shape is not a raster shape, return
  std::vector<Geodetic2D*> coordinates = shape->getCopyRasterCoordinates();
  if (coordinates.empty()) return;
  
  for (int n=0; n<coordinates.size(); n++) {
    Geodetic3D* position = new Geodetic3D(*coordinates[n], 1);
    Shape* vertex = new PointShape(position,
                                   RELATIVE_TO_GROUND,
                                   20,
                                   Color::fromRGBA(0.6, 0.4, 0.4, 1));
    _vertexRenderer->addShape(vertex);
  }
}
