package org.glob3.mobile.generated;import java.util.*;

//
//  MeshHolder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  MeshHolder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//



///#include "GPUProgramState.hpp"

public class MeshHolder extends Mesh
{
  private Mesh _mesh;

  public MeshHolder(Mesh mesh)
  {
	  _mesh = mesh;

  }

  public final void setMesh(Mesh mesh)
  {
	if (_mesh != mesh)
	{
	  if (_mesh != null)
		  _mesh.dispose();
	  _mesh = mesh;
	}
  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
  public final int getVertexCount()
  {
	return _mesh.getVertexCount();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
  public final Vector3D getVertex(int i)
  {
	return _mesh.getVertex(i);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
  public final BoundingVolume getBoundingVolume()
  {
	return _mesh.getBoundingVolume();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
  public final boolean isTransparent(G3MRenderContext rc)
  {
	return _mesh.isTransparent(rc);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const
  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
	_mesh.render(rc, parentGLState);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	_mesh.showNormals(v);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* getMesh() const
  public final Mesh getMesh()
  {
	return _mesh;
  }

}
