package com.englishmanager;
import com.bots.*;
public class EnglishManager {
    protected void quizExit(){
        String userInfoFilePathURI = "./userinfo.txt";
        String questiondataPathURI = "./dataset/";
        Management manager = new ManagerBot();
        int choseQuestionNumber;
        boolean isuserReady = manager.userReady(userInfoFilePathURI,questiondataPathURI);
        choseQuestionNumber = manager.confirmWantQuestionNumber();
        boolean ismanagerReady = manager.managerReady(choseQuestionNumber);
        if(isuserReady && ismanagerReady){
            manager.quizExit();
        };
    }
    
}
