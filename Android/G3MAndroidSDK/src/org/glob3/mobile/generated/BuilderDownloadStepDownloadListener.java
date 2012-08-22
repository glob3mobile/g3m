package org.glob3.mobile.generated; 
public class BuilderDownloadStepDownloadListener implements IDownloadListener
{
  private TileTextureBuilder _builder;
  private final int _position;

  private int _onDownload;
  private int _onError;
  private int _onCancel;

  public BuilderDownloadStepDownloadListener(TileTextureBuilder builder, int position)
  {
	  _builder = builder;
	  _position = position;
	  _onDownload = 0;
	  _onError = 0;
	  _onCancel = 0;
	_builder._retain();
  }

  public final void onDownload(Response response)
  {
	_onDownload++;
	_builder.stepDownloaded(_position, response.getByteBuffer());
  }

  public final void onError(Response response)
  {
	_onError++;
	_builder.stepCanceled(_position);
  }

  public final void onCanceledDownload(Response response)
  {
  }

  public final void onCancel(URL url)
  {
	_onCancel++;
	_builder.stepCanceled(_position);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showInvalidState() const
  public final void showInvalidState()
  {
	System.out.printf("onDownload=%d, onCancel=%d, onError=%d\n", _onDownload, _onCancel, _onError);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void testState() const
  public final void testState()
  {
	if ((_onDownload == 1) && (_onCancel == 0) && (_onError == 0))
	{
	  return;
	}
	if ((_onDownload == 0) && (_onCancel == 1) && (_onError == 0))
	{
	  return;
	}
	if ((_onDownload == 0) && (_onCancel == 0) && (_onError == 1))
	{
	  return;
	}
	showInvalidState();
  }

  public void dispose()
  {
	testState();
  
	if (_builder != null)
	{
	  _builder._release();
	}
  }

}