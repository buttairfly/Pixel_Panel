package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Settings extends Fragment implements SeekBar.OnSeekBarChangeListener{
    private static final String ARG_MBrightness = "MBrightness";
    private static final String ARG_FPS = "FPS";
    private static final String ARG_IP = "IP";

    private int MBrightness;
    private int FPS;
    private String ipAddress;

    private SeekBar SeekBarMBrightness, SeekBarFPS;
    private TextView EditTextMBrightness, EditTextFPS;
    private EditText EditTextIP;
    private Button btnShutdownPi, btnResetIP;

    final static String TAG = "Settings";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param MBrightness Parameter Master Brightness.
     * @param FPS Parameter Frames per Second.
     * @param ipAddress Parameter IP Address
     * @return A new instance of fragment Settings.
     */
    public static Settings newInstance(int MBrightness, int FPS, String ipAddress) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putInt(ARG_MBrightness, MBrightness);
        args.putInt(ARG_FPS, FPS);
        args.putString(ARG_IP, ipAddress);
        fragment.setArguments(args);
        return fragment;
    }

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MBrightness = getArguments().getInt(ARG_MBrightness);
            FPS = getArguments().getInt(ARG_FPS);
            ipAddress = getArguments().getString(ARG_IP);
        }
        if (!Utils.isValidIp(ipAddress)){
            ipAddress = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SeekBarMBrightness = (SeekBar) view.findViewById(R.id.seekBarMBrightness);
        SeekBarMBrightness.setOnSeekBarChangeListener(this);
        SeekBarFPS = (SeekBar) view.findViewById(R.id.seekBarFPS);
        SeekBarFPS.setOnSeekBarChangeListener(this);

        EditTextMBrightness = (TextView) view.findViewById(R.id.textViewMBrightnessProgress);
        EditTextFPS = (TextView) view.findViewById(R.id.textViewFPSProgress);

        EditTextIP = (EditText) view.findViewById(R.id.editTextIP);
        EditTextIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void afterTextChanged(Editable editable) {
                ipAddress = editable.toString();
                if (!Utils.isValidIp(ipAddress)){
                    ipAddress = "";
                    EditTextIP.setBackgroundColor(0xFFFF0000);//red
                }
                else{
                    EditTextIP.setBackgroundColor(0xFF00FF00);//green
                    if (mListener != null) mListener.onIpAddressChange(ipAddress);
                }

            }
        });

        btnResetIP = (Button) view.findViewById(R.id.btnReset);
        btnResetIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAddress = "";
                EditTextIP.setText("");
                EditTextIP.setBackgroundColor(0xFFFF0000);//red
                if (mListener != null) mListener.onIpAddressChange(ipAddress);
            }
        });

        btnShutdownPi = (Button) view.findViewById(R.id.btnShutdown);
        btnShutdownPi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //shutdown
            }
        });

        SeekBarMBrightness.setProgress(MBrightness & 0xff);
        SeekBarFPS.setProgress(FPS & 0xff);
        EditTextIP.setText(ipAddress);

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
        Integer val = new Integer(i);
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
        public void onIpAddressChange(String ip);
    }

}
