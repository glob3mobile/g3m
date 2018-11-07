

package com.glob3mobile.tools.mesh;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.URL;


public class G3MeshMaterial {
   private static final G3MeshMaterial DEFAULT_MATERIAL = new G3MeshMaterial(Color.fromRGBA(1, 1, 0, 1), true);


   public static G3MeshMaterial defaultMaterial() {
      return DEFAULT_MATERIAL;
   }


   public final Color   _color;
   public final URL     _textureURL;
   public final boolean _depthTest;


   public G3MeshMaterial(final Color color,
                         final boolean depthTest) {
      _color = color;
      _textureURL = null;
      _depthTest = depthTest;
   }


   public G3MeshMaterial(final URL textureURL,
                         final boolean depthTest) {
      _color = null;
      _textureURL = textureURL;
      _depthTest = depthTest;
   }


   String getID() {
      return getID(_color) + getID(_textureURL);
   }


   private static String getID(final URL textureURL) {
      if (textureURL == null) {
         return "";
      }
      return textureURL._path;
   }


   private static String getID(final Color color) {
      if (color == null) {
         return "";
      }
      final String alpha = (color._alpha == 1) ? "" : "_" + toString(color._alpha);

      return toString(color._red) + "_" + toString(color._green) + "_" + toString(color._blue) + alpha;
   }


   private static final DecimalFormat df = new DecimalFormat("#.##");


   private static String toString(final float f) {
      return df.format(f);
   }


   public boolean isEquals(final G3MeshMaterial that) {
      if (this == that) {
         return true;
      }
      if (that == null) {
         return false;
      }
      if (getClass() != that.getClass()) {
         return false;
      }
      if (_color == null) {
         if (that._color != null) {
            return false;
         }
      }
      else if (!_color.isEquals(that._color)) {
         return false;
      }
      if (_textureURL == null) {
         if (that._textureURL != null) {
            return false;
         }
      }
      else if (!_textureURL.isEquals(that._textureURL)) {
         return false;
      }
      return true;
   }


   void validate() {
      if ((_color == null) && ((_textureURL == null) || _textureURL.isNull())) {
         throw new RuntimeException("Material with no color neither texture");
      }
   }


   public Map<String, Object> toJSON() {
      validate();

      final Map<String, Object> result = new HashMap<>();

      result.put("id", getID());
      if (_color != null) {
         result.put("color", toJSON(_color));
      }
      if ((_textureURL != null) && !_textureURL.isNull()) {
         result.put("textureURL", _textureURL._path);
      }

      return result;
   }


   private static List<Float> toJSON(final Color color) {
      final List<Float> result = new ArrayList<>();
      result.add(color._red);
      result.add(color._green);
      result.add(color._blue);
      result.add(color._alpha);
      return result;
   }


}
