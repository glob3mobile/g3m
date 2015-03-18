package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;

public class PozosLaPalmaParser {
	  ArrayList<Marker> markers;
	   
	   PozosLaPalmaParser(IDownloader downloader, final MarksRenderer mRenderer, String jsonURL){
		   final IBufferDownloadListener myListener = new IBufferDownloadListener() {

			      @Override
			      public void onDownload(final URL url, final IByteBuffer buffer, boolean expired) {
			        final String response = buffer.getAsString();
			        JavaScriptObject data = string2JSON(response);
			        parseMarkerData(data);
			        drawMarkers(mRenderer);
			      }

			      @Override
			      public void onError(final URL url) {}

			      @Override
			      public void onCancel(final URL url) {}

			      @Override
			      public void onCanceledDownload(final URL url, final IByteBuffer data, boolean expired) {}
			};

			downloader.requestBuffer(
			      new URL(jsonURL, false),
			      (long)0,
			      TimeInterval.fromHours(1.0),
			      false,                                                             
			      myListener,
			      false);

	   }
	   
	   private native JavaScriptObject string2JSON(String jsonRead)/*-{
	   		var object = JSON.parse(jsonRead); 
	   		return object;
	   }-*/;
	   
	   private void parseMarkerData(JavaScriptObject jsonMarkers){
		   
		   markers = new ArrayList<Marker>();
		   try
		   {
			   JSONObject json = new JSONObject(jsonMarkers);
			   JSONArray features = json.get("features").isArray();
			   for (int i=0; i<features.size();i++){
				   JSONObject feat = features.get(i).isObject();
				   String ptName = feat.get("properties").isObject().get("Nombre_de").isString().toString();
				   String peligro = feat.get("properties").isObject().get("Peligrosid").isString().toString();
				   JSONArray wpData = feat.get("geometry").isObject().get("coordinates").isArray();
				   double lon = wpData.get(0).isNumber().doubleValue();
				   double lat = wpData.get(1).isNumber().doubleValue();
						
				   markers.add(new Marker(lat,lon,ptName,peligro));
			   }
		   } 
		   catch (JSONException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
	   }
	   
	   private void drawMarkers(MarksRenderer renderer){
		   for (int i=0; i<markers.size();i++) renderer.addMark(markers.get(i).createMarkFromMarker());
	   }
	   
	   
	   public class Marker {
			
			Geodetic3D point;
			String name;
			String icon = "images/pozo.png";
			int peligro;
			
			Marker(double lat, double lon, String n, String peligro){
				name = n;
				point = new Geodetic3D(Angle.fromDegrees(lat),Angle.fromDegrees(lon),0);
				
				// Fijando valor de peligrosidad (Ad-Hoc)
				if (peligro.contains("ALTO")) this.peligro = 2;
				else if (peligro.contains("MUY BAJO")) this.peligro = 0;
				else if (peligro.contains("MEDIO") || peligro.contains("BAJO")) this.peligro = 1;
				else this.peligro = 0;
				
			}
			
			public Mark createMarkFromMarker(){
				
				// Fijando alturas m�ximas para las marcas seg�n peligrosidad.
				
				double highness = 3000;
				if (this.peligro == 2) highness = 4.5e+06;
				else if (this.peligro == 1) highness = 10000;
				
				return new Mark(name,
						new URL(this.icon, false),
						this.point,
						AltitudeMode.RELATIVE_TO_GROUND,
						highness,
						true,
						12, //
						Color.newFromRGBA(1,1,1,1),
						Color.newFromRGBA(0,0,0,1),
						2,
						null,
						true,
						null,
						true
						);
			}
	   }
}
