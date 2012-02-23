package org.integrityrater.web;

import org.integrityrater.entity.Challenge;


public class ChallengeForm {
    
    public static final String KEY = "challengeForm"; 
    
    private Challenge challenge;

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }


}
