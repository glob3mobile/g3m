package org.glob3.mobile.generated;
public class XPCNodeContentDownloadListener extends IBufferDownloadListener
{
  private XPCNode _node;
  private final IThreadUtils _threadUtils;

  private final Planet _planet;
  private final float _verticalExaggeration;
  private final float _deltaHeight;


  public XPCNodeContentDownloadListener(XPCNode node, IThreadUtils threadUtils, Planet planet, float verticalExaggeration, float deltaHeight)
  {
     _node = node;
     _threadUtils = threadUtils;
     _planet = planet;
     _verticalExaggeration = verticalExaggeration;
     _deltaHeight = deltaHeight;
    _node._retain();
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    if (_node.isCanceled())
    {
      if (buffer != null)
         buffer.dispose();
    }
    else
    {
      _threadUtils.invokeAsyncTask(new XPCNodeContentParserAsyncTask(_node, buffer, _planet, _verticalExaggeration, _deltaHeight), true);
    }
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