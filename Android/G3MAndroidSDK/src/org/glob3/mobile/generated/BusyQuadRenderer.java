package org.glob3.mobile.generated; 
//
//  BusyQuadRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//
//  BusyQuadRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 13/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//





//***************************************************************


public class BusyQuadRenderer extends Renderer implements EffectTarget
{
  private double _degrees;
  private final String _textureFilename;
  private Mesh _quadMesh;

  private boolean initMesh(RenderContext rc)
  {
	// create quad
	int numVertices = 4;
	int numIndices = 4;
	float[] quadVertices = new float[numVertices *3];
	int[] quadIndices = new int[numIndices];
	float[] texC = new float[numVertices *2];
  
	int nv = 0;
	float halfSize = 16F;
	quadVertices[nv++] = -halfSize;
	quadVertices[nv++] = halfSize;
	quadVertices[nv++] = 0F;
	quadVertices[nv++] = -halfSize;
	quadVertices[nv++] = -halfSize;
	quadVertices[nv++] = 0F;
	quadVertices[nv++] = halfSize;
	quadVertices[nv++] = halfSize;
	quadVertices[nv++] = 0F;
	quadVertices[nv++] = halfSize;
	quadVertices[nv++] = -halfSize;
	quadVertices[nv++] = 0F;
  
	for (int n = 0; n<numIndices; n++)
		quadIndices[n] = n;
  
	int nc = 0;
	texC[nc++] = 0F;
	texC[nc++] = 0.0F;
	texC[nc++] = 0F;
	texC[nc++] = 1.0F;
	texC[nc++] = 1F;
	texC[nc++] = 0.0F;
	texC[nc++] = 1F;
	texC[nc++] = 1.0F;
  
  
	//TEXTURED
	GLTextureId texId = GLTextureId.invalid();
	if (true)
	{
	  texId = rc.getTexturesHandler().getGLTextureIdFromFileName(_textureFilename, 256, 256, false);
	  if (!texId.isValid())
	  {
		rc.getLogger().logError("Can't load file %s", _textureFilename);
		return false;
	  }
	}
  
	IndexedMesh im = IndexedMesh.createFromVector3D(true, GLPrimitive.TriangleStrip, CenterStrategy.NoCenter, new Vector3D(0,0,0), numVertices, quadVertices, quadIndices, numIndices, null);
  
	TextureMapping texMap = new SimpleTextureMapping(texId, texC, true);
  
	_quadMesh = new TexturedMesh(im, true, texMap, true);
  
	return true;
  }



  public BusyQuadRenderer(String textureFilename)
  {
	  _degrees = 0;
	  _quadMesh = null;
	  _textureFilename = textureFilename;
  }

  public final void initialize(InitializationContext ic)
  {
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
  
	if (_quadMesh == null)
	{
	  if (!initMesh(rc))
	  {
		return;
	  }
	}
  
  
	// init effect in the first render
//C++ TO JAVA CONVERTER NOTE: This static local variable declaration (not allowed in Java) has been moved just prior to the method:
//	static boolean firstTime = true;
	if (render_firstTime)
	{
	  render_firstTime = false;
	  Effect effect = new BusyEffect(this);
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
	MutableMatrix44D R2 = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_degrees), new Vector3D(0, 0, 1));
	gl.multMatrixf(R1.multiply(R2));
  
	// draw mesh
	_quadMesh.render(rc);
  
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