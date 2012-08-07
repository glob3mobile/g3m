package org.glob3.mobile.generated; 
public class RenderContext extends Context
{
  private GL _gl;
  private Camera _camera;
  private TexturesHandler _texturesHandler;

  public RenderContext(IFactory factory, ILogger logger, Planet planet, GL gl, Camera camera, TexturesHandler texturesHandler, Downloader downloaderOLD, IDownloader downloader, EffectsScheduler scheduler)
  {
	  super(factory, logger, planet, downloaderOLD, downloader, scheduler);
	  _gl = gl;
	  _camera = camera;
	  _texturesHandler = texturesHandler;

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


}