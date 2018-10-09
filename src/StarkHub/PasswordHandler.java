package StarkHub;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

class PasswordHandler implements Serializable {
    public static String passwordHash;

    /**
     * creates a new file and saves the password in SHA256 to password.ser
     */
    PasswordHandler(){
        try {
            FileReader reader = new FileReader("password.ser");
            int character;
            String s="";
            while ((character = reader.read()) != -1) {
                s+=(char)character;
            }
            passwordHash = s;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param password
     * @return <b>true</b> if password's SHA256 matches to saved password in password.ser else <b>false</b>
     */
    boolean checkPassword(String password){
        if(SHA256Handler.getSha256(password).equals(passwordHash)){
            return true;
        }
        else{
            System.out.println(password+"\n"+SHA256Handler.getSha256(password)+":"+passwordHash);
            return false;
        }
    }
}
