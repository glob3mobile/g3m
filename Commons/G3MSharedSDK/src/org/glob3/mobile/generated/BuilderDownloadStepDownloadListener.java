package org.glob3.mobile.generated; 
public class BuilderDownloadStepDownloadListener extends IImageDownloadListener
{
  private TileTextureBuilder _builder;
  private final int _position;

  public BuilderDownloadStepDownloadListener(TileTextureBuilder builder, int position)
  //_onDownload(0),
  //_onError(0),
  //_onCancel(0)
  {
     _builder = builder;
     _position = position;
    _builder._retain();
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    //  _onDownload++;
    _builder.stepDownloaded(_position, image);
  }

  public final void onError(URL url)
  {
    //  _onError++;
    _builder.stepCanceled(_position);
    ILogger.instance().logError("Error downloading tile texture from \"%s\"", url._path);
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
  }

  public final void onCancel(URL url)
  {
    //  _onCancel++;
    _builder.stepCanceled(_position);
  }

  public void dispose()
  {
    //  testState();
  
    if (_builder != null)
    {
      _builder._release();
    }
  
    super.dispose();
  
  }

}