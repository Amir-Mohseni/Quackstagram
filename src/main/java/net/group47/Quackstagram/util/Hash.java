package net.group47.Quackstagram.util;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Hash {

    private Argon2 argon2;

    public Hash(){
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    }

    public String hash(String password) {
        return argon2.hash(2, 15 * 1024, 1, password.toCharArray());
    }

    public boolean matches(String password, String hash){
        return argon2.verify(hash, password.toCharArray());
    }

}
