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
  Shape* _selectedShape = NULL;
public:
  bool touchedShape(Shape* shape) {
    
    std::vector<Geodetic2D*> coordinates = shape->getCopyRasterCoordinates();
    for (int n=0; n<coordinates.size(); n++)
      printf ("    %d: (%f %f)\n", n, coordinates[n]->_latitude._degrees,
              coordinates[n]->_longitude._degrees);
    
    
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
    return true;
  }
};



ShapesEditorRenderer::ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer):
ShapesRenderer(geoTileRasterizer)
{
  setShapeTouchListener(new MyShapeTouchListener, true);
}
