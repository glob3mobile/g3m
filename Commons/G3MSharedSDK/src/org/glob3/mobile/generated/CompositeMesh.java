package org.glob3.mobile.generated;
//
//  CompositeMesh.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//

//
//  CompositeMesh.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//





public class CompositeMesh extends Mesh
{
  private java.util.ArrayList<Mesh> _children = new java.util.ArrayList<Mesh>();

  private BoundingVolume calculateBoundingVolume()
  {
    final int childrenCount = _children.size();
    if (childrenCount == 0)
    {
      return null;
    }
  
    BoundingVolume childBV = _children.get(0).getBoundingVolume();
    if (childBV == null)
    {
      return null;
    }
    BoundingVolume result = childBV.copy();
    for (int i = 1; i < childrenCount; i++)
    {
      childBV = _children.get(i).getBoundingVolume();
      if (childBV == null)
      {
        if (result != null)
           result.dispose();
        return null;
      }
      BoundingVolume newResult = result.mergedWith(childBV);
      if (result != null)
         result.dispose();
      result = newResult;
    }
  
    return result;
  }

  private BoundingVolume _boundingVolume;


  public void dispose()
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  
    if (_boundingVolume != null)
       _boundingVolume.dispose();
  
    super.dispose();
  }

  public final int getVertexCount()
  {
    int result = 0;
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      result += child.getVertexCount();
    }
    return result;
  }

  public final Vector3D getVertex(int index)
  {
    int acumIndex = 0;
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      final int childIndex = index - acumIndex;
      final int childSize = child.getVertexCount();
      if (childIndex < childSize)
      {
        return child.getVertex(childIndex);
      }
      acumIndex += childSize;
    }
    return Vector3D.NANV;
  }
  public final void getVertex(int index, MutableVector3D result)
  {
    int acumIndex = 0;
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      final int childIndex = index - acumIndex;
      final int childSize = child.getVertexCount();
      if (childIndex < childSize)
      {
        child.getVertex(childIndex, result);
        return;
      }
      acumIndex += childSize;
    }
    result.copyFrom(Vector3D.NANV);
  }

  public final BoundingVolume getBoundingVolume()
  {
    if (_boundingVolume == null)
    {
      _boundingVolume = calculateBoundingVolume();
    }
    return _boundingVolume;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      if (child.isTransparent(rc))
      {
        return true;
      }
    }
    return false;
  }

  public final void addMesh(Mesh mesh)
  {
    if (_boundingVolume != null)
       _boundingVolume.dispose();
    _boundingVolume = null;
  
    _children.add(mesh);
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      child.render(rc, parentGLState);
    }
  }

}