package org.glob3.mobile.generated; 
public class BuilderDownloadStepDownloadListener implements IDownloadListener
{
  private TileTextureBuilder _builder;
  private final int _position;

//  int _onDownload;
//  int _onError;
//  int _onCancel;

  public BuilderDownloadStepDownloadListener(TileTextureBuilder builder, int position)
  //_onDownload(0),
  //_onError(0),
  //_onCancel(0)
  {
	  _builder = builder;
	  _position = position;
	_builder._retain();
  }

  public final void onDownload(Response response)
  {
  //  _onDownload++;
	_builder.stepDownloaded(_position, response.getByteArrayWrapper());
  }

  public final void onError(Response response)
  {
  //  _onError++;
	_builder.stepCanceled(_position);
  }

  public final void onCanceledDownload(Response response)
  {
  }

  public final void onCancel(URL url)
  {
  //  _onCancel++;
	_builder.stepCanceled(_position);
  }

//  void showInvalidState() const {
//    printf("onDownload=%d, onCancel=%d, onError=%d\n", _onDownload, _onCancel, _onError);
//  }

//  void testState() const {
//    if ((_onDownload == 1) && (_onCancel == 0) && (_onError == 0)) {
//      return;
//    }
//    if ((_onDownload == 0) && (_onCancel == 1) && (_onError == 0)) {
//      return;
//    }
//    if ((_onDownload == 0) && (_onCancel == 0) && (_onError == 1)) {
//      return;
//    }
//    showInvalidState();
//  }

  public void dispose()
  {
  //  testState();
  
	if (_builder != null)
	{
	  _builder._release();
	}
  }

}