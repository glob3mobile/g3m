package org.glob3.mobile.generated; 
//
//  GLConstants.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  GLConstants.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class GLCullFace
{
  private static int _front = 0;
  private static int _back = 0;
  private static int _frontAndBack = 0;

  public static int front()
  {
     return _front;
  }
  public static int back()
  {
     return _back;
  }
  public static int frontAndBack()
  {
     return _frontAndBack;
  }

  public static void init(INativeGL ngl)
  {
    _front = ngl.CullFace_Front();
    _back = ngl.CullFace_Back();
    _frontAndBack = ngl.CullFace_FrontAndBack();
  }
}