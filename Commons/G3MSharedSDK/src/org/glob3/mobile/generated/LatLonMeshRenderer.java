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




public class LatLonMeshRenderer extends LeafRenderer
{

  private Mesh _mesh;

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

  public final void initialize(InitializationContext ic)
  {
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), ic.getPlanet(), Geodetic2D.zero());
	vertices.add(Geodetic3D.fromDegrees(28.753213, -17.898788, 500));
	vertices.add(Geodetic3D.fromDegrees(28.680347, -17.898788, 500));
	vertices.add(Geodetic3D.fromDegrees(28.753213, -17.83287, 500));
	vertices.add(Geodetic3D.fromDegrees(28.680347, -17.83287, 500));
  
	IntBufferBuilder index = new IntBufferBuilder();
	for (int i = 0; i < 4; i++)
	{
	  index.add(i);
	}
  
	Color flatColor = new Color(Color.fromRGBA((float)1.0, (float)1.0, (float)0.0, (float)1.0));
  
	_mesh = new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), index.create(), 1, flatColor);
  
  }

  public final void render(RenderContext rc)
  {
	_mesh.render(rc);
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

  public final void onResume(InitializationContext ic)
  {

  }

  public final void onPause(InitializationContext ic)
  {

  }

}