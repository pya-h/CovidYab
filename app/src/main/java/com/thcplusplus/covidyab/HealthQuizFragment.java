package com.thcplusplus.covidyab;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.thcplusplus.covidyab.databinding.FragmentHealthQuizBinding;

public class HealthQuizFragment extends Fragment {

    private FragmentHealthQuizBinding mBinding;
    private Symptoms mSymptom = Symptoms.Age;
    private Patient mPatient = null;
    public static final String QUIZ_ERRORS_TAG = "QUIZ_ERROR";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentHealthQuizBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    private void loadNextQuestion(Fragment questionFragment) {
        // Create new fragment and transaction
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.question_fragment_container, questionFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    private void answerYesNoQuestion(Boolean answer){
        try {
            if (mPatient != null && mSymptom.isYesOrNoQuestion()) {
                final boolean notFinalQuestion = !mSymptom.isLastOne();
                mSymptom = mPatient.submit(mSymptom, answer); // save current answer and mov to next question
                if (notFinalQuestion) {
                    // load next question
                    if (YesOrNoQuestionsFragment.running())
                        YesOrNoQuestionsFragment
                                .get()
                                .show(mSymptom
                                        .toString(
                                            mBinding.getRoot()
                                        )
                                );
                } else {
                    //end quiz and do the math
                    endQuiz();
                }
            } else throw new InvalidYesOrNoQuestion(mSymptom);
        }
        catch (InvalidYesOrNoQuestion ex){
            Log.e(QUIZ_ERRORS_TAG, ex.getMessage());
            TopSnack.show(mBinding.getRoot(), getString(R.string.invalid_yes_no_question));
            endQuiz();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // load the first question in card view
        loadNextQuestion(BasicQuestionsFragment.newInstance());

        // add next_button click event
        mBinding.nextOkButton.setOnClickListener(v1 -> {
            try {
                switch (mSymptom) { // check in which quiz stage the app was
                    // then move to next stage
                    case Gender:
                    case Age:
                    case BodyTemperature:
                        if(BasicQuestionsFragment.running()){
                            final BasicQuestionsFragment qf = BasicQuestionsFragment.get();
                            mPatient = new Patient(qf.getGender(), qf.readAge(), qf.readBodyTemperature());
                            mSymptom = Symptoms.Headache; // go to next stage

                            loadNextQuestion(YesOrNoQuestionsFragment
                                    .newInstance(mSymptom
                                            .toString(mBinding.getRoot())));

                            // change buttons to yes/no buttons
                            mBinding.nextOkButton.setText(R.string.answer_yes);
                            mBinding.noButton.setText(R.string.answer_no);
                            mBinding.noButton.setVisibility(View.VISIBLE);
                        }
                        else { // if sth went wrong and the basic fragment wasn't running when its supposed to,
                            // load it again
                            loadNextQuestion(BasicQuestionsFragment.newInstance());
                            // change buttons to yes/no buttons
                            mBinding.nextOkButton.setText(R.string.next);
                            mBinding.noButton.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        // other wise current question is a YesOrNo question
                        answerYesNoQuestion(true);
                        break;
                }
            }
            catch (OutOfRangeAgeException ex){
                Log.w(QUIZ_ERRORS_TAG, ex.getMessage());
                TopSnack.show(mBinding.getRoot(), getString(R.string.age_accepted_range));

            }
            catch (OutOfRangeBodyTemperatureException ex){
                Log.w(QUIZ_ERRORS_TAG, ex.getMessage());
                TopSnack.show(mBinding.getRoot(), getString(R.string.body_temperature_accepted_range));

            }
            catch (NumberFormatException ex){
                Log.w(QUIZ_ERRORS_TAG, ex.getMessage());
                TopSnack.show(mBinding.getRoot(), getString(R.string.quiz_wrong_number));

            }
            catch(Exception ex){
                Log.e(QUIZ_ERRORS_TAG, ex.getMessage());
                TopSnack.show(mBinding.getRoot(), getString(R.string.quiz_unexpected_error));

            }
        });

        mBinding.noButton.setOnClickListener(v2 -> {
            try{
                // other wise current question is a YesOrNo question
                answerYesNoQuestion(false);
            }
            catch (Exception ex){
                Log.e(QUIZ_ERRORS_TAG, ex.getMessage());
                TopSnack.show(mBinding.getRoot(), getString(R.string.quiz_unexpected_error));

            }
        });
    }

    private void endQuiz() {
        mSymptom = null;
        Bundle bundle = null;
        if(mPatient != null)
            bundle = mPatient.toBundle();
        NavHostFragment.findNavController(HealthQuizFragment.this)
            .navigate(R.id.action_healthQuizFragment_to_mainFragment, bundle);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding = null;
    }

}