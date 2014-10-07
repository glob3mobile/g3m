package org.glob3.mobile.generated; 
public abstract class IThreadUtils
{
  protected G3MContext _context;

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

  public abstract void invokeInRendererThread(GTask task, boolean autoDelete);

  public abstract void invokeInBackground(GTask task, boolean autoDelete);

  public final void invokeAsyncTask(GAsyncTask task, boolean autodelete)
  {
    invokeInBackground(new IThreadUtils_BackgroundTask(task, autodelete), true);
  }

}