package com.example.sargis_kh.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sargis_kh.model.Company;
import com.example.sargis_kh.model.Vector2;
import com.example.sargis_kh.studioone.R;

import java.util.ArrayList;

public class CameraFragment extends Fragment {

    private static final String CAMERA_FRAGMENT_LOG = "CAMERA_FRAGMENT_LOG";

    private ArrayList<Company> companies;
    private ArrayList<ImageView> imageViewList;
    private RelativeLayout relativeLayout;

    View rootView;
    SurfaceView cameraPreview = null;
    SurfaceHolder cameraPreviewHolder = null;
    Camera camera = null;
    boolean inPreview = false;
    boolean cameraConfigured = false;
    Integer currentCamera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        cameraPreview = (SurfaceView) rootView.findViewById(R.id.cameraView);
        relativeLayout = (RelativeLayout)rootView.findViewById(R.id.relative_layout_camera_fragment);

        if (imageViewList != null) {
            for (int i = 0; i < imageViewList.size(); i ++) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
                imageViewList.get(i).setLayoutParams(params);
                relativeLayout.addView(imageViewList.get(i));
            }
        }

        cameraPreviewHolder = cameraPreview.getHolder();
        cameraPreviewHolder.addCallback(surfaceCallback);
        cameraPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(CAMERA_FRAGMENT_LOG, "Camera onResume");
        camera = getCamera("back");
        currentCamera = 1;
        startPreview();
    }

    @Override
    public void onPause() {
//        Log.e(CAMERA_FRAGMENT_LOG, "Camera onPause");
        if (camera != null) {
            if (inPreview) {
                camera.stopPreview();
            }
            camera.release();
            camera = null;
        }
        inPreview = false;

        super.onPause();
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return (result);
    }

    private void initPreview(int width, int height) {
        if (camera != null && cameraPreviewHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(cameraPreviewHolder);
            } catch (Throwable t) {
                Log.e(CAMERA_FRAGMENT_LOG, "Exception in setPreviewDisplay()", t);
            }

            if (!cameraConfigured) {
                Camera.Parameters parameters = camera.getParameters();
                Log.v(CAMERA_FRAGMENT_LOG, parameters.toString());
                Camera.Size size = getBestPreviewSize(width, height,
                        parameters);

                if (size != null) {
                    Log.v(CAMERA_FRAGMENT_LOG, ""+cameraPreview.getMeasuredHeight());
                    Log.v(CAMERA_FRAGMENT_LOG, size.width + " " + size.height);
                    parameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(parameters);
                    cameraConfigured = true;
                }
            }
        }
    }

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            camera.startPreview();
            inPreview = true;
            camera.setDisplayOrientation(90);
        }
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };

    private Camera getCamera(String getCamera) {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if ((cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) && (getCamera == "front")) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(CAMERA_FRAGMENT_LOG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
            else if ((cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) && (getCamera == "back")) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(CAMERA_FRAGMENT_LOG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    public void setCompanies(Context context, ArrayList<Company> companies) {
        this.companies = companies;
        if (imageViewList == null) {
            imageViewList = new ArrayList<ImageView>();
        }

        Resources resources = context.getResources();
        for (int i = 0; i < companies.size(); i ++) {
            final int resourceId = resources.getIdentifier(companies.get(i).getLogo(), "drawable", context.getPackageName());
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(resourceId);
            imageView.setAdjustViewBounds(true);
            imageView.setVisibility(View.INVISIBLE);
            imageViewList.add(imageView);
        }
    }

    public void setChangeCompanyCoordinate(Company company) {
        if (companies != null) {
            companies.get((int)company.getID()).setLatitude(company.getLatitude());
            companies.get((int)company.getID()).setLongitude(company.getLongitude());
        }
    }

    public void showLogoAtPosition(Company company, Vector2 logoPosition) {
        if (imageViewList.get((int)company.getID()).getVisibility() != View.VISIBLE) {
            imageViewList.get((int)company.getID()).setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imageViewList.get((int)company.getID()).getLayoutParams();
        params.leftMargin = (int)logoPosition.x();
        params.topMargin = (int)logoPosition.y();
        imageViewList.get((int)company.getID()) .setLayoutParams(params);
    }

    public void hideLogo(Company company) {
        if (imageViewList.get((int)company.getID()).getVisibility() == View.VISIBLE) {
            imageViewList.get((int)company.getID()).setVisibility(View.INVISIBLE);
        }
    }

    public void hideAllLogos() {
        for (int i = 0; i < imageViewList.size(); i++) {
            if (imageViewList.get((int)companies.get(i).getID()).getVisibility() == View.VISIBLE) {
                imageViewList.get((int)companies.get(i).getID()).setVisibility(View.INVISIBLE);
            }
        }
    }
}