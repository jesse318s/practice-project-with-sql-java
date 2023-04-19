/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author jesse
 */
public class ManagerMenu extends State {

    // connection status label
    static JLabel labelConnectionStatus = new JLabel();

    @Override
    public void enter() {
        Component[] components = JesseSitesMastery6.managerPanel.getComponents();

        ManagerMenuLoader loader = new ManagerMenuLoader();
        ((JLabel) components[0]).setText("User: " + JesseSitesMastery6.loginName);
        update();
    }

    @Override
    public void update() {
        while (true) {
            JesseSitesMastery6.managerPanel.revalidate();
            JesseSitesMastery6.managerPanel.repaint();

            if (JesseSitesMastery6.employeeTabs.getSelectedIndex() == 0) {
                State.current = State.employeeMenu;
                State.previous = State.managerMenu;
                break;
            }

            if (!JesseSitesMastery6.employeeTabs.isVisible()) {
                break;
            }
        }

        State.current.enter();
    }

    @Override
    public void load() {
        try {
            login.load();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    @Override
    public void save() {
        try {
            login.save();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    public void loadTableEmployees() {
        int customerCount = 0;

        for (Component comp : JesseSitesMastery6.managerPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JesseSitesMastery6.managerPanel.remove(comp);
            }
        }

        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            if (JesseSitesMastery6.people.get(i) instanceof Customer) {
                customerCount++;
            }
        }

        Object[][] data = new Object[JesseSitesMastery6.people.size() - customerCount][9];

        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            if (JesseSitesMastery6.people.get(i) instanceof TravelAgencyEmployee) {
                data[i - customerCount][0] = JesseSitesMastery6.people.get(i).idNumber;
                data[i - customerCount][1] = JesseSitesMastery6.people.get(i).name;
                data[i - customerCount][2] = JesseSitesMastery6.people.get(i).address;
                data[i - customerCount][3] = JesseSitesMastery6.people.get(i).phoneNumber;
                data[i - customerCount][4] = ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).hireDate;
                data[i - customerCount][5] = ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).isAManager;
                data[i - customerCount][6] = ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).salary;
                data[i - customerCount][7] = JesseSitesMastery6.people.get(i).loginName;
                data[i - customerCount][8] = JesseSitesMastery6.people.get(i).password;
            }
        }

        // employees scroll panel
        String[] col = new String[]{"Employee ID", "Name", "Address", "Phone Number",
            "Hire Date", "Is Manager", "Salary", "Login Name", "Password"};
        JTable tableEmployees = new JTable(data, col);
        tableEmployees.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tcm = tableEmployees.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(75);
        tcm.getColumn(1).setPreferredWidth(75);
        tcm.getColumn(2).setPreferredWidth(150);
        tcm.getColumn(3).setPreferredWidth(100);
        tcm.getColumn(4).setPreferredWidth(75);
        tcm.getColumn(5).setPreferredWidth(75);
        tcm.getColumn(6).setPreferredWidth(100);
        tcm.getColumn(7).setPreferredWidth(75);
        tcm.getColumn(8).setPreferredWidth(75);
        JScrollPane employeesScrollableTextArea = new JScrollPane(tableEmployees);
        employeesScrollableTextArea.setBounds(40, 50, 1200, 250);
        JesseSitesMastery6.managerPanel.add(employeesScrollableTextArea);
    }

    public void editEmployee() {
        if (Login.isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.managerPanel.getComponents();
        JTable employees = (JTable) ((JScrollPane) components[JesseSitesMastery6.managerPanel.getComponents().length - 1]).getViewport().getView();
        int editEmployeeId;

        if (employees.getSelectedRow() < 0) {
            return;
        }

        if (employees.getCellEditor() != null) {
            employees.getCellEditor().stopCellEditing();
        }

        editEmployeeId = Integer.parseInt(employees.getModel().getValueAt(
                employees.getSelectedRow(), 0).toString());

        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            if (JesseSitesMastery6.people.get(i) instanceof TravelAgencyEmployee) {
                if (JesseSitesMastery6.people.get(i).idNumber == editEmployeeId) {
                    JesseSitesMastery6.people.get(i).name = employees.getModel().getValueAt(
                            employees.getSelectedRow(), 1).toString();
                    JesseSitesMastery6.people.get(i).address = employees.getModel().getValueAt(
                            employees.getSelectedRow(), 2).toString();
                    JesseSitesMastery6.people.get(i).phoneNumber = employees.getModel().getValueAt(
                            employees.getSelectedRow(), 3).toString();
                    ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).hireDate = employees.getModel().getValueAt(
                            employees.getSelectedRow(), 4).toString();
                    ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).isAManager = Boolean.parseBoolean(employees.getModel().getValueAt(
                            employees.getSelectedRow(), 5).toString());
                    ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).salary = Double.parseDouble(employees.getModel().getValueAt(
                            employees.getSelectedRow(), 6).toString());
                    ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).loginName = employees.getModel().getValueAt(
                            employees.getSelectedRow(), 7).toString();
                    ((TravelAgencyEmployee) JesseSitesMastery6.people.get(i)).password = employees.getModel().getValueAt(
                            employees.getSelectedRow(), 8).toString();
                }
            }
        }

        save();
    }

    public void addEmployee() {
        if (Login.isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.managerPanel.getComponents();
        TravelAgencyEmployee employee = new TravelAgencyEmployee();
        int greatestEmployeeId = 0;

        employee.name = ((JTextField) components[6]).getText();
        employee.address = ((JTextField) components[8]).getText();
        employee.phoneNumber = ((JTextField) components[10]).getText();
        employee.hireDate = ((JTextField) components[12]).getText();
        employee.isAManager = ((JCheckBox) components[14]).isSelected();

        if (!((JTextField) components[16]).getText().equals("")) {
            employee.salary = Double.parseDouble(((JTextField) components[16]).getText());
        }

        employee.loginName = ((JTextField) components[18]).getText();
        employee.password = ((JTextField) components[20]).getText();

        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            if (JesseSitesMastery6.people.get(i) instanceof TravelAgencyEmployee) {
                if (JesseSitesMastery6.people.get(i).idNumber > greatestEmployeeId) {
                    greatestEmployeeId = JesseSitesMastery6.people.get(i).idNumber;
                }
            }
        }

        employee.idNumber = greatestEmployeeId + 1;
        JesseSitesMastery6.people.add(employee);
        save();
    }

    public void removeEmployee() {
        if (Login.isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.managerPanel.getComponents();
        JTable employees = (JTable) ((JScrollPane) components[JesseSitesMastery6.managerPanel.getComponents().length - 1]).getViewport().getView();
        int removeEmployeeId = Integer.parseInt(employees.getModel().getValueAt(
                employees.getSelectedRow(), 0).toString());
        boolean employeeRemoved = false;

        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            if (JesseSitesMastery6.people.get(i) instanceof TravelAgencyEmployee) {
                if (JesseSitesMastery6.people.get(i).idNumber == removeEmployeeId) {
                    JesseSitesMastery6.people.remove(i);
                    employeeRemoved = true;
                }

                if (employeeRemoved == true && i < JesseSitesMastery6.people.size()) {
                    JesseSitesMastery6.people.get(i).idNumber--;
                }
            }
        }

        save();
    }

    public static void printReport(String beginDate, String endDate) {
        String html;
        String path = System.getProperty("user.home") + File.separator + "Documents"
                + File.separator + "Reports";
        File f = new File(path);

        if (!f.exists()) {
            f.mkdirs();
        }

        f = new File(path + File.separator + "report.html");

        try {
            PreparedStatement ps
                    = JesseSitesMastery6.con.prepareStatement("SELECT LodgeIdNumber, SUM(Total) "
                            + "FROM Orders "
                            + "WHERE StartDate >= '" + beginDate + "' "
                            + "AND EndDate <= '" + endDate + "' "
                            + "GROUP BY LodgeIdNumber;");
            ResultSet rs = ps.executeQuery();
            PreparedStatement psCount
                    = JesseSitesMastery6.con.prepareStatement("SELECT COUNT(DISTINCT LodgeIdNumber) "
                            + "FROM Orders "
                            + "WHERE StartDate >= '" + beginDate + "' "
                            + "AND EndDate <= '" + endDate + "';");
            ResultSet rsCount = psCount.executeQuery();
            int count;

            rsCount.next();
            count = rsCount.getInt(1);
            html = "<html>"
                    + "<head>"
                    + "<style>"
                    + " body {background-color:black;}"
                    + " h1 {color:orange;"
                    + " font-family: Helvetica;}"
                    + " li {color:pink;"
                    + " font-size: 24px;}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div><h1>Total Revenue for Lodges</h1><ul>";

            for (int i = 0; i < count; i++) {
                rs.next();
                html += "<li>Lodge ID: " + rs.getInt(1) + "</li>";
                html += "<li style='margin-bottom:10px;'>Revenue: $" + rs.getBigDecimal(2) + "</li>";
            }

            html += "<ul></div>"
                    + "</body>"
                    + "</html>";
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(html);
            bw.close();
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
