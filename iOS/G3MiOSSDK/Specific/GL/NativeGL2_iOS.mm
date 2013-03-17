//
//  NativeGL_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "NativeGL2_iOS.hpp"

//#include <GLKit/GLKTextureLoader.h>
//
//void NativeGL2_iOS::texImage2D(const IImage* image,
//                               int format) const {
//  Image_iOS* image_iOS = ((Image_iOS*) image);
//
//  UIImage* uiImage = image_iOS->getUIImage();
//  
//  GLKTextureInfo* textureInfo = [GLKTextureLoader textureWithCGImage: [uiImage CGImage]
//                                                             options: nil
//                                                               error: nil];
//  if (textureInfo) {
//    NSLog(@"Texture loaded successfully. name = %d size = (%d x %d)",
//          textureInfo.name, textureInfo.width, textureInfo.height);
//  }
//
////  const unsigned char* data = ((Image_iOS*) image)->createByteArrayRGBA8888();
////
////  glTexImage2D(GL_TEXTURE_2D,
////               0,
////               format,
////               image->getWidth(),
////               image->getHeight(),
////               0,
////               format,
////               GL_UNSIGNED_BYTE,
////               data);
////
////  delete [] data;
//}
