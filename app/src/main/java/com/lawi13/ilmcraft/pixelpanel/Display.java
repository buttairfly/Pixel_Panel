package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Display.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Display#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Display extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private static final String ARG_MBrightness = "MBrightness";
    private static final String ARG_FPS = "FPS";

    private int MBrightness;
    private int FPS;

    private SeekBar SeekBarMBrightness, SeekBarFPS;
    private TextView EditTextMBrightness, EditTextFPS;

    final static String TAG = "Display";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param MBrightness Parameter Master Brightness.
     * @param FPS Parameter Frames per Second.
     * @return A new instance of fragment Display.
     */
    public static Display newInstance(int MBrightness, int FPS) {
        Display fragment = new Display();
        Bundle args = new Bundle();
        args.putInt(ARG_MBrightness, MBrightness);
        args.putInt(ARG_FPS, FPS);
        fragment.setArguments(args);
        return fragment;
    }

    public Display() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MBrightness = getArguments().getInt(ARG_MBrightness);
            FPS = getArguments().getInt(ARG_FPS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);

        SeekBarMBrightness = (SeekBar) view.findViewById(R.id.seekBarMBrightness);
        SeekBarMBrightness.setOnSeekBarChangeListener(this);
        SeekBarFPS = (SeekBar) view.findViewById(R.id.seekBarFPS);
        SeekBarFPS.setOnSeekBarChangeListener(this);

        EditTextMBrightness = (TextView) view.findViewById(R.id.textViewMBrightnessProgress);
        EditTextFPS = (TextView) view.findViewById(R.id.textViewFPSProgress);

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Integer val = i;
        if (seekBar.getId()==SeekBarMBrightness.getId()) {
            MBrightness = i;
            EditTextMBrightness.setText(val.toString());
            if (mListener != null) mListener.onMasterBrightnessChange(MBrightness);
        }

        if (seekBar.getId()==SeekBarFPS.getId()) {
            FPS = i;
            EditTextFPS.setText(val.toString());
            if (mListener != null) mListener.onFpsChange(FPS);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onMasterBrightnessChange(int brightness);
        public void onFpsChange(int fps);
    }

}
