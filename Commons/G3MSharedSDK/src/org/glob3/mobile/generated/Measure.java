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
//class Planet;
//class MeasureVertexShape;
//class MeasureVertexSelectionHandler;
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

  private final java.util.ArrayList<MeasureVertex> _vertices = new java.util.ArrayList<MeasureVertex>();

  private ShapesRenderer _shapesRenderer;
  private MeshRenderer _meshRenderer;
  private MarksRenderer _marksRenderer;

  private final Planet _planet;

  private void reset()
  {
    {
      // clean up
      _shapesRenderer.removeAllShapes(new Measure_ShapeFilter(_instanceID), true); // deleteShapes
      _meshRenderer.removeAllMeshes(new Measure_MeshFilter(_instanceID), true); // deleteMeshes
      _marksRenderer.removeAllMarks(new Measure_MarkFilter(_instanceID), false, true); // deleteMarks -  animated
  
      _verticesSpheres.clear();
    }
  
    {
      // create 3d objects
      createVerticesSpheres();
      createEdgeLines();
      createEdgeDistanceLabels();
      createVertexAngleLabels();
    }
  }
  private void createVerticesSpheres()
  {
    final int verticesCount = _vertices.size();
  
    for (int i = 0; i < verticesCount; i++)
    {
      final MeasureVertex vertex = _vertices.get(i);
  
      MeasureVertexShape vertexSphere = new MeasureVertexShape(new Geodetic3D(vertex._geodetic), _vertexSphereRadius, _vertexColor, _vertexSelectedColor, this, _instanceID, i);
  
      _verticesSpheres.add(vertexSphere);
  
      _shapesRenderer.addShape(vertexSphere);
    }
  }
  private void createEdgeLines()
  {
    final int verticesCount = _vertices.size();
    if (verticesCount < 2)
    {
      return;
    }
  
    // create edges lines
    FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_planet);
  
    for (int i = 0; i < verticesCount; i++)
    {
      fbb.add(_vertices.get(i)._geodetic);
    }
  
    Mesh edgesLines = new DirectMesh(GLPrimitive.lineStrip(), true, fbb.getCenter(), fbb.create(), _segmentLineWidth, 1.0f, new Color(_segmentColor), null, false); // depthTest -  const IFloatBuffer* colors -  float pointSize
  
    edgesLines.setToken(_instanceID);
    _meshRenderer.addMesh(edgesLines);
  
    if (fbb != null)
       fbb.dispose();
  }
  private void createEdgeDistanceLabels()
  {
    final int verticesCount = _vertices.size();
    if (verticesCount < 2)
    {
      return;
    }
  
    final IStringUtils su = IStringUtils.instance();
    for (int i = 1; i < verticesCount; i++)
    {
      final MeasureVertex previous = _vertices.get(i - 1);
      final MeasureVertex current = _vertices.get(i);
  
      final String label = su.toString((float) previous._cartesian.distanceTo(current._cartesian)) + "m";
  
      final Geodetic3D position = Geodetic3D.linearInterpolation(previous._geodetic, current._geodetic, 0.5);
  
      Mark mark = new Mark(label, new Geodetic3D(position._latitude, position._longitude, position._height + _vertexSphereRadius), AltitudeMode.ABSOLUTE);
  
      mark.setToken(_instanceID);
  
      _marksRenderer.addMark(mark);
    }
  }
  private void createVertexAngleLabels()
  {
    final int verticesCount = _vertices.size();
    if (verticesCount < 3)
    {
      return;
    }
  
    final IStringUtils su = IStringUtils.instance();
    for (int i = 1; i < verticesCount - 1; i++)
    {
      final MeasureVertex previous = _vertices.get(i - 1);
      final MeasureVertex current = _vertices.get(i);
      final MeasureVertex next = _vertices.get(i + 1);
  
      final Vector3D v0 = current._cartesian.sub(previous._cartesian);
      final Vector3D v1 = current._cartesian.sub(next._cartesian);
  
      final Angle angle = v0.angleBetween(v1);
      final String label = su.toString((float) angle._degrees) + "d";
  
      Mark mark = new Mark(label, new Geodetic3D(current._geodetic._latitude, current._geodetic._longitude, current._geodetic._height + _vertexSphereRadius *2), AltitudeMode.ABSOLUTE);
  
      mark.setToken(_instanceID);
  
      _marksRenderer.addMark(mark);
    }
  }

  private int _selectedVertexIndex;
  private java.util.ArrayList<MeasureVertexShape> _verticesSpheres = new java.util.ArrayList<MeasureVertexShape>();

  private MeasureVertexSelectionHandler _measureVertexSelectionHandler;
  private final boolean _deleteMeasureVertexSelectionHandler;

  private void resetSelection()
  {
    if (_selectedVertexIndex < 0)
    {
      return;
    }
  
    _verticesSpheres.get(_selectedVertexIndex).setSelected(false);
    _selectedVertexIndex = -1;
  
    if (_measureVertexSelectionHandler != null)
    {
      _measureVertexSelectionHandler.onVertexDeselection(this);
    }
  }


  public Measure(double vertexSphereRadius, Color vertexColor, Color vertexSelectedColor, float segmentLineWidth, Color segmentColor, Geodetic3D firstVertex, ShapesRenderer shapesRenderer, MeshRenderer meshRenderer, MarksRenderer marksRenderer, Planet planet, MeasureVertexSelectionHandler measureVertexSelectionHandler, boolean deleteMeasureVertexSelectionHandler)
  {
     _instanceID = "_Measure_" + IStringUtils.instance().toString(INSTANCE_COUNTER++);
     _vertexSphereRadius = vertexSphereRadius;
     _vertexColor = vertexColor;
     _vertexSelectedColor = vertexSelectedColor;
     _segmentLineWidth = segmentLineWidth;
     _segmentColor = segmentColor;
     _shapesRenderer = shapesRenderer;
     _meshRenderer = meshRenderer;
     _marksRenderer = marksRenderer;
     _planet = planet;
     _selectedVertexIndex = -1;
     _measureVertexSelectionHandler = measureVertexSelectionHandler;
     _deleteMeasureVertexSelectionHandler = deleteMeasureVertexSelectionHandler;
    addVertex(firstVertex);
  }

  public final int getVerticesCount()
  {
    return _vertices.size();
  }

  public final void addVertex(Geodetic3D vertex)
  {
    resetSelection();
  
    _vertices.add(new MeasureVertex(vertex, _planet));
  
    reset();
  }

  public final void setVertex(int i, Geodetic3D vertex)
  {
    resetSelection();
  
    if (_vertices.get(i) != null)
       _vertices.get(i).dispose();
  
    _vertices.set(i, new MeasureVertex(vertex, _planet));
  
    reset();
  }

  public final boolean removeVertex(int i)
  {
    if (_vertices.size() == 1)
    {
      return false;
    }
  
    resetSelection();
  
    _vertices.remove(i);
  
    reset();
  
    return true;
  }

  public void dispose()
  {
    for (int i = 0; i < _vertices.size(); i++)
    {
      if (_vertices.get(i) != null)
         _vertices.get(i).dispose();
    }
  
    if (_deleteMeasureVertexSelectionHandler)
    {
      if (_measureVertexSelectionHandler != null)
         _measureVertexSelectionHandler.dispose();
    }
  }

  public final void touchedOn(int vertexIndex)
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
  
    if (_measureVertexSelectionHandler != null)
    {
      if (_selectedVertexIndex < 0)
      {
        _measureVertexSelectionHandler.onVertexDeselection(this);
      }
      else
      {
        final MeasureVertex vertex = _vertices.get(_selectedVertexIndex);
  
        _measureVertexSelectionHandler.onVertexSelection(this, vertex._geodetic, vertex._cartesian, _selectedVertexIndex);
      }
    }
  }

}