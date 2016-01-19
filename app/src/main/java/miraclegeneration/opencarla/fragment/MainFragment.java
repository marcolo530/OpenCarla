package miraclegeneration.opencarla.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import miraclegeneration.opencarla.R;

/**
 * Created by kenex on 10/1/2016.
 */
public class MainFragment extends Fragment {
    @Nullable
    @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }



}
