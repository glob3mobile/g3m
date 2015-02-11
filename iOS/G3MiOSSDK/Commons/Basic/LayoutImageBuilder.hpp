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

  class ChildrenResult {
  private:
    LayoutImageBuilder*    _layoutImageBuilder;
    const G3MContext*      _context;
    IImageBuilderListener* _listener;
    bool                   _deleteListener;

    int _childrenResultPendingCounter;

  public:
    std::vector<ChildResult*> _childrenResult;

    ChildrenResult(LayoutImageBuilder* layoutImageBuilder,
                   int childrenSize,
                   const G3MContext* context,
                   IImageBuilderListener* listener,
                   bool deleteListener) :
    _layoutImageBuilder(layoutImageBuilder),
    _context(context),
    _listener(listener),
    _deleteListener(deleteListener)
    {
      _childrenResultPendingCounter = childrenSize;
      for (int i = 0; i < childrenSize; i++) {
        _childrenResult.push_back(NULL); // make space for the result
      }
    }

    void childImageCreated(const IImage*      image,
                           const std::string& imageName,
                           int                childIndex);

    void childError(const std::string& error,
                    int   childIndex);
  };

  class LayoutImageBuilderChildListener : public IImageBuilderListener {
  private:
    ChildrenResult* _childrenResult;
    const int       _childIndex;

  public:
    LayoutImageBuilderChildListener(ChildrenResult* childrenResult,
                                    const int       childIndex) :
    _childrenResult(childrenResult),
    _childIndex(childIndex)
    {
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
  const int   _borderWidth;
  const Color _borderColor;
  const int   _padding;


  /*
   | margin
   |   border
   |     padding
   |       content
   */

  LayoutImageBuilder(const std::vector<IImageBuilder*>& children,
                     int margin,
                     int borderWidth,
                     const Color& borderColor,
                     int padding) :
  _children(children),
  _margin(margin),
  _borderWidth(borderWidth),
  _borderColor(borderColor),
  _padding(padding)
  {
  }

  LayoutImageBuilder(IImageBuilder* child0,
                     IImageBuilder* child1,
                     int margin,
                     int borderWidth,
                     const Color& borderColor,
                     int padding) :
  _margin(margin),
  _borderWidth(borderWidth),
  _borderColor(borderColor),
  _padding(padding)
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
