package com.example.spacex.ui.map;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private MapViewModel viewModel;
    private GLSurfaceView glSurfaceView;
    private ScaleGestureDetector scaleDetector;
    private float previousX, previousY;
    private SolarSystemRenderer renderer;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        binding = FragmentMapBinding.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MapViewModel.class);


        glSurfaceView = binding.getRoot().findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);

        renderer = new SolarSystemRenderer(requireContext());
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
                        String selectedPlanet = renderer.checkPlanetSelection(event.getX(), event.getY());
                        if (selectedPlanet != null) {
                            showPlanetInfo(selectedPlanet);
                        }
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
            float scaleFactor = detector.getScaleFactor();

            return true;
        }
    }

    private void showPlanetInfo(String planetName) {
        String info = viewModel.getPlanetInfo(planetName);
        binding.planetInfo.setText(info);
        binding.planetInfo.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (binding != null) {
                binding.planetInfo.setVisibility(View.GONE);
            }
        }, 5000);
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
        glSurfaceView = null;
        binding = null;
        super.onDestroyView();
    }
}
