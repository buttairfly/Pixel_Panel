package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Display.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Display#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Connectivity extends Fragment {
    private static final String ARG_IP = "IP";
    private String ipAddress;

    private EditText EditTextIP;
    private Button btnShutdownPi, btnResetIP;

    final static String TAG = "Connectivity";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ipAddress Parameter IP Address
     * @return A new instance of fragment Display.
     */
    public static Connectivity newInstance(String ipAddress) {
        Connectivity fragment = new Connectivity();
        Bundle args = new Bundle();
        args.putString(ARG_IP, ipAddress);
        fragment.setArguments(args);
        return fragment;
    }

    public Connectivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ipAddress = getArguments().getString(ARG_IP);
        }
        if (!Utils.isValidIp(ipAddress)) {
            ipAddress = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connectivity, container, false);

        EditTextIP = (EditText) view.findViewById(R.id.editTextIP);
        EditTextIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ipAddress = editable.toString();
                if (!Utils.isValidIp(ipAddress)) {
                    ipAddress = "";
                    EditTextIP.setBackgroundColor(0xFFFF0000);//red
                } else {
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
        btnShutdownPi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shutdown
            }
        });
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
        public void onIpAddressChange(String ip);
    }

}
