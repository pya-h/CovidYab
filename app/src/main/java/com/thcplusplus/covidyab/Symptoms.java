package com.thcplusplus.covidyab;

import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public enum Symptoms {
    Gender,
    Age,
    BodyTemperature,
    Headache,
    SoreThroat,
    DryCough,
    DiseaseBackground,
    ShortnessOfBreath,
    ChestDistress;
    @NonNull
    public String toString(RelativeLayout root) { // for HealthQuizFragment that wants actual questions,
        // because root element of fragment_health_quiz  is LinearLayout
        // this toString overload gets symptom related question
        return root.getResources()
                .getStringArray(R.array.symptom_fields)[ordinal()];
    }

    @NonNull
    public String toString(LinearLayout root) { // for HealthQuizFragment that wants actual questions,
        // because root element of fragment_health_quiz  is LinearLayout
        // this toString overload gets symptom related question
        return root.getResources()
                .getStringArray(R.array.quiz_questions)[ordinal()];
    }

    public static int count(){
        return values().length;
    }

    public boolean isLastOne() {
        return ordinal() + 1 == values().length;
    }

    public Symptoms next() {
        final Symptoms[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    public Boolean isYesOrNoQuestion(){
        final int index = ordinal();
        return index >= Headache.ordinal() && index <= ChestDistress.ordinal();
    }
}
