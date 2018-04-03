package com.sister.projectpjmc;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class TkykItem implements ClusterItem {
    private final LatLng mPosition;

    public TkykItem(LatLng latLng){
        this.mPosition = latLng;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
