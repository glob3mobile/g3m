package org.glob3.mobile.generated; 
public class TopTileDownloadListener implements IImageDownloadListener
{
  private MultiLayerTileTexturizer _texturizer;

  public TopTileDownloadListener(MultiLayerTileTexturizer texturizer)
  {
	  _texturizer = texturizer;
  }

  public void dispose()
  {

  }

  public final void onDownload(URL url, IImage image)
  {
	_texturizer.countTopTileRequest();
  }

  public final void onError(URL url)
  {
	_texturizer.countTopTileRequest();
  }

  public final void onCanceledDownload(URL url, IImage image)
  {
  }

  public final void onCancel(URL url)
  {
	_texturizer.countTopTileRequest();
  }

}