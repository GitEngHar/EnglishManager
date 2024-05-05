package com.bots;

public interface Management {
    public boolean userReady(String userInfoFilePath,String questiondataPathURI);
    public boolean managerReady(int choseQuestionNumber);
    public int confirmWantQuestionNumber();
    public void quizExit();
}
