package org.glob3.mobile.generated; 
//
//  Trail.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//

//
//  Trail.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//




//class Mesh;
//class Planet;
//class Frustum;
//class G3MRenderContext;
//class GLState;



public class Trail
{

  private static class Segment
  {
    private final Color _color ;
    private final float _ribbonWidth;

    private boolean _positionsDirty;
    private java.util.ArrayList<Geodetic3D> _positions = new java.util.ArrayList<Geodetic3D>();
    private Geodetic3D _nextSegmentFirstPosition;
    private Geodetic3D _previousSegmentLastPosition;

    private Mesh createMesh(Planet planet)
    {
      final int positionsSize = _positions.size();
    
      if (positionsSize < 2)
      {
        return null;
      }
    
    
      java.util.ArrayList<Double> anglesInRadians = new java.util.ArrayList<Double>();
      for (int i = 1; i < positionsSize; i++)
      {
        final Geodetic3D current = _positions.get(i);
        final Geodetic3D previous = _positions.get(i - 1);
    
        final double angleInRadians = Geodetic2D.bearingInRadians(previous._latitude, previous._longitude, current._latitude, current._longitude);
        if (i == 1)
        {
          if (_previousSegmentLastPosition == null)
          {
            anglesInRadians.add(angleInRadians);
            anglesInRadians.add(angleInRadians);
          }
          else
          {
            final double angle2InRadians = Geodetic2D.bearingInRadians(_previousSegmentLastPosition._latitude, _previousSegmentLastPosition._longitude, previous._latitude, previous._longitude);
            final double avr = (angleInRadians + angle2InRadians) / 2.0;
    
            anglesInRadians.add(avr);
            anglesInRadians.add(avr);
          }
        }
        else
        {
          anglesInRadians.add(angleInRadians);
          final double avr = (angleInRadians + anglesInRadians.get(i - 1)) / 2.0;
          anglesInRadians.set(i - 1, avr);
        }
      }
    
      if (_nextSegmentFirstPosition != null)
      {
        final int lastPositionIndex = positionsSize - 1;
        final Geodetic3D lastPosition = _positions.get(lastPositionIndex);
        final double angleInRadians = Geodetic2D.bearingInRadians(lastPosition._latitude, lastPosition._longitude, _nextSegmentFirstPosition._latitude, _nextSegmentFirstPosition._longitude);
    
        final double avr = (angleInRadians + anglesInRadians.get(lastPositionIndex)) / 2.0;
        anglesInRadians.set(lastPositionIndex, avr);
      }
    
    
      final Vector3D offsetP = new Vector3D(_ribbonWidth/2, 0, 0);
      final Vector3D offsetN = new Vector3D(-_ribbonWidth/2, 0, 0);
    
      FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    
    
      final Vector3D rotationAxis = Vector3D.downZ();
      for (int i = 0; i < positionsSize; i++)
      {
        final Geodetic3D position = _positions.get(i);
    
        final MutableMatrix44D rotationMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromRadians(anglesInRadians.get(i)), rotationAxis);
        final MutableMatrix44D geoMatrix = planet.createGeodeticTransformMatrix(position);
        final MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);
    
        vertices.add(offsetN.transformedBy(matrix, 1));
        vertices.add(offsetP.transformedBy(matrix, 1));
      }
    
      Mesh surfaceMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, new Color(_color), null, 0.0f, true); // depthTest -  colorsIntensity -  colors
    
      if (vertices != null)
         vertices.dispose();
    
      return surfaceMesh;
    }

    private Mesh _mesh;
    private Mesh getMesh(Planet planet)
    {
      if (_positionsDirty || (_mesh == null))
      {
        if (_mesh != null)
           _mesh.dispose();
        _mesh = createMesh(planet);
        _positionsDirty = false;
      }
      return _mesh;
    }


    public Segment(Color color, float ribbonWidth)
    {
       _color = new Color(color);
       _ribbonWidth = ribbonWidth;
       _positionsDirty = true;
       _mesh = null;
       _nextSegmentFirstPosition = null;
       _previousSegmentLastPosition = null;
    }

    public void dispose()
    {
      if (_previousSegmentLastPosition != null)
         _previousSegmentLastPosition.dispose();
      if (_nextSegmentFirstPosition != null)
         _nextSegmentFirstPosition.dispose();
    
      if (_mesh != null)
         _mesh.dispose();
    
      final int positionsSize = _positions.size();
      for (int i = 0; i < positionsSize; i++)
      {
        final Geodetic3D position = _positions.get(i);
        if (position != null)
           position.dispose();
      }
    }

    public final int getSize()
    {
      return _positions.size();
    }

    public final void addPosition(Angle latitude, Angle longitude, double height)
    {
      _positionsDirty = true;
      _positions.add(new Geodetic3D(latitude, longitude, height));
    }

    public final void addPosition(Geodetic3D position)
    {
      addPosition(position._latitude, position._longitude, position._height);
    }

    public final void setNextSegmentFirstPosition(Angle latitude, Angle longitude, double height)
    {
      _positionsDirty = true;
      if (_nextSegmentFirstPosition != null)
         _nextSegmentFirstPosition.dispose();
      _nextSegmentFirstPosition = new Geodetic3D(latitude, longitude, height);
    }

    public final void setPreviousSegmentLastPosition(Geodetic3D position)
    {
      _positionsDirty = true;
      if (_previousSegmentLastPosition != null)
         _previousSegmentLastPosition.dispose();
      _previousSegmentLastPosition = new Geodetic3D(position);
    }

    public final Geodetic3D getLastPosition()
    {
      return _positions.get(_positions.size() - 1);
    }

    public final Geodetic3D getPreLastPosition()
    {
      return _positions.get(_positions.size() - 2);
    }

    public final void render(G3MRenderContext rc, Frustum frustum, GLState state)
    {
      Mesh mesh = getMesh(rc.getPlanet());
      if (mesh != null)
      {
        BoundingVolume bounding = mesh.getBoundingVolume();
        if (bounding != null)
        {
          if (bounding.touchesFrustum(frustum))
          {
            mesh.render(rc, state);
          }
        }
      }
    }

  }



  private boolean _visible;

  private final Color _color ;
  private final float _ribbonWidth;
  private final double _deltaHeight;
  private final int _maxPositionsPerSegment;

  private java.util.ArrayList<Segment> _segments = new java.util.ArrayList<Segment>();

  public Trail(Color color, float ribbonWidth, double deltaHeight)
  {
     this(color, ribbonWidth, deltaHeight, 64);
  }
  public Trail(Color color, float ribbonWidth)
  {
     this(color, ribbonWidth, 0.0, 64);
  }
  public Trail(Color color, float ribbonWidth, double deltaHeight, int maxPositionsPerSegment)
  {
     _visible = true;
     _color = new Color(color);
     _ribbonWidth = ribbonWidth;
     _deltaHeight = deltaHeight;
     _maxPositionsPerSegment = maxPositionsPerSegment;
  }

  public void dispose()
  {
    final int segmentsSize = _segments.size();
    for (int i = 0; i < segmentsSize; i++)
    {
      Segment segment = _segments.get(i);
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
        Segment segment = _segments.get(i);
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

  public final void addPosition(Angle latitude, Angle longitude, double height)
  {
    Segment currentSegment;
  
    final int segmentsSize = _segments.size();
    if (segmentsSize == 0)
    {
      currentSegment = new Segment(_color, _ribbonWidth);
      _segments.add(currentSegment);
    }
    else
    {
      currentSegment = _segments.get(segmentsSize - 1);
      if (currentSegment.getSize() > _maxPositionsPerSegment)
      {
        Segment newSegment = new Segment(_color, _ribbonWidth);
  
        currentSegment.setNextSegmentFirstPosition(latitude, longitude, height + _deltaHeight);
        newSegment.setPreviousSegmentLastPosition(currentSegment.getPreLastPosition());
        newSegment.addPosition(currentSegment.getLastPosition());
  
        _segments.add(newSegment);
        currentSegment = newSegment;
      }
    }
  
    currentSegment.addPosition(latitude, longitude, height + _deltaHeight);
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
      Segment segment = _segments.get(i);
      if (segment != null)
         segment.dispose();
    }
    _segments.clear();
  }

}