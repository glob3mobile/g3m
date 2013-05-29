package org.glob3.mobile.generated; 
//
//  CompositeMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//

//
//  CompositeMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//



public class CompositeMesh extends Mesh
{
  private java.util.ArrayList<Mesh> _children = new java.util.ArrayList<Mesh>();

  private Extent calculateExtent()
  {
    final int childrenCount = _children.size();
    if (childrenCount == 0)
    {
      return null;
    }
  
    Extent result = _children.get(0).getExtent();
    for (int i = 1; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      result = result.mergedWith(child.getExtent());
    }
  
    return result;
  }

  private Extent _extent;


  public void dispose()
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  
    if (_extent != null)
       _extent.dispose();
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
    return Vector3D.nan();
  }

  public final void render(G3MRenderContext rc)
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      child.render(rc);
    }
  }

  public final Extent getExtent()
  {
    if (_extent == null)
    {
      _extent = calculateExtent();
    }
    return _extent;
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
    if (_extent != null)
       _extent.dispose();
    _extent = null;
  
    _children.add(mesh);
  
    super.addChild(mesh);
  }

  //GLClient NotDrawable

  public final void notifyGLClientChildrenParentHasChanged()
  {
    final int childrenCount = _children.size();
    for (int i = 0; i < childrenCount; i++)
    {
      Mesh child = _children.get(i);
      child.actualizeGLGlobalState(this);
    }
  }
  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  }
  public final void modifyGPUProgramState(GPUProgramState progState)
  {
  }

  public final void rawRender(G3MRenderContext rc, GLStateTreeNode myStateTreeNode)
  {
  }
  public final boolean isInsideCameraFrustum(G3MRenderContext rc)
  {
    return true;
  }
  public final void modifiyGLState(GLState state)
  {
  }


}