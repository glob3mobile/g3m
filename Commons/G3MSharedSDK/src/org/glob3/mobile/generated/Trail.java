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




//class Planet;
//class Frustum;
//class G3MRenderContext;
//class GLState;
//class IFloatBuffer;


public class Trail
{


  private static class Position
  {
    public final Angle _latitude ;
    public final Angle _longitude ;
    public final double _height;
    public final double _alpha;

    public Position(Angle latitude, Angle longitude, double height, double alpha)
    {
       _latitude = new Angle(latitude);
       _longitude = new Angle(longitude);
       _height = height;
       _alpha = alpha;
    }

    public void dispose()
    {
    }
  }


  private enum SegmentAlphaStatus
  {
    UNKNOWN,
    FULL_HIDDEN,
    HALF,
    FULL_VISIBLE;

     public int getValue()
     {
        return this.ordinal();
     }

     public static SegmentAlphaStatus forValue(int value)
     {
        return values()[value];
     }
  }


  private static class SegmentMeshUserData extends Mesh.MeshUserData
  {
    private final SegmentAlphaStatus _status;
    private final double _visibleAlpha;

    public SegmentMeshUserData(SegmentAlphaStatus status, double visibleAlpha)
    {
       _status = status;
       _visibleAlpha = visibleAlpha;
    }

    public void dispose()
    {
      super.dispose();
    }

    public final boolean isValid(SegmentAlphaStatus status, double visibleAlpha)
    {
      if (status != _status)
      {
        return false;
      }
    
      if ((status == Trail.SegmentAlphaStatus.HALF) && (visibleAlpha != _visibleAlpha))
      {
        return false;
      }
    
      return true;
    }
  }


  private static class Segment
  {
    private final Color _color ;
    private final float _ribbonWidth;
    private double _minAlpha;
    private double _maxAlpha;
    private double _visibleAlpha;
    private SegmentAlphaStatus _alphaStatus;

    private boolean _positionsDirty;
    private java.util.ArrayList<Position> _positions = new java.util.ArrayList<Position>();
    private Position _nextSegmentFirstPosition;
    private Position _previousSegmentLastPosition;

    private Mesh createMesh(Planet planet)
    {
      final int positionsSize = _positions.size();
    
      if (positionsSize < 2)
      {
        return null;
      }
    
      final IFloatBuffer bearings = getBearingsInRadians();
    
      final Vector3D offsetP = new Vector3D(_ribbonWidth/2, 0, 0);
      final Vector3D offsetN = new Vector3D(-_ribbonWidth/2, 0, 0);
    
      FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
    
      final Vector3D rotationAxis = Vector3D.downZ();
      for (int i = 0; i < positionsSize; i++)
      {
        final Position position = _positions.get(i);
    
        if (_alphaStatus == Trail.SegmentAlphaStatus.HALF)
        {
          if (position._alpha > _visibleAlpha)
          {
            break;
          }
        }
    
        final MutableMatrix44D rotationMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromRadians(bearings.get(i)), rotationAxis);
        final MutableMatrix44D geoMatrix = planet.createGeodeticTransformMatrix(position._latitude, position._longitude, position._height);
        final MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);
    
        vertices.add(offsetN.transformedBy(matrix, 1));
        vertices.add(offsetP.transformedBy(matrix, 1));
      }
    
      if (bearings != null)
         bearings.dispose();
    
      Mesh surfaceMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, new Color(_color), null, 0.0f, true); // depthTest -  colorsIntensity -  colors
    
      if (vertices != null)
         vertices.dispose();
    
      surfaceMesh.setUserData(new SegmentMeshUserData(_alphaStatus, _visibleAlpha));
    
      return surfaceMesh;
    }

    private Mesh _mesh;
    private Mesh getMesh(Planet planet)
    {
      if (!isMeshValid())
      {
        if (_mesh != null)
           _mesh.dispose();
        _mesh = createMesh(planet);
        _positionsDirty = false;
      }
      return _mesh;
    }

    private IFloatBuffer getBearingsInRadians()
    {
      final int positionsSize = _positions.size();
    
      IFloatBuffer bearingsInRadians = IFactory.instance().createFloatBuffer(positionsSize);
    
      for (int i = 1; i < positionsSize; i++)
      {
        final Position current = _positions.get(i);
        final Position previous = _positions.get(i - 1);
    
        final float angleInRadians = (float) Geodetic2D.bearingInRadians(previous._latitude, previous._longitude, current._latitude, current._longitude);
        if (i == 1)
        {
          if (_previousSegmentLastPosition == null)
          {
            bearingsInRadians.rawPut(0, angleInRadians);
            bearingsInRadians.rawPut(1, angleInRadians);
          }
          else
          {
            final float angle2InRadians = (float) Geodetic2D.bearingInRadians(_previousSegmentLastPosition._latitude, _previousSegmentLastPosition._longitude, previous._latitude, previous._longitude);
            final float avr = (angleInRadians + angle2InRadians) / 2.0f;
    
            bearingsInRadians.rawPut(0, avr);
            bearingsInRadians.rawPut(1, avr);
          }
        }
        else
        {
          bearingsInRadians.rawPut(i, angleInRadians);
          final float avr = (angleInRadians + bearingsInRadians.get(i - 1)) / 2.0f;
          bearingsInRadians.rawPut(i - 1, avr);
        }
      }
    
      if (_nextSegmentFirstPosition != null)
      {
        final int lastPositionIndex = positionsSize - 1;
        final Position lastPosition = _positions.get(lastPositionIndex);
        final float angleInRadians = (float) Geodetic2D.bearingInRadians(lastPosition._latitude, lastPosition._longitude, _nextSegmentFirstPosition._latitude, _nextSegmentFirstPosition._longitude);
    
        final float avr = (angleInRadians + bearingsInRadians.get(lastPositionIndex)) / 2.0f;
        bearingsInRadians.rawPut(lastPositionIndex, avr);
      }
    
      return bearingsInRadians;
    }

    private Trail.SegmentAlphaStatus calculateAlphaStatus()
    {
      if (_visibleAlpha <= _minAlpha)
      {
        return Trail.SegmentAlphaStatus.FULL_HIDDEN;
      }
      else if (_visibleAlpha >= _maxAlpha)
      {
        return Trail.SegmentAlphaStatus.FULL_VISIBLE;
      }
      else
      {
        return Trail.SegmentAlphaStatus.HALF;
      }
    }

    private boolean isMeshValid()
    {
      if (_positionsDirty || (_mesh == null))
      {
        return false;
      }
    
      final SegmentMeshUserData userData = (SegmentMeshUserData) _mesh.getUserData();
      return userData.isValid(_alphaStatus, _visibleAlpha);
    }

    public Segment(Color color, float ribbonWidth, double visibleAlpha)
    {
       _color = new Color(color);
       _ribbonWidth = ribbonWidth;
       _visibleAlpha = visibleAlpha;
       _alphaStatus = Trail.SegmentAlphaStatus.UNKNOWN;
       _minAlpha = IMathUtils.instance().maxDouble();
       _maxAlpha = IMathUtils.instance().minDouble();
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
        final Position position = _positions.get(i);
        if (position != null)
           position.dispose();
      }
    }

    public final int getSize()
    {
      return _positions.size();
    }

    public final void addPosition(Position position)
    {
      addPosition(position._latitude, position._longitude, position._height, position._alpha);
    }

    public final void addPosition(Angle latitude, Angle longitude, double height, double alpha)
    {
      _positionsDirty = true;
      _positions.add(new Position(latitude, longitude, height, alpha));
      if (alpha < _minAlpha)
      {
         _minAlpha = alpha;
         _alphaStatus = Trail.SegmentAlphaStatus.UNKNOWN;
      }
      if (alpha > _maxAlpha)
      {
         _maxAlpha = alpha;
         _alphaStatus = Trail.SegmentAlphaStatus.UNKNOWN;
      }
    }

    public final void setNextSegmentFirstPosition(Angle latitude, Angle longitude, double height, double alpha)
    {
      _positionsDirty = true;
      if (_nextSegmentFirstPosition != null)
         _nextSegmentFirstPosition.dispose();
      _nextSegmentFirstPosition = new Position(latitude, longitude, height, alpha);
    }

    public final void setPreviousSegmentLastPosition(Position position)
    {
      _positionsDirty = true;
      if (_previousSegmentLastPosition != null)
         _previousSegmentLastPosition.dispose();
      _previousSegmentLastPosition = new Position(position._latitude, position._longitude, position._height, position._alpha);
    }

    public final Trail.Position getLastPosition()
    {
      return _positions.get(_positions.size() - 1);
    }

    public final Trail.Position getPreLastPosition()
    {
      return _positions.get(_positions.size() - 2);
    }

    public final void render(G3MRenderContext rc, Frustum frustum, GLState state)
    {
    
      if (_alphaStatus == Trail.SegmentAlphaStatus.UNKNOWN)
      {
        _alphaStatus = calculateAlphaStatus();
      }
    
      if ((_alphaStatus != Trail.SegmentAlphaStatus.UNKNOWN) && (_alphaStatus != Trail.SegmentAlphaStatus.FULL_HIDDEN))
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

    public final void setVisibleAlpha(double visibleAlpha)
    {
      if (visibleAlpha != _visibleAlpha)
      {
        _visibleAlpha = visibleAlpha;
        _alphaStatus = Trail.SegmentAlphaStatus.UNKNOWN;
      }
    }
  }



  private boolean _visible;

  private final Color _color ;
  private final float _ribbonWidth;
  private final double _deltaHeight;
  private final int _maxPositionsPerSegment;

  private double _alpha;

  private java.util.ArrayList<Segment> _segments = new java.util.ArrayList<Segment>();

  public Trail(Color color, float ribbonWidth, double deltaHeight)
  {
     this(color, ribbonWidth, deltaHeight, 8);
  }
  public Trail(Color color, float ribbonWidth)
  {
     this(color, ribbonWidth, 0.0, 8);
  }
  public Trail(Color color, float ribbonWidth, double deltaHeight, int maxPositionsPerSegment)
  {
     _visible = true;
     _color = new Color(color);
     _ribbonWidth = ribbonWidth;
     _deltaHeight = deltaHeight;
     _maxPositionsPerSegment = maxPositionsPerSegment;
     _alpha = 1.0;
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

  public final void addPosition(Angle latitude, Angle longitude, double height, double alpha)
  {
    Segment currentSegment;
  
    final int segmentsSize = _segments.size();
    if (segmentsSize == 0)
    {
      currentSegment = new Segment(_color, _ribbonWidth, _alpha);
      _segments.add(currentSegment);
    }
    else
    {
      currentSegment = _segments.get(segmentsSize - 1);
  
      if (currentSegment.getSize() >= _maxPositionsPerSegment)
      {
        Segment newSegment = new Segment(_color, _ribbonWidth, _alpha);
        _segments.add(newSegment);
  
        currentSegment.setNextSegmentFirstPosition(latitude, longitude, height + _deltaHeight, alpha);
        newSegment.setPreviousSegmentLastPosition(currentSegment.getPreLastPosition());
        newSegment.addPosition(currentSegment.getLastPosition());
  
        currentSegment = newSegment;
      }
    }
  
    currentSegment.addPosition(latitude, longitude, height + _deltaHeight, alpha);
  }

  public final void addPosition(Geodetic3D position, double alpha)
  {
    addPosition(position._latitude, position._longitude, position._height, alpha);
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

  public final void setAlpha(double alpha)
  {
    final double a = IMathUtils.instance().clamp(alpha, 0.0, 1.0);
    if (a != _alpha)
    {
      _alpha = a;
      final int segmentsSize = _segments.size();
      for (int i = 0; i < segmentsSize; i++)
      {
        Segment segment = _segments.get(i);
        segment.setVisibleAlpha(_alpha);
      }
    }
  }

}