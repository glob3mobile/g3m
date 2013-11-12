//
//  G3MWeatherDownloadListener.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 23/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <G3MiOSSDK/IBufferDownloadListener.hpp>

@class G3MWidget_iOS;
class GInitializationTask;

class G3MWeatherDownloadListener : public IBufferDownloadListener {

private:
  GInitializationTask* _initTask;
  G3MWidget_iOS* _widget;

public:
  G3MWeatherDownloadListener(GInitializationTask* initTask,
                             G3MWidget_iOS* widget);

  void onDownload(const URL& url,
                  IByteBuffer* buffer, bool expired);

  void onError(const URL& url);

  void onCancel(const URL& url) {
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data, bool expired) {
  }

};

