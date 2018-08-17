package org.glob3.mobile.generated;import java.util.*;

//
//  LeveledMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/04/13.
//
//

//
//  LeveledMesh.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/04/13.
//
//



public class LeveledMesh extends Mesh
{
  private Mesh _mesh;

  private int _currentLevel;

  public LeveledMesh(Mesh mesh, int level)
  {
	  _mesh = mesh;
	  _currentLevel = level;

  }

  public final void setMesh(Mesh mesh, int level)
  {
	if (_mesh != mesh && level >= _currentLevel)
	{
	  if (_mesh != null)
		  _mesh.dispose();
	  _mesh = mesh;
	  _currentLevel = level;
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
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentState) const
  public final void rawRender(G3MRenderContext rc, GLState parentState)
  {
	_mesh.render(rc, parentState);
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
//ORIGINAL LINE: int getLevel() const
  public final int getLevel()
  {
	return _currentLevel;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	_mesh.showNormals(v);
  }

}
