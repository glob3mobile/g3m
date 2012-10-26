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


public class BusyMeshRenderer extends LeafRenderer implements EffectTarget
{
  private Mesh _mesh;
  private double _degrees;

  public BusyMeshRenderer()
  {
	  _degrees = 0;
  }

  public final void initialize(InitializationContext ic)
  {
	int numStrides = 60;
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
	IntBufferBuilder indices = new IntBufferBuilder();
  
	int indicesCounter = 0;
	final float r1 = 12F;
	final float r2 = 18F;
	for (int step = 0; step<=numStrides; step++)
	{
	  final double angle = (double) step * 2 * IMathUtils.instance().pi() / numStrides;
	  final double c = IMathUtils.instance().cos(angle);
	  final double s = IMathUtils.instance().sin(angle);
  
	  vertices.add((r1 * c), (r1 * s), 0);
	  vertices.add((r2 * c), (r2 * s), 0);
  
	  indices.add(indicesCounter++);
	  indices.add(indicesCounter++);
  
	  float col = (float)(1.1 * step / numStrides);
	  if (col>1)
	  {
		colors.add(255, 255, 255, 0);
		colors.add(255, 255, 255, 0);
	  }
	  else
	  {
		colors.add(255, 255, 255, 1 - col);
		colors.add(255, 255, 255, 1 - col);
	  }
	}
  
	// the two last indices
	indices.add(0);
	indices.add(1);
  
	// create mesh
	_mesh = new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, null, colors.create());
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

  public final void onResume(InitializationContext ic)
  {

  }

  public final void onPause(InitializationContext ic)
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void unusedMethod() const
  public final void unusedMethod()
  {

  }

}