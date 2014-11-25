package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneColorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneColorFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLOR1 = "color1";

    private int color1;
    private ColorPicker picker;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private OpacityBar opacityBar;
    private LinearLayout rl;

    final static String TAG = "OneColorFragment";

    private OnFragmentInteractionListener mListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param color1 initialize Parameter 1.
     * @return A new instance of fragment OneColorFragment.
     */
    public static OneColorFragment newInstance(int color1) {
        OneColorFragment fragment = new OneColorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOR1, color1);
        fragment.setArguments(args);
        return fragment;
    }

    public OneColorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color1 = getArguments().getInt(ARG_COLOR1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_color, container, false);
        rl = (LinearLayout) view.findViewById(R.id.onColorLayout);
        rl.setBackgroundColor(color1);

        picker = (ColorPicker) view.findViewById(R.id.picker);
        saturationBar = (SaturationBar) view.findViewById(R.id.saturationbar);
        valueBar = (ValueBar) view.findViewById(R.id.valuebar);
        opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.addOpacityBar(opacityBar);
        picker.setShowOldCenterColor(false);
        picker.setColor(color1);
        picker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                color1 = color;
                if (mListener != null) mListener.onColor1Change(color1);
                rl.setBackgroundColor(color1);
            }
        });
        /*
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                if (color1 != picker.getColor()){
                    if (mListener != null) mListener.onColor1Change(picker.getColor());
                    rl.setBackgroundColor(color);
                    color1 = picker.getColor();
                    SystemClock.sleep(20);
                }
            }
        });*/
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onColor1Change(int color1);
    }

}
