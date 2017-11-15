package org.eifer.eiferapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    Dialog dialog;
    ProgressDialog startingDialog;
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.eiferTitleColor));
        setSupportActionBar(myToolbar);

        mPreview = null;
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
            mPreview.setCamera(mCamera);
        }
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
        /*FrameLayout preview = (FrameLayout) findViewById(R.id.theSurfaceView);
        preview.removeView(mPreview);
        mPreview = null;*/
        if (mPreview != null){
            mPreview.setCamera(mCamera);
        }
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
        Spinner mSpinner = vi.findViewById(R.id.spinnerMethod);
        Spinner bSpinner = vi.findViewById(R.id.spinnerColors);
        SeekBar alphaSeekbar = vi.findViewById(R.id.alphaMethodBar);
        SeekBar modeSeekbar = vi.findViewById(R.id.modeSeekbar);

        int alphaValue = (fragment.isHole()) ? 1:0;

        modeSeekbar.setProgress(fragment.getMapMode());
        bSpinner.setSelection(fragment.getBuildingColor());
        //bSpinner.setEnabled(false);
        bSwitch.setChecked(buildings);
        pSwitch.setChecked(pipes);
        mSpinner.setSelection(fragment.getAlphaMethod());
        alphaSeekbar.setProgress(alphaValue);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(vi);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void applySettings(View view){
        Switch bSwitch = dialog.findViewById(R.id.switchBuildings);
        Switch pSwitch = dialog.findViewById(R.id.switchPipes);
        Spinner mSpinner = dialog.findViewById(R.id.spinnerMethod);
        Spinner bSpinner = dialog.findViewById(R.id.spinnerColors);
        SeekBar alphaSeekbar = dialog.findViewById(R.id.alphaMethodBar);
        SeekBar modeSeekbar = dialog.findViewById(R.id.modeSeekbar);

        FragmentManager fmanager = this.getSupportFragmentManager();
        GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
        fragment.setMapMode(modeSeekbar.getProgress());
        fragment.setBuildingsEnabled(bSwitch.isChecked());
        fragment.setPipesEnabled(pSwitch.isChecked());
        fragment.setAlphaMethod(mSpinner.getSelectedItemPosition());
        fragment.setBuildingColor(bSpinner.getSelectedItemPosition());
        boolean isHole = (alphaSeekbar.getProgress() == 1) ? true : false;
        fragment.setHole(isHole);

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
