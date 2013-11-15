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
    return new Vector3F(vertices.get(index), vertices.get(index + 1), vertices.get(index + 2));
  }


  public static IFloatBuffer createTriangleSmoothNormals(IFloatBuffer vertices, IShortBuffer indices)
  {
  
    final int size = vertices.size();
    IFloatBuffer normals = IFactory.instance().createFloatBuffer(size);
    for (int i = 0; i < size; i++)
    {
      normals.rawPut(i, 0);
    }
  
    final int indicesSize = indices.size();
    for (int i = 0; i < indicesSize; i += 3)
    {
      final short index0 = indices.get(i);
      final short index1 = indices.get(i + 1);
      final short index2 = indices.get(i + 2);
  
      final Vector3F vertex0 = getVertex(vertices, index0);
      final Vector3F vertex1 = getVertex(vertices, index1);
      final Vector3F vertex2 = getVertex(vertices, index2);
  
      final Vector3F p10 = vertex1.sub(vertex0);
      final Vector3F p20 = vertex2.sub(vertex0);
  
      final Vector3F normal = p10.cross(p20);
      normals.rawAdd(i, normal._x);
      normals.rawAdd(i + 1, normal._y);
      normals.rawAdd(i + 2, normal._z);
    }
  
    for (int i = 0; i < size; i += 3)
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

//  static IFloatBuffer* createTriangleStripSmoothNormals(const IFloatBuffer* vertices,
//                                                        const IShortBuffer* indices);

}
//IFloatBuffer* NormalsUtils::createTriangleStripSmoothNormals(const IFloatBuffer* vertices,
//                                                             const IShortBuffer* indices) {
//  const int size = vertices->size();
//  IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(size);
//  for (int i = 0; i < size; i++) {
//    normals->rawPut(i, 0);
//  }
//
//  int __TODO;
//  //http://stackoverflow.com/questions/3485034/convert-triangle-strips-to-triangles
//  return normals;
//}
