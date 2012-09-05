package org.glob3.mobile.generated; 
//
//  BusyMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//
//  BusyMeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//***************************************************************


public class BusyMeshRenderer extends Renderer implements EffectTarget
{
  private Mesh _mesh;
  private double _degrees;

  public BusyMeshRenderer()
  {
	  _degrees = 0;
  }

  public final void initialize(InitializationContext ic)
  {
	// compute number of vertex for the ring
	int numStrides = 60;
	int numVertices = numStrides * 2 + 2;
	int numIndices = numVertices + 2;
  
	// add number of vertex for the square
  
	// create vertices and indices in dinamic memory
	float[] vertices = new float[numVertices *3];
	int[] indices = new int[numIndices];
	float[] colors = new float[numVertices *4];
  
	// create vertices
	int nv = 0;
	int ni = 0;
	int nc = 0;
  //  float r1=200, r2=230;
	float r1 = 12F;
	float r2 = 18F;
	for (int step = 0; step<=numStrides; step++)
	{
	  double angle = (double) step * 2 * IMathUtils.instance().pi() / numStrides;
	  double c = IMathUtils.instance().cos(angle);
	  double s = IMathUtils.instance().sin(angle);
	  vertices[nv++] = (float)(r1 * c);
	  vertices[nv++] = (float)(r1 * s);
	  vertices[nv++] = 0.0F;
	  vertices[nv++] = (float)(r2 * c);
	  vertices[nv++] = (float)(r2 * s);
	  vertices[nv++] = 0.0F;
	  indices[ni] = ni;
	  indices[ni+1] = ni+1;
	  ni+=2;
	  float col = (float)(1.1 * step / numStrides);
	  if (col>1)
	  {
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 0F;
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 0F;
	  }
	  else
	  {
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 1-col;
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 255F;
		colors[nc++] = 1-col;
	  }
	}
  
	// the two last indices
	indices[ni++] = 0;
	indices[ni++] = 1;
  
  
	// create mesh
	//Color *flatColor = new Color(Color::fromRGBA(1.0, 1.0, 0.0, 1.0));
	_mesh = IndexedMesh.createFromVector3D(true, GLPrimitive.TriangleStrip, CenterStrategy.NoCenter, new Vector3D(0,0,0), numVertices, vertices, indices, numIndices, null, colors);
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

//C++ TO JAVA CONVERTER NOTE: This was formerly a static local variable declaration (not allowed in Java):
  private boolean render_firstTime = true;
  public final void render(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	// init effect in the first render
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//	static boolean firstTime = true;
	if (render_firstTime)
	{
	  render_firstTime = false;
	  Effect effect = new BusyMeshEffect(this);
	  rc.getEffectsScheduler().startEffect(effect, this);
	}
  
	// init modelview matrix
	int[] currentViewport = new int[4];
	gl.getViewport(currentViewport);
	int halfWidth = currentViewport[2] / 2;
	int halfHeight = currentViewport[3] / 2;
	MutableMatrix44D M = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
	gl.setProjection(M);
	gl.loadMatrixf(MutableMatrix44D.identity());
  
	// clear screen
	//gl->clearScreen(0.0f, 0.2f, 0.4f, 1.0f);
	gl.clearScreen(0.0f, 0.0f, 0.0f, 1.0f);
  
	gl.enableBlend();
	gl.setBlendFuncSrcAlpha();
  
	gl.pushMatrix();
	MutableMatrix44D R1 = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(0), new Vector3D(-1, 0, 0));
	MutableMatrix44D R2 = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, -1));
	gl.multMatrixf(R1.multiply(R2));
  
	// draw mesh
	_mesh.render(rc);
  
	gl.popMatrix();
  
	gl.disableBlend();
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public void dispose()
  {
  }

  public final void incDegrees(double value)
  {
	_degrees += value;
	if (_degrees>360)
		_degrees -= 360;
  }

  public final void start()
  {
	//int _TODO_start_effects;
  }

  public final void stop()
  {
	//int _TODO_stop_effects;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEffectable() const
  public final boolean isEffectable()
  {
	return true;
  }

}