package org.glob3.mobile.generated; 
//************************************************************


public class RenderContext extends Context
{
  private GL _gl;
  private Camera _camera;
  private TexturesHandler _texturesHandler;
  private ITimer _frameStartTimer;

  public RenderContext(IFactory factory, ILogger logger, Planet planet, GL gl, Camera camera, TexturesHandler texturesHandler, Downloader downloaderOLD, IDownloader downloader, EffectsScheduler scheduler, ITimer frameStartTimer)
  {
	  super(factory, logger, planet, downloaderOLD, downloader, scheduler);
	  _gl = gl;
	  _camera = camera;
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
//ORIGINAL LINE: Camera* getCamera() const
  public final Camera getCamera()
  {
	return _camera;
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