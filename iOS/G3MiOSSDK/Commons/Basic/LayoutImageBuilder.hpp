//
//  LayoutImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

#ifndef __G3MiOSSDK__LayoutImageBuilder__
#define __G3MiOSSDK__LayoutImageBuilder__

#include "AbstractImageBuilder.hpp"
#include "IImageBuilderListener.hpp"
#include "Color.hpp"
#include "IImage.hpp"
#include "RCObject.hpp"
#include <vector>


class LayoutImageBuilder : public AbstractImageBuilder {
protected:


  class ChildResult {
  public:
    const IImage*     _image;
    const std::string _imageName;
    const std::string _error;

    ChildResult(const IImage*      image,
                const std::string& imageName) :
    _image(image),
    _imageName(imageName),
    _error("")
    {
    }

    ChildResult(const std::string& error) :
    _image(NULL),
    _imageName(""),
    _error(error)
    {
    }

    ~ChildResult() {
      delete _image;
    }
  };


  class ChildrenResult : public RCObject {
  private:
    LayoutImageBuilder*    _layoutImageBuilder;
    const G3MContext*      _context;
    IImageBuilderListener* _listener;
    bool                   _deleteListener;

    size_t _childrenResultPendingCounter;

  protected:
    ~ChildrenResult() {
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

  public:
    std::vector<ChildResult*> _childrenResult;

    ChildrenResult(LayoutImageBuilder* layoutImageBuilder,
                   size_t childrenSize,
                   const G3MContext* context,
                   IImageBuilderListener* listener,
                   bool deleteListener) :
    _layoutImageBuilder(layoutImageBuilder),
    _context(context),
    _listener(listener),
    _deleteListener(deleteListener)
    {
      _childrenResultPendingCounter = childrenSize;
      for (size_t i = 0; i < childrenSize; i++) {
        _childrenResult.push_back(NULL); // make space for the result
      }
    }

    void childImageCreated(const IImage*      image,
                           const std::string& imageName,
                           size_t             childIndex);

    void childError(const  std::string& error,
                    size_t childIndex);
  };


  class LayoutImageBuilderChildListener : public IImageBuilderListener {
  private:
    ChildrenResult* _childrenResult;
    const size_t    _childIndex;

  public:
    LayoutImageBuilderChildListener(ChildrenResult* childrenResult,
                                    const size_t    childIndex) :
    _childrenResult(childrenResult),
    _childIndex(childIndex)
    {
      _childrenResult->_retain();
    }

    ~LayoutImageBuilderChildListener() {
      _childrenResult->_release();
    }

    void imageCreated(const IImage*      image,
                      const std::string& imageName) {
      _childrenResult->childImageCreated(image, imageName, _childIndex);
    }

    void onError(const std::string& error) {
      _childrenResult->childError(error, _childIndex);
    }
  };


  std::vector<IImageBuilder*> _children;

  const int   _margin;

  const float _borderWidth;
  const Color _borderColor;

  const int   _padding;

  const Color _backgroundColor;
  const float _cornerRadius;

  const int _childrenSeparation;

  /*
   | margin
   |   border
   |     padding
   |       content
   */

  LayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                     int            margin,
                     float          borderWidth,
                     const Color&   borderColor,
                     int            padding,
                     const Color&   backgroundColor,
                     float          cornerRadius,
                     int            childrenSeparation) :
  _children(children),
  _margin(margin),
  _borderWidth(borderWidth),
  _borderColor(borderColor),
  _padding(padding),
  _backgroundColor(backgroundColor),
  _cornerRadius(cornerRadius),
  _childrenSeparation(childrenSeparation)
  {
  }

  LayoutImageBuilder(IImageBuilder* child0,
                     IImageBuilder* child1,
                     int            margin,
                     float          borderWidth,
                     const Color&   borderColor,
                     int            padding,
                     const Color&   backgroundColor,
                     const float    cornerRadius,
                     int            childrenSeparation) :
  _margin(margin),
  _borderWidth(borderWidth),
  _borderColor(borderColor),
  _padding(padding),
  _backgroundColor(backgroundColor),
  _cornerRadius(cornerRadius),
  _childrenSeparation(childrenSeparation)
  {
    _children.push_back(child0);
    _children.push_back(child1);
  }

  virtual void doLayout(const G3MContext* context,
                        IImageBuilderListener* listener,
                        bool deleteListener,
                        const std::vector<ChildResult*>& results) = 0;

public:
  ~LayoutImageBuilder();

  bool isMutable() const;

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
