package org.glob3.mobile.generated; 
//class IStorageImageListener {
//public:
///#if C_CODE
//  virtual ~IStorageImageListener() { }
///#endif
///#ifdef JAVA_CODE
//  void dispose();
///#endif
//
//  /**
//   Callback method image reading.
//   The image will be NULL if it doesn't exist
//   The image has to be deleted in C++ / and disposed() en Java.
//   */
//  virtual void imageRead(IImage* image,
//                         bool expired) = 0;
//
//};


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

//  virtual void readImage(const URL& url,
//                         bool readExpired,
//                         IStorageImageListener* imageListener,
//                         bool autodeleteImageListener) = 0;

  public abstract void saveBuffer(URL url, IByteBuffer buffer, TimeInterval timeToExpires, boolean saveInBackground);

  public abstract void saveImage(URL url, IImage image, TimeInterval timeToExpires, boolean saveInBackground);


  public abstract void onResume(G3MContext context);

  public abstract void onPause(G3MContext context);

  public abstract void onDestroy(G3MContext context);


  public abstract boolean isAvailable();

}