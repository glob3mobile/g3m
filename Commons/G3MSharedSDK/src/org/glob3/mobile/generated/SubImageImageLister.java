package org.glob3.mobile.generated; 
//const void CPUTextureBuilder::createTextureFromImages(GL* gl,
//                                                      const IFactory* factory,
//                                                      const std::vector<const IImage*>& images,
//                                                      int width, int height,
//                                                      IImageListener* listener,
//                                                      bool autodelete) const{
//
//  const int imagesSize = images.size();
//
//  if (imagesSize == 0) {
//    ILogger::instance()->logWarning("Creating blank Image");
//    // return factory->createImageFromSize(width, height);
//    factory->createImageFromSize(width, height, listener, autodelete);
//  }
//  else {
//    IImage* im = images[0]->shallowCopy();
//    IImage* im2 = NULL;
//    for (int i = 1; i < imagesSize; i++) {
//      const IImage* imTrans = images[i];
//      im2 = im->combineWith(*imTrans, width, height);
//      delete im;
//      im = im2;
//    }
//    // return im;
//    listener->imageCreated( im );
//    if (autodelete) {
//      delete listener;
//    }
//  }
//}


public class SubImageImageLister implements IImageListener
{
  private final int _width;
  private final int _height;

  private IImageListener _listener;
  private final boolean _autodelete;

  public SubImageImageLister(int width, int height, IImageListener listener, boolean autodelete)
  {
	  _width = width;
	  _height = height;
	  _listener = listener;
	  _autodelete = autodelete;

  }

  public final void imageCreated(IImage image)
  {
	image.scale(_width, _height, _listener, _autodelete);
	if (image != null)
		image.dispose();
  }
}