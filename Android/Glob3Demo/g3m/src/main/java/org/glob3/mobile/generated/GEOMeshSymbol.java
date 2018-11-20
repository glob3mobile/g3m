package org.glob3.mobile.generated;
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



//class Geodetic2D;
//class Color;
//class Ellipsoid;
//class Planet;

public abstract class GEOMeshSymbol extends GEOSymbol
{

  protected final Mesh createLine2DMesh(java.util.ArrayList<Geodetic2D> coordinates, Color lineColor, float lineWidth, double deltaHeight, Planet planet)
  {
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
  
    final int coordinatesCount = coordinates.size();
    for (int i = 0; i < coordinatesCount; i++)
    {
      final Geodetic2D coordinate = coordinates.get(i);
      vertices.add(coordinate._latitude, coordinate._longitude, deltaHeight);
    }
  
    Mesh result = new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, new Color(lineColor), null, false);
  
    modifyMeshAfterCreation(result);
  
    if (vertices != null)
       vertices.dispose();
  
    return result;
  }

  protected final Mesh createLines2DMesh(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Color lineColor, float lineWidth, double deltaHeight, Planet planet)
  {
  
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int coordinatesArrayCount = coordinatesArray.size();
    short index = 0;
  
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      final java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
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
  
    Mesh result = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, lineWidth, 1, new Color(lineColor), null, false);
  
    if (vertices != null)
       vertices.dispose();
  
    modifyMeshAfterCreation(result);
  
    return result;
  }


  protected abstract Mesh createMesh(G3MRenderContext rc);

  protected boolean _meshEnabledAtCreation;
  protected Mesh.MeshUserData _meshData;
  protected final void modifyMeshAfterCreation(Mesh result)
  {
    if (_meshData != null)
    {
      result.setUserData(_meshData);
      _meshData = null;
    }
    result.setEnable(_meshEnabledAtCreation);
  }


  public GEOMeshSymbol()
  {
     _meshData = null;
     _meshEnabledAtCreation = true;
  }

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

  public final void setMeshUserData(Mesh.MeshUserData meshData)
  {
    _meshData = meshData;
  }

  public final void setMeshEnabled(boolean enabled)
  {
    _meshEnabledAtCreation = enabled;
  }


}
