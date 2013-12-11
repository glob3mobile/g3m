//
//  ShapesEditorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#include "ShapesEditorRenderer.hpp"


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



ShapesEditorRenderer::ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer):
ShapesRenderer(geoTileRasterizer)
{
  setShapeTouchListener(new MyShapeTouchListener(this), true);
}


void ShapesEditorRenderer::selectShape(Shape* shape)
{
  // if shape is not a raster shape, return
  std::vector<Geodetic2D*> coordinates = shape->getCopyRasterCoordinates();
  if (coordinates.empty()) return;
  
  for (int n=0; n<coordinates.size(); n++)
    printf ("    %d: (%f %f)\n", n, coordinates[n]->_latitude._degrees,
            coordinates[n]->_longitude._degrees);
}
