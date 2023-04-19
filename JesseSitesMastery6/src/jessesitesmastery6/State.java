/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

import java.sql.SQLException;

/**
 *
 * @author jesse
 */
public abstract class State {

    static State login, employeeMenu, managerMenu, customerMenu, lodgeImageMenu,
            current, previous;

    public abstract void enter();

    public abstract void update();

    public abstract void load() throws SQLException;

    public abstract void save() throws SQLException;
}
