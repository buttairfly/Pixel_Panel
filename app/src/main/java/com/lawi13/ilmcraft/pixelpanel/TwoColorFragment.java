package com.lawi13.ilmcraft.pixelpanel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TwoColorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TwoColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwoColorFragment extends Fragment {
    private static final String ARG_ID = "id";
    private static final String ARG_PARAM1 = "color1";
    private static final String ARG_PARAM2 = "color2";

    private int pId;
    private int pColor1;
    private int pColor2;

    private TextView tv1, tv2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id     id of Fragment... defines its usage
     * @param color1 first color
     * @param color2 guess... second color :)
     * @return A new instance of fragment TwoColorFragment.
     */
    public static TwoColorFragment newInstance(int id, int color1, int color2) {
        TwoColorFragment fragment = new TwoColorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putInt(ARG_PARAM1, color1);
        args.putInt(ARG_PARAM2, color2);
        fragment.setArguments(args);
        return fragment;
    }

    public TwoColorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pId = getArguments().getInt(ARG_PARAM1);
            pColor1 = getArguments().getInt(ARG_PARAM1);
            pColor2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_color, container, false);
        tv1 = (TextView) view.findViewById(R.id.textViewColor1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv2 = (TextView) view.findViewById(R.id.textViewColor2);

        // Inflate the layout for this fragment
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


    public interface OnFragmentInteractionListener {
        public void twoColorChanged(int id, int color1, int color2);
    }

}
