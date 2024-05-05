package com.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class EnglishQuiz implements Quiz{
    private Phrase phrase = new Phrase();
    protected int numberOfquestionPattern = 2;

    String alphabetPattern = ".*[a-zA-Z].*";
    Pattern englishPattern = Pattern.compile(alphabetPattern);

    @Override
    public List<String> manualCreate(List<String> questionDataList,int quizPatternNumber){
        List<String> createdQuestionDatas = new ArrayList<>();  
        this.phrase.resetPhrase();
        switch(quizPatternNumber){
            case 1:
                createdQuestionDatas = translation(questionDataList);
                break;
            case 2:
                createdQuestionDatas = leafwarber(questionDataList);
                break;
        }
        return createdQuestionDatas;
    }
    public List<String> autoCreate(List<String> questionDataList){
        List<String> createdQuestionDatas = new ArrayList<>();
        Random rand = new Random();
        List<String> splitQuestionDataList;
        int beforeProcessingCountNumber = 0;
        int questionPatternCase;
        List<Integer> numberOfProcessingQuestions = new ArrayList<>(createArrayOfProcessingQuestion(questionDataList.size()));
        for(int i = 0; i < numberOfProcessingQuestions.size(); i++ ){
            questionPatternCase = rand.nextInt(this.numberOfquestionPattern) + 1;
            splitQuestionDataList = new ArrayList<>();
            for(int j = 0; j < numberOfProcessingQuestions.get(i); j++){
                int getQuestionDataElementNumber = j + beforeProcessingCountNumber;
                splitQuestionDataList.add(questionDataList.get(getQuestionDataElementNumber));
            }
            createdQuestionDatas.addAll(manualCreate(splitQuestionDataList, questionPatternCase));
            beforeProcessingCountNumber += numberOfProcessingQuestions.get(i);
        }
        return createdQuestionDatas;
    }

    protected List<Integer> createArrayOfProcessingQuestion(int numberOfQuestion){
        List<Integer> numberOfProcessingQuestions = new ArrayList<>();
        int questionPartision = numberOfQuestion / this.numberOfquestionPattern;
        int overValue = numberOfQuestion % this.numberOfquestionPattern;
        int processingQuestionPartision = 0;
        for(int i=0;i<this.numberOfquestionPattern;i++){
            processingQuestionPartision = questionPartision;
            if(overValue >= 2){//余りが存在する処理 
                overValue -= 2;
                processingQuestionPartision +=2;
            }
            else if(overValue == 1){//余りが1の場合
                overValue -= 1;
                processingQuestionPartision +=1;
            }
            if(processingQuestionPartision % 2 == 1){//奇数の処理
                if(i % 2 == 0){ //偶数
                    processingQuestionPartision +=1;
                }else{//奇数
                    processingQuestionPartision -=1;
                }
            }
            numberOfProcessingQuestions.add(processingQuestionPartision);
        }

        return numberOfProcessingQuestions;
    }
    private List<String> translation(List<String> questionDataList){
        String explain = "Q. 次の文を翻訳してください\n";
        List<String> quesitonDatas = new ArrayList<>();
        questionDataList.stream().filter(questionString -> isEnglish(questionString)).forEach(s -> this.phrase.answerPhrase.add("A. " + s)); //問題の格納
        questionDataList.stream().filter(questionString -> !(isEnglish(questionString))).forEach(s -> quesitonDatas.add(explain + s)); //答えの格納
        return quesitonDatas;
    }

    private List<String> leafwarber(List<String> questionDataList){
        List<String> quesitonDatas = new ArrayList<>();
        List<String []> allQuestionEnglishWords = new ArrayList<>(); //StringBufferのほうがいい？？ -> 文字列の結合や変更が発生しやすい
        List<String> allQuestionJapanesePhrase = new ArrayList<>();
        questionDataList.stream().filter(questionString -> isEnglish(questionString)).forEach(s -> allQuestionEnglishWords.add(s.split(" ")));//英文の取得 , 英文章の分割 split " "
        questionDataList.stream().filter(questionString -> !(isEnglish(questionString))).forEach(s -> allQuestionJapanesePhrase.add(s)); //日本語を全て抽出
        createLeafwarberQuestionPhrase(allQuestionJapanesePhrase);//日本問題文作成
        createLeafwarberQuestionElementPhraseAndSetAnswer(allQuestionEnglishWords);//問題要素の作成と答えの作成とセット
        IntStream.range(0, this.phrase.questionPhrase.size()).forEach(i -> {//問題文の作成
            quesitonDatas.add(this.phrase.questionPhrase.get(i) + this.phrase.qurstionElementPhrase.get(i));
        });
        return quesitonDatas;
    }
    private void createLeafwarberQuestionPhrase(List<String> allQuestionJapanesePhrase){
        String explain = "Q. 日本文を参考に次の□を埋めてください\n";
        allQuestionJapanesePhrase.stream().forEach(s -> 
            this.phrase.questionPhrase.add(explain + s + "\n")
        );
    }
    private void createLeafwarberQuestionElementPhraseAndSetAnswer(List<String[]> allQuestionEnglishWords){
        Random rand = new Random();
        allQuestionEnglishWords.stream().forEach(s -> {//全ての 単語分割済み 英文処理
            StringBuilder amswerphrase = new StringBuilder();
            StringBuilder questionphrase = new StringBuilder();
            for(String questionEnglishWord : s){//英単語 処理
                StringBuilder answerWord = new StringBuilder();
                StringBuilder questionEnglishWordBuilder = new StringBuilder(questionEnglishWord);
                List<Integer> sortedPositionArrays = new ArrayList<>(); 
                int numberOfReplaceTargetChar = questionEnglishWord.length()/2;
                if(numberOfReplaceTargetChar >= 1){
                    List<Integer> positionArrays = new ArrayList<>();
                    IntStream.range(0,numberOfReplaceTargetChar).forEach (//置き換えたい英単語数分の処理 答えの作成
                        i -> {//置き換えたい文字数分のループ
                            int replaceCharPosition = rand.nextInt(numberOfReplaceTargetChar); //乱数の生成
                            positionArrays.add(replaceCharPosition);//乱数位置を追加
                            questionEnglishWordBuilder.replace(replaceCharPosition,replaceCharPosition+1,"□");//虫食い単語に置き換え
                        }
                    );
                    sortedPositionArrays = positionArrays.stream().sorted().distinct().toList(); //乱数をソート
                    sortedPositionArrays.stream().forEach(i -> answerWord.append(questionEnglishWord.charAt(i))); //単語作成
                    amswerphrase.append(answerWord.toString() + " ");//単語からフレーズを作成
                }
                questionphrase.append(questionEnglishWordBuilder.toString() + " ");
            }
            this.phrase.qurstionElementPhrase.add(questionphrase.toString());
            this.phrase.answerPhrase.add("A. " + amswerphrase.toString());
        });
    }

    private boolean isEnglish(String targetWord){
        Matcher englishMatch = englishPattern.matcher(targetWord);
        return englishMatch.matches(); 
    }

    public List<String> getAnswer(){
        return this.phrase.answerPhrase;
    }
}

class Phrase{
    List<String> questionPhrase;
    List<String> qurstionElementPhrase;
    List<String> answerPhrase;
    public Phrase(){
        this.answerPhrase = new ArrayList<>();
        this.questionPhrase = new ArrayList<>();
        this.qurstionElementPhrase = new ArrayList<>();
    }
    public void resetPhrase(){
        this.questionPhrase = new ArrayList<>();
        this.qurstionElementPhrase = new ArrayList<>();        
    }
}
