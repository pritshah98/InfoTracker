package model;

/**
 * Created by pritshah on 8/1/17.
 */

public class User {
    private String username;
    private String password;

    public User(){

    }

    public User(String u, String p) {
        username = u;
        password = p;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }
}
