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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* calculateExtent() const
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
  
	_extent = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int index) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const G3MRenderContext* rc) const
  public final void render(G3MRenderContext rc)
  {
	final int childrenCount = _children.size();
	for (int i = 0; i < childrenCount; i++)
	{
	  Mesh child = _children.get(i);
	  child.render(rc);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* getExtent() const
  public final Extent getExtent()
  {
	if (_extent == null)
	{
	  _extent = calculateExtent();
	}
	return _extent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
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
	_extent = null;
	_extent = null;
  
	_children.add(mesh);
  }

}