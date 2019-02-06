package com.kodewerk.jptwml.demo.memoryleak;

/********************************************
 * Copyright (c) 2019 Kirk Pepperdine
 * All right reserved
 ********************************************/

import java.util.ArrayList;


public class LeakyModel {

    private ArrayList<LeakingClass> leaker;

    public LeakyModel() {
        leaker = new ArrayList<>();
    }

    public void leak(int range) {
        char[] aString = { 's','o','m','e',' ','l','e','a','k','i','n','g',' ','s','t','u','f','f'};
        for ( int i = 0; i < range; i++) {
            leaker.add(new LeakingClass(new String(aString), new LeakingField()));
        }
    }
}
