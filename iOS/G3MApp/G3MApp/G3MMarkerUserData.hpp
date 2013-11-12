//
//  G3MMarkerUserData.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <G3MiOSSDK/Mark.hpp>

class G3MMarkerUserData : public MarkUserData {
  
private:
  const std::string _title;
  const URL         _url;
  
public:
  G3MMarkerUserData(const std::string& title,
                    const URL& url);
  G3MMarkerUserData(const std::string& title);
  
  std::string getTitle();
  URL getUrl();
  
};
