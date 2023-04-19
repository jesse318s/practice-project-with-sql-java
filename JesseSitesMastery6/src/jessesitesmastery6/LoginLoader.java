/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class LoginLoader implements Runnable {

    Thread t;

    LoginLoader() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            State.login.load();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
