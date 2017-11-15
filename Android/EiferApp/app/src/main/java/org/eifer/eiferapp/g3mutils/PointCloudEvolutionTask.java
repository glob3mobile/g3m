package org.eifer.eiferapp.g3mutils;

import org.eifer.eiferapp.GlobeFragment;
import org.glob3.mobile.generated.BuildingDataParser;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.ColorLegend;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.PointCloudMesh;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

import java.util.ArrayList;

/**
 * Created by chano on 13/11/17.
 */

public class PointCloudEvolutionTask extends GTask {

    private PointCloudMesh _pcMesh;
    private float _delta;
    private int _step;
    private GlobeFragment gf;
    private ColorLegend _colorLegend;
    private boolean _using0Color;

    public PointCloudEvolutionTask(GlobeFragment globeFragment){
        _pcMesh = null;
        _delta = 0.0f;
        _step = 0;
        gf = globeFragment;
        _using0Color = false;

        ArrayList<ColorLegend.ColorAndValue> legend = new ArrayList<>();
        legend.add(new ColorLegend.ColorAndValue(Color.black(), 0)); //Min
        //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(51, 31, 0, 255), 6)); //Percentile 10 (without 0's)
        legend.add(new ColorLegend.ColorAndValue(Color.fromRGBA255(128, 77, 0, 255), 21)); //Percentile 25 (without 0's)
        legend.add(new ColorLegend.ColorAndValue(Color.fromRGBA255(255, 153, 0, 255), 75)); //Mean (without 0's)
        legend.add(new ColorLegend.ColorAndValue(Color.fromRGBA255(255, 204, 128, 255), 100)); //Percentile 75 (without 0's)
        legend.add(new ColorLegend.ColorAndValue(Color.white(), 806.0)); //Max
        _colorLegend = new ColorLegend(legend);
    }

    @Override
    public void run(final G3MContext context){
        _step++;
        IDownloader downloader = gf.widget.getG3MContext().getDownloader();
        String vertices = "file:///Solar Radiation/buildings_imp_table_0/vertices.csv";
        if (_pcMesh == null) {
            downloader.requestBuffer(new URL(vertices, false), 1000, TimeInterval.fromDays(30), true, new IBufferDownloadListener() {
                @Override
                public void onDownload(URL url, IByteBuffer buffer, boolean expired) {
                    String result = buffer.getAsString();

                    _pcMesh = (PointCloudMesh) BuildingDataParser.createSolarRadiationMeshFromCSV(result,
                            gf.widget.getG3MContext().getPlanet(),
                            gf.elevData,
                            _colorLegend);

                    gf.addPointCloudMesh(_pcMesh);
                    generateColors(context);
                }

                @Override
                public void onError(URL url) {
                }

                @Override
                public void onCancel(URL url) {
                }

                @Override
                public void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired) {
                }
            }, true);
        }
        else {
            generateColors(context);
        }
    }

    private void generateColors(G3MContext context){
        if (_pcMesh != null){
            IDownloader downloader = gf.widget.getG3MContext().getDownloader();
            String fileColors = "file:///Solar Radiation/buildings_imp_table_0/values_t"+ _step+".csv";
            downloader.requestBuffer(new URL(fileColors, false), 1000, TimeInterval.fromDays(30), true, new IBufferDownloadListener() {
                @Override
                public void onDownload(URL url, IByteBuffer buffer, boolean expired) {
                    try {
                        String result = buffer.getAsString();

                        IFloatBuffer colors = BuildingDataParser.createColorsForSolarRadiationMeshFromCSV(result, _colorLegend);
                        _pcMesh.changeToColors(colors);
                        _using0Color = false;
                    }
                    catch (Exception E){}

                }

                @Override
                public void onError(URL url) {
                    try {
                        if (!_using0Color) {
                            IFloatBuffer colors = BuildingDataParser.create0ColorsForSolarRadiationMeshFromCSV(_colorLegend, (int) _pcMesh.getVertexCount());
                            _pcMesh.changeToColors(colors);
                            _using0Color = true;
                        }
                    }
                    catch (Exception E){}
                }

                @Override
                public void onCancel(URL url) {
                }

                @Override
                public void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired) {
                }
            }, true);



            int hour = _step;

            //Label
            String s = "Day " + context.getStringUtils().toString(hour / 24) + " " +
                    context.getStringUtils().toString(hour % 24) + ":00";

            gf.setPointCloudBar(true,s);
        }
    }

    @Override
    public void dispose(){
         _colorLegend = null;
        gf.removePointCloudMesh();
    }
}