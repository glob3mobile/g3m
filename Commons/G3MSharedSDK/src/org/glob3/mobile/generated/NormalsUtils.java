package org.glob3.mobile.generated; 
//
//  NormalsUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/15/13.
//
//

//
//  NormalsUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/15/13.
//
//


//class IFloatBuffer;
//class IShortBuffer;


public class NormalsUtils
{
  private NormalsUtils()
  {
  }

  private static Vector3F getVertex(IFloatBuffer vertices, short index)
  {
    return new Vector3F(vertices.get(index *3), vertices.get(index *3 + 1), vertices.get(index *3 + 2));
  }

  private static void addNormal(IFloatBuffer normals, int index, Vector3F normal)
  {
    normals.rawAdd(index *3, normal._x);
    normals.rawAdd(index *3 + 1, normal._y);
    normals.rawAdd(index *3 + 2, normal._z);
  }

  private static Vector3F calculateNormal(IFloatBuffer vertices, short index0, short index1, short index2)
  {
    final Vector3F vertex0 = getVertex(vertices, index0);
    final Vector3F vertex1 = getVertex(vertices, index1);
    final Vector3F vertex2 = getVertex(vertices, index2);
  
    final Vector3F p10 = vertex1.sub(vertex0);
    final Vector3F p20 = vertex2.sub(vertex0);
  
    return p10.cross(p20);
  }


  public static IFloatBuffer createTriangleSmoothNormals(IFloatBuffer vertices, IShortBuffer indices)
  {
  
    final int verticesSize = vertices.size();
    IFloatBuffer normals = IFactory.instance().createFloatBuffer(verticesSize);
    for (int i = 0; i < verticesSize; i++)
    {
      normals.rawPut(i, 0.0f);
    }
  
    final int indicesSize = indices.size();
    for (int i = 0; i < indicesSize; i += 3)
    {
      final short index0 = indices.get(i);
      final short index1 = indices.get(i + 1);
      final short index2 = indices.get(i + 2);
  
      final Vector3F normal = calculateNormal(vertices, index0, index1, index2);
      addNormal(normals, index0, normal);
      addNormal(normals, index1, normal);
      addNormal(normals, index2, normal);
    }
  
    for (int i = 0; i < verticesSize; i += 3)
    {
      final float x = normals.get(i);
      final float y = normals.get(i + 1);
      final float z = normals.get(i + 2);
  
      final Vector3F normal = new Vector3F(x, y, z).normalized();
      normals.rawPut(i, normal._x);
      normals.rawPut(i + 1, normal._y);
      normals.rawPut(i + 2, normal._z);
    }
  
    return normals;
  }

  public static IFloatBuffer createTriangleStripSmoothNormals(IFloatBuffer vertices, IShortBuffer indices)
  {
    final int verticesSize = vertices.size();
    IFloatBuffer normals = IFactory.instance().createFloatBuffer(verticesSize);
    for (int i = 0; i < verticesSize; i++)
    {
      normals.rawPut(i, 0.0f);
    }
  
    short index0 = indices.get(0);
    short index1 = indices.get(1);
  
    final int indicesSize = indices.size();
    for (int i = 2; i < indicesSize; i++)
    {
      final short index2 = indices.get(i);
  
      final Vector3F normal = (i % 2 == 0) ? calculateNormal(vertices, index0, index1, index2) : calculateNormal(vertices, index0, index2, index1);
      /*                          */
      /*                          */
      addNormal(normals, index0, normal);
      addNormal(normals, index1, normal);
      addNormal(normals, index2, normal);
  
      index0 = index1;
      index1 = index2;
    }
  
    //http://stackoverflow.com/questions/3485034/convert-triangle-strips-to-triangles
  
    for (int i = 0; i < verticesSize; i += 3)
    {
      final float x = normals.get(i);
      final float y = normals.get(i + 1);
      final float z = normals.get(i + 2);
  
      final Vector3F normal = new Vector3F(x, y, z).normalized();
      normals.rawPut(i, normal._x);
      normals.rawPut(i + 1, normal._y);
      normals.rawPut(i + 2, normal._z);
    }
  
    return normals;
  }

}