

package com.glob3mobile.tools.mesh;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.URL;


public class G3MeshMaterial {
   //   private static final G3MeshMaterial DEFAULT_MATERIAL = new G3MeshMaterial(Color.yellow());
   private static final G3MeshMaterial DEFAULT_MATERIAL = new G3MeshMaterial(Color.fromRGBA(1, 1, 0, 0.5f));


   public static G3MeshMaterial defaultMaterial() {
      return DEFAULT_MATERIAL;
   }


   private final Color _color;
   private final URL   _textureURL;


   public G3MeshMaterial(final Color color) {
      _color = color;
      _textureURL = null;
   }


   public G3MeshMaterial(final URL textureURL) {
      _color = null;
      _textureURL = textureURL;
   }


   String getID() {
      return getID(_color) + getID(_textureURL);
   }


   private static String getID(final URL textureURL) {
      if (textureURL == null) {
         return "";
      }
      return "(U/" + textureURL.getPath() + ")";
   }


   private static String getID(final Color color) {
      if (color == null) {
         return "";
      }
      return "(C/" + color._red + "/" + color._green + "/" + color._blue + "/" + color._alpha + ")";
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


   public JSONObject toG3MeshJSON() {
      validate();

      final JSONObject result = new JSONObject();

      result.put("id", getID());
      if (_color != null) {
         result.put("color", toJSON(_color));
      }
      if ((_textureURL != null) && !_textureURL.isNull()) {
         result.put("textureURL", _textureURL.getPath());
      }

      return result;
   }


   private static JSONArray toJSON(final Color color) {
      final JSONArray result = new JSONArray();
      result.add(color._red);
      result.add(color._green);
      result.add(color._blue);
      result.add(color._alpha);
      return result;
   }


}
