package org.glob3.mobile.generated;
public class XPCNodeContentDownloadListener extends IBufferDownloadListener
{
  private XPCNode _node;
  private final IThreadUtils _threadUtils;
  private final Planet _planet;


  public XPCNodeContentDownloadListener(XPCNode node, IThreadUtils threadUtils, Planet planet)
  {
     _node = node;
     _threadUtils = threadUtils;
     _planet = planet;
    _node._retain();
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    _threadUtils.invokeAsyncTask(new XPCNodeContentParserAsyncTask(_node, buffer, _planet), true);
  }

  public final void onError(URL url)
  {
    _node.errorDownloadingContent();
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

  public void dispose()
  {
    _node._release();
  }


}