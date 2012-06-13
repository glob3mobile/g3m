package org.glob3.mobile.generated; 
//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  TileRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class TileRenderer extends Renderer
{

  private final int _resolution;

  private java.util.ArrayList<Tile > initialTiles = new java.util.ArrayList<Tile >();


  public TileRenderer(int resolution)
  {
	  _resolution = resolution;
  }
  public void dispose()
  {
	if (!initialTiles.isEmpty())
		Tile.deleteIndices();
  }

  public final void initialize(InitializationContext ic)
  {
	Tile.createIndices(_resolution, true);
	int initialDiv = 2;
	double angle = 180 / initialDiv;
	for (int j = 0; j<initialDiv; j++)
		for (int i = 0; i<initialDiv *2; i++)
		{
	  Sector bbox = new Sector(Angle.fromDegrees(-90+j *angle), Angle.fromDegrees(-180+i *angle), Angle.fromDegrees(-90+(j+1)*angle), Angle.fromDegrees(-180+(i+1)*angle));
	  Tile tile = new Tile(bbox);
	  tile.createVertices(ic.getPlanet());
	  initialTiles.add(tile);
	}
  }

  public final int render(RenderContext rc)
  {
	for (int n = 0; n<initialTiles.size(); n++)
		initialTiles.get(n).render(rc);
  
	return 9999;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public final boolean onResizeViewportEvent(int width, int height)
  {
	  return false;
  }


}