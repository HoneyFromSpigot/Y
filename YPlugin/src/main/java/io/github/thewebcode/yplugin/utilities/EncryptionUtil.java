package io.github.thewebcode.yplugin.utilities;

public class EncryptionUtil {
    public static String genPassword(int length){
        String password = "";
        for(int i = 0; i < length; i++){
            password += (char) (Math.random() * 26 + 97);
        }
        return password;
    }

    public static String randomCase(String string){
        String newString = "";
        for(int i = 0; i < string.length(); i++){
            if(Math.random() > 0.5){
                newString += string.substring(i, i + 1).toUpperCase();
            }else{
                newString += string.substring(i, i + 1).toLowerCase();
            }
        }
        return newString;
    }
}
