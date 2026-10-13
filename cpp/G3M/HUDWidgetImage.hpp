//
//  HUDWidgetImage.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//

#ifndef __G3M__HUDWidgetImage__
#define __G3M__HUDWidgetImage__

#include <string>
#include <vector>

class IImage;
class IImageFactory;
class G3MContext;
class ChangedListener;


// An image a HUD widget draws, built by its factory (owned)
class HUDWidgetImage {
private:
  IImageFactory* _imageFactory;

#ifdef C_CODE
  const IImage* _image;
#endif
#ifdef JAVA_CODE
  private IImage _image;
#endif

  std::string _imageName;
  int         _imageWidth;
  int         _imageHeight;

  bool _building;

  std::vector<std::string> _errors;

  void startBuilding(const G3MContext* context);

public:
  HUDWidgetImage(IImageFactory* imageFactory);

  ~HUDWidgetImage();

  /** starts building the image unless it exists or is being built; a mutable factory reports its changes to changedListener */
  void initialize(const G3MContext* context,
                  ChangedListener*  changedListener);

  /** drops the image and builds it again */
  void rebuild(const G3MContext* context);

  bool isBuilding() const {
    return _building;
  }

  const std::vector<std::string> getErrors() const {
    return _errors;
  }

  /** NULL until built */
  const IImage* getImage() const {
    return _image;
  }

  const std::string getImageName() const {
    return _imageName;
  }

  int getImageWidth() const {
    return _imageWidth;
  }

  int getImageHeight() const {
    return _imageHeight;
  }

  /** private, do not call */
  void imageCreated(const IImage*      image,
                    const std::string& imageName);

  /** private, do not call */
  void onImageBuildError(const std::string& error);

};

#endif
