//
//  ByteBuffer_iOS.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


#include "ByteBuffer_iOS.hpp"

#include "IStringBuilder.hpp"

const std::string ByteBuffer_iOS::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("ByteBuffer_iOS (size=");
  isb->add(_size);
  isb->add("_");
  std::string s = isb->getString();
  delete isb;
  return s;
}
