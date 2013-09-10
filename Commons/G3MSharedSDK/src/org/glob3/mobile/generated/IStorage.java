package org.glob3.mobile.generated; 
public abstract class IStorage
{
  protected G3MContext _context;

  public IStorage()
  {
     _context = null;
  }

  public void dispose()
  {
  }

  public void initialize(G3MContext context)
  {
    _context = context;
  }


  public abstract IByteBufferResult readBuffer(URL url, boolean readExpired);

  public abstract IImageResult readImage(URL url, boolean readExpired);

  public abstract void saveBuffer(URL url, IByteBuffer buffer, TimeInterval timeToExpires, boolean saveInBackground);

  public abstract void saveImage(URL url, IImage image, TimeInterval timeToExpires, boolean saveInBackground);


  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);


  public abstract boolean isAvailable();

}