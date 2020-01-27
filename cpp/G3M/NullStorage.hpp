//
//  NullStorage.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 29/06/12.
//

#ifndef G3M_NullStorage
#define G3M_NullStorage

#include "IStorage.hpp"

class NullStorage: public IStorage {
public:
  IByteBufferResult readBuffer(const URL& url,
                               bool readExpired) {
    return IByteBufferResult(NULL, false);
  }

  IImageResult readImage(const URL& url,
                         bool readExpired) {
    return IImageResult(NULL, false);
  }


  void saveBuffer(const URL& url,
                  const IByteBuffer* buffer,
                  const TimeInterval& timeToExpires,
                  bool saveInBackground) {

  }

  void saveImage(const URL& url,
                 const IImage* image,
                 const TimeInterval& timeToExpires,
                 bool saveInBackground) {

  }


  void onResume(const G3MContext* context) {
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }
  
  void merge(const std::string& databasePath) {
    
  }


  bool isAvailable() {
    return false;
  }
  
  
};

#endif
