package org.glob3.mobile.generated; 
//
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc) const
  protected final void rawRender(G3MRenderContext rc)
  {
	GL gl = rc.getGL();
  
	final int verticesCount = getVertexCount();
	gl.drawArrays(_primitive, 0, verticesCount);
  }


  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, Color flatColor, IFloatBuffer colors)
  {
	  this(primitive, owner, center, vertices, lineWidth, flatColor, colors, (float)0.0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, Color flatColor)
  {
	  this(primitive, owner, center, vertices, lineWidth, flatColor, null, (float)0.0);
  }
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth)
  {
	  this(primitive, owner, center, vertices, lineWidth, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: DirectMesh(const int primitive, boolean owner, const Vector3D& center, IFloatBuffer* vertices, float lineWidth, Color* flatColor = null, IFloatBuffer* colors = null, const float colorsIntensity = (float)0.0) : AbstractMesh(primitive, owner, center, vertices, lineWidth, flatColor, colors, colorsIntensity)
  public DirectMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
	  super(primitive, owner, center, vertices, lineWidth, flatColor, colors, colorsIntensity);
  }

  public void dispose()
  {

  }

}