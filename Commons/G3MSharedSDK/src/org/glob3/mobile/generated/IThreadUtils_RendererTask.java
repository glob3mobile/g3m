package org.glob3.mobile.generated; 
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
    if (_autodelete)
    {
      if (_task != null)
         _task.dispose();
    }
  }
}