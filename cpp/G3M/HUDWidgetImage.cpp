//
//  HUDWidgetImage.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//

#include "HUDWidgetImage.hpp"

#include "IImage.hpp"
#include "IImageFactory.hpp"
#include "IImageFactoryListener.hpp"


class HUDWidgetImage_ImageFactoryListener : public IImageFactoryListener {
private:
  HUDWidgetImage* _widgetImage;

public:
  HUDWidgetImage_ImageFactoryListener(HUDWidgetImage* widgetImage) :
  _widgetImage(widgetImage)
  {
  }

  ~HUDWidgetImage_ImageFactoryListener() {
  }

  void imageCreated(const IImage* image,
                    const std::string& imageName) {
    _widgetImage->imageCreated(image, imageName);
  }

  void onError(const std::string& error) {
    _widgetImage->onImageBuildError(error);
  }
};


HUDWidgetImage::HUDWidgetImage(IImageFactory* imageFactory) :
_imageFactory(imageFactory),
_image(NULL),
_imageWidth(0),
_imageHeight(0),
_building(false)
{
}

HUDWidgetImage::~HUDWidgetImage() {
  delete _imageFactory;
  delete _image;
}

void HUDWidgetImage::startBuilding(const G3MContext* context) {
  _building = true;
  _imageFactory->create(context,
                        new HUDWidgetImage_ImageFactoryListener(this),
                        true);
}

void HUDWidgetImage::initialize(const G3MContext* context,
                                ChangedListener*  changedListener) {
  if (!_building && (_image == NULL)) {
    startBuilding(context);
    if (_imageFactory->isMutable()) {
      _imageFactory->setChangeListener(changedListener);
    }
  }
}

void HUDWidgetImage::rebuild(const G3MContext* context) {
  delete _image;
  _image       = NULL;
  _imageName   = "";
  _imageWidth  = 0;
  _imageHeight = 0;

  startBuilding(context);
}

void HUDWidgetImage::imageCreated(const IImage*      image,
                                  const std::string& imageName) {
  _building    = false;
  _image       = image;
  _imageName   = imageName;
  _imageWidth  = _image->getWidth();
  _imageHeight = _image->getHeight();
}

void HUDWidgetImage::onImageBuildError(const std::string& error) {
  _errors.push_back(error);
}
