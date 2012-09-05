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

  private Mesh mesh;


  public void dispose()
  {
	if (mesh != null)
		mesh.dispose();
  }

  public final void initialize(InitializationContext ic)
  {
	int numVertices = 4;
	int numIndices = 4;
  
	float[] v = { (float) 28.753213, (float) -17.898788, 500, (float) 28.680347, (float) -17.898788, 500, (float) 28.753213, (float) -17.83287, 500, (float) 28.680347, (float) -17.83287, 500 };
  
	int[] i = { 0, 1, 2, 3};
  
	// create vertices and indices in dinamic memory
	float[] vertices = new float [numVertices *3];
	int[] indices = new int [numIndices];
  
	Color flatColor = new Color(Color.fromRGBA((float)1.0, (float)1.0, (float)0.0, (float)1.0));
  
	System.arraycopy(v, 0, vertices, 0, v.length);
	System.arraycopy(i, 0, indices, 0, i.length);
	// create mesh
	mesh = IndexedMesh.createFromGeodetic3D(ic.getPlanet(), true, GLPrimitive.TriangleStrip,
  										  CenterStrategy.NoCenter, new Vector3D((double)0.0,(double)0.0,(double)0.0),
  										  4, vertices, indices, 4, flatColor);
  
  }

  public final void render(RenderContext rc)
  {
  //  GL *gl = rc->getGL();
  
	mesh.render(rc);
  
  /*  gl->pushMatrix();
	Geodetic2D centerMesh = Geodetic2D(Angle::fromDegrees(28.715), Angle::fromDegrees(-17.855));
	Vector3D normal = rc->getPlanet()->geodeticSurfaceNormal(centerMesh);
	gl->multMatrixf(MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(45), normal,
																  rc->getPlanet()->toVector3D(centerMesh)));
	mesh->render(rc);
	gl->popMatrix();*/
  
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

}