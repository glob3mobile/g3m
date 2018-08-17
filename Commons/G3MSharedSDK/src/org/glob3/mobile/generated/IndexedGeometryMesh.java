package org.glob3.mobile.generated;import java.util.*;

//
//  IndexedGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  IndexedGeometryMesh.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IShortBuffer;

public class IndexedGeometryMesh extends AbstractGeometryMesh
{
  private boolean _ownsIndices;
  private IShortBuffer _indices;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc) const
  protected final void rawRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
	gl.drawElements(_primitive, _indices, _glState, rc.getGPUProgramManager());
  }

  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
	  this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill)
  {
	  this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest)
  {
	  this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, depthTest, false, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
  {
	  this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, pointSize, true, false, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth)
  {
	  this(primitive, center, vertices, ownsVertices, indices, ownsIndices, lineWidth, 1, true, false, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices)
  {
	  this(primitive, center, vertices, ownsVertices, indices, ownsIndices, 1, 1, true, false, 0, 0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedGeometryMesh(const int primitive, const Vector3D& center, IFloatBuffer* vertices, boolean ownsVertices, IShortBuffer* indices, boolean ownsIndices, float lineWidth = 1, float pointSize = 1, boolean depthTest = true, boolean polygonOffsetFill = false, float polygonOffsetFactor = 0, float polygonOffsetUnits = 0) : AbstractGeometryMesh(primitive, center, vertices, ownsVertices, null, false, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits), _indices(indices), _ownsIndices(ownsIndices)
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
	  super(primitive, center, vertices, ownsVertices, null, false, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits);
	  _indices = indices;
	  _ownsIndices = ownsIndices;
  //  ILogger::instance()->logInfo("Created an IndexedGeometryMesh with %d vertices, %d indices",
  //                               vertices->size(),
  //                               indices->size());
  }

  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
	  this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill)
  {
	  this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, polygonOffsetFill, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest)
  {
	  this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, depthTest, false, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize)
  {
	  this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, pointSize, true, false, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth)
  {
	  this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, lineWidth, 1, true, false, 0, 0);
  }
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices)
  {
	  this(primitive, center, vertices, ownsVertices, normals, ownsNormals, indices, ownsIndices, 1, 1, true, false, 0, 0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedGeometryMesh(const int primitive, const Vector3D& center, IFloatBuffer* vertices, boolean ownsVertices, IFloatBuffer* normals, boolean ownsNormals, IShortBuffer* indices, boolean ownsIndices, float lineWidth = 1, float pointSize = 1, boolean depthTest = true, boolean polygonOffsetFill = false, float polygonOffsetFactor = 0, float polygonOffsetUnits = 0) : AbstractGeometryMesh(primitive, center, vertices, ownsVertices, normals, ownsNormals, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits), _indices(indices), _ownsIndices(ownsIndices)
  public IndexedGeometryMesh(int primitive, Vector3D center, IFloatBuffer vertices, boolean ownsVertices, IFloatBuffer normals, boolean ownsNormals, IShortBuffer indices, boolean ownsIndices, float lineWidth, float pointSize, boolean depthTest, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
	  super(primitive, center, vertices, ownsVertices, normals, ownsNormals, lineWidth, pointSize, depthTest, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits);
	  _indices = indices;
	  _ownsIndices = ownsIndices;
  //  ILogger::instance()->logInfo("Created an IndexedGeometryMesh with %d vertices, %d indices, %d normals",
  //                               vertices->size(),
  //                               indices->size(),
  //                               normals->size());
  }

  public void dispose()
  {
	if (_ownsIndices)
	{
	  if (_indices != null)
		  _indices.dispose();
	}
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IShortBuffer* getIndices() const
  public final IShortBuffer getIndices()
  {
	return _indices;
  }
}
