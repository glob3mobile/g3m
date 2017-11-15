package org.glob3.mobile.generated; 
//
//  CircleShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

//
//  CircleShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//



public class CircleShape extends AbstractMeshShape
{
  private float _radius;
  private int _steps;
  private Color _color;

  private final boolean _useNormals;

  protected final Mesh createMesh(G3MRenderContext rc)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    // first is the center
    vertices.add(0.0, 0.0, 0.0);
  
    final double twicePi = DefineConstants.PI * 2;
  
    for (int i = 0; i <= _steps; i++)
    {
      final double angleInRadians = i * twicePi / _steps;
      final double x = _radius * mu.cos(angleInRadians);
      final double y = _radius * mu.sin(angleInRadians);
      vertices.add(x, y, 0);
    }
  
    Color color = (_color == null) ? null : new Color(_color);
  
    if (_useNormals)
    {
  
      FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      for (int i = 0; i <= _steps+1; i++)
      {
        normals.add(0.0, 0.0, 1.0);
      }
  
      Mesh result = new DirectMesh(GLPrimitive.triangleFan(), true, Vector3D.zero, vertices.create(), 1, 1, color, null, 1, true, normals.create());
  
      if (normals != null)
         normals.dispose();
      if (vertices != null)
         vertices.dispose();
  
      return result;
  
    }
  
    Mesh result = new DirectMesh(GLPrimitive.triangleFan(), true, Vector3D.zero, vertices.create(), 1, 1, color);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  public CircleShape(Geodetic3D position, AltitudeMode altitudeMode, float radius, Color color, int steps)
  {
     this(position, altitudeMode, radius, color, steps, true);
  }
  public CircleShape(Geodetic3D position, AltitudeMode altitudeMode, float radius, Color color)
  {
     this(position, altitudeMode, radius, color, 64, true);
  }
  public CircleShape(Geodetic3D position, AltitudeMode altitudeMode, float radius, Color color, int steps, boolean useNormals)
  {
     super(position, altitudeMode);
     _radius = radius;
     _color = new Color(color);
     _steps = steps;
     _useNormals = useNormals;

  }

  public void dispose()
  {
    if (_color != null)
       _color.dispose();

  super.dispose();

  }

  public final void setRadius(float radius)
  {
    if (_radius != radius)
    {
      _radius = radius;
      cleanMesh();
    }
  }

  public final void setColor(Color color)
  {
    if (_color != color)
    {
      if (_color != null)
         _color.dispose();
      _color = color;
      cleanMesh();
    }
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
    return intersections;
  }


}