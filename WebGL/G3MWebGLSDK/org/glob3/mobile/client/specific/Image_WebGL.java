

package org.glob3.mobile.client.specific;

import org.glob3.mobile.client.generated.IImage;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Image;


public class Image_WebGL
         extends
            IImage {

   private JavaScriptObject _imgObject;
   
   private boolean _arrived = false;


   public Image_WebGL() {
   }
   
   public JavaScriptObject getImage(){
	   if (_arrived){
		   return jsGetImage(_imgObject);
	   } else{
		   return null;
	   }
   }


   
   public void loadFromURL(String url)
   {
	   _imgObject = jsCreateImgObject(this, url);
   }
   
   public boolean isLoadedFromURL(){ 
	   return _arrived;
   }
   
   private void onArrive()
   {
	   _arrived = true;
   }
   
   private void onError()
   {
	   _arrived = false;
   }
   
   private native JavaScriptObject jsGetImage(JavaScriptObject imgObject) /*-{ 
		return imgObject.image;
	}-*/;
   
   
   private native JavaScriptObject jsCreateImgObject(Image_WebGL instance, String url) /*-{                     
	   imgObject = new Object();
	   imgObject.image = new Image();
	   imgObject.image.onload = function() {
	   	debugger;
	   	$entry(instance.@org.glob3.mobile.client.specific.Image_WebGL::onArrive()());
	   }
	   imgObject.image.onabort = function() {
	   	debugger;
	   	$entry(instance.@org.glob3.mobile.client.specific.Image_WebGL::onError()());
	   }
	   imgObject.image.onerror = function() {
	   	debugger;
	   	$entry(instance.@org.glob3.mobile.client.specific.Image_WebGL::onError()());
	   }
	   
	   imgObject.image.src = url;
	   
	   return imgObject;
   }-*/;
   
}
