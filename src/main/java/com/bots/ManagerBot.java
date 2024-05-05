package com.bots;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.io.File;
import java.io.IOException;
import com.quiz.*;
public class ManagerBot extends Bot implements Management  {
    private QuestionDatas questiondatas = new QuestionDatas();

    @Override
    public boolean userReady(String userInfoFilePathURI,String questiondataPathURI){
        this.questiondatas.questionDir = new File(questiondataPathURI);
        questiondatas.questionFiles = questiondatas.questionDir.listFiles();
        questiondatas.questionFilesList = new ArrayList<File>(Arrays.asList(questiondatas.questionFiles));
        questiondatas.questiondataPath = Paths.get(questiondataPathURI);
        super.helloUser(userInfoFilePathURI); // ユーザー名の登録確認と挨拶
        return isAvailableQuestionDataSet();
    }

    private boolean isAvailableQuestionDataSet(){
        boolean questionFlag = false;
        if(Files.exists(questiondatas.questiondataPath)){ //Dirの存在確認
            if(questiondatas.questionFiles.length > 0){
                questionFlag = true;  
            }else{
                System.out.printf("データを出題データを配置して再度実行してください. %n");  
            }
        }else{
            createQuestionDataDir(questiondatas.questiondataPath);
            System.out.printf("フォルダを作成しました. 出題データを配置して再度起動してください. %n");
        }
        return questionFlag;
    }
    
    @Override
    public boolean managerReady(int choseQuestionNumber){
        boolean managerFlag = false;
        try{
            createQuestion(choseQuestionNumber);// 出題する問題を作成できているか
            managerFlag = true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return managerFlag;
    }

    protected void createQuestionDataDir(Path questiondataPath){
        try{
            
            Files.createDirectories(questiondataPath);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public int confirmWantQuestionNumber(){
        int choseQuestionNumber = 0; 
        int numberOfFile = questiondatas.questionFiles.length;
        int minFileNumber = 1;
        System.out.println("出題してほしいファイル番号を選んでください");
        for(int i = 0;i<numberOfFile; i++){
            System.out.printf("%d : %s %n",i+1,questiondatas.questionFiles[i]);    
        }
        while(minFileNumber>choseQuestionNumber || choseQuestionNumber > numberOfFile){
            System.out.printf("番号(%d~%d) > ",minFileNumber,numberOfFile);
            if(super.sc.hasNextInt()){
                choseQuestionNumber = sc.nextInt();
            }
        }
        return choseQuestionNumber;
    }

    private void createQuestion(int choseQuestionNumber) throws IOException{
        Path choseQuestionPath = 
            Paths.get(
                this.questiondatas.questionFiles[choseQuestionNumber-1].toString()
            );
        this.questiondatas.questionStrings = Files.readString(choseQuestionPath);
        deleteQuestionEmptyLien();
        this.questiondatas.questionDataList = new ArrayList<String>(
            Arrays.asList(questiondatas.questionStrings.split("\n"))
            );
        Quiz engQuiz = new EnglishQuiz();
        this.questiondatas.questionData = engQuiz.autoCreate(questiondatas.questionDataList);
        this.questiondatas.answer = engQuiz.getAnswer();
    }
    
    private void deleteQuestionEmptyLien(){ //問題データの余計な空行を削除
        String targetQuestion = this.questiondatas.questionStrings.replaceAll("(\n|\r|\n\r|\r\n){2,}", "\n");
        String replacedQuestion = targetQuestion.replaceAll("[ \t\\x0B\f]+(\n|\r|\n\r|\r\n)", "");
        if(replacedQuestion.substring(replacedQuestion.length() - 1).equals("\n")){
            replacedQuestion=replacedQuestion.substring(0,replacedQuestion.length()-1);
        }
        this.questiondatas.questionStrings =  replacedQuestion;
    }

    @Override
    public void quizExit(){
        int numberOfQuestion = this.questiondatas.questionData.size();
        System.out.printf("試験スタート(問題数:%d)%n----------%n",numberOfQuestion);// クイズスタート
        IntStream.range(0, numberOfQuestion).forEach(i -> {
            System.out.printf("%s %n",this.questiondatas.questionData.get(i)); // 問題の出題
            confirmUserAnswer();// 入力を待つ
            System.out.printf("%s %n%n",this.questiondatas.answer.get(i)); // 回答の表示
        });
    }

    private String confirmUserAnswer(){
        StringBuilder userAnswer = new StringBuilder("");
        System.out.print("回答 > ");
        while(userAnswer.length() == 0){
            if(super.sc.hasNextLine()){
                userAnswer.append(sc.nextLine());
            }
        }
        return userAnswer.toString();
    }
}

class QuestionDatas {
    File questionDir;
    File[] questionFiles;
    List<File> questionFilesList;
    Path questiondataPath;
    String questionStrings;
    List<String> questionDataList; 
    List<String> questionData;
    List<String> answer;
}