//
//  HUDQuadWidget.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#include "HUDQuadWidget.hpp"

#include "Context.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"

class HUDQuadWidget_ImageDownloadListener : public IImageDownloadListener {
  HUDQuadWidget* _quadWidget;

public:
  HUDQuadWidget_ImageDownloadListener(HUDQuadWidget* quadWidget) :
  _quadWidget(quadWidget)
  {
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired)  {
    _quadWidget->onImageDownload(image);
  }

  void onError(const URL& url) {
    _quadWidget->onImageDownloadError(url);
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }

};


void HUDQuadWidget::initialize(const G3MContext* context) {
  IDownloader* downloader = context->getDownloader();
  downloader->requestImage(_imageURL,
                           1000000, // priority
                           TimeInterval::fromDays(30),
                           true, // readExpired
                           new HUDQuadWidget_ImageDownloadListener(this),
                           true);
}

void HUDQuadWidget::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width,
                                          int height) {
  aa;
}

void HUDQuadWidget::onImageDownload(IImage* image) {
  aa;
}

void HUDQuadWidget::onImageDownloadError(const URL& url) {
  aa;
}

void HUDQuadWidget::render(const G3MRenderContext* rc,
                           GLState* glState) {
  aa;
}
