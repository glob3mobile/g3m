package org.glob3.mobile.generated; 
//
//  PointCloudMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/09/14.
//
//

//
//  PointCloudMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/09/14.
//
//




//class MutableMatrix44D;
//class IFloatBuffer;
//class IByteBuffer;
//class Color;

public class PointCloudMesh extends Mesh
{
  private IFloatBuffer _points;
  private boolean _ownsPoints;
  private IByteBuffer _rgbColors;
  private boolean _ownsColors;
  private float _pointSize;
  private boolean _depthTest;

  private int _nPoints;

  private GLState _glState;
  private void createGLState()
  {
  
    _geometryGLFeature = new GeometryGLFeature(_points, 3, 0, false, 0, _depthTest, false, 0, false, 0, 0, (float)1.0, _pointSize); //Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    _glState.addGLFeature(_geometryGLFeature, false);
  
    _glState.addGLFeature(new ColorGLFeature(_rgbColors, 3, 0, false, 0, true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false); // Stride 0 -  Not normalized -  Index 0 -  Our buffer contains elements of 3 -  The attribute is a byte vector of 3 elements RGB
  }

  private BoundingVolume computeBoundingVolume()
  {
    final int vertexCount = getVertexCount();
  
    if (vertexCount <= 0)
    {
      return null;
    }
  
    double minX = 1e12;
    double minY = 1e12;
    double minZ = 1e12;
  
    double maxX = -1e12;
    double maxY = -1e12;
    double maxZ = -1e12;
  
    for (int i = 0; i < vertexCount; i++)
    {
      final int i3 = i * 3;
  
      final double x = _points.get(i3); // + _center._x;
      final double y = _points.get(i3 + 1); // + _center._y;
      final double z = _points.get(i3 + 2); // + _center._z;
  
      if (x < minX)
         minX = x;
      if (x > maxX)
         maxX = x;
  
      if (y < minY)
         minY = y;
      if (y > maxY)
         maxY = y;
  
      if (z < minZ)
         minZ = z;
      if (z > maxZ)
         maxZ = z;
    }
  
    return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
  }
  private BoundingVolume _boundingVolume;

  private GeometryGLFeature _geometryGLFeature;



  public PointCloudMesh(IFloatBuffer points, boolean ownsPoints, IByteBuffer rgbColors, boolean ownsColors, float pointSize)
  {
     this(points, ownsPoints, rgbColors, ownsColors, pointSize, true);
  }
  public PointCloudMesh(IFloatBuffer points, boolean ownsPoints, IByteBuffer rgbColors, boolean ownsColors)
  {
     this(points, ownsPoints, rgbColors, ownsColors, (float)1.0, true);
  }
  public PointCloudMesh(IFloatBuffer points, boolean ownsPoints, IByteBuffer rgbColors, boolean ownsColors, float pointSize, boolean depthTest)
  {
     _points = points;
     _ownsPoints = ownsPoints;
     _rgbColors = rgbColors;
     _ownsColors = ownsColors;
     _pointSize = pointSize;
     _depthTest = depthTest;
     _nPoints = points.size() / 3;
     _boundingVolume = null;
     _glState = new GLState();
     _geometryGLFeature = null;
    if (_nPoints != (rgbColors.size() / 3))
    {
      ILogger.instance().logError("Wrong parameters for PointCloudMesh()");
    }
  
    createGLState();
  }
  public void dispose()
  {
    if (_ownsPoints)
    {
      if (_points != null)
         _points.dispose();
    }
    if (_ownsColors)
    {
      if (_rgbColors != null)
         _rgbColors.dispose();
    }
    _glState._release();
    if (_boundingVolume != null)
       _boundingVolume.dispose();
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    _glState.setParent(parentGLState);
    GL gl = rc.getGL();
    gl.drawArrays(GLPrimitive.points(), 0, _nPoints, _glState, rc.getGPUProgramManager());
  }

  public final int getVertexCount()
  {
    return _nPoints;
  }

  public final Vector3D getVertex(int i)
  {
    final int p = i * 3;
    return new Vector3D(_points.get(p), _points.get(p+1), _points.get(p+2)); // + _center._z); -  + _center._y, -  + _center._x,
  }

  public final BoundingVolume getBoundingVolume()
  {
    if (_boundingVolume == null)
    {
      _boundingVolume = computeBoundingVolume();
    }
    return _boundingVolume;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return false;
  }

  public final void showNormals(boolean v)
  {
    //IDLE
  }

  public final void setPointSize(float v)
  {
    _geometryGLFeature.setPointSize(v);
  }

}