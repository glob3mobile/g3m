package org.glob3.mobile.generated; 
public class Trail extends Disposable
{
  private boolean _visible;

  private Color _color ;
  private final float _ribbonWidth;

  private java.util.ArrayList<TrailSegment> _segments = new java.util.ArrayList<TrailSegment>();


  public Trail(Color color, float ribbonWidth)
  {
     _visible = true;
     _color = new Color(color);
     _ribbonWidth = ribbonWidth;
  }

  public void dispose()
  {
    //  delete _mesh;
    //
    //  const int positionsSize = _positions.size();
    //  for (int i = 0; i < positionsSize; i++) {
    //    const Geodetic3D* position = _positions[i];
    //    delete position;
    //  }
    final int segmentsSize = _segments.size();
    for (int i = 0; i < segmentsSize; i++)
    {
      TrailSegment segment = _segments.get(i);
      if (segment != null)
         segment.dispose();
    }
  
    super.dispose();
  
  }

  public final void render(G3MRenderContext rc, Frustum frustum, GLState state)
  {
    if (_visible)
    {
      final int segmentsSize = _segments.size();
      for (int i = 0; i < segmentsSize; i++)
      {
        TrailSegment segment = _segments.get(i);
        segment.render(rc, frustum, state);
      }
    }
  }

  public final void setVisible(boolean visible)
  {
    _visible = visible;
  }

  public final boolean isVisible()
  {
    return _visible;
  }

  public final void addPosition(Geodetic3D position)
  {
  
    final int lastSegmentIndex = _segments.size() - 1;
  
    TrailSegment currentSegment;
    if ((lastSegmentIndex < 0) || (_segments.get(lastSegmentIndex).getSize() > DefineConstants.MAX_POSITIONS_PER_SEGMENT))
    {
  
      TrailSegment newSegment = new TrailSegment(_color, _ribbonWidth);
      if (lastSegmentIndex >= 0)
      {
        TrailSegment previousSegment = _segments.get(lastSegmentIndex);
        previousSegment.setNextSegmentFirstPosition(position);
        newSegment.setPreviousSegmentLastPosition(previousSegment.getPreLastPosition());
        newSegment.addPosition(previousSegment.getLastPosition());
      }
      _segments.add(newSegment);
      currentSegment = newSegment;
    }
    else
    {
      currentSegment = _segments.get(lastSegmentIndex);
    }
  
    currentSegment.addPosition(position);
  }

}