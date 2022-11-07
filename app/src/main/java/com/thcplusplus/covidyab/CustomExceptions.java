package com.thcplusplus.covidyab;

class OutOfRangeAgeException extends Exception{

    public OutOfRangeAgeException(int age){
        super("OutOfRangeAgeException:\nage is not logical.\tentered: " + age);
    }

    public OutOfRangeAgeException(int age, Throwable rootException){
        super("OutOfRangeAgeException:\nage is not logical.\tentered: " + age, rootException);
    }
}

class OutOfRangeBodyTemperatureException extends Exception {

    public OutOfRangeBodyTemperatureException(float temperature) {
        super("OutOfRangeBodyTemperatureException:\ntemperature entered is for the dead.\tentered: " + temperature);
    }

    public OutOfRangeBodyTemperatureException(float temperature, Throwable rootException) {
        super("OutOfRangeBodyTemperatureException:\ntemperature entered is for the dead.\tentered: " + temperature, rootException);
    }
}

class InvalidYesOrNoQuestion extends Exception {
    public InvalidYesOrNoQuestion(Symptoms question) {
        super("InvalidYesOrNoQuestion:\nQuestion is not a yes or no; question: " + question.toString());
    }

    public InvalidYesOrNoQuestion(Symptoms question, Throwable rootException) {
        super("InvalidYesOrNoQuestion:\nQuestion is not a yes or no; question: " + question.toString(), rootException);
    }
}