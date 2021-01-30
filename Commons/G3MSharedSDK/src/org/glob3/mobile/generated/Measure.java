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




//class Geodetic3D;
//class ShapesRenderer;
//class MeshRenderer;
//class MarksRenderer;
//class Planet;
//class MeasureVertexShape;
//class MeasureVertexSelectionHandler;


public class Measure
{
  private final double _vertexSphereRadius;
  private final Color _vertexColor ;
  private final Color _vertexSelectedColor ;
  private final float _segmentLineWidth;
  private final Color _segmentColor ;

  private final java.util.ArrayList<Geodetic3D> _vertices = new java.util.ArrayList<Geodetic3D>();

  private ShapesRenderer _shapesRenderer;
  private MeshRenderer _meshRenderer;
  private MarksRenderer _marksRenderer;

  private final Planet _planet;

  private void reset()
  {
    _shapesRenderer.removeAllShapes();
    _meshRenderer.clearMeshes();
    _marksRenderer.removeAllMarks();
  
    _verticesSpheres.clear();
  
    final int verticesCount = _vertices.size();
  
    // create vertices spheres
    for (int i = 0; i < verticesCount; i++)
    {
      final Geodetic3D geodetic = _vertices.get(i);
  
      MeasureVertexShape vertexSphere = new MeasureVertexShape(new Geodetic3D(geodetic), _vertexSphereRadius, _vertexColor, _vertexSelectedColor, this, i);
  
      _verticesSpheres.add(vertexSphere);
  
      _shapesRenderer.addShape(vertexSphere);
    }
  
  
    if (verticesCount > 1)
    {
      {
        // create edges lines
        FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_planet);
  
        for (int i = 0; i < verticesCount; i++)
        {
          final Geodetic3D geodetic = _vertices.get(i);
          fbb.add(geodetic);
        }
  
        Mesh edgesLines = new DirectMesh(GLPrimitive.lineStrip(), true, fbb.getCenter(), fbb.create(), _segmentLineWidth, 1.0f, new Color(_segmentColor), null, false); // depthTest -  const IFloatBuffer* colors -  float pointSize
  
        _meshRenderer.addMesh(edgesLines);
  
        if (fbb != null)
           fbb.dispose();
      }
  
      {
        // create edges distance labels
        Geodetic3D previousGeodetic = _vertices.get(0);
        Vector3D previousCartesian = new Vector3D(_planet.toCartesian(previousGeodetic));
        for (int i = 1; i < verticesCount; i++)
        {
          final Geodetic3D currentGeodetic = _vertices.get(i);
          final Vector3D currentCartesian = new Vector3D(_planet.toCartesian(currentGeodetic));
  
          Geodetic3D middle = Geodetic3D.linearInterpolation(previousGeodetic, currentGeodetic, 0.5);
          Mark distanceLabel = new Mark(IStringUtils.instance().toString((float) previousCartesian.distanceTo(currentCartesian)) + "m", middle, AltitudeMode.ABSOLUTE);
  
          _marksRenderer.addMark(distanceLabel);
  
          previousGeodetic = currentGeodetic;
  
          if (previousCartesian != null)
             previousCartesian.dispose();
          previousCartesian = currentCartesian;
        }
  
        if (previousCartesian != null)
           previousCartesian.dispose();
      }
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: vertices angle labels
  
    }
  
  }

  private int _selectedVertexIndex;
  private java.util.ArrayList<MeasureVertexShape> _verticesSpheres = new java.util.ArrayList<MeasureVertexShape>();

  private MeasureVertexSelectionHandler _measureVertexSelectionHandler;
  private final boolean _deleteMeasureVertexSelectionHandler;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO: add vertexSelectionHandler onVertexSelection(measure, geodetic, i);

  public Measure(double vertexSphereRadius, Color vertexColor, Color vertexSelectedColor, float segmentLineWidth, Color segmentColor, Geodetic3D firstVertex, ShapesRenderer shapesRenderer, MeshRenderer meshRenderer, MarksRenderer marksRenderer, Planet planet, MeasureVertexSelectionHandler measureVertexSelectionHandler, boolean deleteMeasureVertexSelectionHandler)
  {
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

  public final int getVexticesCount()
  {
    return _vertices.size();
  }

  public final void addVertex(Geodetic3D vertex)
  {
    _vertices.add(vertex);
  
    reset();
  }

  public final void setVertex(int i, Geodetic3D vertex)
  {
    final Geodetic3D current = _vertices.get(i);
    if (vertex != current)
    {
      if (current != null)
         current.dispose();
      _vertices.set(i, vertex);
  
      reset();
    }
  }

  public final boolean removeVertex(int i)
  {
    if (_vertices.size() == 1)
    {
      return false;
    }
  
    _vertices.remove(i);
  
    reset();
  
    return true;
  }

  public void dispose()
  {
    for (int i = 0; i < _vertices.size(); i++)
    {
      final Geodetic3D vertex = _vertices.get(i);
      if (vertex != null)
         vertex.dispose();
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
      final Geodetic3D geodetic = (_selectedVertexIndex < 0) ? null : _vertices.get(_selectedVertexIndex);
  
      _measureVertexSelectionHandler.onVextexSelection(this, geodetic, _selectedVertexIndex);
    }
  }

}