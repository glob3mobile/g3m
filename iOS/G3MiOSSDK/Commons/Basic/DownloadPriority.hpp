//
//  DownloadPriority.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 08/02/13.
//
//

#ifndef __G3MiOSSDK__DownloadPriority__
#define __G3MiOSSDK__DownloadPriority__

class DownloadPriority {
public:
  static const long long LOWEST;
  static const long long LOWER;
  static const long long MEDIUM;
  static const long long HIGHER;
  static const long long HIGHEST;
  
  static const long long getMarkDownloadPriority();
  static const long long getTileDownloadPriority();
  static const long long getTextureDownloadPriority();
};


#endif /* defined(__G3MiOSSDK__DownloadPriority__) */
