package org.glob3.mobile.generated; 
public class ShapesEditorRenderer extends ShapesRenderer
{
  private java.util.ArrayList<RasterShapes> _rasterShapes = new java.util.ArrayList<RasterShapes>();
  private java.util.ArrayList<PointShape> _vertexShapes = new java.util.ArrayList<PointShape>();

  private boolean _activatedEdition;
  private int _selectedVertex;

  private MyShapeSelectionListener _shapeTouchListener;

  private boolean _creatingShape;
  private RasterShapes _shapeInCreation = new RasterShapes();

  private enum AnonymousEnum
  {
    LINE_SHAPE,
    POLYGON_SHAPE;

     public int getValue()
     {
        return this.ordinal();
     }

     public static AnonymousEnum forValue(int value)
     {
        return values()[value];
     }
  }
  private AnonymousEnum _rasterShapeKind;


  private void removeRasterShapesFromShapesRenderer()
  {
    // remove raster shapes from shapeRenderer
    int size = _rasterShapes.size();
    for (int n = 0; n<size; n++)
    {
      removeShape(_rasterShapes.get(n)._shape);
      _rasterShapes.get(n)._shape = null;
    }
  }
  private void addRasterShapes()
  {
    Shape shape;
    for (int n = 0; n<_rasterShapes.size(); n++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = _rasterShapes.get(n)._coordinates;
      if (coordinates.size() > 2)
      {
        java.util.ArrayList<Geodetic2D> vertices = new java.util.ArrayList<Geodetic2D>();
        for (int k = 0; k<coordinates.size(); k++)
        {
          Geodetic2D pos2D = coordinates.get(k);
          vertices.add(new Geodetic2D(pos2D));
        }
        shape = new RasterPolygonShape(vertices, _rasterShapes.get(n)._borderWidth, _rasterShapes.get(n)._borderColor, _rasterShapes.get(n)._surfaceColor);
      }
      else
      {
        Geodetic2D pos0 = coordinates.get(0);
        Geodetic2D pos1 = coordinates.get(1);
        shape = new RasterLineShape(new Geodetic2D(pos0), new Geodetic2D(pos1), _rasterShapes.get(n)._borderWidth, _rasterShapes.get(n)._borderColor);
      }
      super.addShape(shape);
      _rasterShapes.get(n)._shape = shape;
    }
  }

  private int getSelectedVertex()
  {
    return _selectedVertex;
  }

  private void startRasterShape(float borderWidth, Color borderColor, Color surfaceColor)
  {
    if (_creatingShape)
      endPolygon(true);
  
    // if there is something selected, unselect
    Shape selectedShape = _shapeTouchListener._selectedShape;
    if (selectedShape != null)
      _shapeTouchListener.touchedShape(selectedShape);
  
    _creatingShape = true;
    removeRasterShapesFromShapesRenderer();
    _shapeInCreation = new RasterShapes(borderWidth, borderColor, surfaceColor);
  }
  private void endRasterShape()
  {
     endRasterShape(false);
  }
  private void endRasterShape(boolean cancelVertices)
  {
    if (!_creatingShape)
      return;
  
    if (cancelVertices)
      _shapeInCreation._coordinates.clear();
  
    _creatingShape = false;
    clearVertexShapes();
    super._geoTileRasterizer.clear();
    addRasterShapes();
  
    java.util.ArrayList<Geodetic2D> coordinates = _shapeInCreation._coordinates;
    if (coordinates.size() > 2)
    {
      java.util.ArrayList<Geodetic2D> vertices = new java.util.ArrayList<Geodetic2D>();
      for (int n = 0; n<coordinates.size(); n++)
      {
        Geodetic2D pos2D = coordinates.get(n);
        vertices.add(new Geodetic2D(pos2D));
      }
      Shape shape = new RasterPolygonShape(vertices, _shapeInCreation._borderWidth, _shapeInCreation._borderColor, _shapeInCreation._surfaceColor);
      addShape(shape);
    }
    if (coordinates.size()==2 && _rasterShapeKind==AnonymousEnum.LINE_SHAPE)
    {
      Geodetic2D pos0 = coordinates.get(0);
      Geodetic2D pos1 = coordinates.get(1);
      Shape shape = new RasterLineShape(new Geodetic2D(pos0), new Geodetic2D(pos1), _shapeInCreation._borderWidth, _shapeInCreation._borderColor);
      addShape(shape);
    }
  
    _shapeInCreation._coordinates.clear();
  }


  public int _selectedRasterShape;

  public ShapesEditorRenderer(GEOTileRasterizer geoTileRasterizer)
  {
     super(geoTileRasterizer);
     _shapeTouchListener = new MyShapeSelectionListener(this);
     _activatedEdition = false;
     _selectedVertex = -1;
     _selectedRasterShape = -1;
     _creatingShape = false;
    setShapeTouchListener(_shapeTouchListener, true);
  }

  public final void addShape(Shape shape)
  {
    super.addShape(shape);
  
    // if shape is raster, it is saved in the class
    java.util.ArrayList<Geodetic2D> coordinates = shape.getCopyRasterCoordinates();
    int size = coordinates.size();
    if (size == 2)
    {
      RasterLineShape lineShape = (RasterLineShape) shape;
      _rasterShapes.add(new RasterShapes(shape, coordinates, lineShape.getWidth(), lineShape.getColor(), lineShape.getColor()));
    }
  
    if (size > 2)
    {
      RasterPolygonShape polygonShape = (RasterPolygonShape) shape;
      _rasterShapes.add(new RasterShapes(shape, coordinates, polygonShape.getBorderWidth(), polygonShape.getBorderColor(), polygonShape.getSurfaceColor()));
    }
  }

  public final void onTouch(Geodetic3D position)
  {
   /*
    // ***************************
    // *************************** TO TEST STARTPOLYGON AND ENDPOLYGON
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
    // ****************************/
  
    if (_creatingShape)
    {
      Geodetic2D pos2D = position.asGeodetic2D();
      Geodetic3D vertexPosition = new Geodetic3D(pos2D, 1);
      PointShape vertex = new PointShape(vertexPosition, AltitudeMode.RELATIVE_TO_GROUND, GlobalMembersShapesEditorRenderer.vertexWidth, Color.fromRGBA(0.3f, 0.3f, 0.0f, 1));
      addShape(vertex);
      _vertexShapes.add(vertex);
      _shapeInCreation._coordinates.add(new Geodetic2D(pos2D));
  
      if (_rasterShapeKind==AnonymousEnum.LINE_SHAPE && _shapeInCreation._coordinates.size()==2)
        endRasterShape();
      return;
    }
  
    if (_selectedVertex<0 || _selectedRasterShape<0)
       return;
  
    // clean vertex and raster shapes
    removeRasterShapesFromShapesRenderer();
    super._geoTileRasterizer.clear();
  
    // modify vertex
    if (_rasterShapes.get(_selectedRasterShape)._coordinates.get(_selectedVertex) != null)
       _rasterShapes.get(_selectedRasterShape)._coordinates.get(_selectedVertex).dispose();
    _rasterShapes.get(_selectedRasterShape)._coordinates.set(_selectedVertex, new Geodetic2D(position.asGeodetic2D()));
  
    // create new points for vertex render
    clearVertexShapes();
    java.util.ArrayList<Geodetic2D> coordinates = _rasterShapes.get(_selectedRasterShape)._coordinates;
    for (int n = 0; n<coordinates.size(); n++)
    {
      Geodetic2D pos2D = coordinates.get(n);
      Geodetic3D posVertex = new Geodetic3D(pos2D, 1);
      PointShape vertex = new PointShape(posVertex, AltitudeMode.RELATIVE_TO_GROUND, GlobalMembersShapesEditorRenderer.vertexWidth, Color.fromRGBA(0.6f, 0.4f, 0.4f, 1));
      addShape(vertex);
      _vertexShapes.add(vertex);
    }
  
    // create again new raster shapes
    addRasterShapes();
  
    // select again the vertex
    _shapeTouchListener.touchedShape(_vertexShapes.get(_selectedVertex));
  }
  public final void setSelectedVertex(int value)
  {
    // if value is valid, remove raster shapes from shapesrenderer to allow clicking inside
    if (value>=0 && _rasterShapes.get(0)._shape!=null)
      removeRasterShapesFromShapesRenderer();
  
    // if value is null and raster shapes were removed, must be recovered
    if (value<0 && _rasterShapes.get(0)._shape == null)
    {
      super._geoTileRasterizer.clear();
      addRasterShapes();
    }
  
    // set value
    _selectedVertex = value;
  }
  public final void clearVertexShapes()
  {
    int size = _vertexShapes.size();
    for (int n = 0; n<size; n++)
    {
      removeShape(_vertexShapes.get(n));
    }
    _vertexShapes.clear();
  }

  public final int getRasterShapeId(Shape shape)
  {
    int pos = -1;
    final int size = _rasterShapes.size();
    for (int i = 0; i < size; i++)
    {
      if (_rasterShapes.get(i)._shape == shape)
      {
        pos = i;
        break;
      }
    }
    return pos;
  }
  public final void selectRasterShape(int id)
  {
    _selectedRasterShape = id;
  
    // creamos nuevos points para el vertexRenderer
    java.util.ArrayList<Geodetic2D> coordinates = _rasterShapes.get(id)._coordinates;
    clearVertexShapes();
    for (int n = 0; n<coordinates.size(); n++)
    {
      Geodetic2D pos2D = coordinates.get(n);
      Geodetic3D position = new Geodetic3D(pos2D, 1);
      PointShape vertex = new PointShape(position, AltitudeMode.RELATIVE_TO_GROUND, GlobalMembersShapesEditorRenderer.vertexWidth, Color.fromRGBA(0.6f, 0.4f, 0.4f, 1));
      addShape(vertex);
      _vertexShapes.add(vertex);
    }
  }
  public final int getVertexShapeId(Shape shape)
  {
    int pos = -1;
    final int size = _vertexShapes.size();
    for (int i = 0; i < size; i++)
    {
      if (_vertexShapes.get(i) == shape)
      {
        pos = i;
        break;
      }
    }
    return pos;
  }

  public final void activateEdition(PlanetRenderer planetRenderer)
  {
    planetRenderer.addTerrainTouchListener(new MyTerrainTouchListener(this));
    _activatedEdition = true;
  }

  public final void startPolygon(float borderWidth, Color borderColor, Color surfaceColor)
  {
    _rasterShapeKind = AnonymousEnum.POLYGON_SHAPE;
    startRasterShape(borderWidth, borderColor, surfaceColor);
  }

  public final void endPolygon()
  {
     endPolygon(false);
  }
  public final void endPolygon(boolean cancelVertices)
  {
    endRasterShape(cancelVertices);
  }

  public final void startLine(float width, Color color)
  {
    _rasterShapeKind = AnonymousEnum.LINE_SHAPE;
    startRasterShape(width, color, color);
  }

  public final void endLine()
  {
     endLine(false);
  }
  public final void endLine(boolean cancelVertices)
  {
    endRasterShape(cancelVertices);
  }



  public final boolean creatingShape()
  {
    return _creatingShape;
  }
}