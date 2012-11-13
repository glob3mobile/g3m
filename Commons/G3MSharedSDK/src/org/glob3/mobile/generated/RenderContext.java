package org.glob3.mobile.generated; 
//************************************************************


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrameTasksExecutor;


public class RenderContext extends Context
{
  private FrameTasksExecutor _frameTasksExecutor;
  private GL _gl;
  private final Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;
  private TextureBuilder _textureBuilder;
  private ITimer _frameStartTimer;
  private java.util.ArrayList<OrderedRenderable> _orderedRenderables;

  public RenderContext(FrameTasksExecutor frameTasksExecutor, IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, GL gl, Camera currentCamera, Camera nextCamera, TexturesHandler texturesHandler, TextureBuilder textureBuilder, IDownloader downloader, EffectsScheduler scheduler, ITimer frameStartTimer, IStorage storage)
  {
	  super(factory, stringUtils, threadUtils, logger, mathUtils, jsonParser, planet, downloader, scheduler, storage);
	  _frameTasksExecutor = frameTasksExecutor;
	  _gl = gl;
	  _currentCamera = currentCamera;
	  _nextCamera = nextCamera;
	  _texturesHandler = texturesHandler;
	  _textureBuilder = textureBuilder;
	  _frameStartTimer = frameStartTimer;
	  _orderedRenderables = null;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GL* getGL() const
  public final GL getGL()
  {
	return _gl;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Camera* getCurrentCamera() const
  public final Camera getCurrentCamera()
  {
	return _currentCamera;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Camera* getNextCamera() const
  public final Camera getNextCamera()
  {
	return _nextCamera;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TexturesHandler* getTexturesHandler() const
  public final TexturesHandler getTexturesHandler()
  {
	return _texturesHandler;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TextureBuilder* getTextureBuilder() const
  public final TextureBuilder getTextureBuilder()
  {
	return _textureBuilder;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ITimer* getFrameStartTimer() const
  public final ITimer getFrameStartTimer()
  {
	return _frameStartTimer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: FrameTasksExecutor* getFrameTasksExecutor() const
  public final FrameTasksExecutor getFrameTasksExecutor()
  {
	return _frameTasksExecutor;
  }

  public void dispose()
  {
	//  delete _frameStartTimer;
	IFactory.instance().deleteTimer(_frameStartTimer);
	_orderedRenderables = null;
  }

  /*
   Get the OrderedRenderables, sorted by distanceFromEye()
   */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<OrderedRenderable*>* getSortedOrderedRenderables() const
  public final java.util.ArrayList<OrderedRenderable> getSortedOrderedRenderables()
  {
	if (_orderedRenderables != null)
	{
  	java.util.Collections.sort(
  							   _orderedRenderables,
  							   new java.util.Comparator<OrderedRenderable>() {
  								 @Override
  								 public int compare(final OrderedRenderable o1,
  													final OrderedRenderable o2) {
  								   return Double.compare(o2.distanceFromEye(), o1.distanceFromEye());
  								 }
  							   });
	}
  
	return _orderedRenderables;
  }

  public final void addOrderedRenderable(OrderedRenderable orderedRenderable)
  {
	if (_orderedRenderables == null)
	{
	  _orderedRenderables = new java.util.ArrayList<OrderedRenderable>();
	}
	_orderedRenderables.add(orderedRenderable);
  }

}