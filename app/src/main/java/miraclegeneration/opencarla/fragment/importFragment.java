package miraclegeneration.opencarla.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import miraclegeneration.opencarla.R;

/**
 * Created by kenex on 10/1/2016.
 */
public class importFragment extends Fragment  {
    SupportMapFragment sMapFragment;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_import, container, false);
        return rootView;
    }

}