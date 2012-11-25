package org.glob3.mobile.generated; 
//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGGeometryNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;

public class SGGeometryNode extends SGNode
{
  private int _primitive;
  private IFloatBuffer _vertices;
  private IFloatBuffer _colors;
  private IFloatBuffer _uv;
  private IFloatBuffer _normals;
  private IIntBuffer _indices;

  protected final void rawRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
  
	// TEMP_commented_by_Agustin_until_decision_about_glstate
	/*
	gl->enableVerticesPosition();
  
	if (_colors == NULL) {
	  gl->disableVertexColor();
	}
	else {
	  const float colorsIntensity = 1;
	  gl->enableVertexColor(_colors, colorsIntensity);
	}*/
  
  
  //  if (_transparent) {
  //    gl->enableBlend();
  //  }
  //
  //  gl->enableTextures();
  //  gl->enableTexture2D();
  //
  //  _textureMapping->bind(rc);
  //
  //  _mesh->render(rc);
  //
  //  gl->disableTexture2D();
  //  gl->disableTextures();
  //
  //  if (_transparent) {
  //    gl->disableBlend();
  //  }
  
  
	gl.vertexPointer(3, 0, _vertices);
  
	if (_primitive == GLPrimitive.triangles())
	{
	  gl.drawTriangles(_indices);
	}
	else if (_primitive == GLPrimitive.triangleStrip())
	{
	  gl.drawTriangleStrip(_indices);
	}
	else if (_primitive == GLPrimitive.triangleFan())
	{
	  gl.drawTriangleFan(_indices);
	}
	else if (_primitive == GLPrimitive.lines())
	{
	  gl.drawLines(_indices);
	}
	else if (_primitive == GLPrimitive.lineStrip())
	{
	  gl.drawLineStrip(_indices);
	}
	else if (_primitive == GLPrimitive.lineLoop())
	{
	  gl.drawLineLoop(_indices);
	}
	else if (_primitive == GLPrimitive.points())
	{
	  gl.drawPoints(_indices);
	}
  
	//gl->disableVerticesPosition();
  }


  public SGGeometryNode(int primitive, IFloatBuffer vertices, IFloatBuffer colors, IFloatBuffer uv, IFloatBuffer normals, IIntBuffer indices)
  {
	  _primitive = primitive;
	  _vertices = vertices;
	  _colors = colors;
	  _uv = uv;
	  _normals = normals;
	  _indices = indices;

  }

}