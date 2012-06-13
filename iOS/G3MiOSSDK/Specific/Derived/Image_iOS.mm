//
//  Image_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Image_iOS.h"

void Image_iOS::loadFromFileName(std::string filename)
{
  NSString *fn = [NSString stringWithCString:filename.c_str() encoding:[NSString defaultCStringEncoding]];
  _image = [UIImage imageWithContentsOfFile:fn];
  int w = [_image size].width;
  printf("Loaded Image %d\n", w);
  
}
