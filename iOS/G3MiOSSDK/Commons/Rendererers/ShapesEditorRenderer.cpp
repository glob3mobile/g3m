//
//  ShapesEditorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#include "ShapesEditorRenderer.hpp"


ShapesEditorRenderer::ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer):
ShapesRenderer(geoTileRasterizer)
{
  // adding shape touch listener
  class MyShapeTouchListener : public ShapeTouchListener {
  private:
    Shape* _selectedShape = NULL;
  public:
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
      return true;
    }
  };
  
  setShapeTouchListener(new MyShapeTouchListener, true);
}
