package org.glob3.mobile.generated;
public class LeveledTexturedMesh extends Mesh
{
  private final Mesh _mesh;
  private final boolean _ownedMesh;

  private java.util.ArrayList<LazyTextureMapping> _mappings;
  private int _currentLevel;

  private LazyTextureMapping getCurrentTextureMapping()
  {
    if (_mappings == null)
    {
      return null;
    }
  
    if (_currentLevel < 0)
    {
      int newCurrentLevel = -1;
  
      final int levelsCount = _mappings.size();
  
      for (int i = 0; i < levelsCount; i++)
      {
        final LazyTextureMapping mapping = _mappings.get(i);
        if (mapping != null)
        {
          if (mapping.isValid())
          {
            newCurrentLevel = i;
            break;
          }
        }
      }
  
      if (newCurrentLevel >= 0)
      {
        // ILogger::instance()->logInfo("LeveledTexturedMesh: changed from level %d to %d",
        //                              _currentLevel,
        //                              newCurrentLevel);
        _currentLevel = newCurrentLevel;
  
        _mappings.get(_currentLevel).modifyGLState(_glState);
  
        if (_currentLevel < levelsCount-1)
        {
          for (int i = levelsCount-1; i > _currentLevel; i--)
          {
            final LazyTextureMapping mapping = _mappings.get(i);
            if (mapping != null)
               mapping.dispose();
            _mappings.remove(i);
          }
          _mappings.trimToSize();
        }
      }
    }
  
    return (_currentLevel >= 0) ? _mappings.get(_currentLevel) : null;
  }

  private GLState _glState;

  public LeveledTexturedMesh(Mesh mesh, boolean ownedMesh, java.util.ArrayList<LazyTextureMapping> mappings)
  {
     _mesh = mesh;
     _ownedMesh = ownedMesh;
     _mappings = mappings;
     _currentLevel = -1;
     _glState = new GLState();
    if (_mappings == null)
    {
      throw new RuntimeException("LeveledTexturedMesh: mappings can't be NULL!");
    }
    if (_mappings.size() <= 0)
    {
      throw new RuntimeException("LeveledTexturedMesh: empty mappings");
    }
  }

  public void dispose()
  {
    if (_ownedMesh)
    {
      if (_mesh != null)
         _mesh.dispose();
    }
  
    if (_mappings != null)
    {
      for (int i = 0; i < _mappings.size(); i++)
      {
        LazyTextureMapping mapping = _mappings.get(i);
        if (mapping != null)
           mapping.dispose();
      }
  
      _mappings = null;
    }
  
    _glState._release();
  
    super.dispose();
  }

  public final int getVertexCount()
  {
    return _mesh.getVertexCount();
  }

  public final Vector3D getVertex(int index)
  {
    return _mesh.getVertex(index);
  }

  public final BoundingVolume getBoundingVolume()
  {
    return (_mesh == null) ? null : _mesh.getBoundingVolume();
  }

  public final boolean setGLTextureIDForLevel(int level, TextureIDReference glTextureID)
  {
  
    if (_mappings.size() > 0)
    {
      if (glTextureID != null)
      {
        if ((_currentLevel < 0) || (level < _currentLevel))
        {
          _mappings.get(level).setGLTextureID(glTextureID);
          _currentLevel = -1;
          return true;
        }
      }
    }
  
    return false;
  }

  public final TextureIDReference getTopLevelTextureID()
  {
    final LazyTextureMapping mapping = getCurrentTextureMapping();
    if (mapping != null)
    {
      if (_currentLevel == 0)
      {
        return mapping.getGLTextureID();
      }
    }
  
    return null;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    if (_mesh.isTransparent(rc))
    {
      return true;
    }
  
    LazyTextureMapping mapping = getCurrentTextureMapping();
  
    return (mapping == null) ? false : mapping._transparent;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    LazyTextureMapping mapping = getCurrentTextureMapping();
    if (mapping == null)
    {
      ILogger.instance().logError("LeveledTexturedMesh: No Texture Mapping");
      _mesh.render(rc, parentGLState);
    }
    else
    {
      _glState.setParent(parentGLState);
      _mesh.render(rc, _glState);
    }
  }

}