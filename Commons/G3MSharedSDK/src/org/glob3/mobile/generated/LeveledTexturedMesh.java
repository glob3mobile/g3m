package org.glob3.mobile.generated; 
public class LeveledTexturedMesh extends Mesh
{
  private final Mesh _mesh;
  private final boolean _ownedMesh;

  private java.util.ArrayList<LazyTextureMapping> _mappings;


  private final int _levelsCount;

  private int _currentLevel;
  private boolean _currentLevelIsValid;

  private LazyTextureMapping getCurrentTextureMapping()
  {
    if (_mappings == null)
    {
      return null;
    }
  
    if (!_currentLevelIsValid)
    {
  
      int newCurrentLevel = 0;
  
      for (int i = 0; i < _levelsCount; i++)
      {
        LazyTextureMapping mapping = _mappings.get(i);
        if (mapping != null)
        {
          if (mapping.isValid())
          {
            //ILogger::instance()->logInfo("LeveledTexturedMesh changed from level %d to %d", _currentLevel, i);
            newCurrentLevel = i;
            _currentLevelIsValid = true;
            break;
          }
        }
      }
  
      if (newCurrentLevel != _currentLevel)
      {
        _currentLevel = newCurrentLevel;
        //MESH SHOULD BE NOTIFIED TO CHANGE STATE FROM TILE
  
        if (_currentLevelIsValid)
        {
          for (int i = _currentLevel+1; i < _levelsCount; i++)
          {
            LazyTextureMapping mapping = _mappings.get(i);
            if (mapping != null)
            {
              _mappings.get(i).dispose();
              if (mapping != null)
                 mapping.dispose();
              _glState.clearGLFeatureGroup(GLFeatureGroupName.CAMERA_GROUP);
            }
          }
        }
      }
  
  
    }
  
    return _currentLevelIsValid ? _mappings.get(_currentLevel) : null;
  }

  private GLState _glState = new GLState();
  private LazyTextureMapping _mappingOnGLState;

  public LeveledTexturedMesh(Mesh mesh, boolean ownedMesh, java.util.ArrayList<LazyTextureMapping> mappings)
  {
     _mesh = mesh;
     _ownedMesh = ownedMesh;
     _mappings = mappings;
     _levelsCount = mappings.size();
     _currentLevel = mappings.size() + 1;
     _currentLevelIsValid = false;
     _mappingOnGLState = null;
    if (_mappings.size() <= 0)
    {
      ILogger.instance().logError("LOGIC ERROR\n");
    }
  }

  public void dispose()
  {
    synchronized (this) {
  
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
        _mappings = null;
      }
  
    }
  }

  public final int getVertexCount()
  {
    return _mesh.getVertexCount();
  }

  public final Vector3D getVertex(int i)
  {
    return _mesh.getVertex(i);
  }

  public final BoundingVolume getBoundingVolume()
  {
    return (_mesh == null) ? null : _mesh.getBoundingVolume();
  }

  public final boolean setGLTextureIdForLevel(int level, IGLTextureId glTextureId)
  {
    if (glTextureId != null)
    {
      if (!_currentLevelIsValid || (level < _currentLevel))
      {
        _mappings.get(level).setGLTextureId(glTextureId);
        _currentLevelIsValid = false;
        return true;
      }
    }
  
    return false;
  }

  public final IGLTextureId getTopLevelGLTextureId()
  {
    final LazyTextureMapping mapping = getCurrentTextureMapping();
    if (mapping != null)
    {
      if (_currentLevel == 0)
      {
        return mapping.getGLTextureId();
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
  
    if (mapping == null)
    {
      return false;
    }
  
    return mapping.isTransparent();
  }

  public final void render(G3MRenderContext rc, GLState parentGLState)
  {
  
    LazyTextureMapping mapping = getCurrentTextureMapping();
    if (mapping != null)
    {
      if (mapping != _mappingOnGLState)
      {
        _mappingOnGLState = mapping;
        mapping.modifyGLState(_glState);
      }
  
      _glState.setParent(parentGLState);
      _mesh.render(rc, _glState);
    }
    else
    {
      ILogger.instance().logError("No Texture Mapping");
    }
  }

}