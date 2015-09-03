package org.glob3.mobile.generated; 
public class FeatureInfoDownloadListener extends IBufferDownloadListener
{
  private MapBooOLDApplicationChangeListener _applicationListener;

  /**
   Callback method invoked on a successful download.  The buffer has to be deleted in C++ / .disposed() in Java
   */
  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    if (_applicationListener != null)
    {
      _applicationListener.onFeatureInfoReceived(buffer);
    }
  }

  /**
   Callback method invoke after an error trying to download url
   */
  public final void onError(URL url)
  {

  }

  /**
   Callback method invoke after canceled request
   */
  public final void onCancel(URL url)
  {

  }

  /**
   This method will be call, before onCancel, when the data arrived before the cancelation.
   
   The buffer WILL be deleted/disposed after the method finishs.  If you need to keep the buffer, use shallowCopy() to store a copy of the buffer.
   */
  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {

  }

  public FeatureInfoDownloadListener(MapBooOLDApplicationChangeListener applicationListener)
  {
     _applicationListener = applicationListener;

  }

  public void dispose()
  {
    super.dispose();

  }

}