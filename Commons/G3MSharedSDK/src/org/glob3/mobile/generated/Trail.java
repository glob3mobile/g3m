package org.glob3.mobile.generated; 
public class Trail
{
  private boolean _visible;

  private Color _color ;
  private final float _ribbonWidth;

<<<<<<< HEAD
  private java.util.ArrayList<Geodetic3D> _positions = new java.util.ArrayList<Geodetic3D>();

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
        anglesInRadians.add(angleInRadians);
        anglesInRadians.add(angleInRadians);
      }
      else
      {
        anglesInRadians.add(angleInRadians);
        final double avr = (angleInRadians + anglesInRadians.get(i - 1)) / 2.0;
        anglesInRadians.set(i - 1, avr);
      }
    }
  
  
    float centerX = 0F;
    float centerY = 0F;
    float centerZ = 0F;
    final Vector3D offsetP = new Vector3D(_ribbonWidth/2, 0, 0);
    final Vector3D offsetN = new Vector3D(-_ribbonWidth/2, 0, 0);
  
    IFloatBuffer vertices = IFactory.instance().createFloatBuffer(positionsSize * 3 * 2);
  
    final Vector3D rotationAxis = Vector3D.downZ();
    for (int i = 0; i < positionsSize; i++)
    {
      final Geodetic3D position = _positions.get(i);
  
      final MutableMatrix44D rotationMatrix = MutableMatrix44D.createRotationMatrix(Angle.fromRadians(anglesInRadians.get(i)), rotationAxis);
      final MutableMatrix44D geoMatrix = planet.createGeodeticTransformMatrix(position);
      final MutableMatrix44D matrix = geoMatrix.multiply(rotationMatrix);
  
      final int i6 = i * 6;
      final Vector3D offsetNTransformed = offsetN.transformedBy(matrix, 1);
      if (i == 0)
      {
        centerX = (float) offsetNTransformed._x;
        centerY = (float) offsetNTransformed._y;
        centerZ = (float) offsetNTransformed._z;
      }
      vertices.rawPut(i6, (float) offsetNTransformed._x - centerX);
      vertices.rawPut(i6 + 1, (float) offsetNTransformed._y - centerY);
      vertices.rawPut(i6 + 2, (float) offsetNTransformed._z - centerZ);
  
  
      final Vector3D offsetPTransformed = offsetP.transformedBy(matrix, 1);
      vertices.rawPut(i6 + 3, (float) offsetPTransformed._x - centerX);
      vertices.rawPut(i6 + 4, (float) offsetPTransformed._y - centerY);
      vertices.rawPut(i6 + 5, (float) offsetPTransformed._z - centerZ);
    }
  
    final Vector3D center = new Vector3D(centerX, centerY, centerZ);
    Mesh surfaceMesh = new DirectMesh(GLPrimitive.triangleStrip(), true, center, vertices, 1, 1, new Color(_color));
  
    // Debug unions
  //  Mesh* edgesMesh = new DirectMesh(GLPrimitive::lines(),
  //                                   false,
  //                                   center,
  //                                   vertices,
  //                                   2,
  //                                   1,
  //                                   Color::newFromRGBA(1, 1, 1, 0.7f));
  //
  //  CompositeMesh* cm = new CompositeMesh();
  //
  //  cm->addMesh(surfaceMesh);
  //  cm->addMesh(edgesMesh);
  //
  //  return cm;
  
    return surfaceMesh;
  }
=======
  private java.util.ArrayList<TrailSegment> _segments = new java.util.ArrayList<TrailSegment>();
>>>>>>> webgl-port


<<<<<<< HEAD
  private GLState _glState = new GLState();

  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    if (_projection == null)
    {
      _projection = new ProjectionGLFeature(cam.getProjectionMatrix44D());
      _glState.addGLFeature(_projection, true);
    }
    else
    {
      _projection.setMatrix(cam.getProjectionMatrix44D());
    }
  
    if (_model == null)
    {
      _model = new ModelGLFeature(cam.getModelMatrix44D());
      _glState.addGLFeature(_model, true);
    }
    else
    {
      _model.setMatrix(cam.getModelMatrix44D());
    }
  }
  private ProjectionGLFeature _projection;
  private ModelGLFeature _model;

  public Trail(int maxSteps, Color color, float ribbonWidth)
=======
  public Trail(Color color, float ribbonWidth)
>>>>>>> webgl-port
  {
     _visible = true;
     _color = new Color(color);
     _ribbonWidth = ribbonWidth;
     _projection = null;
     _model = null;
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
  }

<<<<<<< HEAD
  public final void render(G3MRenderContext rc)
=======
  public final void render(G3MRenderContext rc, GLState parentState, Frustum frustum)
>>>>>>> webgl-port
  {
  //  if (_visible) {
  //    Mesh* mesh = getMesh(rc->getPlanet());
  //    if (mesh != NULL) {
  //      mesh->render(rc, parentState);
  //    }
  //  }
  
    if (_visible)
    {
<<<<<<< HEAD
  
      Mesh mesh = getMesh(rc.getPlanet());
      if (mesh != null)
      {
  
        updateGLState(rc);
  
        mesh.render(rc, _glState);
=======
      final int segmentsSize = _segments.size();
      for (int i = 0; i < segmentsSize; i++)
      {
        TrailSegment segment = _segments.get(i);
        segment.render(rc, parentState, frustum);
>>>>>>> webgl-port
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