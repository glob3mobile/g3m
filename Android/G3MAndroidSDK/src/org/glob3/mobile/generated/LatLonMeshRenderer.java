package org.glob3.mobile.generated; 
//
//  LatLonMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  LatLonMeshRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




public class LatLonMeshRenderer extends Renderer
{

  private LatLonMesh mesh;


  public void dispose()
  {
	if (mesh != null)
		mesh.dispose();
  }

  public final void initialize(InitializationContext ic)
  {
	int numVertices = 4;
	int numIndices = 4;
  
	float[] v = { 28.753213, -17.898788, 500, 28.680347, -17.898788, 500, 28.753213, -17.83287, 500, 28.680347, -17.83287, 500 };
  
	int[] i = { 0, 1, 2, 3};
  
	// create vertices and indices in dinamic memory
	float[] vertices = new float [numVertices *3];
//C++ TO JAVA CONVERTER TODO TASK: The memory management function 'memcpy' has no equivalent in Java:
//C++ TO JAVA CONVERTER TODO TASK: There is no Java equivalent to 'sizeof':
	memcpy(vertices, v, numVertices *3 *sizeof(float));
	int[] indices = new int [numIndices];
//C++ TO JAVA CONVERTER TODO TASK: The memory management function 'memcpy' has no equivalent in Java:
//C++ TO JAVA CONVERTER TODO TASK: There is no Java equivalent to 'sizeof':
	memcpy(indices, i, numIndices *sizeof(int));
  
	// create mesh
	Color flatColor = new Color(Color.fromRGBA(1.0, 1.0, 0.0, 1.0));
	mesh = new LatLonMesh(ic, true, GLPrimitive.TriangleStrip, CenterStrategy.NoCenter, new Vector3D(0,0,0), 4, vertices, indices, 4, flatColor);
  }

  public final int render(RenderContext rc)
  {
	mesh.render(rc);
  
	return Renderer.maxTimeToRender;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(int width, int height)
  {
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

}