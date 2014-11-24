package com.lawi13.ilmcraft.pixelpanel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link DefaultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private CharSequence mParam1;

    private TextView tv;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DefaultFragment.
     */
    public static DefaultFragment newInstance(CharSequence param1) {
        DefaultFragment fragment = new DefaultFragment();
        Bundle args = new Bundle();
        args.putCharSequence(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public DefaultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getCharSequence(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_default, container, false);
        tv = (TextView) view.findViewById(R.id.TextViewDefault);
        tv.setText(mParam1);
        return view;
    }

}
