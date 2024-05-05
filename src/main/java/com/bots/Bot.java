package com.bots;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Bot {
    protected Scanner sc = new Scanner(System.in, "Shift-JIS");
    String username = "";
    
    public void helloUser(String userInfoFilePathURI){
        File userInfoFile = new File(userInfoFilePathURI);
        try{
            confirmUserinfo(sc, userInfoFile, userInfoFilePathURI);
        }catch(IOException e){
            System.out.println("RegistUserInfoError\n" + e.getMessage());
            e.printStackTrace();
        }
        
    }

    protected void confirmUserinfo(Scanner sc,File userInfoFile,String userInfoFilePathURI) throws IOException{
        if(userInfoFile.exists()){//既に登録済みのユーザーであるかを確認する
            String readString[];
            readString = Files.lines(Paths.get(userInfoFilePathURI), Charset.forName("UTF-8"))
            .collect(Collectors.joining(System.getProperty("line.separator"))).split("name:");//ファイルから文字列を読み取る処理
            username = readString[1];
            System.out.printf("%sさん よろしくお願いします。%n",username);
        }else{
            System.out.print("あなたの名前を教えてください\n名前 >");
            if(sc.hasNextLine()){
                username = sc.nextLine();
            }
            createUserInfoFile(username,userInfoFile); //ユーザー情報入力に進む
            System.out.printf("%sさん これからよろしくお願いします。%n",username);
        }
    }

    protected void createUserInfoFile(String username,File userInfoFile) throws IOException{
            BufferedWriter bw = new BufferedWriter(new FileWriter(userInfoFile));
            bw.write("name:" + username);
            bw.close();
    }
}
