package com.thcplusplus.covidyab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.thcplusplus.covidyab.databinding.FragmentQuizOutcomeBinding;
import com.thcplusplus.covidyab.databinding.FragmentWelcomeBinding;

public class MainFragment extends Fragment {
    private FragmentWelcomeBinding mWelcomeBinding;
    private FragmentQuizOutcomeBinding mQuizOutcomeBinding;
    private Patient mPatient = null;
    public static final String OUTCOMES_ERROR_TAG = "ERROR_OUTCOMES";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                mQuizOutcomeBinding = FragmentQuizOutcomeBinding.inflate(inflater, container, false);
                mWelcomeBinding = null;
                mPatient = Patient.toPatient(arguments);
                return mQuizOutcomeBinding.getRoot();
            } catch (OutOfRangeAgeException ex) {
                Log.e(OUTCOMES_ERROR_TAG, ex.getMessage());
                TopSnack.show(mQuizOutcomeBinding.getRoot(), getString(R.string.age_accepted_range));
            } catch (OutOfRangeBodyTemperatureException ex) {
                Log.e(OUTCOMES_ERROR_TAG, ex.getMessage());
                TopSnack.show(mQuizOutcomeBinding.getRoot(), getString(R.string.body_temperature_accepted_range));
            } catch (NumberFormatException ex) {
                Log.e(OUTCOMES_ERROR_TAG, ex.getMessage());
                TopSnack.show(mQuizOutcomeBinding.getRoot(), getString(R.string.quiz_wrong_number));
            } catch (Exception ex) {
                Log.e(OUTCOMES_ERROR_TAG, ex.getMessage());
                TopSnack.show(mQuizOutcomeBinding.getRoot(), getString(R.string.quiz_unexpected_error));
            }
        }
        mWelcomeBinding = FragmentWelcomeBinding.inflate(inflater, container, false);
        mPatient = null;
        mQuizOutcomeBinding = null;
        return mWelcomeBinding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mPatient != null && mQuizOutcomeBinding != null){
            mQuizOutcomeBinding.startQuizFab.setOnClickListener(v -> NavHostFragment.findNavController(MainFragment.this)
                    .navigate(R.id.action_mainFragment_to_healthQuizFragment));
            mQuizOutcomeBinding.startTreeQuizFab.setOnClickListener(v -> NavHostFragment.findNavController(MainFragment.this)
                    .navigate(R.id.action_mainFragment_to_treeQuizFragment));
            writeOutcomes();
        }
        else{
            mWelcomeBinding.startQuizFab.setOnClickListener(v -> NavHostFragment.findNavController(MainFragment.this)
                    .navigate(R.id.action_mainFragment_to_healthQuizFragment));
            mWelcomeBinding.startTreeQuizFab.setOnClickListener(v -> NavHostFragment.findNavController(MainFragment.this)
                    .navigate(R.id.action_mainFragment_to_treeQuizFragment));
        }

    }

    private void writeOutcomes() {
        try{
            // probability info
            final Probability probability = mPatient.getProbability();
            mQuizOutcomeBinding
                    .outcomeDiseaseSeverityField
                    .setText(
                            mPatient
                                    .getDiseaseSeverity()
                                    .toString(mQuizOutcomeBinding.getRoot()) );
            mQuizOutcomeBinding
                    .outcomeLightnessProbabilityField
                    .setText(
                            String.valueOf(
                                    probability.getLightness()
                            )
                    );
            mQuizOutcomeBinding
                    .outcomeNormalityProbabilityField
                    .setText(
                            String.valueOf(
                                    probability.getNormality()
                            )
                    );
            mQuizOutcomeBinding
                    .outcomeSeverenessProbabilityField
                    .setText(
                            String.valueOf(
                                    probability.getSevereness()
                            )
                    );

            // patient physical info
            mQuizOutcomeBinding
                    .outcomeAgeTextField
                    .setText( String.valueOf(mPatient.getAge()) );
            mQuizOutcomeBinding
                    .outcomeGenderTextField
                    .setText(
                            mPatient
                                .getGender()
                                .toString(mQuizOutcomeBinding.getRoot())
                    );
            mQuizOutcomeBinding
                    .outcomeBodyTemperatureTextField
                    .setText( String.valueOf(mPatient.getBodyTemperature()) );

            // yes or no symptoms
            mQuizOutcomeBinding
                    .outcomeHeadacheField
                    .setSelected(mPatient.hasHeadache());
            mQuizOutcomeBinding
                    .outcomeSoreThroatField
                    .setSelected(mPatient.hasSoreThroat());
            mQuizOutcomeBinding
                    .outcomeDryCoughField
                    .setSelected(mPatient.hasDryCough());
            mQuizOutcomeBinding
                    .outcomeDiseaseBackgroundField
                    .setSelected(mPatient.hasDiseaseBackground());
            mQuizOutcomeBinding
                    .outcomeShortnessOfBreathField
                    .setSelected(mPatient.hasShortnessOfBreath());
            mQuizOutcomeBinding
                    .outcomeChestDistressField
                    .setSelected(mPatient.hasChestDistress());

        }
        catch (Exception ex){
            Log.e(OUTCOMES_ERROR_TAG, ex.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}