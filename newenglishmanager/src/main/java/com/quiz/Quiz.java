package com.quiz;
import java.util.List;

public interface Quiz {
    public List<String> autoCreate(List<String> questionDataList);  // 渡した問題をランダムでパターンを選択し動作させる
    public List<String> manualCreate(List<String> questionDataList, int quizPatternNumber); // 渡した問題をパターンごとに選択して動作させる
    public List<String> getAnswer();
}
