package org.eifer.eiferapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/*import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;*/

import org.glob3.mobile.generated.Geodetic3D;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Dialog dialog, graphDialog;
    ProgressDialog startingDialog;
    private Camera mCamera;
    private CameraPreview mPreview;
    private MenuItem dialogItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.eiferTitleColor));
        setSupportActionBar(myToolbar);

        mPreview = null;
    }

    public void setZ(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.bringToFront();
    }

    @Override
    protected void onPause(){
        super.onPause();
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        if (fragment.getMapMode() == 3){
            releaseCamera();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Create an instance of Camera
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        if (fragment.getMapMode() == 3){
            setCameraPreview();
        }
    }

    public void setPointCloudBar(final boolean active,final String content){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout bar = (LinearLayout) findViewById(R.id.pointCloudBarLayout);
                if (active && (bar.getVisibility() != View.VISIBLE)){
                    bar.setVisibility(View.VISIBLE);
                }
                if (active){
                    TextView t = (TextView) findViewById(R.id.pointCloudBarText);
                    t.setText(content);
                }
                else {
                    bar.setVisibility(View.GONE);
                }
            }
        });

    }

    public void setPositionFixerBar(final boolean active){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout bar = (LinearLayout) findViewById(R.id.positionFixerLayout);
                if (active && (bar.getVisibility() != View.VISIBLE)){
                    bar.setVisibility(View.VISIBLE);
                }
                if (active){
                    TextView t = (TextView) findViewById(R.id.positionFixerText);
                    t.setText(generateMessage());
                }
                else {
                    bar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void stopPositionFixerAction(View view){
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);

        fragment.stopPositionFixer();
        setPositionFixerBar(false);

        if (dialogItem != null)
            dialogItem.setEnabled(true);
        openDialog();
    }

    public void openPositionFixer(View view){

        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);

        if (fragment.getMapMode() > 0){
            Toast.makeText(this,getString(R.string.fixer_unable),Toast.LENGTH_SHORT).show();
        }
        else {
            dialog.dismiss();
            dialog = null;
            if (dialogItem != null)
                dialogItem.setEnabled(false);
            setPositionFixerBar(true);
            fragment.activePositionFixer();
        }
    }

    private String generateMessage(){
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);


        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(8);
        df.setMaximumFractionDigits(8);
        Geodetic3D mg = fragment.positionMark.getPosition();
        String message = "";
        if (fragment.heading != null) {
            try {
                message = "Lat: " + df.format(mg._latitude._degrees);
                message = message + ", lon: " + df.format(mg._longitude._degrees);
                df.setMinimumFractionDigits(2);
                df.setMaximumFractionDigits(2);
                message = message + ", hgt: " + df.format(mg._height);
                message = message + ", heading: " + df.format(fragment.heading._degrees);
            } catch (Exception E) {
            }
        }
        else {
            message = getString(R.string.undefined);
        }
        return message;
    }



    public void updatePositionFixer(final String string){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout bar = (LinearLayout) findViewById(R.id.positionFixerLayout);
                TextView t = (TextView) findViewById(R.id.positionFixerText);
                t.setText(string);
            }
        });
    }

    public void stopPointCloudAnimationAction(View view){
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        fragment.removeSolarRadiationPointCloud();
    }

    public void setCameraPreview(){
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        if (mPreview == null) {
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.theSurfaceView);
            preview.addView(mPreview);
        }
        else {
            if (mCamera != null)
                mPreview.setCamera(mCamera);
            else
                Log.e("___ ERRORS ___", "Camera not released! ");
        }
        mPreview.setZOrderMediaOverlay(false);
        mPreview.setZOrderOnTop(false);
        if (Build.VERSION.SDK_INT >= 21)
            mPreview.setZ(0);
    }

    public float getHorizontalFoV(){
        if (mPreview != null)
            return mPreview.getHorizontalFOV();
        else
            return Float.NaN;
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void unsetCameraPreviewIfNeeded(){
        releaseCamera();
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
        FrameLayout preview = (FrameLayout) findViewById(R.id.theSurfaceView);
        preview.removeView(mPreview);
        mPreview = null;
        /*if (mPreview != null){
            mPreview.setCamera(mCamera);
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.menu_settings:
                dialogItem = item;
                openDialog();

                return true;
            default:
                return true;
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        fragment.setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        fragment.onPostCreate();
    }

    public void openGraphDialog(){
        //TODO Reimport OpenGraph. Maven failures somewhere ...

        /*graphDialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.sensor_dialog, null);

        GraphView graph = (GraphView) vi.findViewById(R.id.SensorGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        String message = "Fake sensor \n\nPosition: Schlossgarten, Karlsruhe, Germany \nModel: Fake Sensor 101\nManufacturer: Fake Industry" +
                "\nMeasured property: Temperature \nSupported protocol: http \nLast value: 6 (2017/11/12 12:16:00)\nLast five values:\n ";
        TextView textView = (TextView) vi.findViewById(R.id.SensorText);
        textView.setText(message);

        graphDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        graphDialog.setContentView(vi);
        graphDialog.setCancelable(true);
        graphDialog.show();*/
    }

    public void closeAction(View view){
        graphDialog.dismiss();
        graphDialog = null;
    }

    private void openDialog(){
// Inflar la vista del dialog
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.menu_dialog, null);

        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        boolean pipes = fragment.arePipesEnabled();
        boolean buildings = fragment.areBuildingsEnabled();
        Switch bSwitch = (Switch) vi.findViewById(R.id.switchBuildings);
        Switch pSwitch = (Switch) vi.findViewById(R.id.switchPipes);
        Switch lSwitch = (Switch) vi.findViewById(R.id.switchLocation);
        Switch cSwitch = (Switch) vi.findViewById(R.id.switchCorrection);
        Spinner mSpinner = vi.findViewById(R.id.spinnerMethod);
        Spinner bSpinner = vi.findViewById(R.id.spinnerColors);
        SeekBar alphaSeekbar = vi.findViewById(R.id.alphaMethodBar);
        SeekBar ditchSeekbar = vi.findViewById(R.id.ditchMethodBar);
        SeekBar modeSeekbar = vi.findViewById(R.id.modeSeekbar);
        TextView position = vi.findViewById(R.id.textView14);

        int alphaValue = (fragment.isHole()) ? 1:0;
        int ditchValue = (fragment.isDitch()) ? 1:0;

        modeSeekbar.setProgress(fragment.getMapMode());
        bSpinner.setSelection(fragment.getBuildingColor());
        //bSpinner.setEnabled(false);
        bSwitch.setChecked(buildings);
        pSwitch.setChecked(pipes);
        lSwitch.setChecked(fragment.getUsesGPS());
        cSwitch.setChecked(fragment.getCorrection());
        mSpinner.setSelection(fragment.getAlphaMethod());
        alphaSeekbar.setProgress(alphaValue);
        ditchSeekbar.setProgress(ditchValue);
        position.setText(generateMessage());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(vi);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void applySettings(View view){
        Switch bSwitch = dialog.findViewById(R.id.switchBuildings);
        Switch pSwitch = dialog.findViewById(R.id.switchPipes);
        Switch lSwitch = dialog.findViewById(R.id.switchLocation);
        Switch cSwitch = dialog.findViewById(R.id.switchCorrection);
        Spinner mSpinner = dialog.findViewById(R.id.spinnerMethod);
        Spinner bSpinner = dialog.findViewById(R.id.spinnerColors);
        SeekBar alphaSeekbar = dialog.findViewById(R.id.alphaMethodBar);
        SeekBar ditchSeekbar = dialog.findViewById(R.id.ditchMethodBar);
        SeekBar modeSeekbar = dialog.findViewById(R.id.modeSeekbar);

        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        fragment.setUsesGPS(lSwitch.isChecked());
        fragment.setMapMode(modeSeekbar.getProgress());
        fragment.setBuildingsEnabled(bSwitch.isChecked());
        fragment.setPipesEnabled(pSwitch.isChecked());
        fragment.setAlphaMethod(mSpinner.getSelectedItemPosition());
        fragment.setBuildingColor(bSpinner.getSelectedItemPosition());
        fragment.setCorrection(cSwitch.isChecked());
        boolean isHole = (alphaSeekbar.getProgress() == 1);
        boolean isDitch = (ditchSeekbar.getProgress() == 1);
        fragment.setHole(isHole);
        fragment.setDitch(isDitch);

        dialog.dismiss();
        dialog = null;
    }

    public void openProgressDialog(final int models){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (startingDialog == null) {
                    startingDialog = new ProgressDialog(MainActivity.this);
                    startingDialog.setTitle(getString(R.string.startTitle));
                    startingDialog.setMessage(getString(R.string.startMessage));
                    startingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    startingDialog.setMax(models);
                    startingDialog.setProgress(0);
                    startingDialog.setCancelable(false);
                    startingDialog.show();
                }
            }
        });

    }

    public void updateDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int progress = startingDialog.getProgress();
                startingDialog.setProgress(progress + 1);
                if (progress+1 == startingDialog.getMax())
                    startingDialog.dismiss();
            }
        });

    }


}
