package org.glob3.mobile.generated; 
public class CityGMLParser_BufferDownloadListener extends IBufferDownloadListener
{
  private CityGMLListener _listener;
  private boolean _deleteListener;
  private final IThreadUtils _threadUtils;

  private static class ParsingTask extends GAsyncTask
  {
    private final String _s;
    private java.util.ArrayList<CityGMLBuilding> _buildings = new java.util.ArrayList<CityGMLBuilding>();

    private CityGMLListener _listener;
    private boolean _deleteListener;

    public ParsingTask(String s, CityGMLListener listener, boolean deleteListener)
    {
       _s = s;
       _listener = listener;
       _deleteListener = deleteListener;
    }

    public void runInBackground(G3MContext context)
    {
      _buildings = CityGMLParser.parseLOD2Buildings2(_s);
    }

    public void onPostExecute(G3MContext context)
    {
      _listener.onBuildingsCreated(_buildings);
      if (_deleteListener)
      {
        if (_listener != null)
           _listener.dispose();
      }
    }
  }


  public CityGMLParser_BufferDownloadListener(IThreadUtils threadUtils, CityGMLListener listener, boolean deleteListener)
  {
     _threadUtils = threadUtils;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public void dispose()
  {
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    String s = buffer.getAsString();
    if (buffer != null)
       buffer.dispose();

    //More "expensive" way of parsing
    //    IXMLNode* xml = IFactory::instance()->createXMLNodeFromXML(s);
    //    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(xml);
    //    delete xml;

    _threadUtils.invokeAsyncTask(new ParsingTask(s, _listener, _deleteListener), true);

    //    std::vector<CityGMLBuilding*> buildings = CityGMLParser::parseLOD2Buildings2(s);
    //
    //    _listener->onBuildingsCreated(buildings);
    //    if (_deleteListener){
    //      delete _listener;
    //    }
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url.getPath());
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

}