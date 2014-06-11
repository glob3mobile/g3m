package org.glob3.mobile.generated; 
//************************************************************


//class FrameTasksExecutor;


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

  public G3MRenderContext(FrameTasksExecutor frameTasksExecutor, IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, IMathUtils mathUtils, IJSONParser jsonParser, Planet planet, GL gl, Camera currentCamera, Camera nextCamera, TexturesHandler texturesHandler, IDownloader downloader, EffectsScheduler scheduler, ITimer frameStartTimer, IStorage storage, GPUProgramManager gpuProgramManager, SurfaceElevationProvider surfaceElevationProvider)
  {
     super(factory, stringUtils, threadUtils, logger, mathUtils, jsonParser, planet, downloader, scheduler, storage, surfaceElevationProvider);
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

    _orderedRenderables = null;
    _orderedRenderables = null;
  }

  public final GL getGL()
  {
    return _gl;
  }

  public final Camera getCurrentCamera()
  {
    return _currentCamera;
  }

  public final Camera getNextCamera()
  {
    return _nextCamera;
  }

  public final TexturesHandler getTexturesHandler()
  {
    return _texturesHandler;
  }

  public final ITimer getFrameStartTimer()
  {
    return _frameStartTimer;
  }

  public final FrameTasksExecutor getFrameTasksExecutor()
  {
    return _frameTasksExecutor;
  }

  public final GPUProgramManager getGPUProgramManager()
  {
    return _gpuProgramManager;
  }

  public void dispose()
  {
    if (_frameStartTimer != null)
       _frameStartTimer.dispose();
    _orderedRenderables = null;
  
    super.dispose();
  
  }

  /*
   Get the OrderedRenderables, sorted by distanceFromEye()
   */
  public final java.util.ArrayList<OrderedRenderable> getSortedOrderedRenderables()
  {
    if (_orderedRenderables != null)
    {
      java.util.Collections.sort(
                                 _orderedRenderables,
                                 new java.util.Comparator<OrderedRenderable>() {
                                   @Override
                                   public int compare(final OrderedRenderable or1,
                                                      final OrderedRenderable or2) {
                                     return Double.compare(or2.squaredDistanceFromEye(),
                                                           or1.squaredDistanceFromEye());
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