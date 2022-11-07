package com.thcplusplus.covidyab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;
import java.util.UUID;

class Patient {
    private Genders mGender;
    private float mBodyTemperature;
    private int mAge;
    private Boolean mHeadache, mSoreThroat, mDryCough, mDiseaseBackground,
            mShortnessOfBreath, mChestDistress;
    private final Probability[] mProbabilities;
    private UUID mID;

    public static final String ERROR_SYMPTOM_SUBMIT = "PATIENT_SUBMIT_ERROR";
    public static final String INFO_PATIENT_TAG = "PATIENT_INFO";

    public Patient(){
        this.mProbabilities = new Probability[Symptoms.count()];
    }

    public Patient(Genders gender, int age, float bodyTemperature)
            throws OutOfRangeAgeException,
                OutOfRangeBodyTemperatureException {
        this.mID = UUID.randomUUID();
        this.mProbabilities = new Probability[Symptoms.count()];
        this.setGender(gender);
        this.setAge(age);
        this.setBodyTemperature(bodyTemperature);

    }

    public Patient(Genders gender, int age, float bodyTemperature, Boolean headache,
                   Boolean soreThroat, Boolean dryCough, Boolean diseaseBackground, Boolean shortnessOfBreath,
                   Boolean chestDistress) throws OutOfRangeAgeException, OutOfRangeBodyTemperatureException {
        this.mID = UUID.randomUUID();
        this.mProbabilities = new Probability[Symptoms.count()];
        this.setGender(gender);
        this.setAge(age);
        this.setBodyTemperature(bodyTemperature);
        this.setHeadache(headache);
        this.setSoreThroat(soreThroat);
        this.setDryCough(dryCough);
        this.setDiseaseBackground(diseaseBackground);
        this.setShortnessOfBreath((shortnessOfBreath));
        this.setChestDistress(chestDistress);
    }

    // toString():
    @NonNull
    @Override
    public String toString() {
        return "A " + this.getAge() + " years old " + this.getGender();
    }

    // submit yes or no questions to symptom fields
    public Symptoms submit(Symptoms currentSymptom, Boolean answer){
        try {
            switch (currentSymptom) {
                case Headache:
                    this.setHeadache(answer);
                    break;
                case SoreThroat:
                    this.setSoreThroat(answer);
                    break;
                case DryCough:
                    this.setDryCough(answer);
                    break;
                case DiseaseBackground:
                    this.setDiseaseBackground(answer);
                    break;
                case ShortnessOfBreath:
                    this.setShortnessOfBreath(answer);
                    break;
                case ChestDistress:
                    this.setChestDistress(answer);
                    break;
            }

            return currentSymptom.next();
        } catch (Exception ex){
            Log.e(ERROR_SYMPTOM_SUBMIT, ex.getMessage());
            return currentSymptom;
        }
    }
    // END OF INPUT MANAGERS

    // END OF SETTERS -> FOR SYMPTOMS PROBABILITY CALCULATORS
    public void setGender(Genders gender) {
        this.mGender = gender;

        final int index = Symptoms.Gender.ordinal();
        if(gender == Genders.Male)//yes
            mProbabilities[index] = new Probability(23, 71, 6);
        else if(gender == Genders.Female)//no headaches
            mProbabilities[index] = new Probability(27, 58, 15);
        else // other states that gender is other; not happening in the application though
            mProbabilities[index] = new Probability(); //set probabilities to zero.
    }

    public void setBodyTemperature(float temperature)
            throws OutOfRangeBodyTemperatureException {
        if(temperature < 36 || temperature > 40)
            throw new OutOfRangeBodyTemperatureException(temperature);
        this.mBodyTemperature = temperature;

        // set probabilities
        final int index = Symptoms.BodyTemperature.ordinal();
        if(temperature < 37.3) // < 37.3
            mProbabilities[index] = new Probability(22, 61, 17);
        else if(temperature <= 38) // 37.3 -> 38.0
            mProbabilities[index] = new Probability(23, 66, 11);
        else if(temperature <= 39) // 38.1***** -> 39.0
            mProbabilities[index] = new Probability(25, 65, 10);
        else // >= 39.0
            mProbabilities[index] = new Probability(33, 59, 8);
    }

    public void setAge(int age) throws OutOfRangeAgeException {
        if(age <= 0 || age > 250) // maximum maximum wide range
            throw new OutOfRangeAgeException(age);
        this.mAge = age;

        // set probabilities
        final int index = Symptoms.Age.ordinal();
        if(age <= 14) // < 15
            mProbabilities[index] = new Probability(20, 80, 0);
        else if(age <= 39) // 15 -> 39
            mProbabilities[index] = new Probability(28, 62, 10);
        else if(age <= 64) // 40 -> 64
            mProbabilities[index] = new Probability(23, 67, 10);
        else // >= 65
            mProbabilities[index] = new Probability(20, 60, 20);
    }

    public void setHeadache(Boolean headache) {
        this.mHeadache = headache;
         // set probabilities
        final int index = Symptoms.Headache.ordinal();
        if(headache)//yes
            mProbabilities[index] = new Probability(27, 53, 20);
        else //no headaches
            mProbabilities[index] = new Probability(24, 67, 9);
    }

    public void setSoreThroat(Boolean soreThroat) {
        this.mSoreThroat = soreThroat;

        // set probabilities
        final int index = Symptoms.SoreThroat.ordinal();
        if(soreThroat) //yes
            mProbabilities[index] = new Probability(30, 50, 10);
        else //no sore throats
            mProbabilities[index] = new Probability(24, 66, 10);
    }

    public void setDryCough(Boolean dryCough) {
        this.mDryCough = dryCough;

        // set probabilities
        final int index = Symptoms.DryCough.ordinal();
        if(dryCough)//yes
            mProbabilities[index] = new Probability(25, 64, 11);
        else //no dry coughs
            mProbabilities[index] = new Probability(24, 65, 11); // common line in if/else
    }


    public void setDiseaseBackground(Boolean diseaseBackground) {
        this.mDiseaseBackground = diseaseBackground;

        // set probabilities
        final int index = Symptoms.DiseaseBackground.ordinal();
        if(diseaseBackground)//yes
            mProbabilities[index] = new Probability(27, 64, 9);
        else //no backgrounds
            mProbabilities[index] = new Probability(24, 65, 11);
    }


    public void setShortnessOfBreath(Boolean shortnessOfBreath) {
        this.mShortnessOfBreath = shortnessOfBreath;

        // set probabilities
        final int index = Symptoms.ShortnessOfBreath.ordinal();
        if(shortnessOfBreath) //yes
            mProbabilities[index] = new Probability(24, 60, 16);
        else //no trouble in breathing
            mProbabilities[index] = new Probability(24, 65, 11);
    }

    public void setChestDistress(Boolean chestDistress) {
        this.mChestDistress = chestDistress;

        // set probabilities
        final int index = Symptoms.ChestDistress.ordinal();
        if(chestDistress) //yes
            mProbabilities[index] = new Probability(25, 60, 15);
        else //no chest distresses
            mProbabilities[index] = new Probability(24, 66, 10);
    }
    // END OF SETTERS -> FOR SYMPTOMS PROBABILITY CALCULATORS

    // SURVEY THE DISEASE TREE and find most matching severity
    public Severities getDiseaseSeverity(){
        // according to the symptoms TREE
        if(hasHeadache()){
            // right hand of the tree
            if(this.mAge >= 40 && this.mAge <= 64){
                if(hasDiseaseBackground()) return Severities.Light;
                else {
                    if(hasDryCough()){
                        if(this.mBodyTemperature > 39.0f) return Severities.Light;
                        else if(this.mBodyTemperature >= 37.3f){ // && this.mBodyTemperature <= 39.0f
                            if(hasChestDistress()) return Severities.Normal;
                            else{
                                return this.mBodyTemperature >= 38.1f && this.mBodyTemperature <= 39.0f
                                        ? Severities.Normal
                                        : Severities.Severe;
                                /* Above Code's Equivalent:
                                     if(this.mBodyTemperature >= 38.1f && this.mBodyTemperature <= 39.0f) return Severities.Normal;
                                    else if(this.mBodyTemperature >= 37.3f && this.mBodyTemperature <= 38.0f)
                                        return Severities.Sever;
                                 */
                            }
                        }
                    }
                    else return Severities.Normal;
                }
            }
            else {
                if(hasDryCough()){
                    if(hasShortnessOfBreath()) return Severities.Light;
                    else return Severities.Normal;
                } else{
                    if(hasChestDistress()) return Severities.Light;
                    else {
                        if(this.mAge >= 65) return Severities.Light;
                        else {
                            if(hasDiseaseBackground()) return Severities.Normal;
                            else {
                                if(this.mAge < 15) return Severities.Light;
                                else return this.mBodyTemperature >= 39.0f ? Severities.Light : Severities.Normal;
                            }
                        }
                    }
                }
            }
        }
        else {
            // left hand of the tree
            if(hasSoreThroat()){
                if(this.mAge >= 40) return Severities.Normal;
                else if(this.mAge >= 15) { // & <= 39
                    if(this.mBodyTemperature <= 37.3f) return Severities.Light;
                    else if(this.mBodyTemperature <= 39.0f){ // && this.mBodyTemperature > 37.3
                        if(hasShortnessOfBreath()) return Severities.Normal;
                        else return hasDryCough() ? Severities.Normal : Severities.Light;
                    }
                }
            }
            else {
                if(this.mAge >= 65) return hasChestDistress() ? Severities.Severe : Severities.Normal;
                else return Severities.Normal;
            }
        }
        return Severities.UnCertain;
    }
    // END OF TREE SURVEYING

    // BUNDLE INTERACTIONS
    private static final String BUNDLE_GENDER = "PATIENT_GENDER", BUNDLE_AGE = "PATIENT_AGE", BUNDLE_BODY_TEMP = "PATIENT_BODY_TEMP",
            BUNDLE_HEADACHE = "PATIENT_HEADACHE", BUNDLE_SORE_THROAT = "PATIENT_SORE_THROAT",
            BUNDLE_DRY_COUGH = "PATIENT_DRY_COUGH", BUNDLE_DISEASE_BACKGROUND = "PATIENT_DISEASE_BACKGROUND",
            BUNDLE_SHORTNESS_OF_BREATH = "PATIENT_SHORTNESS_OF_BREATH", BUNDLE_CHEST_DISTRESS = "PATIENT_CHEST_DISTRESS";

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        //put extracted fields in a bundle and return to put on a fragment
        bundle.putInt(BUNDLE_GENDER, this.getGender().ordinal());
        bundle.putInt(BUNDLE_AGE, this.getAge());
        bundle.putFloat(BUNDLE_BODY_TEMP, this.getBodyTemperature());
        bundle.putBoolean(BUNDLE_HEADACHE, this.hasHeadache());
        bundle.putBoolean(BUNDLE_SORE_THROAT, this.hasSoreThroat());
        bundle.putBoolean(BUNDLE_DRY_COUGH, this.hasDryCough());
        bundle.putBoolean(BUNDLE_DISEASE_BACKGROUND, this.hasDiseaseBackground());
        bundle.putBoolean(BUNDLE_SHORTNESS_OF_BREATH, this.hasShortnessOfBreath());
        bundle.putBoolean(BUNDLE_CHEST_DISTRESS, this.hasChestDistress());

        return bundle;
    }

    public static Patient toPatient(Bundle bundle) throws
            OutOfRangeAgeException, NumberFormatException,
            OutOfRangeBodyTemperatureException { //convert bundle to Patient
        final Genders gender = Genders.convert(bundle.getInt(BUNDLE_GENDER));
        final int age = bundle.getInt(BUNDLE_AGE);
        final float bodyTemperature = bundle.getFloat(BUNDLE_BODY_TEMP);
        final Boolean headache = bundle.getBoolean(BUNDLE_HEADACHE);
        final Boolean soreThroat = bundle.getBoolean(BUNDLE_SORE_THROAT);
        final Boolean dryCough = bundle.getBoolean(BUNDLE_DRY_COUGH);
        final Boolean diseaseBackground = bundle.getBoolean(BUNDLE_DISEASE_BACKGROUND);
        final Boolean shortnessOfBreath = bundle.getBoolean(BUNDLE_SHORTNESS_OF_BREATH);
        final Boolean chestDistress = bundle.getBoolean(BUNDLE_CHEST_DISTRESS);

        return new Patient(gender, age, bodyTemperature, headache, soreThroat, dryCough, diseaseBackground, shortnessOfBreath, chestDistress);
    }
    // END OF BUNDLE INTERACTIONS

    // CONVERTERS
    public float getFahrenheitBodyTemperature() {
        return 32.0f + (this.getBodyTemperature() * 9 / 5);
    }

    public float getKelvinBodyTemperature() {
        return 273.15f + this.getBodyTemperature();
    }

    public static float toCelsius(float fahrenheit){
        return (fahrenheit - 32.0f) * 5 / 9;
    }

    public static float kelvinToCelsius(float kelvin){
        return kelvin - 273.15f;
    }
    // END OF CONVERTERS

    // Getters
    public Probability getProbability(){
        Log.i(INFO_PATIENT_TAG, Arrays.toString(this.mProbabilities));
        return Probability.average(this.mProbabilities);
    }

    public Genders getGender() {
        return this.mGender;
    }
    public float getBodyTemperature() {
        return this.mBodyTemperature;
    }
    public int getAge() {
        return this.mAge;
    }
    public Boolean hasHeadache() {
        return this.mHeadache;
    }
    public Boolean hasSoreThroat() {
        return this.mSoreThroat;
    }
    public Boolean hasDryCough() {
        return this.mDryCough;
    }
    public Boolean hasDiseaseBackground() {
        return this.mDiseaseBackground;
    }
    public Boolean hasShortnessOfBreath() {
        return this.mShortnessOfBreath;
    }
    public Boolean hasChestDistress() {
        return this.mChestDistress;
    }

}
