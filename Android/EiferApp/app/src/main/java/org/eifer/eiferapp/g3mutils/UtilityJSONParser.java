package org.eifer.eiferapp.g3mutils;

import android.util.Log;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.JSONParser_Android;

/**
 * Created by chano on 19/6/18.
 */

public class UtilityJSONParser {

    private static IThreadUtils _tUtils = null;
    private static Planet _planet = null;
    private static IDownloader _downloader = null;
    private static ElevationData _elevationData = null;
    private static MeshRenderer _mr = null;
    private static double _hOffset;

    public static void initialize(G3MContext context, ElevationData elevationData, MeshRenderer mr, double heightOffset)
    {
        _planet = context.getPlanet();
        _downloader = context.getDownloader();
        _elevationData = elevationData;
        _mr = mr;
        _hOffset = heightOffset;
        _tUtils = context.getThreadUtils();
    }

    public static void parseFromURL(URL url)
    {

        if (_downloader == null)
        {
            throw new RuntimeException("UtilityParser not initialized");
        }

        _downloader.requestBuffer(url, DownloadPriority.HIGHEST, TimeInterval.fromHours(1), true, new IBufferDownloadListener() {
            @Override
            public void onDownload(URL url, IByteBuffer buffer, boolean expired) {
                JSONArray array = JSONParser_Android.instance().parse(buffer).asArray();
                array = correctHeightIfNecessary(array);
                PipesModel.parseComplexContent(array,_planet,_mr, _elevationData, _hOffset);
            }

            @Override
            public void onError(URL url) {}

            @Override
            public void onCancel(URL url) {}

            @Override
            public void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired) {}
        }, true);
    }

    private static JSONArray correctHeightIfNecessary(JSONArray a){
        if (_elevationData == null)
            return a;
        Sector s = _elevationData.getSector();

        for (int i=0;i<a.size();i++){ //Objetos
            for (int j=0;j<a.getAsObject(i).getAsArray("covers").size();j++) { //Covers
                for (int k=0;k<a.getAsObject(i).getAsArray("covers").getAsArray(j).size();k++){ //Puntos
                    double lat = a.getAsObject(i).getAsArray("covers").getAsArray(j).getAsArray(k).getAsNumber(1).value();
                    double lon = a.getAsObject(i).getAsArray("covers").getAsArray(j).getAsArray(k).getAsNumber(0).value();
                    double hgt = a.getAsObject(i).getAsArray("covers").getAsArray(j).getAsArray(k).getAsNumber(2).value();
                    if (s.contains(Angle.fromDegrees(lat),Angle.fromDegrees(lon))){
                       hgt = hgt + _elevationData.getElevationAt(Angle.fromDegrees(lat),Angle.fromDegrees(lon));
                       a.getAsObject(i).getAsArray("covers").getAsArray(j).getAsArray(k).getAsNumber(2).updateValue(hgt);
                    }

                }
            }
            for (int j=0;j<a.getAsObject(i).getAsArray("ditches").size();j++) { //Covers
                for (int k=0;k<a.getAsObject(i).getAsArray("ditches").getAsArray(j).size();k++){ //Puntos
                    double lat = a.getAsObject(i).getAsArray("ditches").getAsArray(j).getAsArray(k).getAsNumber(1).value();
                    double lon = a.getAsObject(i).getAsArray("ditches").getAsArray(j).getAsArray(k).getAsNumber(0).value();
                    double hgt = a.getAsObject(i).getAsArray("ditches").getAsArray(j).getAsArray(k).getAsNumber(2).value();
                    if (s.contains(Angle.fromDegrees(lat),Angle.fromDegrees(lon))){
                        hgt = hgt + _elevationData.getElevationAt(Angle.fromDegrees(lat),Angle.fromDegrees(lon));
                        a.getAsObject(i).getAsArray("ditches").getAsArray(j).getAsArray(k).getAsNumber(2).updateValue(hgt);
                    }
                }
            }

            for (int j=0;j<a.getAsObject(i).getAsArray("line").size();j++) { //Covers
                double lat = a.getAsObject(i).getAsArray("line").getAsArray(j).getAsNumber(1).value();
                double lon = a.getAsObject(i).getAsArray("line").getAsArray(j).getAsNumber(0).value();
                double hgt = a.getAsObject(i).getAsArray("line").getAsArray(j).getAsNumber(2).value();
                if (s.contains(Angle.fromDegrees(lat),Angle.fromDegrees(lon))){
                    hgt = hgt + _elevationData.getElevationAt(Angle.fromDegrees(lat),Angle.fromDegrees(lon));
                    a.getAsObject(i).getAsArray("line").getAsArray(j).getAsNumber(2).updateValue(hgt);
                }
            }

            double lat = a.getAsObject(i).getAsArray("startPoint").getAsNumber(1).value();
            double lon = a.getAsObject(i).getAsArray("startPoint").getAsNumber(0).value();
            double hgt = a.getAsObject(i).getAsArray("startPoint").getAsNumber(2).value();
            if (s.contains(Angle.fromDegrees(lat),Angle.fromDegrees(lon))){
                hgt = hgt + _elevationData.getElevationAt(Angle.fromDegrees(lat),Angle.fromDegrees(lon));
                a.getAsObject(i).getAsArray("startPoint").getAsNumber(2).updateValue(hgt);
            }
            lat = a.getAsObject(i).getAsArray("endPoint").getAsNumber(1).value();
            lon = a.getAsObject(i).getAsArray("endPoint").getAsNumber(0).value();
            hgt = a.getAsObject(i).getAsArray("endPoint").getAsNumber(2).value();
            if (s.contains(Angle.fromDegrees(lat),Angle.fromDegrees(lon))){
                hgt = hgt + _elevationData.getElevationAt(Angle.fromDegrees(lat),Angle.fromDegrees(lon));
                a.getAsObject(i).getAsArray("endPoint").getAsNumber(2).updateValue(hgt);
            }
        }
        return a;
    }
}
