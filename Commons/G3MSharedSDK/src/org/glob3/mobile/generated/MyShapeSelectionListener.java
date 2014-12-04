package org.glob3.mobile.generated; 
public class MyShapeSelectionListener extends SimpleShapeSelectionListener
{
  private ShapesEditorRenderer _renderer;


  public Shape _selectedShape;

  public MyShapeSelectionListener(ShapesEditorRenderer renderer)
  {
     _renderer = renderer;
     _selectedShape = null;
  }

  public final boolean touchedShape(Shape shape)
  {
    if (_renderer.creatingShape())
      return true;

    super.touchedShape(shape);
    _selectedShape = getSelectedShape();
    if (_selectedShape == null)
    {
      _renderer.setSelectedVertex(-1);
      _renderer._selectedRasterShape = -1;
      _renderer.clearVertexShapes();
      return true;
    }

    int id = _renderer.getRasterShapeId(_selectedShape);
    if (id>=0)
    {
      _renderer.setSelectedVertex(-1);
      _renderer.selectRasterShape(id);
      return true;
    }

    id = _renderer.getVertexShapeId(_selectedShape);
    _renderer.setSelectedVertex(id);
    if (id<0)
    {
      _renderer.clearVertexShapes();
    }
    return true;
  }

}