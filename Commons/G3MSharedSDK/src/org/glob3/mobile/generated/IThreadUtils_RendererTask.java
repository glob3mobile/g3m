package org.glob3.mobile.generated;import java.util.*;

public class IThreadUtils_RendererTask extends GTask
{
  private GAsyncTask _task;
  private final boolean _autodelete;

  public IThreadUtils_RendererTask(GAsyncTask task, boolean autodelete)
  {
	  _task = task;
	  _autodelete = autodelete;
  }

  public final void run(G3MContext context)
  {
	_task.onPostExecute(context);
  }

  public void dispose()
  {
	if (_autodelete)
	{
	  if (_task != null)
		  _task.dispose();
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }
}
