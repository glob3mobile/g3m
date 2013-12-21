//
//  ShapesEditorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 11/12/13.
//
//

#include "ShapesEditorRenderer.hpp"
#include "PointShape.hpp"
#include "PlanetRenderer.hpp"
#include "TerrainTouchListener.hpp"
#include "RasterPolygonShape.hpp"
#include "RasterLineShape.hpp"

class MyTerrainTouchListener : public TerrainTouchListener {
private:
  ShapesEditorRenderer* _renderer;

public:
  MyTerrainTouchListener(ShapesEditorRenderer* renderer):
  _renderer(renderer)
  {}
  
  MyTerrainTouchListener() {}

  bool onTerrainTouch(const G3MEventContext* ec,
                      const Vector2I&        pixel,
                      const Camera*          camera,
                      const Geodetic3D&      position,
                      const Tile*            tile) {
    _renderer->onTouch(position);
    return true;
  }
};


class MyShapeSelectionListener : public SimpleShapeSelectionListener {
private:
  ShapesEditorRenderer* _renderer;
  
  
public:
  Shape* _selectedShape;
  
  MyShapeSelectionListener(ShapesEditorRenderer* renderer):
  _renderer(renderer),
  _selectedShape(NULL)
  {}
  
  bool touchedShape(Shape* shape) {
    if (_renderer->creatingShape())
      return true;
    
    SimpleShapeSelectionListener::touchedShape(shape);
    _selectedShape = getSelectedShape();
    if (_selectedShape == NULL) {
      _renderer->setSelectedVertex(-1);
      _renderer->_selectedRasterShape = -1;
      _renderer->clearVertexShapes();
      return true;
    }
    
    int id = _renderer->getRasterShapeId(_selectedShape);
    if (id>=0) {
      _renderer->setSelectedVertex(-1);
      _renderer->selectRasterShape(id);
      return true;
    }
    
    id = _renderer->getVertexShapeId(_selectedShape);
    _renderer->setSelectedVertex(id);
    if (id<0) {
      _renderer->clearVertexShapes();
    }
    return true;
  }
  
};



ShapesEditorRenderer::ShapesEditorRenderer(GEOTileRasterizer* geoTileRasterizer):
ShapesRenderer(geoTileRasterizer),
_shapeTouchListener(new MyShapeSelectionListener(this)),
_activatedEdition(false),
_selectedVertex(-1),
_selectedRasterShape(-1),
_creatingShape(false)
{
  setShapeTouchListener(_shapeTouchListener, true);
}


void ShapesEditorRenderer::clearVertexShapes()
{
  int size = _vertexShapes.size();
  for (int n=0; n<size; n++) {
    removeShape(_vertexShapes[n]);
  }
  _vertexShapes.clear();
}


void ShapesEditorRenderer::removeRasterShapesFromShapesRenderer()
{
  // remove raster shapes from shapeRenderer
  int size = _rasterShapes.size();
  for (int n=0; n<size; n++) {
    removeShape(_rasterShapes[n]._shape);
    _rasterShapes[n]._shape = NULL;
  }
}


void ShapesEditorRenderer::selectRasterShape(int id)
{
  _selectedRasterShape = id;

  // creamos nuevos points para el vertexRenderer
  std::vector<Geodetic2D*> coordinates = _rasterShapes[id]._coordinates;
  clearVertexShapes();
  for (int n=0; n<coordinates.size(); n++) {
    Geodetic2D* pos2D = coordinates[n];
    Geodetic3D* position = new Geodetic3D(*pos2D, 1);
    PointShape* vertex = new PointShape(position,
                                        RELATIVE_TO_GROUND,
                                        20,
                                        Color::fromRGBA(0.6f, 0.4f, 0.4f, 1));
    addShape(vertex);
    _vertexShapes.push_back(vertex);
  }
}


void ShapesEditorRenderer::addShape(Shape* shape)
{
  ShapesRenderer::addShape(shape);
  
  // if shape is raster, it is saved in the class
  std::vector<Geodetic2D*> coordinates = shape->getCopyRasterCoordinates();
  int size = coordinates.size();
  if (size == 2) {
    RasterLineShape* lineShape = (RasterLineShape*) shape;
    _rasterShapes.push_back(RasterShapes(shape,
                                         coordinates,
                                         lineShape->getWidth(),
                                         lineShape->getColor(),
                                         lineShape->getColor()));
  }
  
  if (size > 2) {
    RasterPolygonShape* polygonShape = (RasterPolygonShape*) shape;
    _rasterShapes.push_back(RasterShapes(shape,
                                         coordinates,
                                         polygonShape->getBorderWidth(),
                                         polygonShape->getBorderColor(),
                                         polygonShape->getSurfaceColor()));
  }
}


int ShapesEditorRenderer::getVertexShapeId(Shape* shape)
{
  int pos = -1;
  const int size = _vertexShapes.size();
  for (int i = 0; i < size; i++) {
    if (_vertexShapes[i] == shape) {
      pos = i;
      break;
    }
  }
  return pos;
}


int ShapesEditorRenderer::getRasterShapeId(Shape* shape)
{
  int pos = -1;
  const int size = _rasterShapes.size();
  for (int i = 0; i < size; i++) {
    if (_rasterShapes[i]._shape == shape) {
      pos = i;
      break;
    }
  }
  return pos;
}


void ShapesEditorRenderer::activateEdition(PlanetRenderer* planetRenderer)
{
  planetRenderer->addTerrainTouchListener(new MyTerrainTouchListener(this));
  _activatedEdition = true;
}


void ShapesEditorRenderer::onTouch(const Geodetic3D& position)
{
  
  // ***************************
  // ***************************
  // ***************************
  if (position._longitude._degrees<2.4) {
    printf ("\n---------- starting polygon\n");
    //startPolygon(3, Color::fromRGBA255(20, 30, 50, 255), Color::fromRGBA255(20, 30, 40, 60));
    startLine(7, Color::fromRGBA255(180, 30, 50, 255));
    return;
  }
  if (position._longitude._degrees>3.4) {
    printf ("\n---------- finishing polygon\n");
    //endPolygon();
    endLine();
    return;
  }
  // ***************************
  // ***************************
  // ***************************
  // ***************************
  // ***************************
  
  if (_creatingShape) {
    Geodetic2D pos2D = position.asGeodetic2D();
    Geodetic3D* vertexPosition = new Geodetic3D(pos2D, 1);
    PointShape* vertex = new PointShape(vertexPosition,
                                        RELATIVE_TO_GROUND,
                                        20,
                                        Color::fromRGBA(0.3f, 0.3f, 0.0f, 1));
    addShape(vertex);
    _vertexShapes.push_back(vertex);
    _shapeInCreation._coordinates.push_back(new Geodetic2D(pos2D));
    
    if (_rasterShapeKind==LINE_SHAPE && _shapeInCreation._coordinates.size()==2)
      endRasterShape();
    return;
  }
  
  if (_selectedVertex<0 || _selectedRasterShape<0) return;
  
  // clean vertex and raster shapes
  removeRasterShapesFromShapesRenderer();
  ShapesRenderer::_geoTileRasterizer->clear();

  // modify vertex
  delete _rasterShapes[_selectedRasterShape]._coordinates[_selectedVertex];
  _rasterShapes[_selectedRasterShape]._coordinates[_selectedVertex] = new Geodetic2D(position.asGeodetic2D());
  
  // create new points for vertex render
  clearVertexShapes();
  std::vector<Geodetic2D*> coordinates = _rasterShapes[_selectedRasterShape]._coordinates;
  for (int n=0; n<coordinates.size(); n++) {
    Geodetic2D* pos2D = coordinates[n];
    Geodetic3D* posVertex = new Geodetic3D(*pos2D, 1);
    PointShape* vertex = new PointShape(posVertex,
                                        RELATIVE_TO_GROUND,
                                        20,
                                        Color::fromRGBA(0.6f, 0.4f, 0.4f, 1));
    addShape(vertex);
    _vertexShapes.push_back(vertex);
  }
  
  // create again new raster shapes
  addRasterShapes();
  
  // select again the vertex
  _shapeTouchListener->touchedShape(_vertexShapes[_selectedVertex]);
}


void ShapesEditorRenderer::setSelectedVertex(int value)
{
  // if value is valid, remove raster shapes from shapesrenderer to allow clicking inside
  if (value>=0 && _rasterShapes[0]._shape!=NULL)
    removeRasterShapesFromShapesRenderer();
  
  // if value is null and raster shapes were removed, must be recovered
  if (value<0 && _rasterShapes[0]._shape==NULL)
    addRasterShapes();
  
  // set value
  _selectedVertex = value;
}


void ShapesEditorRenderer::addRasterShapes()
{
  Shape *shape;
  for (int n=0; n<_rasterShapes.size(); n++) {
    std::vector<Geodetic2D*> coordinates = _rasterShapes[n]._coordinates;
    if (coordinates.size() > 2) {
      std::vector<Geodetic2D*>* vertices = new std::vector<Geodetic2D*>;
      for (int k=0; k<coordinates.size(); k++) {
        Geodetic2D* pos2D = coordinates[k];
        vertices->push_back(new Geodetic2D(*pos2D));
      }
      shape = new RasterPolygonShape(vertices,
                                     _rasterShapes[n]._borderWidth,
                                     *_rasterShapes[n]._borderColor,
                                     *_rasterShapes[n]._surfaceColor);
    } else {
      Geodetic2D* pos0 = coordinates[0];
      Geodetic2D* pos1 = coordinates[1];
      shape = new RasterLineShape(new Geodetic2D(*pos0),
                                  new Geodetic2D(*pos1),
                                  _rasterShapes[n]._borderWidth,
                                  *_rasterShapes[n]._borderColor);
    }
    ShapesRenderer::addShape(shape);
    _rasterShapes[n]._shape = shape;
  }
}


void ShapesEditorRenderer::startRasterShape(float borderWidth,
                                            const Color& borderColor,
                                            const Color& surfaceColor)
{
  if (_creatingShape)
    endPolygon(true);
  
  // if there is something selected, unselect
  Shape* selectedShape = _shapeTouchListener->_selectedShape;
  if (selectedShape != NULL)
    _shapeTouchListener->touchedShape(selectedShape);
  
  _creatingShape = true;
  removeRasterShapesFromShapesRenderer();
  _shapeInCreation = RasterShapes(borderWidth, borderColor, surfaceColor);
}


void ShapesEditorRenderer::endRasterShape(bool cancelVertices)
{
  if (!_creatingShape)
    return;
  
  if (cancelVertices)
    _shapeInCreation._coordinates.clear();
  
  _creatingShape = false;
  clearVertexShapes();
  ShapesRenderer::_geoTileRasterizer->clear();
  addRasterShapes();
  
  std::vector<Geodetic2D*> coordinates = _shapeInCreation._coordinates;
  if (coordinates.size() > 2) {
    std::vector<Geodetic2D*>* vertices = new std::vector<Geodetic2D*>;
    for (int n=0; n<coordinates.size(); n++) {
      Geodetic2D* pos2D = coordinates[n];
      vertices->push_back(new Geodetic2D(*pos2D));
    }
    Shape* shape = new RasterPolygonShape(vertices,
                                          _shapeInCreation._borderWidth,
                                          *_shapeInCreation._borderColor,
                                          *_shapeInCreation._surfaceColor);
    addShape(shape);
  }
  if (coordinates.size()==2 && _rasterShapeKind==LINE_SHAPE) {
    Geodetic2D* pos0 = coordinates[0];
    Geodetic2D* pos1 = coordinates[1];
    Shape *shape = new RasterLineShape(new Geodetic2D(*pos0),
                                       new Geodetic2D(*pos1),
                                       _shapeInCreation._borderWidth,
                                       *_shapeInCreation._borderColor);
    addShape(shape);
  }
  
  _shapeInCreation._coordinates.clear();
}
