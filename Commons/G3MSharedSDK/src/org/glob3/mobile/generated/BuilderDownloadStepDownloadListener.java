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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void onDownload(URL url, <<<<<<< HEAD IImage* image, boolean expired);

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
======= IImage* image);

public final void onError(URL url)
{
  //  _onError++;
  _builder.stepCanceled(_position);
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
  }

}