package com.thcplusplus.covidyab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thcplusplus.covidyab.databinding.FragmentTreeQuizBinding;

import java.util.ArrayList;
import java.util.Objects;

import de.blox.treeview.BaseTreeAdapter;
import de.blox.treeview.TreeNode;

public class TreeQuizFragment extends Fragment {

    private static final String TREE_QUIZ_ERRORS_TAG = "TREE_QUIZ_ERROR";

    private FragmentTreeQuizBinding mBinding;
    private TreeNode[] mRecentTreeNodes; //last opened nodes

    String ANSWER_YES = "Yes", ANSWER_NO = "No", ANSWER_OTHERWISE = "Otherwise";
    private int mCurrentNodeLevel = -1; // not started
    private ArrayList<Integer> mNodeLevels;

    private final Patient mPatient;


    public int getCurrentLevel() {
        return mCurrentNodeLevel;
    }

    public TreeQuizFragment() {
        mNodeLevels = new ArrayList<>(); // tree view recets every time when updating
        // this array saves each tree nodes level according to their position
        mPatient = new Patient();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentTreeQuizBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    private TreeNode addTreeBranch(Symptoms symptom, String subNode1, String subNode2){
        mCurrentNodeLevel++;
        TreeNode treeNode = new TreeNode(symptom.toString(mBinding.getRoot()));
        mNodeLevels.add(mCurrentNodeLevel);

        mRecentTreeNodes = new TreeNode[]{
                new TreeNode(subNode1),
                new TreeNode(subNode2)
        };
        for (TreeNode nodeAnswer : mRecentTreeNodes) {
            treeNode.addChild(nodeAnswer);
            mNodeLevels.add(mCurrentNodeLevel);
        }

        return treeNode;
    }
    private TreeNode addTreeBranch(Symptoms symptom, String subNode1, String subNode2, String subNode3){
        mCurrentNodeLevel++;
        TreeNode treeNode = new TreeNode(symptom.toString(mBinding.getRoot()));
        mNodeLevels.add(mCurrentNodeLevel);

        mRecentTreeNodes = new TreeNode[]{
                new TreeNode(subNode1),
                new TreeNode(subNode2),
                new TreeNode(subNode3)
        };
        for (TreeNode nodeAnswer : mRecentTreeNodes) {
            treeNode.addChild(nodeAnswer);
            mNodeLevels.add(mCurrentNodeLevel);
        }

        return treeNode;
    }

    private void showInputBox(Symptoms symptom){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // customizing input box layout for input box
        final View alertBoxView = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        final EditText inputBoxEditTex = alertBoxView.findViewById(R.id.input_box_edit_text);
        inputBoxEditTex.setFilters(new InputFilter[]{new QuizInputFilter(3, 1)});
        final TextInputLayout inputBoxEditTextLayout = alertBoxView.findViewById(R.id.input_box_edit_text_layout);
        inputBoxEditTextLayout.setHint(symptom.toString(mBinding.getRoot()));

        // adding the customized layout to the builder object
        builder.setView(alertBoxView);

        final String[] inputValue = {""}; //in onClick interface outer variable usage must be through finals
        // set ok onClick and onDismiss event
        builder.setPositiveButton(getString(R.string.answer_ok), (dialog, which) -> {
            inputValue[0] = inputBoxEditTex.getText().toString();
        });
        builder.setOnDismissListener(dialog -> {
            //send answer for survey function
            // this causes input box to show again until getting the right answer
            surveyTree(Float.parseFloat(!inputValue[0].equals("") ? inputValue[0] : "0"));
        });

        builder.show();
    }

    private TreeNode generateTreeQuizOutcome(Severities severity) {
        mCurrentNodeLevel++;
        TreeNode finalNode = new TreeNode(severity.toString(mBinding.getRoot()));
        mNodeLevels.add(mCurrentNodeLevel);
        return finalNode;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // this is used to determine tree type
        ANSWER_YES = getString(R.string.answer_yes);
        ANSWER_NO = getString(R.string.answer_no);
        ANSWER_OTHERWISE = getString(R.string.answer_otherwise);

        // implementing tree adapter; this connects
        BaseTreeAdapter<TreeNodeViewHolder> quizTreeAdapter =
                new BaseTreeAdapter<TreeNodeViewHolder>(Objects.requireNonNull(getContext()), R.layout.tree_node) {
            @NonNull
            @Override
            public TreeNodeViewHolder onCreateViewHolder(View view) {
                return new TreeNodeViewHolder(view, TreeQuizFragment.this);
            }

            @Override
            public void onBindViewHolder(TreeNodeViewHolder viewHolder, Object data, int position) {
                // determine node type
                final String text = data.toString();
                if(text.equals(ANSWER_YES))
                    viewHolder.setNodeType(TreeNodeType.YesAnswer);
                else if(text.equals(ANSWER_NO))
                    viewHolder.setNodeType(TreeNodeType.NoAnswer);
                else
                    viewHolder.setNodeType(TreeNodeType.Field);
                // set text to field
                viewHolder.mBinding.nodeItemFieldButton.setText(data.toString());
                if(mNodeLevels.get(position) < mCurrentNodeLevel)
                    viewHolder.mBinding.nodeItemFieldButton.setEnabled(false);
                viewHolder.setNodeLevel(mNodeLevels.get(position));
            }
        };

        mBinding.quizTreeView.setAdapter(quizTreeAdapter);

        quizTreeAdapter.setRootNode(addTreeBranch(Symptoms.Headache, ANSWER_NO, ANSWER_YES));
    }
    public void surveyTree(float answer){
        // for yes-no questions => answer 1.0f means yes and 0.0f means no
        // for non-yes-no questions answer is the value of age or body temperature
        try {
            if (mCurrentNodeLevel == 0) {
                // headache branch
                mPatient.setHeadache(answer != 0.0f);
                final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                if (mPatient.hasHeadache()) {
                    selectedNode.addChild(
                            addTreeBranch(Symptoms.Age, "40-64", ANSWER_OTHERWISE));
                    showInputBox(Symptoms.Age);
                } else
                    selectedNode.addChild(
                            addTreeBranch(Symptoms.SoreThroat, ANSWER_NO, ANSWER_YES));

            } else {
                if (mPatient.hasHeadache()) {
                    if (mCurrentNodeLevel == 1) {
                        // headache yes: age selected
                        mPatient.setAge((int)answer);
                        if(mPatient.getAge() >= 40 && mPatient.getAge() <= 64){
                            final TreeNode selectedNode = mRecentTreeNodes[0];
                            selectedNode.addChild(
                                    addTreeBranch(Symptoms.DiseaseBackground, ANSWER_NO, ANSWER_YES));
                        }
                        else {
                            final TreeNode selectedNode = mRecentTreeNodes[1];
                            selectedNode.addChild(
                                    addTreeBranch(Symptoms.DryCough, ANSWER_NO, ANSWER_YES));
                        }
                    }
                    else {
                        if(mPatient.getAge() >= 40 && mPatient.getAge() <= 64){
                            // headache: yes -> age: 40-64
                            if(mCurrentNodeLevel == 2){
                                // disease background question answered
                                mPatient.setDiseaseBackground(answer != 0.0f);
                                final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                if (mPatient.hasDiseaseBackground())
                                    selectedNode.addChild(
                                            generateTreeQuizOutcome(Severities.Light));
                                else
                                    selectedNode.addChild(
                                            addTreeBranch(Symptoms.DryCough, ANSWER_NO, ANSWER_YES));
                            }
                            else {
                                // headache: yes -> age: 40-64 -> disease background: no
                                // dry cough question answered
                                if( !mPatient.hasDiseaseBackground()) { // just to make sure path surveyed is right
                                    if(mCurrentNodeLevel == 3) {
                                        mPatient.setDryCough(answer != 0.0f);
                                        final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                        if (mPatient.hasDryCough()) {
                                            selectedNode.addChild(
                                                    addTreeBranch(Symptoms.BodyTemperature, "< 37.3", "37.3-39", "> 39"));
                                            showInputBox(Symptoms.BodyTemperature);
                                        } else
                                            selectedNode.addChild(
                                                    generateTreeQuizOutcome(Severities.Normal));
                                    }
                                    else {
                                        // headache: yes -> age: 40-64 -> disease back: no -> dry cough: yes
                                        if( mPatient.hasDryCough() ){
                                            if(mCurrentNodeLevel == 4) {
                                                // means that body temperature has benn entered
                                                mPatient.setBodyTemperature(answer);
                                                if (mPatient.getBodyTemperature() < 37.3f) {
                                                    final TreeNode selectedNode = mRecentTreeNodes[0];
                                                    selectedNode.addChild(generateTreeQuizOutcome(Severities.UnCertain));
                                                } else if (mPatient.getBodyTemperature() >= 37.3f && mPatient.getBodyTemperature() <= 39.0f) {
                                                    final TreeNode selectedNode = mRecentTreeNodes[1];
                                                    selectedNode.addChild(
                                                            addTreeBranch(Symptoms.ChestDistress, ANSWER_NO, ANSWER_YES));
                                                } else { // < 37.3f
                                                    final TreeNode selectedNode = mRecentTreeNodes[2];
                                                    selectedNode.addChild(generateTreeQuizOutcome(Severities.Light));
                                                }
                                            }
                                            else {
                                                // headache: yes -> age: 40-64 -> disease back: no -> dry cough: yes,
                                                //      body temp: 37.3-39, just to make sure path is right, we checked it in if
                                                if(mPatient.getBodyTemperature() >= 37.3f && mPatient.getBodyTemperature() <= 39.0f){
                                                    if(mCurrentNodeLevel == 5) {
                                                        mPatient.setDryCough(answer != 0.0f);
                                                        final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                                        if (mPatient.hasDryCough()) {
                                                            selectedNode.addChild(
                                                                    generateTreeQuizOutcome(Severities.Normal));
                                                        } else {
                                                            selectedNode.addChild(
                                                                    addTreeBranch(Symptoms.BodyTemperature, "37.3-38", "38.1-39"));
                                                            // next branch auto generates because body temperature is entered previously
                                                            if(mPatient.getBodyTemperature() <= 38.0f){
                                                                final TreeNode finalNode = mRecentTreeNodes[0];
                                                                finalNode.addChild(generateTreeQuizOutcome(Severities.Severe));
                                                            }
                                                            else {
                                                                final TreeNode finalNode = mRecentTreeNodes[1];
                                                                finalNode.addChild(generateTreeQuizOutcome(Severities.Normal));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            // headache: yes -> age 0-39 || 65-250
                            if(mCurrentNodeLevel == 2){
                                // dry cough question answered
                                mPatient.setDryCough(answer != 0.0f);
                                final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                if (mPatient.hasDryCough()) {
                                    selectedNode.addChild(
                                            addTreeBranch(Symptoms.ShortnessOfBreath, ANSWER_NO, ANSWER_YES));
                                } else {
                                    selectedNode.addChild(
                                            addTreeBranch(Symptoms.ChestDistress, ANSWER_NO, ANSWER_YES));
                                }
                            }
                            else {
                                if(mPatient.hasDryCough()){
                                    // headache: yes -> age 0-39 || 65-250 -> dry cough: yes
                                    if(mCurrentNodeLevel == 3){ //to make sure pass is taken correctly
                                        // shortness of breath answered
                                        mPatient.setShortnessOfBreath(answer != 0.0f);
                                        final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                        if (mPatient.hasShortnessOfBreath())
                                            selectedNode.addChild(
                                                    generateTreeQuizOutcome(Severities.Light));
                                        else
                                            selectedNode.addChild(
                                                    generateTreeQuizOutcome(Severities.Normal));
                                    }
                                }
                                else {
                                    // headache: yes -> age 0-39 || 65-250 -> dry cough: no
                                    if(mCurrentNodeLevel == 3) {
                                        // chest distress question answered
                                        mPatient.setChestDistress(answer != 0.0f);
                                        final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                        if (mPatient.hasChestDistress()) {
                                            selectedNode.addChild(
                                                    generateTreeQuizOutcome(Severities.Light));
                                        } else {
                                            selectedNode.addChild(
                                                    addTreeBranch(Symptoms.Age, "1-39", "> 64"));
                                            // age is entered previously, auto-generate next branch
                                            if(mPatient.getAge() <= 39){
                                                final TreeNode nextNode = mRecentTreeNodes[0];
                                                nextNode.addChild(
                                                        addTreeBranch(Symptoms.DiseaseBackground, ANSWER_NO, ANSWER_YES));
                                            }
                                            else { // >= 65
                                                final TreeNode nextNode = mRecentTreeNodes[1];
                                                nextNode.addChild(generateTreeQuizOutcome(Severities.Light));
                                            }
                                            // goes to level 5
                                        }
                                    }
                                    else {
                                        if(mPatient.getAge() <= 39){ // just to make sure we're on the right path
                                            // headache: yes -> {age 0-39} -> dry cough: no -> chest distress: no
                                            if(mCurrentNodeLevel == 5){
                                                // disease background answered
                                                mPatient.setDiseaseBackground(answer != 0.0f);
                                                final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                                if (mPatient.hasDiseaseBackground())
                                                    selectedNode.addChild(
                                                            generateTreeQuizOutcome(Severities.Normal));
                                                else {
                                                    selectedNode.addChild(
                                                            addTreeBranch(Symptoms.Age, "15-39", "< 15"));
                                                    // age is entered previously, auto-generate next branch
                                                    if(mPatient.getAge() < 15){
                                                        final TreeNode nextNode = mRecentTreeNodes[1];
                                                        nextNode.addChild(
                                                                generateTreeQuizOutcome(Severities.Light));
                                                    }
                                                    else { // 15-39
                                                        final TreeNode nextNode = mRecentTreeNodes[0];
                                                        nextNode.addChild(
                                                                addTreeBranch(Symptoms.BodyTemperature, "<= 39", "> 39"));
                                                        showInputBox(Symptoms.BodyTemperature);
                                                    }
                                                    // goes to level 7
                                                }
                                            }
                                            else {
                                                if(!mPatient.hasDiseaseBackground() && mPatient.getAge() >= 15 && mPatient.getAge() <= 39){
                                                    if(mCurrentNodeLevel == 7) {//just to make sure, we wrote these two nested ifs
                                                        mPatient.setBodyTemperature(answer);
                                                        if(mPatient.getBodyTemperature() > 39.0f){
                                                            final TreeNode finalNode = mRecentTreeNodes[1];
                                                            finalNode.addChild(
                                                                    generateTreeQuizOutcome(Severities.Light));
                                                        }
                                                        else {
                                                            final TreeNode finalNode = mRecentTreeNodes[0];
                                                            finalNode.addChild(
                                                                    generateTreeQuizOutcome(Severities.Normal));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else { // no headache
                    if (mCurrentNodeLevel == 1) {
                        final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                        mPatient.setSoreThroat(answer != 0.0f);
                        if(mPatient.hasSoreThroat()){
                            selectedNode.addChild(
                                    addTreeBranch(Symptoms.Age, "< 15","15-39", " > 39"));
                        }
                        else {
                            selectedNode.addChild(
                                    addTreeBranch(Symptoms.Age, "0-64", "> 64"));
                        }
                        showInputBox(Symptoms.Age);
                    }
                    else {
                        // headache: no
                        if(mPatient.hasSoreThroat()){ //this for checking previous answers till here
                            // headache: no -> sore throat: yes
                            if(mCurrentNodeLevel == 2) {
                                mPatient.setAge((int) answer);
                                if (mPatient.getAge() < 15) {
                                    final TreeNode selectedNode = mRecentTreeNodes[0];
                                    selectedNode.addChild(generateTreeQuizOutcome(Severities.UnCertain));
                                } else if(mPatient.getAge() >= 15 && mPatient.getAge() <= 39){
                                    final TreeNode selectedNode = mRecentTreeNodes[1];
                                    selectedNode.addChild(
                                            addTreeBranch(Symptoms.BodyTemperature, "> 39", "37.3-39", "< 37.3"));
                                    showInputBox(Symptoms.BodyTemperature);
                                }
                                else {
                                    final TreeNode selectedNode = mRecentTreeNodes[2];
                                    selectedNode.addChild(generateTreeQuizOutcome(Severities.Normal));
                                }
                            }
                            else {
                                if(mCurrentNodeLevel == 3 && mPatient.getAge() >= 15 && mPatient.getAge() <= 39){
                                    // headache no -> sore throat yes -> age 15-39
                                    mPatient.setBodyTemperature(answer);
                                    if (mPatient.getBodyTemperature() > 39.0f) {
                                        final TreeNode selectedNode = mRecentTreeNodes[0];
                                        selectedNode.addChild(generateTreeQuizOutcome(Severities.UnCertain));
                                    } else if(mPatient.getBodyTemperature() >= 37.3f && mPatient.getBodyTemperature() <= 39.0f){
                                        final TreeNode selectedNode = mRecentTreeNodes[1];
                                        selectedNode.addChild(
                                                addTreeBranch(Symptoms.ShortnessOfBreath, ANSWER_NO, ANSWER_YES));
                                    }
                                    else { // < 37.3f
                                        final TreeNode selectedNode = mRecentTreeNodes[2];
                                        selectedNode.addChild(generateTreeQuizOutcome(Severities.Light));
                                    }
                                }
                                else {
                                    if (mCurrentNodeLevel == 4) {
                                        final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                        mPatient.setShortnessOfBreath(answer != 0.0f);
                                        if(mPatient.hasShortnessOfBreath()){
                                            selectedNode.addChild(
                                                    generateTreeQuizOutcome(Severities.Normal));
                                        }
                                        else {
                                            selectedNode.addChild(
                                                    addTreeBranch(Symptoms.DryCough, ANSWER_NO, ANSWER_YES));
                                        }
                                    }
                                    else {
                                        if(!mPatient.hasShortnessOfBreath()){
                                            final TreeNode selectedNode = mRecentTreeNodes[(int) answer];
                                            mPatient.setDryCough(answer != 0.0f);
                                            if(mPatient.hasDryCough()){
                                                selectedNode.addChild(
                                                        generateTreeQuizOutcome(Severities.Normal));
                                            }
                                            else {
                                                selectedNode.addChild(
                                                        generateTreeQuizOutcome(Severities.Light));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            if(mCurrentNodeLevel == 2) {
                                mPatient.setAge((int) answer);
                                if (mPatient.getAge() <= 64) {
                                    final TreeNode selectedNode = mRecentTreeNodes[0];
                                    selectedNode.addChild(generateTreeQuizOutcome(Severities.Normal));
                                } else {
                                    final TreeNode selectedNode = mRecentTreeNodes[1];
                                    selectedNode.addChild(
                                            addTreeBranch(Symptoms.ChestDistress, ANSWER_NO, ANSWER_YES));
                                }
                            }
                            else {
                                mPatient.setChestDistress(answer != 0.0f);
                                final TreeNode selectedNode = mRecentTreeNodes[(int)answer];
                                selectedNode.addChild(
                                        generateTreeQuizOutcome(
                                                mPatient.hasChestDistress()
                                                        ? Severities.Severe
                                                        : Severities.Normal)
                                );
                            }
                        }
                    }
                }
            }
        }
        catch (OutOfRangeAgeException ex){
            Log.w(TREE_QUIZ_ERRORS_TAG, ex.getMessage());
            TopSnack.show(mBinding.getRoot(), getString(R.string.age_accepted_range));
            showInputBox(Symptoms.Age);
        }
        catch (OutOfRangeBodyTemperatureException ex){
            Log.w(TREE_QUIZ_ERRORS_TAG, ex.getMessage());
            TopSnack.show(mBinding.getRoot(), getString(R.string.body_temperature_accepted_range));
            showInputBox(Symptoms.BodyTemperature);
        }
        catch (Exception ex){
            Log.e(TREE_QUIZ_ERRORS_TAG, ex.getMessage());
            TopSnack.show(mBinding.getRoot(), getString(R.string.quiz_unexpected_error));
        }
    }
}