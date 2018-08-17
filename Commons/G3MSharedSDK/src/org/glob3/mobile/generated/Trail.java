package org.glob3.mobile.generated;import java.util.*;

public class Trail
{
  private boolean _visible;

  private Color _color = new Color();
  private final float _ribbonWidth;
  private final float _heightDelta;

  private java.util.ArrayList<TrailSegment> _segments = new java.util.ArrayList<TrailSegment>();


  public Trail(Color color, float ribbonWidth, float heightDelta)
  {
	  _visible = true;
	  _color = new Color(color);
	  _ribbonWidth = ribbonWidth;
	  _heightDelta = heightDelta;
  }

  public void dispose()
  {
	final int segmentsSize = _segments.size();
	for (int i = 0; i < segmentsSize; i++)
	{
	  TrailSegment segment = _segments.get(i);
	  if (segment != null)
		  segment.dispose();
	}
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isVisible() const
  public final boolean isVisible()
  {
	return _visible;
  }

  public final void addPosition(Angle latitude, Angle longitude, double height)
  {
	final int lastSegmentIndex = _segments.size() - 1;
  
	TrailSegment currentSegment;
	if (lastSegmentIndex < 0)
	{
	  TrailSegment newSegment = new TrailSegment(_color, _ribbonWidth);
	  _segments.add(newSegment);
	  currentSegment = newSegment;
	}
	else
	{
	  TrailSegment previousSegment = _segments.get(lastSegmentIndex);
	  if (previousSegment.getSize() > DefineConstants.MAX_POSITIONS_PER_SEGMENT)
	  {
		TrailSegment newSegment = new TrailSegment(_color, _ribbonWidth);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: previousSegment->setNextSegmentFirstPosition(latitude, longitude, height + _heightDelta);
		previousSegment.setNextSegmentFirstPosition(new Angle(latitude), new Angle(longitude), height + _heightDelta);
		newSegment.setPreviousSegmentLastPosition(previousSegment.getPreLastPosition());
		newSegment.addPosition(previousSegment.getLastPosition());
  
		_segments.add(newSegment);
		currentSegment = newSegment;
	  }
	  else
	  {
		currentSegment = previousSegment;
	  }
	}
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: currentSegment->addPosition(latitude, longitude, height + _heightDelta);
	currentSegment.addPosition(new Angle(latitude), new Angle(longitude), height + _heightDelta);
  }

  public final void addPosition(Geodetic3D position)
  {
	addPosition(position._latitude, position._longitude, position._height);
  }

  public final void clear()
  {
	final int segmentsSize = _segments.size();
	for (int i = 0; i < segmentsSize; i++)
	{
	  TrailSegment segment = _segments.get(i);
	  if (segment != null)
		  segment.dispose();
	}
	_segments.clear();
  }

}
