package org.glob3.mobile.generated; 
//************************************************************


public class RenderContext extends Context
{
  private GL _gl;
  private final Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;
  private ITimer _frameStartTimer;

  public RenderContext(IFactory factory, ILogger logger, Planet planet, GL gl, Camera currentCamera, Camera nextCamera, TexturesHandler texturesHandler, Downloader downloaderOLD, IDownloader downloader, EffectsScheduler scheduler, ITimer frameStartTimer)
  {
	  super(factory, logger, planet, downloaderOLD, downloader, scheduler);
	  _gl = gl;
	  _currentCamera = currentCamera;
	  _nextCamera = nextCamera;
	  _texturesHandler = texturesHandler;
	  _frameStartTimer = frameStartTimer;

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

  public void dispose()
  {
	if (_frameStartTimer != null)
		_frameStartTimer.dispose();
  }

}