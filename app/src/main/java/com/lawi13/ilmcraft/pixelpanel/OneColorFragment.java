package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

//import com.larswerkman.holocolorpicker.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneColorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneColorFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RED = "color1";

    private int color1;
    // private ColorPicker picker;
    private SeekBar SeekBarRed, SeekBarGreen, SeekBarBlue;
    private TextView EditTextRed, EditTextGreen, EditTextBlue;

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
        args.putInt(ARG_RED, color1);
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
            color1 = getArguments().getInt(ARG_RED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_color, container, false);

        /*
        picker = (ColorPicker) view.findViewById(R.id.picker);
        SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);

        picker.setColor(color1);
*/
        SeekBarRed = (SeekBar) view.findViewById(R.id.seekBarRed);
        SeekBarRed.setOnSeekBarChangeListener(this);
        SeekBarGreen = (SeekBar) view.findViewById(R.id.seekBarGreen);
        SeekBarGreen.setOnSeekBarChangeListener(this);
        SeekBarBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
        SeekBarBlue.setOnSeekBarChangeListener(this);

        EditTextRed = (TextView) view.findViewById(R.id.textViewRedProgress);
        EditTextGreen = (TextView) view.findViewById(R.id.textViewGreenProgress);
        EditTextBlue = (TextView) view.findViewById(R.id.textViewBlueProgress);

        SeekBarRed.setProgress(Utils.ARGBtoR(color1));
        SeekBarGreen.setProgress(Utils.ARGBtoG(color1));
        SeekBarBlue.setProgress(Utils.ARGBtoB(color1));

        return view;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.v(TAG, "SeekBar " + seekBar.getId() + " string: " + seekBar.toString() + "i: " + i);
        Integer val = i;
        if (seekBar.getId() == SeekBarRed.getId()) {
            color1 = Utils.RToARGB(color1, i);
            EditTextRed.setText(val.toString());
        }
        if (seekBar.getId() == SeekBarGreen.getId()) {
            color1 = Utils.GToARGB(color1, i);
            EditTextGreen.setText(val.toString());
        }
        if (seekBar.getId() == SeekBarBlue.getId()) {
            color1 = Utils.BToARGB(color1, i);
            EditTextBlue.setText(val.toString());
        }
        if (mListener != null) mListener.onColor1Change(color1);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
