package org.glob3.mobile.generated; 
public class CityGMLParser_BufferDownloadListener extends IBufferDownloadListener
{
  private CityGMLListener _listener;
  private boolean _deleteListener;
  public CityGMLParser_BufferDownloadListener(CityGMLListener listener, boolean deleteListener)
  {
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

    java.util.ArrayList<CityGMLBuilding> buildings = CityGMLParser.parseLOD2Buildings2(s);

    _listener.onBuildingsCreated(buildings);
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
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