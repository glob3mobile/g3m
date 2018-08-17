package org.glob3.mobile.generated;//
//  FlatColorMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  FlatColorMesh.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//





public class FlatColorMesh extends Mesh
{
  private Mesh _mesh;
  private final boolean _ownedMesh;

  private final Color _flatColor;
  private final boolean _ownedColor;

  private GLState _glState;

  private void createGLState()
  {
	_glState.addGLFeature(new FlatColorGLFeature(_flatColor, _flatColor.isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
  }



  public FlatColorMesh(Mesh mesh, boolean ownedMesh, Color color, boolean ownedColor)
  {
	  _mesh = mesh;
	  _ownedMesh = ownedMesh;
	  _flatColor = color;
	  _ownedColor = ownedColor;
	  _glState = new GLState();
	createGLState();
  }

  public void dispose()
  {
	if (_ownedMesh)
	{
	  if (_mesh != null)
		  _mesh.dispose();
	}
	if (_ownedColor)
	{
	  if (_flatColor != null)
		  _flatColor.dispose();
	}

	_glState._release();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
  public final BoundingVolume getBoundingVolume()
  {
	return (_mesh == null) ? null : _mesh.getBoundingVolume();
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
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
  public final boolean isTransparent(G3MRenderContext rc)
  {
	return _flatColor._alpha != 1.0;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentState) const
  public final void rawRender(G3MRenderContext rc, GLState parentState)
  {
	_glState.setParent(parentState);
	_mesh.render(rc, _glState);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	_mesh.showNormals(v);
  }
}
