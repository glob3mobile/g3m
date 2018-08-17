package org.glob3.mobile.generated;//
//  G3MRenderContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

//
//  G3MRenderContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class OrderedRenderable;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrameTasksExecutor;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TexturesHandler;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramManager;

public class G3MRenderContext extends G3MContext
{
  private FrameTasksExecutor _frameTasksExecutor;
  private GL _gl;
  private final Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;
  private ITimer _frameStartTimer;
  private GPUProgramManager _gpuProgramManager;


  private java.util.ArrayList<OrderedRenderable> _orderedRenderables;

  public G3MRenderContext(FrameTasksExecutor frameTasksExecutor, IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, GL gl, Camera currentCamera, Camera nextCamera, TexturesHandler texturesHandler, IDownloader downloader, EffectsScheduler scheduler, ITimer frameStartTimer, IStorage storage, GPUProgramManager gpuProgramManager, SurfaceElevationProvider surfaceElevationProvider, ViewMode viewMode)
  {
	  super(factory, stringUtils, threadUtils, logger, mathUtils, jsonParser, planet, downloader, scheduler, storage, surfaceElevationProvider, viewMode);
	  _frameTasksExecutor = frameTasksExecutor;
	  _gl = gl;
	  _currentCamera = currentCamera;
	  _nextCamera = nextCamera;
	  _texturesHandler = texturesHandler;
	  _frameStartTimer = frameStartTimer;
	  _orderedRenderables = null;
	  _gpuProgramManager = gpuProgramManager;

  }

  public final void clear()
  {
	_frameStartTimer.start();
	  clearOrderedRenderables();
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUProgramManager* getGPUProgramManager() const
  public final GPUProgramManager getGPUProgramManager()
  {
	return _gpuProgramManager;
  }

  public void dispose()
  {
	if (_frameStartTimer != null)
		_frameStartTimer.dispose();
	_orderedRenderables = null;
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  std.sort(_orderedRenderables.iterator(), _orderedRenderables.end(), GlobalMembersG3MRenderContext.MyDataSortPredicate);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  java.util.Collections.sort(_orderedRenderables, new java.util.Comparator<OrderedRenderable>() { Override public int compare(final OrderedRenderable or1, final OrderedRenderable or2) { return Double.compare(or2.squaredDistanceFromEye(), or1.squaredDistanceFromEye()); } });
//#endif
	}
  
	return _orderedRenderables;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void addOrderedRenderable(OrderedRenderable* orderedRenderable) const
  public final void addOrderedRenderable(OrderedRenderable orderedRenderable)
  {
	if (_orderedRenderables == null)
	{
	  _orderedRenderables = new java.util.ArrayList<OrderedRenderable*>();
	}
	_orderedRenderables.add(orderedRenderable);
  }

	public final void clearOrderedRenderables()
	{
		_orderedRenderables = null;
		_orderedRenderables = null;
	}

}
//#endif
