package org.glob3.mobile.generated;import java.util.*;

//
//  GEOMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Ellipsoid;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;

public abstract class GEOMeshSymbol extends GEOSymbol
{

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createLine2DMesh(const java.util.ArrayList<Geodetic2D*>* coordinates, const Color& lineColor, float lineWidth, double deltaHeight, const Planet* planet) const
  protected final Mesh createLine2DMesh(java.util.ArrayList<Geodetic2D> coordinates, Color lineColor, float lineWidth, double deltaHeight, Planet planet)
  {
  
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
  
	final int coordinatesCount = coordinates.size();
	for (int i = 0; i < coordinatesCount; i++)
	{
	  final Geodetic2D coordinate = coordinates.get(i);
	  vertices.add(coordinate._latitude, coordinate._longitude, deltaHeight);
	}
  
	Mesh result = new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, new Color(lineColor), null, 0.0f, false);
  
	if (vertices != null)
		vertices.dispose();
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createLines2DMesh(const java.util.ArrayList<java.util.ArrayList<Geodetic2D*>*>* coordinatesArray, const Color& lineColor, float lineWidth, double deltaHeight, const Planet* planet) const
  protected final Mesh createLines2DMesh(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Color lineColor, float lineWidth, double deltaHeight, Planet planet)
  {
  
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
	ShortBufferBuilder indices = new ShortBufferBuilder();
  
	final int coordinatesArrayCount = coordinatesArray.size();
	short index = 0;
	for (int i = 0; i < coordinatesArrayCount; i++)
	{
	  java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
	  final int coordinatesCount = coordinates.size();
	  for (int j = 0; j < coordinatesCount; j++)
	  {
		final Geodetic2D coordinate = coordinates.get(j);
  
		vertices.add(coordinate._latitude, coordinate._longitude, deltaHeight);
  
		indices.add(index);
		if ((j > 0) && (j < (coordinatesCount-1)))
		{
		  indices.add(index);
		}
		index++;
	  }
	}
  
	Mesh result = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, lineWidth, 1, new Color(lineColor), null, 0.0f, false);
  
	if (vertices != null)
		vertices.dispose();
  
	return result;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createMesh(const G3MRenderContext* rc) const = 0;
  protected abstract Mesh createMesh(G3MRenderContext rc);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean symbolize(const G3MRenderContext* rc, const GEOSymbolizer* symbolizer, MeshRenderer* meshRenderer, ShapesRenderer* shapesRenderer, MarksRenderer* marksRenderer, GEOVectorLayer* geoVectorLayer) const
  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
	if (meshRenderer == null)
	{
	  ILogger.instance().logError("Can't symbolize with Mesh, MeshRenderer was not set");
	}
	else
	{
	  Mesh mesh = createMesh(rc);
	  if (mesh != null)
	  {
		meshRenderer.addMesh(mesh);
	  }
	}
	return true;
  }


}
