package org.glob3.mobile.generated;
/** an outfit's image and texture: the mark's own fields hold the outfit on screen, these the others */
public class MarkOutfitImage
{
  public IImageFactory _imageFactory; // until the image is asked for
  public MarkImageFactoryListener _listener; // while the image is being created
  public boolean _solved;
  public IImage             _image;
  public TextureIDReference _textureID;
  public MarkAnchor         _anchor;
  public String _imageID;
  public float _width;
  public float _height;
  public boolean _hasAnchor; // the outfit's anchor, computed once its image exists
  public float _anchorU;
  public float _anchorV;

  public MarkOutfitImage(IImageFactory imageFactory, MarkAnchor anchor)
  {
     _imageFactory = imageFactory;
     _listener = null;
     _solved = false;
     _image = null;
     _textureID = null;
     _anchor = anchor;
     _imageID = "";
     _width = 0F;
     _height = 0F;
     _hasAnchor = false;
     _anchorU = 0.5f;
     _anchorV = 0.5f;
  }

  public void dispose()
  {
    if (_listener != null)
    {
      _listener.forgetMark();
    }
    if (_imageFactory != null)
       _imageFactory.dispose();
    _image = null;
    if (_textureID != null)
    {
      _textureID.dispose();
      _textureID = null;
    }
  }
}