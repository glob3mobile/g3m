package org.glob3.mobile.generated;import java.util.*;

public class LeveledTexturedMesh extends Mesh
{
  private final Mesh _mesh;
  private final boolean _ownedMesh;

  private java.util.ArrayList<LazyTextureMapping> _mappings;
  private int _currentLevel;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LazyTextureMapping* getCurrentTextureMapping() const
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
  
		tangible.RefObject<GLState> tempRef__glState = new tangible.RefObject<GLState>(_glState);
		_mappings.get(_currentLevel).modifyGLState(tempRef__glState);
		_glState = tempRef__glState.argvalue;
  
		if (_currentLevel < levelsCount-1)
		{
		  for (int i = levelsCount-1; i > _currentLevel; i--)
		  {
			final LazyTextureMapping mapping = _mappings.get(i);
			if (mapping != null)
				mapping.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
			_mappings.remove(i);
//#endif
		  }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		  _mappings.erase(_mappings.iterator() + _currentLevel + 1, _mappings.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		  _mappings.trimToSize();
//#endif
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
	  THROW_EXCEPTION("LeveledTexturedMesh: mappings can't be NULL!");
	}
	if (_mappings.size() <= 0)
	{
	  THROW_EXCEPTION("LeveledTexturedMesh: empty mappings");
	}
  }

  public void dispose()
  {
  ///#ifdef JAVA_CODE
  //  synchronized (this) {
  ///#endif
  
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
  //    _mappings = NULL;
	}
  
	_glState._release();
  
  ///#ifdef JAVA_CODE
  //  }
  ///#endif
  
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
	return (_mesh == null) ? null : _mesh.getBoundingVolume();
  }

  public final boolean setGLTextureIdForLevel(int level, TextureIDReference glTextureId)
  {
  
	if (_mappings.size() > 0)
	{
	  if (glTextureId != null)
	  {
		if ((_currentLevel < 0) || (level < _currentLevel))
		{
		  _mappings.get(level).setGLTextureId(glTextureId);
		  _currentLevel = -1;
		  return true;
		}
	  }
	}
  
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TextureIDReference* getTopLevelTextureId() const
  public final TextureIDReference getTopLevelTextureId()
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
  public final boolean isTransparent(G3MRenderContext rc)
  {
	if (_mesh.isTransparent(rc))
	{
	  return true;
	}
  
	LazyTextureMapping mapping = getCurrentTextureMapping();
  
	return (mapping == null) ? false : mapping._transparent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	_mesh.showNormals(v);
  }

}
