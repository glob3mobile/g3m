package org.glob3.mobile.generated;//
//  DirectMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  DirectMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//



public class DirectMesh extends AbstractMesh
{
  private int _renderVerticesCount;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc) const
  protected final void rawRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.drawArrays(_primitive, 0, (int)_renderVerticesCount, _glState, rc.getGPUProgramManager());
  }

//  Mesh* createNormalsMesh() const;


  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, VertexColorScheme vertexColorScheme)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, vertexColorScheme, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, 0, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, false, 0, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, null, false, 0, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, true, null, false, 0, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, 0.0f, true, null, false, 0, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, null, 0.0f, true, null, false, 0, 0, null, -1.0f);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize)
  {
	  this(primitive, owner, center, vertices, lineWidth, pointSize, null, null, 0.0f, true, null, false, 0, 0, null, -1.0f);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: DirectMesh(const int primitive, boolean owner, const Vector3D& center, const IFloatBuffer* vertices, float lineWidth, float pointSize, const Color* flatColor = null, const IFloatBuffer* colors = null, const float colorsIntensity = 0.0f, boolean depthTest = true, const IFloatBuffer* normals = null, boolean polygonOffsetFill = false, float polygonOffsetFactor = 0, float polygonOffsetUnits = 0, VertexColorScheme* vertexColorScheme = null, float transparencyDistanceThreshold = -1.0f) : AbstractMesh(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, vertexColorScheme, transparencyDistanceThreshold)
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity, boolean depthTest, IFloatBuffer normals, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, VertexColorScheme vertexColorScheme, float transparencyDistanceThreshold)
  {
	  super(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity, depthTest, normals, polygonOffsetFill, polygonOffsetFactor, polygonOffsetUnits, vertexColorScheme, transparencyDistanceThreshold);
	_renderVerticesCount = vertices.size() / 3;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void setRenderVerticesCount(int renderVerticesCount)
  {
	if (renderVerticesCount > getRenderVerticesCount())
	{
	  THROW_EXCEPTION("Invalid renderVerticesCount");
	}
	_renderVerticesCount = renderVerticesCount;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getRenderVerticesCount() const
  public final int getRenderVerticesCount()
  {
	return _renderVerticesCount;
  }

}
