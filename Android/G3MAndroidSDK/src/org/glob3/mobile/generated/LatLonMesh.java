package org.glob3.mobile.generated; 
//
//  LatLonMesh.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  LatLonMesh.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




public class LatLonMesh extends Mesh
{

  private IndexedMesh mesh;

  public LatLonMesh(InitializationContext ic, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int indexes, int numIndex, Color flatColor, float colors, float colorsIntensity)
  {
	  this(ic, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, colorsIntensity, null);
  }
  public LatLonMesh(InitializationContext ic, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int indexes, int numIndex, Color flatColor, float colors)
  {
	  this(ic, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, 0.0, null);
  }
  public LatLonMesh(InitializationContext ic, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int indexes, int numIndex, Color flatColor)
  {
	  this(ic, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, null, 0.0, null);
  }
  public LatLonMesh(InitializationContext ic, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int indexes, int numIndex)
  {
	  this(ic, owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, null, null, 0.0, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: LatLonMesh(const InitializationContext *ic, boolean owner, const GLPrimitive primitive, CenterStrategy strategy, Vector3D center, const int numVertices, float* vertices, const int* indexes, const int numIndex, const Color* flatColor = null, const float * colors = null, const float colorsIntensity = 0.0, const float* normals = null)
  public LatLonMesh(InitializationContext ic, boolean owner, GLPrimitive primitive, CenterStrategy strategy, Vector3D center, int numVertices, float[] vertices, int indexes, int numIndex, Color flatColor, float colors, float colorsIntensity, float normals)
  {
	// convert vertices to latlon coordinates
	final Planet planet = ic.getPlanet();
	for (int n = 0; n<numVertices *3; n+=3)
	{
	  Geodetic3D g = new Geodetic3D(Angle.fromDegrees(vertices[n]), Angle.fromDegrees(vertices[n+1]), vertices[n+2]);
	  Vector3D v = planet.toVector3D(g);
	  vertices[n] = (float) v.x();
	  vertices[n+1] = (float) v.y();
	  vertices[n+2] = (float) v.z();
	}
  
	// create indexed mesh
	mesh = new IndexedMesh(owner, primitive, strategy, center, numVertices, vertices, indexes, numIndex, flatColor, colors, colorsIntensity, normals);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
  public final int getVertexCount()
  {
	  return mesh.getVertexCount();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
  public final Vector3D getVertex(int i)
  {
	  return mesh.getVertex(i);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const RenderContext* rc) const
  public final void render(RenderContext rc)
  {
	  mesh.render(rc);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent *getExtent() const
  public final Extent getExtent()
  {
	  return mesh.getExtent();
  }

  public void dispose()
  {
	  if (mesh != null)
		  mesh.dispose();
  }

}