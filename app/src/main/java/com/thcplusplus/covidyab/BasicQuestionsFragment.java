package com.thcplusplus.covidyab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thcplusplus.covidyab.databinding.FragmentBasicQuestionsBinding;

import java.util.Objects;

public class BasicQuestionsFragment extends Fragment {

    private FragmentBasicQuestionsBinding mBinding;
    private static BasicQuestionsFragment sRecent = null;
    private Genders mGender;
    public static Boolean running(){
        return sRecent != null;
    }

    public static BasicQuestionsFragment get(){
        // return the recent created fragment -> the one that is on screen now
        return sRecent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sRecent = null;
    }

    public BasicQuestionsFragment() {
        mGender = Genders.Male;
    }

    public static BasicQuestionsFragment newInstance() { //(String param1, String param2) {
        sRecent = new BasicQuestionsFragment();
        return sRecent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Float readBodyTemperature() throws NumberFormatException {
        final String input = Objects.requireNonNull(mBinding.bodyTemperatureEditText.getText()).toString().trim();
        return Float.parseFloat(!input.equals("") ? input : "0");
    }

    private void selectGender(Genders gender){
        mGender = gender;
        mBinding.maleOptionButton.setSelected(gender == Genders.Male);
        mBinding.femaleOptionButton.setSelected(gender == Genders.Female);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.bodyTemperatureEditText.setFilters(new InputFilter[] {new QuizInputFilter(3, 1)});
        mBinding.ageEditText.setFilters(new InputFilter[] {new QuizInputFilter(3, 1)});

        selectGender(Genders.Male);
        mBinding.maleOptionButton.setOnClickListener(v -> selectGender(Genders.Male));

        mBinding.femaleOptionButton.setOnClickListener(v -> selectGender(Genders.Female));

        mBinding.increaseTemperatureButton.setOnClickListener(v -> {
            float temperature = Math.min(readBodyTemperature() + 0.1f, 40.0f);
            temperature = Probability.round(Math.max(temperature, 36.0f));

            mBinding.bodyTemperatureEditText
                    .setText(String.valueOf(temperature));
        });

        mBinding.decreaseTemperatureButton.setOnClickListener(v -> {
            float temperature = Math.min(readBodyTemperature() - 0.1f, 40.0f);
            temperature = Probability.round(Math.max(temperature, 36.0f));

            mBinding.bodyTemperatureEditText
                    .setText(String.valueOf(temperature));
        });
    }

    public Genders getGender() {
        return mGender;
    }

    public int readAge() throws NumberFormatException{
        final String input = Objects.requireNonNull(mBinding.ageEditText.getText()).toString().trim();
        return Integer.parseInt(!input.equals("") ? input : "0");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentBasicQuestionsBinding.
                inflate(inflater, container, false);
        return mBinding.getRoot();
    }
}