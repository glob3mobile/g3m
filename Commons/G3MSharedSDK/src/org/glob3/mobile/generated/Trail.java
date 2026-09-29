package org.glob3.mobile.generated;
//
//  Trail.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//

//
//  Trail.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//




//class Planet;
//class Frustum;
//class G3MRenderContext;
//class GLState;
//class IFloatBuffer;
//class MutableMatrix44D;
//class Geodetic2D;
//class Geodetic3D;
//class FloatBufferBuilderFromCartesian3D;
//class ViewportExtentGLFeature;


public class Trail
{


  private static class Position
  {
    public final Angle _latitude ;
    public final Angle _longitude ;
    public final double _height;
    public final double _alpha;
    public final Angle _heading ;

    public Position(Angle latitude, Angle longitude, double height, double alpha, Angle heading)
    {
       _latitude = new Angle(latitude);
       _longitude = new Angle(longitude);
       _height = height;
       _alpha = alpha;
       _heading = new Angle(heading);
    }

    public void dispose()
    {
    }
  }


  private static final int SEGMENT_ALPHA_STATUS_UNKNOWN = 1;
  private static final int SEGMENT_ALPHA_STATUS_FULL_HIDDEN = 2;
  private static final int SEGMENT_ALPHA_STATUS_HALF = 3;
  private static final int SEGMENT_ALPHA_STATUS_FULL_VISIBLE = 4;


  private static class SegmentMeshUserData extends Mesh.MeshUserData
  {
    private final int _status;
    private final double _visibleAlpha;

    public SegmentMeshUserData(int status, double visibleAlpha)
    {
       _status = status;
       _visibleAlpha = visibleAlpha;
    }

    public void dispose()
    {
      super.dispose();
    }

    public final boolean isValid(int status, double visibleAlpha)
    {
      if (status != _status)
      {
        return false;
      }
    
      if ((status == Trail.SEGMENT_ALPHA_STATUS_HALF) && (visibleAlpha != _visibleAlpha))
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
    private final float _minWidthPixels;
    private final boolean _depthTest;
    private final boolean _polygonOffsetFill;
    private final float _polygonOffsetFactor;
    private final float _polygonOffsetUnits;

    private double _minAlpha;
    private double _maxAlpha;
    private double _visibleAlpha;
    private int _alphaStatus;

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
    
      FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();
      FloatBufferBuilderFromCartesian3D sideVectors = (_minWidthPixels > 0) ? FloatBufferBuilderFromCartesian3D.builderWithoutCenter() : null;
    
      double lastAlpha = 0;
    
      final Vector3D rotationAxis = Vector3D.DOWN_Z;
      for (int i = 0; i < positionsSize; i++)
      {
        final Position position = _positions.get(i);
    
        if (_alphaStatus == Trail.SEGMENT_ALPHA_STATUS_HALF)
        {
          if (position._alpha > _visibleAlpha)
          {
            if (lastAlpha < _visibleAlpha)
            {
              if (i > 0)
              {
                final Position previousPosition = _positions.get(i-1);
                final double normalizedAlpha = (_visibleAlpha - previousPosition._alpha) / (position._alpha - previousPosition._alpha);
    
                final MutableMatrix44D matrix = createMatrix(Angle.fromRadians(bearings.get(i)), Angle.linearInterpolation(previousPosition._latitude, position._latitude, normalizedAlpha), Angle.linearInterpolation(previousPosition._longitude, position._longitude, normalizedAlpha), IMathUtils.instance().linearInterpolation(previousPosition._height, position._height, normalizedAlpha), rotationAxis, planet);
    
                addRibbonVertices(matrix, vertices, sideVectors);
              }
            }
            break;
          }
        }
    
        lastAlpha = position._alpha;
    
        final MutableMatrix44D matrix = createMatrix(Angle.fromRadians(bearings.get(i)), position._latitude, position._longitude, position._height, rotationAxis, planet);
    
        addRibbonVertices(matrix, vertices, sideVectors);
      }
    
      if (bearings != null)
         bearings.dispose();
    
      Mesh surfaceMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, new Color(_color), null, _depthTest, null, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, false, GLCullFace.back()); // culledFace -  cullFace -  polygonOffsetUnits -  polygonOffsetFactor -  polygonOffsetFill -  normals -  depthTest -  colors -  flatColor -  pointSize -  lineWidth -  vertices -  center -  owner -  primitive
    
      if (vertices != null)
         vertices.dispose();
    
      setRibbonSides((sideVectors == null) ? null : sideVectors.create());
      if (sideVectors != null)
         sideVectors.dispose();
    
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

    // ribbon mode only: the RibbonSideGLFeature attribute buffer lives here, not in the mesh
    private IFloatBuffer _ribbonSides;
    private GLState _ribbonGLState;
    private void setRibbonSides(IFloatBuffer ribbonSides)
    {
      if (_ribbonGLState != null)
      {
        _ribbonGLState._release();
        _ribbonGLState = null;
      }
      if (_ribbonSides != null)
         _ribbonSides.dispose();
      _ribbonSides = ribbonSides;
    
      if (_ribbonSides != null)
      {
        _ribbonGLState = new GLState();
        _ribbonGLState.addGLFeature(new RibbonSideGLFeature(_ribbonSides), false);
      }
    }
    private GLState getMeshGLState(GLState parent)
    {
      if (_ribbonGLState == null)
      {
        return parent;
      }
      _ribbonGLState.setParent(parent);
      return _ribbonGLState;
    }

    private IFloatBuffer getBearingsInRadians()
    {
      final int positionsSize = _positions.size();
    
      IFloatBuffer bearingsInRadians = IFactory.instance().createFloatBuffer(positionsSize);
    
      for (int i = 1; i < positionsSize; i++)
      {
        final Position previous = _positions.get(i - 1);
        final Position current = _positions.get(i);
    
        final float angleInRadians = (float)(current._heading.isNan() ? Geodetic2D.bearingInRadians(previous._latitude, previous._longitude, current._latitude, current._longitude) : current._heading._radians);
        if (i == 1)
        {
          if (_previousSegmentLastPosition == null)
          {
            bearingsInRadians.rawPut(0, angleInRadians);
            bearingsInRadians.rawPut(1, angleInRadians);
          }
          else
          {
            final float previousAngleInRadians = (float)(previous._heading.isNan() ? Geodetic2D.bearingInRadians(_previousSegmentLastPosition._latitude, _previousSegmentLastPosition._longitude, previous._latitude, previous._longitude) : previous._heading._radians);
            final float avr = (previousAngleInRadians + angleInRadians) / 2.0f;
    
            bearingsInRadians.rawPut(0, avr);
            bearingsInRadians.rawPut(1, avr);
          }
        }
        else
        {
          final float previousAngleInRadians = bearingsInRadians.get(i - 1);
    
          final float avr = (previousAngleInRadians + angleInRadians) / 2.0f;
          bearingsInRadians.rawPut(i - 1, avr);
    
          bearingsInRadians.rawPut(i, angleInRadians);
        }
      }
    
      if (_nextSegmentFirstPosition != null)
      {
        final int lastPositionIndex = positionsSize - 1;
        final Position lastPosition = _positions.get(lastPositionIndex);
        final float angleInRadians = (float)(_nextSegmentFirstPosition._heading.isNan() ? Geodetic2D.bearingInRadians(lastPosition._latitude, lastPosition._longitude, _nextSegmentFirstPosition._latitude, _nextSegmentFirstPosition._longitude) : _nextSegmentFirstPosition._heading._radians);
    
        final float avr = (angleInRadians + bearingsInRadians.get(lastPositionIndex)) / 2.0f;
        bearingsInRadians.rawPut(lastPositionIndex, avr);
      }
    
      return bearingsInRadians;
    }

    private int calculateAlphaStatus()
    {
      if (_visibleAlpha <= _minAlpha)
      {
        return Trail.SEGMENT_ALPHA_STATUS_FULL_HIDDEN;
      }
      else if (_visibleAlpha >= _maxAlpha)
      {
        return Trail.SEGMENT_ALPHA_STATUS_FULL_VISIBLE;
      }
      else
      {
        return Trail.SEGMENT_ALPHA_STATUS_HALF;
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

    private MutableMatrix44D createMatrix(Angle bearing, Angle latitude, Angle longitude, double height, Vector3D rotationAxis, Planet planet)
    {
      final MutableMatrix44D rotationMatrix = MutableMatrix44D.createRotationMatrix(bearing, rotationAxis);
    
      final MutableMatrix44D geoMatrix = planet.createGeodeticTransformMatrix(latitude, longitude, height);
      return geoMatrix.multiply(rotationMatrix);
    }

    private void addRibbonVertices(MutableMatrix44D matrix, FloatBufferBuilderFromCartesian3D vertices, FloatBufferBuilderFromCartesian3D sideVectors)
    {
      if (sideVectors == null)
      {
        final Vector3D offsetN = new Vector3D(-_ribbonWidth/2, 0, 0);
        final Vector3D offsetP = new Vector3D(_ribbonWidth/2, 0, 0);
        vertices.add(offsetN.transformedBy(matrix, 1));
        vertices.add(offsetP.transformedBy(matrix, 1));
      }
      else
      {
        // both edges sit on the center line; RibbonMesh.vsh pushes them apart along ±side
        final Vector3D center = Vector3D.ZERO.transformedBy(matrix, 1);
        final Vector3D side = Vector3D.UP_X.transformedBy(matrix, 0).normalized();
        vertices.add(center);
        sideVectors.add(side.times(-1));
        vertices.add(center);
        sideVectors.add(side);
      }
    }

    public Segment(Color color, float ribbonWidth, float minWidthPixels, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, double visibleAlpha)
    {
       _color = color;
       _ribbonWidth = ribbonWidth;
       _minWidthPixels = minWidthPixels;
       _depthTest = depthTest;
       _polygonOffsetFill = polygonOffsetFill;
       _polygonOffsetFactor = polygonOffsetFactor;
       _polygonOffsetUnits = polygonOffsetUnits;
       _visibleAlpha = visibleAlpha;
       _alphaStatus = Trail.SEGMENT_ALPHA_STATUS_UNKNOWN;
       _minAlpha = IMathUtils.instance().maxDouble();
       _maxAlpha = IMathUtils.instance().minDouble();
       _positionsDirty = true;
       _mesh = null;
       _ribbonSides = null;
       _ribbonGLState = null;
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
      setRibbonSides(null);
    
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
      addPosition(position._latitude, position._longitude, position._height, position._alpha, position._heading);
    }

    public final void addPosition(Angle latitude, Angle longitude, double height, double alpha, Angle heading)
    {
      _positionsDirty = true;
      _positions.add(new Position(latitude, longitude, height, alpha, heading));
      if (alpha < _minAlpha)
      {
         _minAlpha = alpha;
         _alphaStatus = Trail.SEGMENT_ALPHA_STATUS_UNKNOWN;
      }
      if (alpha > _maxAlpha)
      {
         _maxAlpha = alpha;
         _alphaStatus = Trail.SEGMENT_ALPHA_STATUS_UNKNOWN;
      }
    }

    public final void setNextSegmentFirstPosition(Angle latitude, Angle longitude, double height, double alpha, Angle heading)
    {
      _positionsDirty = true;
      if (_nextSegmentFirstPosition != null)
         _nextSegmentFirstPosition.dispose();
      _nextSegmentFirstPosition = new Position(latitude, longitude, height, alpha, heading);
    }

    public final void setPreviousSegmentLastPosition(Position position)
    {
      _positionsDirty = true;
      if (_previousSegmentLastPosition != null)
         _previousSegmentLastPosition.dispose();
      _previousSegmentLastPosition = new Position(position._latitude, position._longitude, position._height, position._alpha, position._heading);
    }

    public final Trail.Position getLastPosition()
    {
      return _positions.get(_positions.size() - 1);
    }

    public final Trail.Position getPenultimatePosition()
    {
      return _positions.get(_positions.size() - 2);
    }

    public final void render(G3MRenderContext rc, Frustum frustum, GLState state)
    {
    
      if (_alphaStatus == Trail.SEGMENT_ALPHA_STATUS_UNKNOWN)
      {
        _alphaStatus = calculateAlphaStatus();
      }
    
      if ((_alphaStatus != Trail.SEGMENT_ALPHA_STATUS_UNKNOWN) && (_alphaStatus != Trail.SEGMENT_ALPHA_STATUS_FULL_HIDDEN))
      {
        Mesh mesh = getMesh(rc.getPlanet());
        if (mesh != null)
        {
          BoundingVolume bounding = mesh.getBoundingVolume();
          if (bounding != null)
          {
            if (bounding.touchesFrustum(frustum))
            {
              mesh.render(rc, getMeshGLState(state));
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
        _alphaStatus = Trail.SEGMENT_ALPHA_STATUS_UNKNOWN;
      }
    }
  }



  private boolean _visible;

  private final Color _color ;
  private final float _ribbonWidth;
  private final float _minWidthPixels;
  private final boolean _depthTest;
  private final boolean _polygonOffsetFill;
  private final float _polygonOffsetFactor;
  private final float _polygonOffsetUnits;
  private final double _deltaHeight;
  private final int _maxPositionsPerSegment;

  private double _alpha;

  private java.util.ArrayList<Segment> _segments = new java.util.ArrayList<Segment>();

  // only when minWidthPixels > 0. The viewport feature must stay out of any shared state:
  // GPUProgramManager reads VIEWPORT_EXTENT alone as "billboard"
  private GLState _ribbonGLState;
  private ViewportExtentGLFeature _ribbonViewportExtent;
  private GLState getSegmentsGLState(G3MRenderContext rc, GLState parent)
  {
    if (_ribbonGLState == null)
    {
      return parent;
    }
  
    final Camera camera = rc.getCurrentCamera();
    int logicWidth = camera.getViewPortWidth();
    if (rc.getViewMode() == ViewMode.STEREO)
    {
      logicWidth /= 2;
    }
    _ribbonViewportExtent.changeExtent(logicWidth, camera.getViewPortHeight());
  
    _ribbonGLState.setParent(parent);
    return _ribbonGLState;
  }

  // minWidthPixels > 0 switches to the RibbonMesh program: the ribbon never gets thinner
  // than that on screen, no matter how far the camera is. 0 keeps the plain fixed-width mesh.
  public Trail(Color color, float ribbonWidth, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, double deltaHeight, int maxPositionsPerSegment)
  {
     this(color, ribbonWidth, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, deltaHeight, maxPositionsPerSegment, 0);
  }
  public Trail(Color color, float ribbonWidth, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, double deltaHeight)
  {
     this(color, ribbonWidth, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, deltaHeight, 32, 0);
  }
  public Trail(Color color, float ribbonWidth, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
     this(color, ribbonWidth, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, 0.0, 32, 0);
  }
  public Trail(Color color, float ribbonWidth, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, double deltaHeight, int maxPositionsPerSegment, float minWidthPixels)
  {
     _visible = true;
     _color = color;
     _ribbonWidth = ribbonWidth;
     _minWidthPixels = minWidthPixels;
     _depthTest = depthTest;
     _polygonOffsetFill = polygonOffsetFill;
     _polygonOffsetFactor = polygonOffsetFactor;
     _polygonOffsetUnits = polygonOffsetUnits;
     _deltaHeight = deltaHeight;
     _maxPositionsPerSegment = maxPositionsPerSegment;
     _alpha = 1.0;
     _ribbonGLState = null;
     _ribbonViewportExtent = null;
    if (_minWidthPixels > 0)
    {
      _ribbonGLState = new GLState();
      _ribbonGLState.addGLFeature(new RibbonWidthGLFeature(_ribbonWidth, _minWidthPixels), false);
      _ribbonViewportExtent = new ViewportExtentGLFeature(0, 0); // real extent set on every render
      _ribbonGLState.addGLFeature(_ribbonViewportExtent, false);
    }
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
  
    if (_ribbonGLState != null)
    {
      _ribbonGLState._release();
    }
  }

  public final void render(G3MRenderContext rc, Frustum frustum, GLState state)
  {
    if (_visible)
    {
      final GLState segmentsState = getSegmentsGLState(rc, state);
  
      final int segmentsSize = _segments.size();
      for (int i = 0; i < segmentsSize; i++)
      {
        Segment segment = _segments.get(i);
        segment.render(rc, frustum, segmentsState);
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
     addPosition(latitude, longitude, height, alpha, Angle.nan());
  }
  public final void addPosition(Angle latitude, Angle longitude, double height, double alpha, Angle heading)
  {
    Segment currentSegment;
  
    final int segmentsSize = _segments.size();
    if (segmentsSize == 0)
    {
      currentSegment = new Segment(_color, _ribbonWidth, _minWidthPixels, _depthTest, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, _alpha);
      _segments.add(currentSegment);
    }
    else
    {
      currentSegment = _segments.get(segmentsSize - 1);
  
      if (currentSegment.getSize() >= _maxPositionsPerSegment)
      {
        Segment newSegment = new Segment(_color, _ribbonWidth, _minWidthPixels, _depthTest, _polygonOffsetFill, _polygonOffsetFactor, _polygonOffsetUnits, _alpha);
        _segments.add(newSegment);
  
        currentSegment.setNextSegmentFirstPosition(latitude, longitude, height + _deltaHeight, alpha, heading);
        newSegment.setPreviousSegmentLastPosition(currentSegment.getPenultimatePosition());
        newSegment.addPosition(currentSegment.getLastPosition());
  
        currentSegment = newSegment;
      }
    }
  
    currentSegment.addPosition(latitude, longitude, height + _deltaHeight, alpha, heading);
  }

  public final void addPosition(Geodetic2D position, double height, double alpha)
  {
     addPosition(position, height, alpha, Angle.nan());
  }
  public final void addPosition(Geodetic2D position, double height, double alpha, Angle heading)
  {
    addPosition(position._latitude, position._longitude, height, alpha, heading);
  }

  public final void addPosition(Geodetic3D position, double alpha)
  {
     addPosition(position, alpha, Angle.nan());
  }
  public final void addPosition(Geodetic3D position, double alpha, Angle heading)
  {
    addPosition(position._latitude, position._longitude, position._height, alpha, heading);
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