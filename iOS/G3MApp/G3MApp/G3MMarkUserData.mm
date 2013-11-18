//
//  G3MMarkUserData.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MMarkUserData.hpp"

G3MMarkUserData::G3MMarkUserData(const std::string& title,
                                     const URL& url) :
_title(title),
_url(url) {
}

G3MMarkUserData::G3MMarkUserData(const std::string& title) :
_title(title) {
}

std::string G3MMarkUserData::getTitle() {
  return _title;
}

URL G3MMarkUserData::getUrl() {
  return _url;
}
