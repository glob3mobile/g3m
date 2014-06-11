package org.glob3.mobile.generated; 
//class DTT_TileTextureBuilder;

public class DTT_TileImageListener extends TileImageListener
{
  private DTT_TileTextureBuilder _builder;

  public DTT_TileImageListener(DTT_TileTextureBuilder builder)
  {
     _builder = builder;
    _builder._retain();
  }

  public void dispose()
  {
    _builder._release();
    super.dispose();
  }

  public final void imageCreated(String tileId, IImage image, String imageId, TileImageContribution contribution)
  {
  
//#define JM at WORK
      if (!contribution.getSector().isNan())
      {
          ILogger.instance().logInfo("DTT_TileImageListener received image that does not fit tile");
      }
  
    _builder.imageCreated(image, imageId, contribution);
  }

  public final void imageCreationError(String tileId, String error)
  {
    _builder.imageCreationError(error);
  }

  public final void imageCreationCanceled(String tileId)
  {
    _builder.imageCreationCanceled();
  }
}