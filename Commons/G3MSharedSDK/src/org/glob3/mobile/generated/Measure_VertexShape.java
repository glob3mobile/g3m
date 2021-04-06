package org.glob3.mobile.generated;
public class Measure_VertexShape extends EllipsoidShape
{
  private final Color _color ;
  private final Color _selectedColor ;
  private Measure _measure;
  private final int _vertexIndex;


  public Measure_VertexShape(Geodetic3D position, double radius, Color color, Color selectedColor, Measure measure, String instanceID, int vertexIndex) // bool mercator -  bool texturedInside -  float borderWidth -  resolution
  {
     super(position, AltitudeMode.ABSOLUTE, new Vector3D(radius, radius, radius), (short) 16, 0, false, false, color);
     _color = color;
     _selectedColor = selectedColor;
     _measure = measure;
     _vertexIndex = vertexIndex;
    setToken(instanceID);
  }

  public final void setSelected(boolean selected)
  {
    setSurfaceColor(selected ? _selectedColor : _color);
  }

  public final boolean touched(G3MEventContext ec)
  {
    _measure.toggleSelection(_vertexIndex);

    return true;
  }

}