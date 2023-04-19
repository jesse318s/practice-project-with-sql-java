/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class ManagerMenuLoader implements Runnable {

    Thread t;
    static boolean isLoading = false;

    ManagerMenuLoader() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            if (!isLoading) {
                isLoading = true;
                State.managerMenu.load();
                isLoading = false;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
