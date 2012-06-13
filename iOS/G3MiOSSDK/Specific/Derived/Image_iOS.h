//
//  Image_iOS.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 13/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#import <UIKit/UIKit.h>

#include <string>

#include "IImage.hpp"

class Image_iOS: public IImage
{
private:
  UIImage * _image;
public:
  void loadFromFileName(std::string filename);
  
  UIImage * getUIImage() const { return _image;}
};