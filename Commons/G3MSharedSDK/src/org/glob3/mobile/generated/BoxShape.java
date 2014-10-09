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



//class OrientedBox;


public class BoxShape extends AbstractMeshShape
{
  private double _extentX;
  private double _extentY;
  private double _extentZ;


  private OrientedBox _boundingVolume;

  private float _borderWidth;
  private float _originalBorderWidth;

  private boolean _useNormals;

  private Color _surfaceColor;
  private Color _borderColor;
  private Color _originalBorderColor;

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
  
  //  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero);
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
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
  
    Mesh result = new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), (_borderWidth>1)? _borderWidth : 1, 1, borderColor);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
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
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
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
  
    Mesh result = new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), (_borderWidth>1)? _borderWidth : 1, 1, surfaceColor);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  private Mesh createSurfaceMeshWithNormals(G3MRenderContext rc)
  {
    final float lowerX = (float) -(_extentX / 2);
    final float upperX = (float) +(_extentX / 2);
    final float lowerY = (float) -(_extentY / 2);
    final float upperY = (float) +(_extentY / 2);
    final float lowerZ = (float) -(_extentZ / 2);
    final float upperZ = (float) +(_extentZ / 2);
  
    FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
    FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
    float[] v = { lowerX, upperY, lowerZ, lowerX, upperY, upperZ, upperX, upperY, lowerZ, upperX, upperY, lowerZ, lowerX, upperY, upperZ, upperX, upperY, upperZ, lowerX, lowerY, lowerZ, lowerX, lowerY, upperZ, upperX, lowerY, lowerZ, upperX, lowerY, lowerZ, lowerX, lowerY, upperZ, upperX, lowerY, upperZ, lowerX, lowerY, upperZ, lowerX, upperY, upperZ, upperX, lowerY, upperZ, upperX, lowerY, upperZ, lowerX, upperY, upperZ, upperX, upperY, upperZ, lowerX, lowerY, lowerZ, lowerX, upperY, lowerZ, upperX, lowerY, lowerZ, upperX, lowerY, lowerZ, lowerX, upperY, lowerZ, upperX, upperY, lowerZ, upperX, lowerY, lowerZ, upperX, lowerY, upperZ, upperX, upperY, lowerZ, upperX, upperY, lowerZ, upperX, lowerY, upperZ, upperX, upperY, upperZ, lowerX, lowerY, lowerZ, lowerX, lowerY, upperZ, lowerX, upperY, lowerZ, lowerX, upperY, lowerZ, lowerX, lowerY, upperZ, lowerX, upperY, upperZ};
      //FACE 1
      //FACE 2
      //FACE 3
      //FACE 4
      //FACE 5
      //FACE 6
  
    float[] n = { 0, 1, 0, 0, -1, 0, 0, 0, 1, 0, 0, -1, 1, 0, 0, -1, 0, 0 };
      //FACE 1
      //FACE 2
      //FACE 3
      //FACE 4
      //FACE 5
      //FACE 6
  
  
    final int numFaces = 6;
    final int numVertices = 6 * numFaces;
  
    for (int i = 0; i<numVertices; i++)
    {
      vertices.add(v[i *3], v[i *3+1], v[i *3+2]);
    }
  
    for (int i = 0; i<numFaces; i++)
    {
      normals.add(n[i *3], n[i *3+1], n[i *3+2]);
      normals.add(n[i *3], n[i *3+1], n[i *3+2]);
      normals.add(n[i *3], n[i *3+1], n[i *3+2]);
      normals.add(n[i *3], n[i *3+1], n[i *3+2]);
      normals.add(n[i *3], n[i *3+1], n[i *3+2]);
      normals.add(n[i *3], n[i *3+1], n[i *3+2]);
    }
  
    Color surfaceColor = (_surfaceColor == null) ? null : new Color(_surfaceColor);
  
    Mesh result = new DirectMesh(GLPrimitive.triangles(), true, vertices.getCenter(), vertices.create(), (_borderWidth>1)? _borderWidth : 1, 1, surfaceColor, null, 1, true, normals.create());
  
    if (vertices != null)
       vertices.dispose();
    if (normals != null)
       normals.dispose();
  
    return result;
  }

  protected final Mesh createMesh(G3MRenderContext rc)
  {
  
    Mesh surface = null;
    if (_useNormals)
    {
      surface = createSurfaceMeshWithNormals(rc);
    }
    else
    {
      surface = createSurfaceMesh(rc);
    }
  
    if (_borderWidth > 0)
    {
      CompositeMesh compositeMesh = new CompositeMesh();
      compositeMesh.addMesh(surface);
      compositeMesh.addMesh(createBorderMesh(rc));
      return compositeMesh;
    }
  
    return surface;
  }
  protected final BoundingVolume getBoundingVolume(G3MRenderContext rc)
  {
    if (_boundingVolume == null)
    {
      final Vector3D upper = getExtent().times(0.5);
      final Vector3D lower = upper.times(-1);
      _boundingVolume = new OrientedBox(lower, upper, getTransformMatrix(rc.getPlanet()));
    }
    return _boundingVolume;
  }

  public BoxShape(Geodetic3D position, AltitudeMode altitudeMode, Vector3D extent, float borderWidth, Color surfaceColor, Color borderColor)
  {
     this(position, altitudeMode, extent, borderWidth, surfaceColor, borderColor, true);
  }
  public BoxShape(Geodetic3D position, AltitudeMode altitudeMode, Vector3D extent, float borderWidth, Color surfaceColor)
  {
     this(position, altitudeMode, extent, borderWidth, surfaceColor, null, true);
  }
  public BoxShape(Geodetic3D position, AltitudeMode altitudeMode, Vector3D extent, float borderWidth, Color surfaceColor, Color borderColor, boolean useNormals)
  {
     super(position, altitudeMode);
     _boundingVolume = null;
     _extentX = extent._x;
     _extentY = extent._y;
     _extentZ = extent._z;
     _borderWidth = borderWidth;
     _originalBorderWidth = borderWidth;
     _surfaceColor = new Color(surfaceColor);
     _borderColor = borderColor;
     _originalBorderColor = (borderColor!=null)? new Color(borderColor) : null;
     _useNormals = useNormals;

  }

  public void dispose()
  {
    if (_surfaceColor != null)
       _surfaceColor.dispose();
    if (_borderColor != null)
       _borderColor.dispose();
    if (_originalBorderColor != null)
       _originalBorderColor.dispose();
    if (_boundingVolume != null)
      if (_boundingVolume != null)
         _boundingVolume.dispose();
  
    super.dispose();
  
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

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Camera camera, Vector3D origin, Vector3D direction)
  {
    return _boundingVolume.intersectionsDistances(origin, direction);
  }

  public final boolean isVisible(G3MRenderContext rc)
  {
    return getBoundingVolume(rc).touchesFrustum(rc.getCurrentCamera().getFrustumInModelCoordinates());
  }

  public final void setSelectedDrawMode(boolean mode)
  {
    if (mode)
    {
      setBorderWidth(7);
      setBorderColor(Color.newFromRGBA(1, 1, 0, 1));
    }
    else
    {
      setBorderWidth(_originalBorderWidth);
      if (_originalBorderColor!=null)
        setBorderColor(new Color(_originalBorderColor));
    }
  }

}