/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class EmployeeMenuLoader implements Runnable {

    Thread t;
    static boolean isLoadingEmployeeMenu = false;

    EmployeeMenuLoader() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            if (!isLoadingEmployeeMenu) {
                isLoadingEmployeeMenu = true;
                State.employeeMenu.load();
                EmployeeMenu.loadLodgeImages();
                isLoadingEmployeeMenu = false;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
