package org.glob3.mobile.generated;
//
//  LeveledMesh.cpp
//  G3M
//
//  Created by Jose Miguel SN on 16/04/13.
//
//

//
//  LeveledMesh.hpp
//  G3M
//
//  Created by Jose Miguel SN on 16/04/13.
//
//




public abstract class LeveledMesh extends Mesh
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
  super.dispose();

  }

  public final int getVerticesCount()
  {
    return _mesh.getVerticesCount();
  }

  public final Vector3D getVertex(int index)
  {
    return _mesh.getVertex(index);
  }

  public final void getVertex(int index, MutableVector3D result)
  {
    _mesh.getVertex(index, result);
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState)
  {
    _mesh.render(rc, parentState);
  }

  public final BoundingVolume getBoundingVolume()
  {
    return _mesh.getBoundingVolume();
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _mesh.isTransparent(rc);
  }

  public final int getLevel()
  {
    return _currentLevel;
  }

}