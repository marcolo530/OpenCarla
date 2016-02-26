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
    //method inflate the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);//first parameter resource id, second is view group, third is attach to root or not

        return rootView;
    }



}
