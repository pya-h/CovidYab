package com.thcplusplus.covidyab;

import android.support.annotation.NonNull;
import android.util.Log;


public class Probability {
    private float mLightness, mNormality, mSevereness;
    public static final String PROBABILITY_ERROR = "ERROR_PROBABILITY";
    public Probability(){
        this.mLightness = this.mNormality = this.mSevereness = 0.0f;
    }
    public Probability(float lightness, float normality, float severeness){
        this.mLightness = lightness;
        this.mNormality = normality;
        this.mSevereness = severeness;
    }

    public static float round(float value){
        //round a number up to 2 decimals
        return Math.round(value * 100f) / 100f;
    }
    public float getLightness() {
        return mLightness;
    }

    public void setLightness(float lightness) {
        this.mLightness = lightness;
    }

    public float getNormality() {
        return mNormality;
    }

    public void setNormality(float normality) {
        this.mNormality = normality;
    }

    public float getSevereness() {
        return mSevereness;
    }

    public void setSevereness(float severeness) {
        this.mSevereness = severeness;
    }

    public void set(float lightness,float normality,float severeness){
        this.mLightness = lightness;
        this.mNormality = normality;
        this.mSevereness = severeness;

    }

    public static Probability average(Probability[] probabilities){
        try {
            float light = 0.0f, normal = 0.0f, severe = 0.0f; // sum of each one

            for (Probability probability : probabilities) {
                light += probability.getLightness();
                normal += probability.getNormality();
                severe += probability.getSevereness();
            }

            final int count = probabilities.length;

            return new Probability(
                    round(light / count),
                    round(normal / count),
                    round(severe / count)
            );

        } catch (Exception ex){
            Log.e(PROBABILITY_ERROR, ex.getMessage());
        }
        return new Probability();
    }

    @NonNull @Override
    public String toString() {
        return "Light: " + getLightness() + "\tNormal: " + getNormality()
                + "\t Severe: " + getSevereness();
    }
}
