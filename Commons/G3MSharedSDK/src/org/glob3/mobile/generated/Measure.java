package org.glob3.mobile.generated;
//
//  Measure.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//

//
//  Measure.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/25/21.
//




//class MeasureVertex;
//class ShapesRenderer;
//class MeshRenderer;
//class MarksRenderer;
//class CompositeRenderer;
//class Planet;
//class Measure_VertexShape;
//class MeasureHandler;
//class Geodetic3D;


public class Measure
{
  private static long INSTANCE_COUNTER = 0;

  private final String _instanceID;

  private final double _vertexSphereRadius;
  private final Color _vertexColor ;
  private final Color _vertexSelectedColor ;
  private final float _segmentLineWidth;
  private final Color _segmentColor ;

  private float _verticalExaggeration;
  private double _deltaHeight;
  private boolean _closed;

  private final java.util.ArrayList<MeasureVertex> _vertices = new java.util.ArrayList<MeasureVertex>();

  private ShapesRenderer _shapesRenderer;
  private MeshRenderer _meshRenderer;
  private MarksRenderer _marksRenderer;
  private CompositeRenderer _compositeRenderer;

  private Planet _planet;

  private void resetUI()
  {
    // clean up
    cleanUI();
  
    // create 3d objects
    createVerticesSpheres();
    createEdgeLines();
    createEdgeDistanceLabels();
    createVertexAngleLabels();
  }
  private void createVerticesSpheres()
  {
    if (_shapesRenderer == null)
    {
      return;
    }
  
    final int verticesCount = _vertices.size();
  
    for (int i = 0; i < verticesCount; i++)
    {
      final MeasureVertex vertex = _vertices.get(i);
  
      Measure_VertexShape vertexSphere = new Measure_VertexShape(new Geodetic3D(vertex.getScaledGeodetic(_verticalExaggeration, _deltaHeight)), _vertexSphereRadius, _vertexColor, _vertexSelectedColor, this, _instanceID, i);
  
      _verticesSpheres.add(vertexSphere);
  
      _shapesRenderer.addShape(vertexSphere);
    }
  }
  private void createEdgeLines()
  {
    if (_meshRenderer == null)
    {
      return;
    }
  
    final int verticesCount = _vertices.size();
    if (verticesCount < 2)
    {
      return;
    }
  
    // create edges lines
    FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_planet);
  
    for (int i = 0; i < verticesCount; i++)
    {
      fbb.add(_vertices.get(i).getScaledGeodetic(_verticalExaggeration, _deltaHeight));
    }
  
    if (_closed && (verticesCount >= 3))
    {
      fbb.add(_vertices.get(0).getScaledGeodetic(_verticalExaggeration, _deltaHeight));
    }
  
    Mesh edgesLines = new DirectMesh(GLPrimitive.lineStrip(), true, fbb.getCenter(), fbb.create(), _segmentLineWidth, 1.0f, new Color(_segmentColor), null, false); // depthTest -  const IFloatBuffer* colors -  float pointSize
  
    edgesLines.setToken(_instanceID);
    _meshRenderer.addMesh(edgesLines);
  
    if (fbb != null)
       fbb.dispose();
  }
  private void createDistanceLabel(int vertexIndexFrom, int vertexIndexTo)
  {
    final MeasureVertex from = _vertices.get(vertexIndexFrom);
    final MeasureVertex to = _vertices.get(vertexIndexTo);
  
    final double distanceInMeters = from.getCartesian(_planet).distanceTo(to.getCartesian(_planet));
  
    final String label = _measureHandler.getDistanceLabel(this, vertexIndexFrom, vertexIndexTo, distanceInMeters);
    if (label == null) {
      return;
    }
    if (label.length() == 0)
    {
      return;
    }
  
    final Geodetic3D position = Geodetic3D.linearInterpolation(from.getScaledGeodetic(_verticalExaggeration, _deltaHeight), to.getScaledGeodetic(_verticalExaggeration, _deltaHeight), 0.5);
  
    Mark mark = new Mark(label, new Geodetic3D(position._latitude, position._longitude, position._height + _vertexSphereRadius), AltitudeMode.ABSOLUTE);
    mark.setZoomInAppears(false);
  
    mark.setToken(_instanceID);
  
    _marksRenderer.addMark(mark);
  }

  private void createEdgeDistanceLabels()
  {
    if (_marksRenderer == null)
    {
      return;
    }
  
    final int verticesCount = _vertices.size();
    if (verticesCount < 2)
    {
      return;
    }
  
    if (_measureHandler == null)
    {
      return;
    }
  
    for (int i = 1; i < verticesCount; i++)
    {
      createDistanceLabel(i - 1, i);
    }
  
    if (_closed && (verticesCount >= 3))
    {
      createDistanceLabel(verticesCount - 1, 0);
    }
  }
  private void createVertexAngleLabels()
  {
    if (_marksRenderer == null)
    {
      return;
    }
  
    final int verticesCount = _vertices.size();
    if (verticesCount < 3)
    {
      return;
    }
  
    for (int i = 1; i < verticesCount - 1; i++)
    {
      final MeasureVertex previous = _vertices.get(i - 1);
      final MeasureVertex current = _vertices.get(i);
      final MeasureVertex next = _vertices.get(i + 1);
  
      final Vector3D v0 = current.getCartesian(_planet).sub(previous.getCartesian(_planet));
      final Vector3D v1 = current.getCartesian(_planet).sub(next.getCartesian(_planet));
  
      final Angle angle = v0.angleBetween(v1);
  
      final String label = _measureHandler.getAngleLabel(this, i, angle);
      if (label == null) {
        continue;
      }
      if (label.length() == 0)
      {
        continue;
      }
  
      final Geodetic3D currentGeodetic = current.getScaledGeodetic(_verticalExaggeration, _deltaHeight);
  
      Mark mark = new Mark(label, new Geodetic3D(currentGeodetic._latitude, currentGeodetic._longitude, currentGeodetic._height + _vertexSphereRadius *2), AltitudeMode.ABSOLUTE);
      mark.setZoomInAppears(false);
  
      mark.setToken(_instanceID);
  
      _marksRenderer.addMark(mark);
    }
  }

  private int _selectedVertexIndex;
  private java.util.ArrayList<Measure_VertexShape> _verticesSpheres = new java.util.ArrayList<Measure_VertexShape>();

  private MeasureHandler _measureHandler;
  private final boolean _deleteMeasureHandler;


  public Measure(double vertexSphereRadius, Color vertexColor, Color vertexSelectedColor, float segmentLineWidth, Color segmentColor, Geodetic3D firstVertex, float verticalExaggeration, double deltaHeight, boolean closed, MeasureHandler measureHandler, boolean deleteMeasureHandler)
  {
     _instanceID = "_Measure_" + IStringUtils.instance().toString(INSTANCE_COUNTER++);
     _vertexSphereRadius = vertexSphereRadius;
     _vertexColor = vertexColor;
     _vertexSelectedColor = vertexSelectedColor;
     _segmentLineWidth = segmentLineWidth;
     _segmentColor = segmentColor;
     _verticalExaggeration = verticalExaggeration;
     _deltaHeight = deltaHeight;
     _closed = closed;
     _measureHandler = measureHandler;
     _deleteMeasureHandler = deleteMeasureHandler;
     _selectedVertexIndex = -1;
     _shapesRenderer = null;
     _meshRenderer = null;
     _marksRenderer = null;
     _compositeRenderer = null;
     _planet = null;
    addVertex(firstVertex);
  }

  public final void initialize(ShapesRenderer shapesRenderer, MeshRenderer meshRenderer, MarksRenderer marksRenderer, CompositeRenderer compositeRenderer, Planet planet)
  {
    _shapesRenderer = shapesRenderer;
    _meshRenderer = meshRenderer;
    _marksRenderer = marksRenderer;
    _compositeRenderer = compositeRenderer;
    _planet = planet;
  
    resetUI();
  }

  public final double getVertexSphereRadius()
  {
    return _vertexSphereRadius;
  }

  public final int getVerticesCount()
  {
    return _vertices.size();
  }

  public final void addVertex(Geodetic3D vertex)
  {
    clearSelection();
  
    _vertices.add(new MeasureVertex(vertex));
  
    resetUI();
  }

  public final void setVertex(int i, Geodetic3D vertex)
  {
    clearSelection();
  
    if (_vertices.get(i) != null)
       _vertices.get(i).dispose();
  
    _vertices.set(i, new MeasureVertex(vertex));
  
    resetUI();
  }

  public final boolean removeVertex(int i)
  {
    if (_vertices.size() == 1)
    {
      return false;
    }
  
    clearSelection();
  
    _vertices.remove(i);
  
    resetUI();
  
    return true;
  }

  public final Geodetic3D getVertex(int i)
  {
    return _vertices.get(i).getGeodetic();
    //  return _vertices[i]->getScaledGeodetic(_verticalExaggeration, _deltaHeight);
  }

  public final float getVerticalExaggeration()
  {
    return _verticalExaggeration;
  }
  public final double getDeltaHeight()
  {
    return _deltaHeight;
  }

  public final void setVerticalExaggeration(float verticalExaggeration)
  {
    if (_verticalExaggeration != verticalExaggeration)
    {
      _verticalExaggeration = verticalExaggeration;
  
      clearSelection();
      resetUI();
    }
  }
  public final void setDeltaHeight(double deltaHeight)
  {
    if (_deltaHeight != deltaHeight)
    {
      _deltaHeight = deltaHeight;
  
      clearSelection();
      resetUI();
    }
  }

  public void dispose()
  {
    for (int i = 0; i < _vertices.size(); i++)
    {
      if (_vertices.get(i) != null)
         _vertices.get(i).dispose();
    }
  
    if (_deleteMeasureHandler)
    {
      if (_measureHandler != null)
         _measureHandler.dispose();
    }
  }

  public final void clearSelection()
  {
    if (_selectedVertexIndex < 0)
    {
      return;
    }
  
    _verticesSpheres.get(_selectedVertexIndex).setSelected(false);
    _selectedVertexIndex = -1;
  
    if (_measureHandler != null)
    {
      _measureHandler.onVertexDeselection(this);
    }
  }

  public final void cleanUI()
  {
    _verticesSpheres.clear();
  
    if (_shapesRenderer != null)
    {
      _shapesRenderer.removeAllShapes(new Measure_ShapeFilter(_instanceID), true); // deleteShapes
    }
    if (_meshRenderer != null)
    {
      _meshRenderer.removeAllMeshes(new Measure_MeshFilter(_instanceID), true); // deleteMeshes
    }
    if (_marksRenderer != null)
    {
      _marksRenderer.removeAllMarks(new Measure_MarkFilter(_instanceID), false, true); // deleteMarks -  animated
    }
  
    if (_compositeRenderer != null)
    {
      _compositeRenderer.removeAllRenderers();
    }
  }

  public final void toggleSelection(int vertexIndex)
  {
    if (vertexIndex == _selectedVertexIndex)
    {
      _verticesSpheres.get(_selectedVertexIndex).setSelected(false);
      _selectedVertexIndex = -1;
    }
    else
    {
      if (_selectedVertexIndex >= 0)
      {
        _verticesSpheres.get(_selectedVertexIndex).setSelected(false);
      }
      _selectedVertexIndex = vertexIndex;
      _verticesSpheres.get(_selectedVertexIndex).setSelected(true);
    }
  
    if (_measureHandler != null)
    {
      if (_selectedVertexIndex < 0)
      {
        _measureHandler.onVertexDeselection(this);
      }
      else
      {
        final MeasureVertex vertex = _vertices.get(_selectedVertexIndex);
  
        _measureHandler.onVertexSelection(this, vertex.getScaledGeodetic(_verticalExaggeration, _deltaHeight), vertex.getCartesian(_planet), _selectedVertexIndex);
      }
    }
  }

  public final void setClosed(boolean closed)
  {
    if (_closed != closed)
    {
      _closed = closed;
  
      resetUI();
    }
  }

  public final boolean isClosed()
  {
    return _closed;
  }

}