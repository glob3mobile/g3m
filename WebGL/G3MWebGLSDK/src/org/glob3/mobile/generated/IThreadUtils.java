package org.glob3.mobile.generated; 
public abstract class IThreadUtils
{
  private static IThreadUtils _instance = null;


  public static void setInstance(IThreadUtils logger)
  {
//    if (_instance != NULL) {
//      printf("Warning, IThreadUtils instance set two times\n");
//    }
	_instance = logger;
  }

  public static IThreadUtils instance()
  {
	return _instance;
  }

  public void dispose()
  {

  }

  public abstract void invokeInRendererThread(GTask task, boolean autoDelete);

}