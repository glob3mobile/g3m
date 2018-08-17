package org.glob3.mobile.generated;import java.util.*;

public abstract class IThreadUtils
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final G3MContext _context;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected G3MContext _context = new protected();
//#endif

  public IThreadUtils()
  {
	  _context = null;
  }

  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);

  public void initialize(G3MContext context)
  {
	_context = context;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void invokeInRendererThread(GTask* task, boolean autoDelete) const = 0;
  public abstract void invokeInRendererThread(GTask task, boolean autoDelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void invokeInBackground(GTask* task, boolean autoDelete) const = 0;
  public abstract void invokeInBackground(GTask task, boolean autoDelete);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void invokeAsyncTask(GAsyncTask* task, boolean autodelete) const
  public final void invokeAsyncTask(GAsyncTask task, boolean autodelete)
  {
	invokeInBackground(new IThreadUtils_BackgroundTask(task, autodelete), true);
  }

}
