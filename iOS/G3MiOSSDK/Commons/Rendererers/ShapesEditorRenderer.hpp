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
class PlanetRenderer;
class MyShapeSelectionListener;


struct RasterShapes {
  Shape* _shape;
  std::vector<Geodetic2D*> _coordinates;
  float _borderWidth;
  Color* _borderColor;
  Color* _surfaceColor;
  
  RasterShapes(Shape* shape,
               std::vector<Geodetic2D*> coordinates,
               float borderWidth,
               Color* borderColor,
               Color* surfaceColor):
  _shape(shape),
  _coordinates(coordinates),
  _borderWidth(borderWidth),
  _borderColor(new Color(*borderColor)),
  _surfaceColor(new Color(*surfaceColor))
  {
    printf ("boderColor=%f, %f, %f\n", borderColor->_red, borderColor->_green, borderColor->_blue);
    printf ("_boderColor=%f, %f, %f\n", _borderColor->_red, _borderColor->_green, _borderColor->_blue);
  }
};


class ShapesEditorRenderer: public ShapesRenderer {
private:
  std::vector<RasterShapes> _rasterShapes;
  std::vector<PointShape*> _vertexShapes;
  
  bool _activatedEdition;
  int _selectedVertex;
  
  MyShapeSelectionListener* _shapeTouchListener;
  
  bool _creatingShape;
  
  void removeRasterShapesFromShapesRenderer();
  void addRasterShapes();
  
  int getSelectedVertex() {
    return _selectedVertex;
  }
  
public:
  int _selectedRasterShape;

  ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer);

  void addShape(Shape* shape);
  
  void onTouch(const Geodetic3D& position);
  void setSelectedVertex(int value);
  void clearVertexShapes();
  
  int getRasterShapeId(Shape* shape);
  void selectRasterShape(int id);
  int getVertexShapeId(Shape* shape);

  void activateEdition(PlanetRenderer* planetRenderer);
  
  void startPolygon(float borderWidth,
                    const Color& borderColor,
                    const Color& surfaceColor);
  void endPolygon();

};



#endif /* defined(__G3MiOSSDK__ShapesEditorRenderer__) */
