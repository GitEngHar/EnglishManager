package com.bots;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class ManagerBotTest {
    private Management mngbot = new ManagerBot();
    String userInfoFilePathURI = "./test/userinfo.txt";
    String questiondataPathURI = "./test/dataset/";
    
    @Test    
    public void userReady(){
        boolean result = mngbot.userReady(userInfoFilePathURI, questiondataPathURI);
        boolean wantToResult = true;
        assertThat(result, is(wantToResult));
    } 
    @Test
    public void managerReady(){
        mngbot.userReady(userInfoFilePathURI, questiondataPathURI);
        boolean result = mngbot.managerReady(2);
        boolean wantToResult = true;
        assertThat(result, is(wantToResult));
    }
}
