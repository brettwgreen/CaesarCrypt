package com.nervii.caesarcrypt;

import android.text.Editable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Brett on 6/25/2015.
 */
public class Cipher
{
    public static String RandomizeCipher(String alpha){
        String randomCipher = "";
        ArrayList<Character> alphaArray = new ArrayList<>();
        for (int i=0; i<alpha.length(); i++){
            alphaArray.add(alpha.charAt(i));
        }
        Random rand = new Random();
        int randomNum;
        while (alphaArray.size() > 0) {
            if  (alphaArray.size() == 1) {
                randomNum = 0;
            }
            else {
                randomNum = rand.nextInt(alphaArray.size() - 1) + 1;
                if (randomNum > alphaArray.size() - 1) {
                    randomNum = alphaArray.size() - 1;
                }
            }

            randomCipher += alphaArray.get(randomNum);
            alphaArray.remove(randomNum);
        }
        return randomCipher;
    }

    public static String EncryptMessage(String s, String alpha, String cipher) {
        if (alpha.length() != cipher.length()) {
            throw new IllegalArgumentException("alpha and cipher string must have same length");
        }
        String eMsg = "";
        for (int i=0; i < s.length(); i++) {
            String character = Character.toString(s.charAt(i));
            String charUpper = character.toUpperCase();
            boolean lowerCase = false;
            if (!charUpper.equals(character)) {
                lowerCase = true;
            }
            int index = alpha.indexOf(charUpper);
            if (index == -1) {
                eMsg += character;
            }
            else {
                String eChar = Character.toString(cipher.charAt(index));
                eMsg += lowerCase ? eChar.toLowerCase() : eChar;
            }
        }
        return eMsg;
    }
}
