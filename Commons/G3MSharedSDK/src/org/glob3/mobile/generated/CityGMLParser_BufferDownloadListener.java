package org.glob3.mobile.generated; 
///#import <mach/mach.h>


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

    //    vm_size_t report_memory(void) {
    //      struct task_basic_info info;
    //      mach_msg_type_number_t size = sizeof(info);
    //      kern_return_t kerr = task_info(mach_task_self(),
    //                                     TASK_BASIC_INFO,
    //                                     (task_info_t)&info,
    //                                     &size);
    //      if( kerr == KERN_SUCCESS ) {
    //        //    printf("Memory in use (in bytes): %lu", info.resident_size);
    //        return info.resident_size;
    //      } else {
    //        //    printf("Error with task_info(): %s", mach_error_string(kerr));
    //        return 0;
    //      }
    //    }

    public void runInBackground(G3MContext context)
    {

      //      vm_size_t startM = report_memory();
      _buildings = CityGMLParser.parseLOD2Buildings2(_s); //SAX

      //      IXMLNode* xml = IFactory::instance()->createXMLNodeFromXML(_s);
      //      _buildings = CityGMLParser::parseLOD2Buildings2(xml); //DOM
      //      delete xml;


      //      vm_size_t finalM = report_memory();
      //      printf("MEMORY USAGE DOM PARSING OF %lu buildings: %lu\n", _buildings.size(), (finalM - startM));

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