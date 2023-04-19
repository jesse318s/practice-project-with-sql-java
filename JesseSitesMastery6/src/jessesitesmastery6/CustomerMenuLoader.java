/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class CustomerMenuLoader implements Runnable {

    Thread t;
    static boolean isLoading = false;

    CustomerMenuLoader() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            if (!isLoading) {
                isLoading = true;
                State.customerMenu.load();
                EmployeeMenu.loadLodgeImages();
                isLoading = false;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
