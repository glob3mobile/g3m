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



public class BoxShape extends MeshShape
{
  private double _extentX;
  private double _extentY;
  private double _extentZ;

  private float _borderWidth;

  private Color _surfaceColor;
  private Color _borderColor;

  private Mesh createBorderMesh(RenderContext rc)
  {
	final float lowerX = (float) -(_extentX / 2);
	final float upperX = (float) +(_extentX / 2);
	final float lowerY = (float) -(_extentY / 2);
	final float upperY = (float) +(_extentY / 2);
	final float lowerZ = (float) -(_extentZ / 2);
	final float upperZ = (float) +(_extentZ / 2);
  
	float[] v = { lowerX, lowerY, lowerZ, lowerX, upperY, lowerZ, lowerX, upperY, upperZ, lowerX, lowerY, upperZ, upperX, lowerY, lowerZ, upperX, upperY, lowerZ, upperX, upperY, upperZ, upperX, lowerY, upperZ };
  
	final int numIndices = 48;
	int[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2, 2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	IntBufferBuilder indices = new IntBufferBuilder();
  
	final int numVertices = 8;
	for (int n = 0; n<numVertices; n++)
	{
	  vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
	}
  
	for (int n = 0; n<numIndices; n++)
	{
	  indices.add(i[n]);
	}
  
	Color borderColor = (_borderColor != null) ? _borderColor : _surfaceColor;
  
	return new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), _borderWidth, borderColor);
  }
  private Mesh createSurfaceMesh(RenderContext rc)
  {
	final float lowerX = (float) -(_extentX / 2);
	final float upperX = (float) +(_extentX / 2);
	final float lowerY = (float) -(_extentY / 2);
	final float upperY = (float) +(_extentY / 2);
	final float lowerZ = (float) -(_extentZ / 2);
	final float upperZ = (float) +(_extentZ / 2);
  
	float[] v = { lowerX, lowerY, lowerZ, lowerX, upperY, lowerZ, lowerX, upperY, upperZ, lowerX, lowerY, upperZ, upperX, lowerY, lowerZ, upperX, upperY, lowerZ, upperX, upperY, upperZ, upperX, lowerY, upperZ };
  
	final int numIndices = 23;
	int[] i = { 3, 0, 7, 4, 6, 5, 2, 1, 3, 0, 0, 2, 2, 3, 6, 7, 7, 5, 5, 4, 1, 0, 0 };
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	IntBufferBuilder indices = new IntBufferBuilder();
  
	final int numVertices = 8;
	for (int n = 0; n<numVertices; n++)
	{
	  vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
	}
  
	for (int n = 0; n<numIndices; n++)
	{
	  indices.add(i[n]);
	}
  
	return new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), _borderWidth, _surfaceColor);
  }

  protected final Mesh createMesh(RenderContext rc)
  {
  
	//  const float lowerX = (float) -(_extentX / 2);
	//  const float upperX = (float) +(_extentX / 2);
	//  const float lowerY = (float) -(_extentY / 2);
	//  const float upperY = (float) +(_extentY / 2);
	//  const float lowerZ = (float) -(_extentZ / 2);
	//  const float upperZ = (float) +(_extentZ / 2);
	//
	//  float v[] = {
	//    lowerX, lowerY, lowerZ,
	//    lowerX, upperY, lowerZ,
	//    lowerX, upperY, upperZ,
	//    lowerX, lowerY, upperZ,
	//    upperX, lowerY, lowerZ,
	//    upperX, upperY, lowerZ,
	//    upperX, upperY, upperZ,
	//    upperX, lowerY, upperZ
	//  };
	//
	//  // lines
	////  const int numIndices = 48;
	////  int i[] = {
	////    0, 1, 1, 2, 2, 3, 3, 0,
	////    1, 5, 5, 6, 6, 2, 2, 1,
	////    5, 4, 4, 7, 7, 6, 6, 5,
	////    4, 0, 0, 3, 3, 7, 7, 4,
	////    3, 2, 2, 6, 6, 7, 7, 3,
	////    0, 1, 1, 5, 5, 4, 4, 0
	////  };
	//
	//  // triangleStrip
	//  const int numIndices = 23;
	//  int i[] = {
	//    3, 0, 7, 4, 6, 5, 2, 1, 3, 0, 0,
	//    2, 2, 3, 6, 7, 7,
	//    5, 5, 4, 1, 0, 0
	//  };
	//
	//  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
	//  IntBufferBuilder indices;
	//
	//  const unsigned int numVertices = 8;
	//  for (unsigned int n=0; n<numVertices; n++) {
	//    vertices.add(v[n*3], v[n*3+1], v[n*3+2]);
	//  }
	//
	//  for (unsigned int n=0; n<numIndices; n++) {
	//    indices.add(i[n]);
	//  }
	//
	//  return new IndexedMesh(GLPrimitive::triangleStrip(),
	//                         true,
	//                         vertices.getCenter(),
	//                         vertices.create(),
	//                         indices.create(),
	//                         1,
	//                         _color);
  
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: BoxShape(Geodetic3D* position, const Vector3D& extent, float borderWidth, Color* surfaceColor = null, Color* borderColor = null) : MeshShape(position), _extentX(extent._x), _extentY(extent._y), _extentZ(extent._z), _borderWidth(borderWidth), _surfaceColor(surfaceColor), _borderColor(borderColor)
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
	_surfaceColor = null;
	_borderColor = null;
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

  public final void setSurfaceColor(Color color)
  {
	_surfaceColor = null;
	_surfaceColor = color;
	cleanMesh();
  }

  public final void setBorderColor(Color color)
  {
	_borderColor = null;
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