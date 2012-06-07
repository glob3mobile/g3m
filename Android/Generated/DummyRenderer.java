package org.glob3.mobile.generated; 
//
//  DummyRenderer.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

//
//  DummyRenderer.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//



public class DummyRenderer extends Renderer
{
  public final void initialize(InitializationContext ic)
  {
  }

  public final int render(RenderContext rc)
  {
	int res = 12;
	float[] vertices = new float[res * res * 3];
	int numIndices = 2 * (res - 1) * (res + 1);
	byte []index = new byte[numIndices];
  
	// create vertices
	float size = 1e7F;
	int n = 0;
	for (int j = 0; j < res; j++)
	{
	  for (int i = 0; i < res; i++)
	  {
		vertices[n++] = (float) 0;
		vertices[n++] = (float)(-size + i / (float)(res - 1) * 2 *size);
		vertices[n++] = (float)(size - j / (float)(res - 1) * 2 *size);
	  }
	}
  
	n = 0;
	for (int j = 0; j < res - 1; j++)
	{
	  if (j > 0)
		  index[n++] = (byte)(j * res);
	  for (int i = 0; i < res; i++)
	  {
		index[n++] = (byte)(j * res + i);
		index[n++] = (byte)(j * res + i + res);
	  }
	  index[n++] = (byte)(j * res + 2 * res - 1);
	}
  
  
  
	// obtaing gl object reference
	IGL gl = rc.getGL();
  
	// draw a white square
	gl.color((float) 1, (float) 0, (float) 0);
  
	// insert pointers
	gl.disableTextures();
	gl.vertexPointer(3, 0, vertices);
  
	gl.pushMatrix();
	gl.enablePolygonOffset(1.0f, 5.0f);
	gl.drawTriangleStrip(n, index);
	gl.disablePolygonOffset();
	gl.popMatrix();
	gl.enableTextures();
  
	index = null;
	vertices = null;
  
	return 9999;
  }

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  boolean onTouchEvent(const TouchEvent* event);
}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//boolean DummyRenderer::onTouchEvent(const TouchEvent* event)
//{
//  return false;
//}
