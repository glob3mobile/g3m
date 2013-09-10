//
//  G3MPlaneParseTask.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <G3MiOSSDK/GTask.hpp>
@class G3MWidget_iOS;

class G3MPlaneParseTask : public GTask {
  
private:
  G3MWidget_iOS* _widget;
  
public:
  G3MPlaneParseTask(G3MWidget_iOS* widget);
  
  void run(const G3MContext* context);
};
