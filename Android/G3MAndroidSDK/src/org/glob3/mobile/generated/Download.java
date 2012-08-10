package org.glob3.mobile.generated; 
public class Download
{

  private static int _currentID = 0;

  public String _url;
  public int _priority;
  public java.util.ArrayList<ListenerID> _listeners = new java.util.ArrayList<ListenerID>();

  public Download(String url, int priority)
  {
	  _url = url;
	  _priority = priority;
  }

  public final boolean cancel(int id)
  {
	final Url url = new Url(_url);

	for (int j = 0; j < _listeners.size(); j++)
	{
	  if (_listeners.get(j)._id == id)
	  {
		_listeners.get(j)._listener.onCancel(url); //CANCELING OPERATION
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		_listeners.remove(j);
		return true;
	  }
	}
	return false;
  }

  public final int addListener(IDownloadListener listener)
  {
	ListenerID lid = new ListenerID();
	lid._listener = listener;
	lid._id = _currentID++;
	_listeners.add(lid);
	return lid._id;
  }
}