package com.ijoomer.components.jomsocial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ijoomer.common.classes.IjoomerMapClusterIconProvider;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.InfoWindowAdapter;
import pl.mg6.android.maps.extensions.GoogleMap.OnInfoWindowClickListener;
import pl.mg6.android.maps.extensions.Marker;

/**
 * This Class Contains All Method Related To JomMapActivity.
 *
 * @author tasol
 *
 */
public class JomMapActivity extends JomMasterActivity implements OnInfoWindowClickListener {

    private GoogleMap googleMap;

    private ArrayList<HashMap<String, String>> IN_MAPLIST;

    private boolean IN_SHOW_BUBBLE;
    private AQuery androidQuery;
    private HashMap<Marker, HashMap<String, String>> markerHashMap;
    Bitmap bitmapCreate = null;
    Bitmap bitmapScale = null;

    // 180 - 8*8,160 - 9*9,144 - 10*10,120 - 12*12 and 96 - 15*15 grids
    // respectively on zoom level 2.
    private final double[] CLUSTER_SIZES = new double[] { 180, 160, 144, 120, 96 };

    private MutableData[] dataArray = { new MutableData(6, new LatLng(-50, 0)), new MutableData(28, new LatLng(-52, 1)), new MutableData(496, new LatLng(-51, -2)), };
    private Handler handler = new Handler();
    private Runnable dataUpdater = new Runnable() {

        @Override
        public void run() {
            for (MutableData data : dataArray) {
                data.value = 7 + 3 * data.value;
            }
            onDataUpdate();
            handler.postDelayed(this, 1000);
        }
    };

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initComponents() {

        googleMap = getMapView();
        googleMap.moveCamera(CameraUpdateFactory.zoomBy(1));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        androidQuery = new AQuery(this);

        IN_MAPLIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_MAPLIST");
        IN_SHOW_BUBBLE = getIntent().getBooleanExtra("IS_SHOW_BUBBLE", true);
        markerHashMap = new HashMap<Marker, HashMap<String, String>>();

    }

    @Override
    public void prepareViews() {
        populateMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IN_SHOW_BUBBLE) {
            handler.post(dataUpdater);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (IN_SHOW_BUBBLE) {
            handler.removeCallbacks(dataUpdater);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.isCluster()) {
            List<Marker> markers = marker.getMarkers();
            Builder builder = LatLngBounds.builder();
            for (Marker m : markers) {
                builder.include(m.getPosition());
            }
            LatLngBounds bounds = builder.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));
        } else {
            final HashMap<String, String> data = markerHashMap.get(marker);
            if (data.containsKey(USER_ID) && !data.get(USER_ID).equals("0")) {
                gotoProfile(data.get(USER_ID));
            }
            marker.hideInfoWindow();
        }

    }

    @Override
    public void setActionListeners() {
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {

    }

    /**
     * Class methods
     */

    /**
     * This method used to set clustering view.
     */
    private void setUpClusteringViews() {
        ClusteringSettings clusteringSettings = new ClusteringSettings();
        clusteringSettings.addMarkersDynamically(true);

        clusteringSettings.iconDataProvider(new IjoomerMapClusterIconProvider(getResources()));

        double clusterSize = CLUSTER_SIZES[0];
        clusteringSettings.clusterSize(clusterSize);

        googleMap.setClustering(clusteringSettings);
    }

    /**
     * This method used to populate map.
     */
    private void populateMap() {

        if (IN_MAPLIST != null && IN_MAPLIST.size() > 0) {

            if (!IN_SHOW_BUBBLE) {
                try{
                    if (IN_MAPLIST.get(0).get(LAT) != null && IN_MAPLIST.get(0).get(LAT).toString().trim().length() > 0 && IN_MAPLIST.get(0).get(LONG) != null
                            && IN_MAPLIST.get(0).get(LONG).toString().trim().length() > 0) {

                        HashMap<String, String> mapItem = IN_MAPLIST.get(0);
                        placeMarker(mapItem);
                        googleMap
                                .animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(IN_MAPLIST.get(0).get(LAT)), Double.parseDouble(IN_MAPLIST.get(0).get(LONG)))));
                        googleMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try{
                    googleMap.setClustering(new ClusteringSettings().iconDataProvider(new IjoomerMapClusterIconProvider(getResources())).addMarkersDynamically(true));
                    for (HashMap<String, String> mapItem : IN_MAPLIST) {
                        if (mapItem.get(USER_LAT) != null && mapItem.get(USER_LAT).toString().trim().length() > 0 && mapItem.get(USER_LONG) != null
                                && mapItem.get(USER_LONG).toString().trim().length() > 0) {
                            placeMarker(mapItem);
                        }
                    }
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(IN_MAPLIST.get(0).get(USER_LAT)), Double.parseDouble(IN_MAPLIST.get(0).get(
                            USER_LONG)))));
                    googleMap.setInfoWindowAdapter(new InfoAdapter());
                    googleMap.setOnInfoWindowClickListener(this);
                    setUpClusteringViews();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    /**
     * This method used to place marker on map.
     *
     * @param markerData
     *            represented markar data.
     */
    private void placeMarker(final HashMap<String, String> markerData) {
        if (!IN_SHOW_BUBBLE) {
            markerHashMap.put(
                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(
                            new LatLng(Double.parseDouble(markerData.get(LAT)), Double.parseDouble(markerData.get(LONG))))), markerData);

        } else {
            androidQuery.ajax(markerData.get(USER_AVATAR), Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap object, AjaxStatus status) {
                    super.callback(url, object, status);
                    markerHashMap.put(googleMap.addMarker(new MarkerOptions().title(markerData.get(USER_NAME))
                            .icon(BitmapDescriptorFactory.fromBitmap(combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.ijoomer_map_custom_marker), object)))
                            .position(new LatLng(Double.parseDouble(markerData.get(USER_LAT)), Double.parseDouble(markerData.get(USER_LONG))))), markerData);
                    if (bitmapCreate != null) {
                        bitmapCreate.recycle();
                        bitmapCreate = null;
                    }
                }
            });
        }
    }

    /**
     * This method used to on map data update.
     */
    private void onDataUpdate() {
        Marker m = googleMap.getMarkerShowingInfoWindow();
        if (m != null && !m.isCluster() && m.getData() instanceof MutableData) {
            m.showInfoWindow();
        }
    }

    /**
     * This method used to combine custom image on map marker icon.
     *
     * @param frame
     *            represented default icon frame bitmap.
     * @param image
     *            represented dynamic icon bitmap.
     * @return represented {@link Bitmap}
     */
    public Bitmap combineImages(Bitmap frame, Bitmap image) {

        bitmapScale = Bitmap.createScaledBitmap(image, convertSizeToDeviceDependent(45), convertSizeToDeviceDependent(40), true);

        bitmapCreate = Bitmap.createBitmap(frame.getWidth(), frame.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(bitmapCreate);

        comboImage.drawBitmap(bitmapScale, convertSizeToDeviceDependent(7), convertSizeToDeviceDependent(7), null);
        comboImage.drawBitmap(frame, 0, 0, null);
        if (frame != null) {
            try {
                bitmapScale.recycle();
                frame.recycle();
                image.recycle();
                bitmapScale = null;
                frame = null;
                image = null;
            } catch (Throwable e) {
            }
        }
        return bitmapCreate;
    }

    /**
     * Custom marker info adapter.
     *
     * @author tasol
     *
     */
    class InfoAdapter implements InfoWindowAdapter {

        private TextView tv;
        {

            tv = new TextView(JomMapActivity.this);
            tv.setTextColor(Color.BLACK);
        }

        private Collator collator = Collator.getInstance();
        private Comparator<Marker> comparator = new Comparator<Marker>() {
            public int compare(Marker lhs, Marker rhs) {
                String leftTitle = lhs.getTitle();
                String rightTitle = rhs.getTitle();
                if (leftTitle == null && rightTitle == null) {
                    return 0;
                }
                if (leftTitle == null) {
                    return 1;
                }
                if (rightTitle == null) {
                    return -1;
                }
                return collator.compare(leftTitle, rightTitle);
            }
        };

        @Override
        public View getInfoContents(Marker marker) {
            if (marker.isCluster()) {
                List<Marker> markers = marker.getMarkers();
                int i = 0;
                String text = "";
                while (i < 3 && markers.size() > 0) {
                    Marker m = Collections.min(markers, comparator);
                    String title = m.getTitle();
                    if (title == null) {
                        break;
                    }
                    text += title + "\n";
                    markers.remove(m);
                    i++;
                }
                if (text.length() == 0) {
                    text = "Markers with mutable data";
                } else if (markers.size() > 0) {
                    text += "and " + markers.size() + " more...";
                } else {
                    text = text.substring(0, text.length() - 1);
                }
                tv.setText(text);
                return tv;
            } else {
                Object data = marker.getData();
                if (data instanceof MutableData) {
                    MutableData mutableData = (MutableData) data;
                    tv.setText("Value: " + mutableData.value);
                    return tv;
                } else {
                    return null;
                }
            }
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if (!marker.isCluster()) {
                final HashMap<String, String> data = markerHashMap.get(marker);

                View view = LayoutInflater.from(JomMapActivity.this).inflate(R.layout.jom_map_bubble, null);

                IjoomerTextView txtMapUserName = (IjoomerTextView) view.findViewById(R.id.txtMapUserName);

                txtMapUserName.setText(data.get(USER_NAME));

                return view;
            } else {
                return null;
            }

        }

    }


    /**
     * Inner class
     * @author tasol
     *
     */
    private class MutableData {

        private int value;
        @SuppressWarnings("unused")
        private LatLng position;

        public MutableData(int value, LatLng position) {
            this.value = value;
            this.position = position;
        }
    }

}
