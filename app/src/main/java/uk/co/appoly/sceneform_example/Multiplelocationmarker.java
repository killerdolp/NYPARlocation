package uk.co.appoly.sceneform_example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.LocationScene;
import uk.co.appoly.arcorelocation.rendering.LocationNode;
import uk.co.appoly.arcorelocation.rendering.LocationNodeRender;
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper;

/**
 * This is a simple example that shows how to create an augmented reality (AR) application using the
 * ARCore and Sceneform APIs.
 */
public class Multiplelocationmarker extends AppCompatActivity {
    private boolean installRequested;
    private boolean hasFinishedLoading = false;

    private Snackbar loadingMessageSnackbar = null;

    private ArSceneView arSceneView;

    // Renderables for this example
    private ModelRenderable andyRenderable;
    private ViewRenderable KoufuLayoutRenderable;
    private ViewRenderable FoodgleLayoutRenderable;
    private ViewRenderable TJunctionLayoutRenderable;
    private ViewRenderable FoodCentralLayoutRenderable;
    private ViewRenderable SouthCanteenLayoutRenderable;
    private ViewRenderable NorthCanteenLayoutRenderable;


    // Our ARCore-Location scene
    private LocationScene locationScene;




    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sceneform);
        arSceneView = findViewById(R.id.ar_scene_view);
        Intent intent2 = getIntent();
        // Build a renderable from a 2D View.
        //Second location marker
        CompletableFuture<ViewRenderable> layoutkoufu = ViewRenderable.builder().setView(this,R.layout.locationmarkerkoufu).build();
        CompletableFuture<ViewRenderable> layoutfoodgle = ViewRenderable.builder().setView(this,R.layout.locationmarkerfoodgle).build();
        CompletableFuture<ViewRenderable> layout_t_junction = ViewRenderable.builder().setView(this,R.layout.locationmarker_t_junction).build();
        CompletableFuture<ViewRenderable> layoutfoodcentral = ViewRenderable.builder().setView(this,R.layout.locationmarkerfoodcentral).build();
        CompletableFuture<ViewRenderable> layoutnorthcanteen = ViewRenderable.builder().setView(this,R.layout.locationmarkernorthcanteen).build();
        CompletableFuture<ViewRenderable> layoutsouthcanteen = ViewRenderable.builder().setView(this,R.layout.locationmarkersouthcanteen).build();

        // When you build a Renderable, Sceneform loads its resources in the backgroundpicture while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        CompletableFuture<ModelRenderable> andy = ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build();


        CompletableFuture.allOf(
                layoutkoufu,
                layoutfoodgle,
                layout_t_junction,
                layoutfoodcentral,
                layoutnorthcanteen,
                layoutsouthcanteen,
                andy)
                .handle(
                        (notUsed, throwable) -> {
                            // When you build a Renderable, Sceneform loads its resources in the backgroundpicture while
                            // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                            // before calling get().

                            if (throwable != null) {
                                DemoUtils.displayError(this, "Unable to load renderables", throwable);
                                return null;
                            }

                            try {
                                KoufuLayoutRenderable = layoutkoufu.get();
                                FoodgleLayoutRenderable = layoutfoodgle.get();
                                TJunctionLayoutRenderable = layout_t_junction.get();
                                FoodCentralLayoutRenderable =layoutfoodcentral.get();
                                NorthCanteenLayoutRenderable = layoutnorthcanteen.get();
                                SouthCanteenLayoutRenderable =layoutsouthcanteen.get();

                                andyRenderable = andy.get();
                                hasFinishedLoading = true;

                            } catch (InterruptedException | ExecutionException ex) {
                                DemoUtils.displayError(this, "Unable to load renderables", ex);
                            }

                            return null;
                        });

        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        arSceneView
                .getScene()
                .setOnUpdateListener(
                        frameTime -> {
                            if (!hasFinishedLoading) {
                                return;
                            }

                            if (locationScene == null) {
                                // If our locationScene object hasn't been setup yet, this is a good time to do it
                                // We know that here, the AR components have been initiated.
                                locationScene = new LocationScene(this, this, arSceneView);

                                // Now lets create our location markers.
                                // First, a layout
                                // Second location marker
                                LocationMarker koufulayoutLocationMarker = new LocationMarker(
                                        103.849135,
                                        1.379692,
                                        koufuview()
                                );

                                LocationMarker foodglelayoutlocationmarker = new LocationMarker(
                                        103.849510,
                                        1.376946,
                                        Foodgleview()
                                );

                                LocationMarker t_junctionlayoutlocationmarker = new LocationMarker(
                                        103.847374,
                                        1.379638,
                                        T_Junctionview()
                                );

                                LocationMarker foodcentrallayoutlocationmarker = new LocationMarker(
                                        103.849159,
                                        1.380512,
                                        foodcentralview()
                                );

                                LocationMarker northcanteenlayoutlocationmarker = new LocationMarker(
                                        103.849295,
                                        1.381901,
                                        northcanteenview()
                                );
                                LocationMarker southcanteenlayoutlocationmarker = new LocationMarker(
                                        103.849358,
                                        1.377981,
                                        southcanteenview()
                                );


                                koufulayoutLocationMarker.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView2 = KoufuLayoutRenderable.getView();
                                        TextView distanceTextView2  = eView2.findViewById(R.id.textViewl2);
                                        distanceTextView2.setText(node.getDistance() + "M");
                                    }
                                });
                                foodglelayoutlocationmarker.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView2 = FoodgleLayoutRenderable.getView();
                                        TextView distanceTextView2  = eView2.findViewById(R.id.textViewl2);
                                        distanceTextView2.setText(node.getDistance() + "M");
                                    }
                                });
                                t_junctionlayoutlocationmarker.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView2 = TJunctionLayoutRenderable.getView();
                                        TextView distanceTextView2  = eView2.findViewById(R.id.textViewl2);
                                        distanceTextView2.setText(node.getDistance() + "M");
                                    }
                                });
                                foodcentrallayoutlocationmarker.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView2 = FoodCentralLayoutRenderable.getView();
                                        TextView distanceTextView2  = eView2.findViewById(R.id.textViewl2);
                                        distanceTextView2.setText(node.getDistance() + "M");
                                    }
                                });

                                northcanteenlayoutlocationmarker.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView2 = NorthCanteenLayoutRenderable.getView();
                                        TextView distanceTextView2  = eView2.findViewById(R.id.textViewl2);
                                        distanceTextView2.setText(node.getDistance() + "M");
                                    }
                                });
                                southcanteenlayoutlocationmarker.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView2 = SouthCanteenLayoutRenderable.getView();
                                        TextView distanceTextView2  = eView2.findViewById(R.id.textViewl2);
                                        distanceTextView2.setText(node.getDistance() + "M");
                                    }
                                });

                                //End of second marker
                                // An example "onRender" event, called every frame
                                // Updates the layout with the markers distance

                                // Adding the marker
                                locationScene.mLocationMarkers.add(koufulayoutLocationMarker);
                                locationScene.mLocationMarkers.add(foodglelayoutlocationmarker);
                                locationScene.mLocationMarkers.add(t_junctionlayoutlocationmarker);
                                locationScene.mLocationMarkers.add(foodcentrallayoutlocationmarker);
                                locationScene.mLocationMarkers.add(northcanteenlayoutlocationmarker);
                                locationScene.mLocationMarkers.add(southcanteenlayoutlocationmarker);

                                // Adding a simple location marker of a 3D model

                            }

                            Frame frame = arSceneView.getArFrame();
                            if (frame == null) {
                                return;
                            }

                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                return;
                            }

                            if (locationScene != null) {
                                locationScene.processFrame(frame);
                            }

                            if (loadingMessageSnackbar != null) {
                                for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                                    if (plane.getTrackingState() == TrackingState.TRACKING) {
                                        hideLoadingMessage();
                                    }
                                }
                            }
                            locationScene.setAnchorRefreshInterval(1);
                        });


        // Lastly request CAMERA & fine location permission which is required by ARCore-Location.
        ARLocationPermissionHelper.requestPermission(this);
    }

    /**
     * Example node of a layout
     *
     * @return
     */
    private Node koufuview(){
        Node base1 = new Node();
        base1.setRenderable(KoufuLayoutRenderable);
        Context c = this;
        base1.setOnTapListener((v, event) -> {
           Intent koufuRenderable = new Intent(this,KoufuInfomation.class);
           startActivity(koufuRenderable);
        });
        return base1;
    }
    private Node Foodgleview() {
        Node base1 = new Node();
        base1.setRenderable(FoodgleLayoutRenderable);
        Context c = this;
        base1.setOnTapListener((v, event) -> {
            Intent koufuRenderable = new Intent(this, FoodgleInfo.class);
            startActivity(koufuRenderable);
        });
        return base1;
    }
    private Node T_Junctionview() {
        Node base1 = new Node();
        base1.setRenderable(TJunctionLayoutRenderable);
        Context c = this;
        base1.setOnTapListener((v, event) -> {
            Intent koufuRenderable = new Intent(this, t_junction.class);
            startActivity(koufuRenderable);
        });
        return base1;
    }
    private Node foodcentralview() {
        Node base1 = new Node();
        base1.setRenderable(FoodCentralLayoutRenderable);
        Context c = this;
        base1.setOnTapListener((v, event) -> {
            Intent koufuRenderable = new Intent(this, foodcentral.class);
            startActivity(koufuRenderable);
        });
        return base1;
    }
    private Node northcanteenview(){
        Node base1 = new Node();
        base1.setRenderable(NorthCanteenLayoutRenderable);
        Context c = this;
        base1.setOnTapListener((v, event) -> {
            Intent koufuRenderable = new Intent(this, northcanteen.class);
            startActivity(koufuRenderable);
        });
        return base1;
    }
    private Node southcanteenview(){
        Node base1 = new Node();
        base1.setRenderable(SouthCanteenLayoutRenderable);
        Context c = this;
        base1.setOnTapListener((v, event) -> {
            Intent koufuRenderable = new Intent(this, southcanteen.class);
            startActivity(koufuRenderable);
        });
        return base1;
    }

    private Node getAndy() {
        Node base = new Node();
        base.setRenderable(andyRenderable);
        Context c = this;
        base.setOnTapListener((v, event) -> {
            Toast.makeText(
                    c, "Andy touched.", Toast.LENGTH_LONG)
                    .show();
        });
        return base;
    }
     /***
     * Example Node of a 3D model
     *
     * @return
     */

    /**
     * Make sure we call locationScene.resume();
     */
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (locationScene != null) {
            locationScene.resume();
        }

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                Session session = DemoUtils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = ARLocationPermissionHelper.hasPermission(this);
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                DemoUtils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            DemoUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

        if (arSceneView.getSession() != null) {
            showLoadingMessage();
        }
    }

    /**
     * Make sure we call locationScene.pause();
     */
    @Override
    public void onPause() {
        super.onPause();

        if (locationScene != null) {
            locationScene.pause();
        }

        arSceneView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arSceneView.destroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!ARLocationPermissionHelper.hasPermission(this)) {
            if (!ARLocationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                ARLocationPermissionHelper.launchPermissionSettings(this);
            } else {
                Toast.makeText(
                        this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
            }
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Standard Android full-screen functionality.
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }

        loadingMessageSnackbar =
                Snackbar.make(
                        Multiplelocationmarker.this.findViewById(android.R.id.content),
                        R.string.plane_finding,
                        Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }

        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }
}
