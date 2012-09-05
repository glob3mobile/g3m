package org.glob3.mobile.generated; 
public class TopTileDownloadListener implements IDownloadListener
{
  private MultiLayerTileTexturizer _texturizer;

  public TopTileDownloadListener(MultiLayerTileTexturizer texturizer)
  {
	  _texturizer = texturizer;
  }

  public final void onDownload(Response response)
  {
	_texturizer.countTopTileRequest();
  }

  public final void onError(Response response)
  {
	_texturizer.countTopTileRequest();
  }

  public final void onCanceledDownload(Response response)
  {
  }

  public final void onCancel(URL url)
  {
	_texturizer.countTopTileRequest();
  }

}