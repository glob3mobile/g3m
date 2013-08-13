package org.glob3.mobile.generated; 
//
//  BoxShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//

//
//  BoxShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//



public class BoxShape extends AbstractMeshShape
{
  private double _extentX;
  private double _extentY;
  private double _extentZ;

  private float _borderWidth;

  private Color _surfaceColor;
  private Color _borderColor;

  private Mesh createBorderMesh(G3MRenderContext rc)
  {
    final float lowerX = (float) -(_extentX / 2);
    final float upperX = (float) +(_extentX / 2);
    final float lowerY = (float) -(_extentY / 2);
    final float upperY = (float) +(_extentY / 2);
    final float lowerZ = (float) -(_extentZ / 2);
    final float upperZ = (float) +(_extentZ / 2);
  
    float[] v = { lowerX, lowerY, lowerZ, lowerX, upperY, lowerZ, lowerX, upperY, upperZ, lowerX, lowerY, upperZ, upperX, lowerY, lowerZ, upperX, upperY, lowerZ, upperX, upperY, upperZ, upperX, lowerY, upperZ };
  
    final int numIndices = 48;
    short[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2, 2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };
  
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int numVertices = 8;
    for (int n = 0; n<numVertices; n++)
    {
      vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
    }
  
    for (int n = 0; n<numIndices; n++)
    {
      indices.add(i[n]);
    }
  
    Color borderColor = (_borderColor != null) ? new Color(_borderColor) : new Color(_surfaceColor);
  
    return new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), _borderWidth, 1, borderColor);
  }
  private Mesh createSurfaceMesh(G3MRenderContext rc)
  {
    final float lowerX = (float) -(_extentX / 2);
    final float upperX = (float) +(_extentX / 2);
    final float lowerY = (float) -(_extentY / 2);
    final float upperY = (float) +(_extentY / 2);
    final float lowerZ = (float) -(_extentZ / 2);
    final float upperZ = (float) +(_extentZ / 2);
  
    float[] v = { lowerX, lowerY, lowerZ, lowerX, upperY, lowerZ, lowerX, upperY, upperZ, lowerX, lowerY, upperZ, upperX, lowerY, lowerZ, upperX, upperY, lowerZ, upperX, upperY, upperZ, upperX, lowerY, upperZ };
  
    final int numIndices = 23;
    short[] i = { 3, 0, 7, 4, 6, 5, 2, 1, 3, 0, 0, 2, 2, 3, 6, 7, 7, 5, 5, 4, 1, 0, 0 };
  
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int numVertices = 8;
    for (int n = 0; n<numVertices; n++)
    {
      vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
    }
  
    for (int n = 0; n<numIndices; n++)
    {
      indices.add(i[n]);
    }
  
    Color surfaceColor = (_surfaceColor == null) ? null : new Color(_surfaceColor);
  
    return new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), _borderWidth, 1, surfaceColor);
  }

  protected final Mesh createMesh(G3MRenderContext rc)
  {
    if (_borderWidth > 0)
    {
      CompositeMesh compositeMesh = new CompositeMesh();
      compositeMesh.addMesh(createSurfaceMesh(rc));
      compositeMesh.addMesh(createBorderMesh(rc));
      return compositeMesh;
    }
  
    return createSurfaceMesh(rc);
  }

  public BoxShape(Geodetic3D position, Vector3D extent, float borderWidth, Color surfaceColor)
  {
     this(position, extent, borderWidth, surfaceColor, null);
  }
  public BoxShape(Geodetic3D position, Vector3D extent, float borderWidth)
  {
     this(position, extent, borderWidth, null, null);
  }
  public BoxShape(Geodetic3D position, Vector3D extent, float borderWidth, Color surfaceColor, Color borderColor)
  {
     super(position);
     _extentX = extent._x;
     _extentY = extent._y;
     _extentZ = extent._z;
     _borderWidth = borderWidth;
     _surfaceColor = surfaceColor;
     _borderColor = borderColor;

  }

  public void dispose()
  {
    if (_surfaceColor != null)
       _surfaceColor.dispose();
    if (_borderColor != null)
       _borderColor.dispose();
    JAVA_POST_DISPOSE
  }

  public final void setExtent(Vector3D extent)
  {
    if ((_extentX != extent._x) || (_extentY != extent._y) || (_extentZ != extent._z))
    {
      _extentX = extent._x;
      _extentY = extent._y;
      _extentZ = extent._z;
      cleanMesh();
    }
  }

  public final Vector3D getExtent()
  {
    return new Vector3D(_extentX, _extentY, _extentZ);
  }

  public final void setSurfaceColor(Color color)
  {
    if (_surfaceColor != null)
       _surfaceColor.dispose();
    _surfaceColor = color;
    cleanMesh();
  }

  public final void setBorderColor(Color color)
  {
    if (_borderColor != null)
       _borderColor.dispose();
    _borderColor = color;
    cleanMesh();
  }

  public final void setBorderWidth(float borderWidth)
  {
    if (_borderWidth != borderWidth)
    {
      _borderWidth = borderWidth;
      cleanMesh();
    }
  }

}