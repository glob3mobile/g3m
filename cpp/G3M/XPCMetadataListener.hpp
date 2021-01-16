//
//  XPCMetadataListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCMetadataListener_hpp
#define XPCMetadataListener_hpp

class XPCMetadata;


class XPCMetadataListener {
public:
  virtual ~XPCMetadataListener() {
  }

  virtual void onMetadata(const XPCMetadata* metadata) const = 0;

};


#endif

