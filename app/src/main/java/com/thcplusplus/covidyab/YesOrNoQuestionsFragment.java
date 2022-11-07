package com.thcplusplus.covidyab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thcplusplus.covidyab.databinding.FragmentYesOrNoQuestionsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YesOrNoQuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YesOrNoQuestionsFragment extends Fragment {

    public static final String ARGUMENT_QUESTION = "CURRENT_SYMPTOM";
    private FragmentYesOrNoQuestionsBinding mBinding;
    private static YesOrNoQuestionsFragment sRecent = null;

    public static Boolean running(){
        return sRecent != null;
    }

    public static YesOrNoQuestionsFragment get(){
        // return the recent created fragment -> the one that is on screen now
        return sRecent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sRecent = null;
    }

    public YesOrNoQuestionsFragment() {
        // Required empty public constructor
    }

    public static YesOrNoQuestionsFragment newInstance(String question) {
        YesOrNoQuestionsFragment fragment = new YesOrNoQuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_QUESTION, question);
        fragment.setArguments(args);
        sRecent = fragment;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentYesOrNoQuestionsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // load question
        Bundle args = getArguments();
        if(args != null)
            show(args.getString(ARGUMENT_QUESTION));

    }

    public void show(String question){
        mBinding.questionTextField.setText(question);
    }

}