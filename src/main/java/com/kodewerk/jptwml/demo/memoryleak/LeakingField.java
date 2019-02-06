package com.kodewerk.jptwml.demo.memoryleak;

/********************************************
 * Copyright (c) 2019 Kirk Pepperdine
 * All right reserved
 ********************************************/

public class LeakingField {

    private String string = "more string stuff to leak";

    public LeakingField() {}

    public String toString() {
        return string;
    }
}
