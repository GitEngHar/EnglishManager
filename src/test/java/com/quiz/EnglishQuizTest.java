package com.quiz;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import org.junit.Test;


public class EnglishQuizTest {
    @Test
    public void testCreateArrayOfProcessingQuestion()
    {
        List<int []> wantToResultItems = new ArrayList<>();
        int [] case1WantToResultItem = {8,4,6,4};
        int [] case2WantToResultItem = {8,6,6};
        int [] case3WantToResultItem = {10,8};
        int [] case4WantToResultItem = {14,12,14,12,14,12,14};//7 | 92
        wantToResultItems.add(case1WantToResultItem);
        wantToResultItems.add(case2WantToResultItem);
        wantToResultItems.add(case3WantToResultItem);
        wantToResultItems.add(case4WantToResultItem);
        wantToResultItems.stream().forEach(iArray -> {
            EnglishQuiz englishQuiz = new EnglishQuiz();
            englishQuiz.numberOfquestionPattern = iArray.length;
            testCreateArrayOfProcessingQuestion actCase = new testCreateArrayOfProcessingQuestion(iArray,Arrays.stream(iArray).sum());
            List<Integer> result = new ArrayList<Integer>(englishQuiz.createArrayOfProcessingQuestion(actCase.getNumberOfQuestion()));
            assertThat(result, is(actCase.getWantToResult()));
        });
        }
    
}

class testCreateArrayOfProcessingQuestion{
    int [] wantToResultItem;
    List<Integer> wantToResult;
    int numberOfQuestion;
    testCreateArrayOfProcessingQuestion(int[] wantToResultItem,int numberOfQuestion){
        this.wantToResult = new ArrayList<>();
        this.wantToResultItem = wantToResultItem;
        this.numberOfQuestion = numberOfQuestion;
        setWantToResult();
    }
    private void setWantToResult(){
        for(int i:wantToResultItem){
            this.wantToResult.add(i);
        }
    }
    protected List<Integer> getWantToResult(){
        return wantToResult;
    }
    protected int getNumberOfQuestion(){
        return numberOfQuestion;
    }
}
