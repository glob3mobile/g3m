package org.glob3.mobile.generated;//
//  DefaultRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

//
//  DefaultRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ChangedInfoListener;



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;

public abstract class DefaultRenderer extends Renderer
{


  private boolean _enable;

  private final java.util.ArrayList<Info> _info = new java.util.ArrayList<Info>();

  private void notifyChangedInfo(java.util.ArrayList<const Info> info)
  {
	if(_changedInfoListener!= null)
	{
	  if(isEnable())
	  {
		_changedInfoListener.changedRendererInfo(_rendererIdentifier, info);
	  }
	}
  }


  protected ChangedRendererInfoListener _changedInfoListener = null;

  protected int _rendererIdentifier = 0;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final G3MContext _context;
//#else
  protected G3MContext _context;
//#endif

  protected DefaultRenderer()
  {
	  _enable = true;

  }

  protected DefaultRenderer(boolean enable)
  {
	  _enable = enable;

  }

  public void dispose()
  {
	_context = null;
	_changedInfoListener = null;
  }

  protected void onChangedContext()
  {
  }

  protected void onLostContext()
  {
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnable() const
  public final boolean isEnable()
  {
	return _enable;
  }

  public void setEnable(boolean enable)
  {
	if(enable != _enable)
	{
	  _enable = enable;
	  if(_changedInfoListener!= null)
	  {
		if(isEnable())
		{
		  notifyChangedInfo(_info);
		}
		else
		{
		  final java.util.ArrayList<const Info> info = new java.util.ArrayList<const Info>();
		  _changedInfoListener.changedRendererInfo(_rendererIdentifier, info);
		}
	  }
	}
  }

  public void initialize(G3MContext context)
  {
	_context = context;
	onChangedContext();
  }

  public void onResume(G3MContext context)
  {
	_context = context;
	onChangedContext();
  }

  public void onPause(G3MContext context)
  {
	_context = null;
	onLostContext();
  }

  public void onDestroy(G3MContext context)
  {
	_context = null;
	onLostContext();
  }


  public RenderState getRenderState(G3MRenderContext rc)
  {
	return RenderState.ready();
  }

  public abstract void render(G3MRenderContext rc, GLState glState);

  public boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public void start(G3MRenderContext rc)
  {

  }

  public void stop(G3MRenderContext rc)
  {

  }

  public SurfaceElevationProvider getSurfaceElevationProvider()
  {
	return null;
  }

  public PlanetRenderer getPlanetRenderer()
  {
	return null;
  }

  public boolean isPlanetRenderer()
  {
	return false;
  }

  public final void setInfo(java.util.ArrayList<const Info> info)
  {
	_info.clear();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_info.insert(_info.end(), info.iterator(), info.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_info.addAll(info);
//#endif
	notifyChangedInfo(_info);
  }

  public final void addInfo(java.util.ArrayList<const Info> info)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_info.insert(_info.end(), info.iterator(), info.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_info.addAll(info);
//#endif
	notifyChangedInfo(_info);
  }

  public final void addInfo(Info info)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_info.insert(_info.end(), info);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_info.add(info);
//#endif
	notifyChangedInfo(_info);
  }




  public void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererIdentifier)
  {
	if (_changedInfoListener != null)
	{
	  ILogger.instance().logError("Changed Renderer Info Listener of DefaultRenderer already set");
	}
	else
	{
	  _changedInfoListener = changedInfoListener;
	  _rendererIdentifier = rendererIdentifier;
	  notifyChangedInfo(_info);
	}
  }
}
