package org.glob3.mobile.generated;
//
//  LayoutImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//

//
//  LayoutImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/11/15.
//
//




//class ImageBackground;


public abstract class LayoutImageBuilder extends AbstractImageBuilder
{


  protected static class ChildResult
  {
    public final IImage _image;
    public final String _imageName;
    public final String _error;

    public ChildResult(IImage image, String imageName)
    {
       _image = image;
       _imageName = imageName;
       _error = "";
    }

    public ChildResult(String error)
    {
       _image = null;
       _imageName = "";
       _error = error;
    }

    public void dispose()
    {
      if (_image != null)
         _image.dispose();
    }
  }


  protected static class ChildrenResult extends RCObject
  {
    private LayoutImageBuilder _layoutImageBuilder;
    private final G3MContext _context;
    private IImageBuilderListener _listener;
    private boolean _deleteListener;

    private int _childrenResultPendingCounter;

    public void dispose()
    {
      super.dispose();
    }

    public java.util.ArrayList<ChildResult> _childrenResult = new java.util.ArrayList<ChildResult>();

    public ChildrenResult(LayoutImageBuilder layoutImageBuilder, int childrenSize, G3MContext context, IImageBuilderListener listener, boolean deleteListener)
    {
       _layoutImageBuilder = layoutImageBuilder;
       _context = context;
       _listener = listener;
       _deleteListener = deleteListener;
      _childrenResultPendingCounter = childrenSize;
      for (int i = 0; i < childrenSize; i++)
      {
        _childrenResult.add(null); // make space for the result
      }
    }

    public final void childImageCreated(IImage image, String imageName, int childIndex)
    {
      if (_childrenResult.get(childIndex) != null)
      {
        throw new RuntimeException("Logic error");
      }
    
      _childrenResult.set(childIndex, new ChildResult(image, imageName));
      if (--_childrenResultPendingCounter == 0)
      {
        _layoutImageBuilder.doLayout(_context, _listener, _deleteListener, _childrenResult);
      }
    }

    public final void childError(String error, int childIndex)
    {
      if (_childrenResult.get(childIndex) != null)
      {
        throw new RuntimeException("Logic error");
      }
    
      _childrenResult.set(childIndex, new ChildResult(error));
      if (--_childrenResultPendingCounter == 0)
      {
        _layoutImageBuilder.doLayout(_context, _listener, _deleteListener, _childrenResult);
      }
    }
  }


  protected static class LayoutImageBuilderChildListener implements IImageBuilderListener
  {
    private ChildrenResult _childrenResult;
    private final int _childIndex;

    public LayoutImageBuilderChildListener(ChildrenResult childrenResult, int childIndex)
    {
       _childrenResult = childrenResult;
       _childIndex = childIndex;
      _childrenResult._retain();
    }

    public void dispose()
    {
      _childrenResult._release();
    }

    public final void imageCreated(IImage image, String imageName)
    {
      _childrenResult.childImageCreated(image, imageName, _childIndex);
    }

    public final void onError(String error)
    {
      _childrenResult.childError(error, _childIndex);
    }
  }


  protected java.util.ArrayList<IImageBuilder> _children = new java.util.ArrayList<IImageBuilder>();

  protected final ImageBackground _background;

  /*
   | margin
   |   border
   |     padding
   |       content
   */


  protected LayoutImageBuilder(java.util.ArrayList<IImageBuilder> children, ImageBackground background)
  {
     _children = children;
     _background = (background == null) ? new NullImageBackground() : background;
  }

  protected LayoutImageBuilder(IImageBuilder child0, IImageBuilder child1, ImageBackground background)
  {
     _background = (background == null) ? new NullImageBackground() : background;
    _children.add(child0);
    _children.add(child1);
  }

  protected LayoutImageBuilder(IImageBuilder child0, ImageBackground background)
  {
     _background = (background == null) ? new NullImageBackground() : background;
    _children.add(child0);
  }

  protected abstract void doLayout(G3MContext context, IImageBuilderListener listener, boolean deleteListener, java.util.ArrayList<ChildResult> results);

  public void dispose()
  {
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      IImageBuilder child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  
    if (_background != null)
       _background.dispose();
    super.dispose();
  }


  public final boolean isMutable()
  {
    //TODO: #warning TODO: make mutable if any children is
    return false;
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    final int childrenSize = _children.size();
    if (childrenSize > 0)
    {
      ChildrenResult childrenResult = new ChildrenResult(this, childrenSize, context, listener, deleteListener);
      for (int i = 0; i < childrenSize; i++)
      {
        IImageBuilder child = _children.get(i);
  
        child.build(context, new LayoutImageBuilderChildListener(childrenResult, i), true);
      }
  
      childrenResult._release();
    }
  }

}