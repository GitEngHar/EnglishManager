package com.bots;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BotTest {
    private Bot bot = new Bot();
    String userInfoFilePathURI = "./test/userinfo.txt";
    //String questiondataPathURI = "./dataset/";
    @Test
    public void helloUserTest(){
        bot.helloUser(userInfoFilePathURI);
        String mockUserName = "TestUser";
        String username = bot.username;
        assertThat(mockUserName,is(username));
    } 
}
