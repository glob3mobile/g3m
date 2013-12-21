package org.glob3.mobile.generated; 
public class SimpleShapeSelectionListener extends ShapeTouchListener
{
  private Shape _selectedShape = null;


  public final boolean touchedShape(Shape shape)
  {
    if (_selectedShape == null)
    {
      shape.select();
      _selectedShape = shape;
    }
    else
    {
      if (_selectedShape == shape)
      {
        shape.unselect();
        _selectedShape = null;
      }
      else
      {
        _selectedShape.unselect();
        _selectedShape = shape;
        shape.select();
      }
    }
    return true;
  }

  public final Shape getSelectedShape()
  {
    return _selectedShape;
  }
}