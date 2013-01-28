package org.glob3.mobile.generated; 
//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IShortBuffer;

public class IndexedMesh extends AbstractMesh
{
  private IShortBuffer _indices;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState& parentState) const
  protected final void rawRender(G3MRenderContext rc, GLState parentState)
  {
	GL gl = rc.getGL();
	gl.drawElements(_primitive, _indices);
  }

  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, pointSize, flatColor, colors, 0.0f);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, pointSize, flatColor, null, 0.0f);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, pointSize, null, null, 0.0f);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, 1, null, null, 0.0f);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(const int primitive, boolean owner, const Vector3D& center, IFloatBuffer* vertices, IShortBuffer* indices, float lineWidth, float pointSize = 1, Color* flatColor = null, IFloatBuffer* colors = null, const float colorsIntensity = 0.0f) : AbstractMesh(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity), _indices(indices)
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IShortBuffer indices, float lineWidth, float pointSize, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
	  super(primitive, owner, center, vertices, lineWidth, pointSize, flatColor, colors, colorsIntensity);
	  _indices = indices;
  }

  public void dispose()
  {
	if (_owner)
	{
	  if (_indices != null)
		  _indices.dispose();
	}
  }

}