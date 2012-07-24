//
//  Rectangle.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Rectangle_hpp
#define G3MiOSSDK_Rectangle_hpp

class Rectangle
{
public:
  const double _x, _y, _width, _height;
  
  Rectangle(double x, double y, double width, double height): 
  _x(x), _y(y), _width(width), _height(height){}
  
};

#endif
