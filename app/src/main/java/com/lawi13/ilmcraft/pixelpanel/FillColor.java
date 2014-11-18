package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FillColor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FillColor#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FillColor extends Fragment implements SeekBar.OnSeekBarChangeListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RED = "red";
    private static final String ARG_GREEN = "green";
    private static final String ARG_BLUE = "blue";

    private int red;
    private int green;
    private int blue;

    private SeekBar SeekBarRed, SeekBarGreen, SeekBarBlue;
    private TextView EditTextRed, EditTextGreen, EditTextBlue;

    final static String TAG = "FillColor";


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param red initialize Parameter 1.
     * @param green initialize Parameter 2.
     * @param blue initialize Parameter 3.
     * @return A new instance of fragment FillColor.
     */
    public static FillColor newInstance(int red, int green, int blue) {
        FillColor fragment = new FillColor();
        Bundle args = new Bundle();
        args.putInt(ARG_RED, red);
        args.putInt(ARG_GREEN, green);
        args.putInt(ARG_BLUE, blue);
        fragment.setArguments(args);
        return fragment;
    }

    public FillColor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            red = getArguments().getInt(ARG_RED);
            green = getArguments().getInt(ARG_GREEN);
            blue = getArguments().getInt(ARG_BLUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fill_color, container, false);

        SeekBarRed = (SeekBar) view.findViewById(R.id.seekBarRed);
        SeekBarRed.setOnSeekBarChangeListener(this);
        SeekBarGreen = (SeekBar) view.findViewById(R.id.seekBarGreen);
        SeekBarGreen.setOnSeekBarChangeListener(this);
        SeekBarBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
        SeekBarBlue.setOnSeekBarChangeListener(this);

        EditTextRed = (TextView) view.findViewById(R.id.textViewRedProgress);
        EditTextGreen = (TextView) view.findViewById(R.id.textViewGreenProgress);
        EditTextBlue = (TextView) view.findViewById(R.id.textViewBlueProgress);

        SeekBarRed.setProgress(red & 0xff);
        SeekBarGreen.setProgress(green & 0xff);
        SeekBarBlue.setProgress(blue & 0xff);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.v(TAG, "SeekBar " + seekBar.getId() + " string: " + seekBar.toString()+ "i: "+ i);
        Integer val = new Integer(i);
        if (seekBar.getId() == SeekBarRed.getId()) {
            red = i;
            EditTextRed.setText(val.toString());
            if (mListener != null) mListener.onRedChange(red);
        }

        if (seekBar.getId() == SeekBarGreen.getId()) {
            green = i;
            EditTextGreen.setText(val.toString());
            if (mListener != null) mListener.onGreenChange(green);
        }

        if (seekBar.getId() == SeekBarBlue.getId()) {
            blue = i;
            EditTextBlue.setText(val.toString());
            if (mListener != null) mListener.onBlueChange(blue);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onRedChange(int red);
        public void onGreenChange(int green);
        public void onBlueChange(int blue);
    }

}
