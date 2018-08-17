package org.glob3.mobile.generated;import java.util.*;

//
//  HUDImageRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

//
//  HUDImageRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;


public class HUDImageRenderer extends DefaultRenderer
{

  public abstract static class ImageFactory
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	public void dispose()
	{
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	public void dispose();
//#endif

	public abstract void create(G3MRenderContext rc, int width, int height, IImageListener listener, boolean deleteListener);
  }

  public abstract static class CanvasImageFactory extends HUDImageRenderer.ImageFactory
  {

	protected abstract void drawOn(ICanvas canvas, int width, int height);

	public CanvasImageFactory()
	{

	}

	public final void create(G3MRenderContext rc, int width, int height, IImageListener listener, boolean deleteListener)
	{
    
	  ICanvas canvas = rc.getFactory().createCanvas(true);
	  canvas.initialize(width, height);
    
	  drawOn(canvas, width, height);
    
	  canvas.createImage(listener, deleteListener);
    
	  if (canvas != null)
		  canvas.dispose();
	}

  }

  private static long INSTANCE_COUNTER = 0;
  private long _instanceID;
  private long _changeCounter;

  private static class ImageListener implements IImageListener
  {
	private HUDImageRenderer _hudImageRenderer;

	public ImageListener(HUDImageRenderer hudImageRenderer)
	{
		_hudImageRenderer = hudImageRenderer;
	}

	public final void imageCreated(IImage image)
	{
	  _hudImageRenderer.setImage(image);
	}

  }

  private GLState _glState;
  private Mesh _mesh;
  private HUDImageRenderer.ImageFactory _imageFactory;
  private boolean _creatingMesh;

  private Mesh getMesh(G3MRenderContext rc)
  {
	if (_mesh == null)
	{
	  if (!_creatingMesh)
	  {
		if (_image == null)
		{
		  _creatingMesh = true;
  
		  final Camera camera = rc.getCurrentCamera();
  
		  int width = camera.getViewPortWidth();
		  if (rc.getViewMode() == ViewMode.STEREO)
		  {
			width /= 2;
		  }
		  final int height = camera.getViewPortHeight();
  
		  _imageFactory.create(rc, width, height, new HUDImageRenderer.ImageListener(this), true);
		}
	  }
  
	  if (_image != null)
	  {
		_mesh = createMesh(rc);
	  }
	}
  
	return _mesh;
  }
  private Mesh createMesh(G3MRenderContext rc)
  {
	_creatingMesh = false;
  
	if (_mesh != null)
	{
	  if (_mesh != null)
		  _mesh.dispose();
	  _mesh = null;
	}
  
	final IStringUtils su = IStringUtils.instance();
	final String textureName = "HUDImageRenderer" + su.toString(_instanceID) + "/" + su.toString(_changeCounter++);
  
	final TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(_image, GLFormat.rgba(), textureName, false);
  
	if (_image != null)
		_image.dispose();
	_image = null;
  
	if (texId == null)
	{
	  rc.getLogger().logError("Can't upload texture to GPU");
	  return null;
	}
  
  
	final Camera camera = rc.getCurrentCamera();
	int viewPortWidth = camera.getViewPortWidth();
	if (rc.getViewMode() == ViewMode.STEREO)
	{
	  viewPortWidth /= 2;
	}
	final double halfWidth = viewPortWidth / 2.0;
	final double halfHeight = camera.getViewPortHeight() / 2.0;
  
	FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
	vertices.add(-halfWidth, halfHeight, 0);
	vertices.add(-halfWidth, -halfHeight, 0);
	vertices.add(halfWidth, halfHeight, 0);
	vertices.add(halfWidth, -halfHeight, 0);
  
	DirectMesh mesh = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1);
  
	if (vertices != null)
		vertices.dispose();
  
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
	texCoords.add(0, 0);
	texCoords.add(0, 1);
	texCoords.add(1, 0);
	texCoords.add(1, 1);
  
	TextureMapping textureMapping = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
	return new TexturedMesh(mesh, true, textureMapping, true, true);
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _image;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _image = new internal();
//#endif
  private void setImage(IImage image)
  {
	_image = image;
  }

  public HUDImageRenderer(HUDImageRenderer.ImageFactory imageFactory)
  {
	  _imageFactory = imageFactory;
	  _glState = new GLState();
	  _creatingMesh = false;
	  _image = null;
	  _mesh = null;
	  _instanceID = INSTANCE_COUNTER++;
	  _changeCounter = 0;
  
  }

  public final void initialize(G3MContext context)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
	Mesh mesh = getMesh(rc);
	if (mesh != null)
	{
	  mesh.render(rc, _glState);
	}
  }


  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
	int logicWidth = width;
	if (ec.getViewMode() == ViewMode.STEREO)
	{
	  logicWidth /= 2;
	}
	final int halfWidth = logicWidth / 2;
	final int halfHeight = height / 2;
	MutableMatrix44D projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
  
	ProjectionGLFeature pr = (ProjectionGLFeature) _glState.getGLFeature(GLFeatureID.GLF_PROJECTION);
	if (pr == null)
	{
	  _glState.addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()), false);
	}
	else
	{
	  pr.setMatrix(projectionMatrix.asMatrix44D());
	}
  
	recreateImage();
  }

  public void dispose()
  {
	_glState._release();
  
	if (_mesh != null)
		_mesh.dispose();
	if (_image != null)
		_image.dispose();
  
	if (_imageFactory != null)
		_imageFactory.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void recreateImage()
  {
	_creatingMesh = false;
  
	if (_mesh != null)
		_mesh.dispose();
	_mesh = null;
  
	if (_image != null)
		_image.dispose();
	_image = null;
  }

  public final void stop(G3MRenderContext rc)
  {
	recreateImage();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: HUDImageRenderer::ImageFactory* getImageFactory() const
  public final HUDImageRenderer.ImageFactory getImageFactory()
  {
	return _imageFactory;
  }

}
