//
//  G3MMarkerUserData.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MMarkerUserData.hpp"

G3MMarkerUserData::G3MMarkerUserData(std::string title, const URL &url) :
_title(title),
_url(url) {
}

G3MMarkerUserData::G3MMarkerUserData(std::string title) :
_title(title) {
}

std::string G3MMarkerUserData::getTitle() {
  return _title;
}

URL G3MMarkerUserData::getUrl() {
  return _url;
}