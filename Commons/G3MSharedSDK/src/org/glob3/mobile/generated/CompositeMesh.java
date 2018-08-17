package org.glob3.mobile.generated;//
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
//ORIGINAL LINE: BoundingVolume* calculateBoundingVolume() const
	private BoundingVolume calculateBoundingVolume()
	{
	  final int childrenCount = _children.size();
	  if (childrenCount == 0)
	  {
		return null;
	  }
    
	  BoundingVolume result = _children.get(0).getBoundingVolume();
		result = result.mergedWith(result);
	  for (int i = 1; i < childrenCount; i++)
	  {
		Mesh child = _children.get(i);
		BoundingVolume newResult = result.mergedWith(child.getBoundingVolume());
		if (result != null)
			result.dispose();
		result = newResult;
	  }
    
	  return result;
	}

	private BoundingVolume _boundingVolume;



	public CompositeMesh()
	{
		_boundingVolume = null;
	}

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
    
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  super.dispose();
//#endif
    
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
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
	public final BoundingVolume getBoundingVolume()
	{
	  if (_boundingVolume == null)
	  {
		_boundingVolume = calculateBoundingVolume();
	  }
	  return _boundingVolume;
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
	  if (_boundingVolume != null)
		  _boundingVolume.dispose();
	  _boundingVolume = null;
    
	  _children.add(mesh);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const
	public final void rawRender(G3MRenderContext rc, GLState parentGLState)
	{
	  final int childrenCount = _children.size();
	  for (int i = 0; i < childrenCount; i++)
	  {
		Mesh child = _children.get(i);
		child.render(rc, parentGLState);
	  }
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
	public final void showNormals(boolean v)
	{
	  final int childrenCount = _children.size();
	  for (int i = 0; i < childrenCount; i++)
	  {
		Mesh child = _children.get(i);
		child.showNormals(v);
	  }
	}

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff

	public final Mesh getChildAtIndex(int i)
	{
		return _children.get(i);
	}

	public final void setColorTransparency(java.util.ArrayList<Double> transparency)
	{
		final int childrenCount = _children.size();
		for (int i = 0; i < childrenCount; i++)
		{
			Mesh child = _children.get(i);
			child.setColorTransparency(transparency);
		}
	}

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Chano adding stuff
