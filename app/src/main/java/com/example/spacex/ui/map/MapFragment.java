package com.example.spacex.ui.map;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spacex.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private GLSurfaceView glSurfaceView;
    private SolarSystemRenderer renderer;
    private ScaleGestureDetector scaleDetector;
    private float previousX, previousY;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = binding.progressBar;
        glSurfaceView = binding.glSurfaceView;
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new SolarSystemRenderer(requireContext());


        renderer.setOnRendererReadyListener(() -> {
            requireActivity().runOnUiThread(() -> progressBar.setVisibility(View.GONE));
        });

        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        scaleDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());

        glSurfaceView.setOnTouchListener((v, event) -> {
            scaleDetector.onTouchEvent(event);

            if (event.getPointerCount() == 1) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        previousX = event.getX();
                        previousY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (!scaleDetector.isInProgress()) {
                            float dx = event.getX() - previousX;
                            float dy = event.getY() - previousY;
                            previousX = event.getX();
                            previousY = event.getY();
                            renderer.updateUserRotation(dx, dy);
                        }
                        break;
                }
            }
            return true;
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            renderer.handlePinchZoom(detector.getScaleFactor());
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (glSurfaceView != null) glSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        if (glSurfaceView != null) glSurfaceView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
