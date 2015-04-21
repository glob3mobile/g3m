package org.glob3.mobile.generated; 
public class IThreadUtils_BackgroundTask extends GTask
{
  private GAsyncTask _task;
  private final boolean _autodelete;

  public IThreadUtils_BackgroundTask(GAsyncTask task, boolean autodelete)
  {
     _task = task;
     _autodelete = autodelete;
  }

  public final void run(G3MContext context)
  {
    _task.runInBackground(context);

    context.getThreadUtils().invokeInRendererThread(new IThreadUtils_RendererTask(_task, _autodelete), true);
  }

}