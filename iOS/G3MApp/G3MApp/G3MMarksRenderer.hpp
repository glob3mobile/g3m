//
//  G3MMarksRenderer.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 25/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

class MarksRenderer;
@class G3MViewController;

class G3MMarksRenderer {
public:
  static MarksRenderer* createMarksRenderer(G3MViewController* vc);
};
