

package org.glob3.mobile.specific;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.Vector2I;


public final class Image_JavaDesktop
         extends
            IImage {

   private static final boolean DEBUG = false;


   private static String createCallStackString() {
      final Exception e = new Exception();
      final StringWriter wr = new StringWriter();
      final PrintWriter err = new PrintWriter(wr);
      e.printStackTrace(err);
      err.flush();
      return wr.toString() //
      .replace("org.glob3.mobile.specific.", "") //
      .replace("org.glob3.mobile.generated.", "") //
      .replace("android.opengl.", "") //
      .replace("at", "") //
      .replace("java.lang.Exception\n", "");
   }


   private static class BitmapHolder {
      private BufferedImage      _bufferedImage;
      private int                _referencesCount;

      @SuppressWarnings("unused")
      private final String       _createdAt;
      private final List<String> _retainedAt = new ArrayList<String>();
      private final List<String> _releasedAt = new ArrayList<String>();


      private BitmapHolder(final BufferedImage bufferedImage) {
         _bufferedImage = bufferedImage;
         _referencesCount = 1;
         _createdAt = DEBUG ? createCallStackString() : null;
      }


      private void _retain() {
         synchronized (this) {
            _referencesCount++;
            if (DEBUG) {
               _retainedAt.add(createCallStackString());
            }
         }
      }


      private void _release() {
         synchronized (this) {
            _referencesCount--;
            if (_referencesCount == 0) {
               if (_bufferedImage != null) {
                  _bufferedImage.flush();
                  _bufferedImage = null;
               }

            }
            if (DEBUG) {
               _releasedAt.add(createCallStackString());
            }
         }
      }


      //      @Override
      //      protected void finalize() throws Throwable {
      //         if (DEBUG) {
      //            if (_referencesCount != 0) {
      //               synchronized (ILogger.instance()) {
      //                  ILogger.instance().logError("=======");
      //                  ILogger.instance().logError("***** BitmapHolder deleted with invalid _referencesCount=" + _referencesCount);
      //
      //                  final StringBuffer msg = new StringBuffer();
      //                  msg.append("Created At:\n");
      //                  msg.append(_createdAt);
      //                  msg.append("Retained At:\n");
      //                  for (final String e : _retainedAt) {
      //                     msg.append(e);
      //                     msg.append("---\n");
      //                  }
      //                  msg.append("Released At:\n");
      //                  for (final String e : _releasedAt) {
      //                     msg.append(e);
      //                     msg.append("---\n");
      //                  }
      //                  msg.append("=======\n");
      //
      //                  ILogger.instance().logError(msg.toString());
      //               }
      //            }
      //         }
      //         super.finalize();
      //      }


   }


   final private BitmapHolder _bitmapHolder;
   private byte[]             _source;

   @SuppressWarnings("unused")
   private boolean            _bitmapHolderReleased = false;
   @SuppressWarnings("unused")
   private final String       _createdAt;


   public Image_JavaDesktop(final BufferedImage bufferedImage,
                            final byte[] source) {
      _createdAt = DEBUG ? createCallStackString() : null;

      if (bufferedImage == null) {
         throw new RuntimeException("Can't create an Image_JavaDesktop with a null bufferedImage");
      }

      _bitmapHolder = new BitmapHolder(bufferedImage);
      _source = source;
   }


   private Image_JavaDesktop(final BitmapHolder bitmapHolder,
                             final byte[] source) {
      _createdAt = DEBUG ? createCallStackString() : null;

      if (bitmapHolder == null) {
         throw new RuntimeException("Can't create an Image_JavaDesktop with a null bitmap");
      }
      bitmapHolder._retain();

      _bitmapHolder = bitmapHolder;
      _source = source;
   }


   public BufferedImage getBufferedImage() {
      return _bitmapHolder._bufferedImage;
   }


   @Override
   public int getWidth() {
      return (_bitmapHolder._bufferedImage == null) ? 0 : _bitmapHolder._bufferedImage.getWidth();
   }


   @Override
   public int getHeight() {
      return (_bitmapHolder._bufferedImage == null) ? 0 : _bitmapHolder._bufferedImage.getHeight();
   }


   @Override
   public Vector2I getExtent() {
      return new Vector2I(getWidth(), getHeight());
   }


   @Override
   public String description() {
      return "Image_JavaDesktop " + getWidth() + " x " + getHeight() + ", _image=(" + _bitmapHolder._bufferedImage.toString()
             + ")";
   }


   public byte[] getSourceBuffer() {
      return _source;
   }


   public void releaseSourceBuffer() {
      _source = null;
   }


   @Override
   public Image_JavaDesktop shallowCopy() {
      return new Image_JavaDesktop(_bitmapHolder, _source);
   }


   @Override
   public void dispose() {
      synchronized (this) {
         _bitmapHolderReleased = true;
         _bitmapHolder._release();
      }

      super.dispose();
   }


   //   @Override
   //   protected void finalize() throws Throwable {
   //      if (DEBUG) {
   //         if (!_bitmapHolderReleased) {
   //            synchronized (ILogger.instance()) {
   //               final StringBuffer msg = new StringBuffer();
   //               msg.append("************\n");
   //               msg.append("Image_JavaDesktop finalized without releasing the _bitmapHolder created at: \n");
   //               msg.append(_createdAt);
   //               msg.append("************\n");
   //               ILogger.instance().logError(msg.toString());
   //            }
   //         }
   //      }
   //      super.finalize();
   //   }


}
