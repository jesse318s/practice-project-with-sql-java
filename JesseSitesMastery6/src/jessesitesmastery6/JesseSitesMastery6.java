/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jessesitesmastery6;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;

/**
 *
 * @author jesse - module 6
 */
public class JesseSitesMastery6 {

    // create arraylists to hold person and lodging subclass objects with unique ids
    static ArrayList<Person> people = new ArrayList<>();
    static ArrayList<Lodging> lodges = new ArrayList<>();
    // create window jframe object
    static JFrame jframe = new JFrame("Jesse Sites Mastery 6");
    // create state panel objects
    static JPanel loginPanel = new JPanel();
    static JPanel employeePanel = new JPanel();
    static JPanel managerPanel = new JPanel();
    static JPanel customerPanel = new JPanel();
    static JPanel lodgeImagePanel = new JPanel();
    // create employee tabbed pane
    static JTabbedPane employeeTabs = new JTabbedPane();
    // user info
    static String loginName;
    static boolean userIsAManager;
    static int verifiedCustomerIdNumber;
    // db connection
    static Connection con = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // connect to db
        try {
            loginUser();
        } catch (Exception x) {
            x.printStackTrace();
        }
        // maintain db connection
        ConnectionMaintenance conMaintenance = new ConnectionMaintenance();
        // create and set state objects
        State.login = new Login();
        State.employeeMenu = new EmployeeMenu();
        State.managerMenu = new ManagerMenu();
        State.customerMenu = new CustomerMenu();
        State.lodgeImageMenu = new LodgeImageMenu();
        State.current = State.login;
        // init window jframe
        jframe.setBounds(50, 50, 1300, 750);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(null);
        // init state panels
        initLoginPanel();
        initEmployeePanel();
        initManagerPanel();
        initEmployeeTabs();
        initCustomerPanel();
        initLodgeImagePanel();
        // add color to state panels
        loginPanel.setBackground(Color.ORANGE);
        employeePanel.setBackground(Color.ORANGE);
        managerPanel.setBackground(Color.ORANGE);
        customerPanel.setBackground(Color.ORANGE);
        lodgeImagePanel.setBackground(Color.ORANGE);
        // display window
        jframe.add(loginPanel);
        jframe.add(employeeTabs);
        jframe.add(customerPanel);
        jframe.add(lodgeImagePanel);
        jframe.setVisible(true);
        // start state machine
        State.current.enter();
    }

    public static boolean loginUser() throws SQLException {
        String mySQLURL = "jdbc:mysql://13.58.236.216:3306/sitesjsp23";
        String username = "sitesjsp23";
        String password = "123456789";

        con = DriverManager.getConnection(mySQLURL, username, password);
        return con.isValid(0);
    }

    static class Helper extends TimerTask {

        public void run() {
            String mySQLURL = "jdbc:mysql://13.58.236.216:3306/sitesjsp23";
            String username = "sitesjsp23";
            String password = "123456789";

            try {
                DriverManager.getConnection(mySQLURL, username, password).isValid(3000);
                Login.labelConnectionStatus.setText("Connected");
                Login.labelConnectionStatus.setForeground(Color.BLUE);
                EmployeeMenu.labelConnectionStatus.setText("Connected");
                EmployeeMenu.labelConnectionStatus.setForeground(Color.BLUE);
                ManagerMenu.labelConnectionStatus.setText("Connected");
                ManagerMenu.labelConnectionStatus.setForeground(Color.BLUE);
                CustomerMenu.labelConnectionStatus.setText("Connected");
                CustomerMenu.labelConnectionStatus.setForeground(Color.BLUE);
            } catch (Exception x) {
                Login.labelConnectionStatus.setText("Disconnected");
                Login.labelConnectionStatus.setForeground(Color.RED);
                EmployeeMenu.labelConnectionStatus.setText("Disconnected");
                EmployeeMenu.labelConnectionStatus.setForeground(Color.RED);
                ManagerMenu.labelConnectionStatus.setText("Disconnected");
                ManagerMenu.labelConnectionStatus.setForeground(Color.RED);
                CustomerMenu.labelConnectionStatus.setText("Disconnected");
                CustomerMenu.labelConnectionStatus.setForeground(Color.RED);

                try {
                    Thread.sleep(3000);
                    con = DriverManager.getConnection(mySQLURL, username, password);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }

                Login.labelConnectionStatus.setText("Reconnecting...");
                Login.labelConnectionStatus.setForeground(Color.BLACK);
                EmployeeMenu.labelConnectionStatus.setText("Reconnecting...");
                EmployeeMenu.labelConnectionStatus.setForeground(Color.BLACK);
                ManagerMenu.labelConnectionStatus.setText("Reconnecting...");
                ManagerMenu.labelConnectionStatus.setForeground(Color.BLACK);
                CustomerMenu.labelConnectionStatus.setText("Reconnecting...");
                CustomerMenu.labelConnectionStatus.setForeground(Color.BLACK);
            }
        }
    }

    static class ConnectionMaintenance implements Runnable {

        Thread t;

        ConnectionMaintenance() {
            t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new Helper();

            timer.schedule(task, 0, 3000);
        }
    }

    public static void initLoginPanel() {
        // login panel
        loginPanel.setSize(1300, 750);
        loginPanel.setLayout(null);
        // login title
        JLabel labelLogin = new JLabel("Login");
        labelLogin.setBounds(610, 75, 100, 48);
        labelLogin.setFont(new Font("Tahoma", Font.BOLD, 24));
        loginPanel.add(labelLogin);
        // username label
        JLabel labelUsername = new JLabel("Username: ");
        labelUsername.setBounds(400, 125, 200, 36);
        labelUsername.setFont(new Font("Verdana", Font.PLAIN, 18));
        loginPanel.add(labelUsername);
        // username text field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(550, 138, 200, 18);
        loginPanel.add(usernameField);
        // password label
        JLabel labelPassword = new JLabel("Password: ");
        labelPassword.setBounds(407, 155, 200, 36);
        labelPassword.setFont(new Font("Verdana", Font.PLAIN, 18));
        loginPanel.add(labelPassword);
        // password text field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(550, 167, 200, 18);
        passwordField.setEchoChar('•');
        passwordField.setToolTipText("New passwords must be at least 8 "
                + "characters long, contain at least one special character, and "
                + "contain at least one uppercase character.");
        loginPanel.add(passwordField);
        // register checkbox 
        JCheckBox checkBoxRegister = new JCheckBox("Register");
        checkBoxRegister.setBounds(675, 185, 200, 50);
        checkBoxRegister.setForeground(Color.BLUE);
        checkBoxRegister.setBackground(Color.ORANGE);
        loginPanel.add(checkBoxRegister);
        // checkbox listener
        checkBoxRegister.addActionListener((ActionEvent e) -> {
            if (!checkBoxRegister.isSelected()) {
                passwordField.setForeground(Color.BLACK);
                return;
            }

            if (validatePassword(String.valueOf(passwordField.getPassword()))) {
                passwordField.setForeground(Color.GREEN);
                return;
            }

            passwordField.setForeground(Color.RED);
        });
        // password listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (checkBoxRegister.isSelected()) {
                    if (validatePassword(String.valueOf(passwordField.getPassword()))) {
                        passwordField.setForeground(Color.GREEN);
                        return;
                    }

                    passwordField.setForeground(Color.RED);
                } else {
                    passwordField.setForeground(Color.BLACK);
                }
            }
        });
        // password info label
        JLabel labelPasswordInfo = new JLabel("Password: ");
        labelPasswordInfo.setBounds(390, 220, 510, 80);
        labelPasswordInfo.setFont(new Font("SansSerif", Font.ITALIC, 14));
        labelPasswordInfo.setText("<html>New passwords must be at least 8 "
                + "characters long, contain at least one special character, and "
                + "contain at least one uppercase character.</html>");
        loginPanel.add(labelPasswordInfo);
        // register button
        JButton buttonRegister = new JButton("Register");
        buttonRegister.setBounds(360, 300, 100, 50);
        buttonRegister.setForeground(Color.WHITE);
        buttonRegister.setBackground(Color.BLACK);
        buttonRegister.addActionListener((ActionEvent e) -> {
            registerCustomer(usernameField.getText(),
                    String.valueOf(passwordField.getPassword()));
            usernameField.setText("");
            passwordField.setText("");
        });
        loginPanel.add(buttonRegister);
        // login button
        JButton buttonLogin = new JButton("Login");
        buttonLogin.setBounds(470, 300, 100, 50);
        buttonLogin.setForeground(Color.PINK);
        buttonLogin.setBackground(Color.BLACK);
        buttonLogin.addActionListener((ActionEvent e) -> {
            if (verifyCustomer(usernameField.getText(),
                    String.valueOf(passwordField.getPassword()))) {
                loginName = usernameField.getText();
                usernameField.setText("");
                passwordField.setText("");
                State.current = State.customerMenu;
                State.previous = State.login;
                loginPanel.setVisible(false);
                customerPanel.setVisible(true);
                return;
            }

            if (verifyEmployee(usernameField.getText(),
                    String.valueOf(passwordField.getPassword()))) {
                loginName = usernameField.getText();
                usernameField.setText("");
                passwordField.setText("");
                State.current = State.employeeMenu;
                State.previous = State.login;
                loginPanel.setVisible(false);
                employeeTabs.setVisible(true);
                employeeTabs.setEnabledAt(0, true);

                if (userIsAManager) {
                    employeeTabs.setEnabledAt(1, true);
                }
            }
        });
        loginPanel.add(buttonLogin);
        // guest button
        JButton buttonGuest = new JButton("Continue as Guest");
        buttonGuest.setBounds(580, 300, 200, 50);
        buttonGuest.setForeground(Color.WHITE);
        buttonGuest.setBackground(Color.BLACK);
        buttonGuest.addActionListener((ActionEvent e) -> {
            loginName = "Guest";
            verifiedCustomerIdNumber = 0;
            usernameField.setText("");
            passwordField.setText("");
            State.current = State.customerMenu;
            State.previous = State.login;
            loginPanel.setVisible(false);
            customerPanel.setVisible(true);
        });
        loginPanel.add(buttonGuest);
        // exit button
        JButton buttonExit = new JButton("Exit");
        buttonExit.setBounds(790, 300, 100, 50);
        buttonExit.setForeground(Color.RED);
        buttonExit.setBackground(Color.BLACK);
        buttonExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        loginPanel.add(buttonExit);
        // connection status label
        Login.labelConnectionStatus.setBounds(1125, 6, 200, 36);
        Login.labelConnectionStatus.setFont(new Font("Verdana", Font.PLAIN, 18));
        loginPanel.add(Login.labelConnectionStatus);
    }

    public static void initEmployeePanel() {
        employeePanel.setSize(1300, 750);
        employeePanel.setLayout(null);
        employeePanel.setVisible(false);
        // user label
        JLabel labelUser = new JLabel();
        labelUser.setBounds(15, 6, 200, 36);
        labelUser.setFont(new Font("Verdana", Font.PLAIN, 18));
        employeePanel.add(labelUser);
        // log out button
        JButton buttonLogOut = new JButton("Log Out");
        buttonLogOut.setBounds(600, 625, 100, 50);
        buttonLogOut.setForeground(Color.RED);
        buttonLogOut.setBackground(Color.BLACK);
        buttonLogOut.addActionListener((ActionEvent e) -> {
            loginName = "Guest";
            userIsAManager = false;
            State.current = State.login;
            State.previous = State.employeeMenu;
            employeeTabs.setVisible(false);
            employeeTabs.setEnabledAt(0, false);
            employeePanel.setVisible(false);
            employeeTabs.setEnabledAt(1, false);
            managerPanel.setVisible(false);
            loginPanel.setVisible(true);
        });
        employeePanel.add(buttonLogOut);
        // remove button
        JButton buttonRemove = new JButton("Remove");
        buttonRemove.setBounds(420, 310, 100, 50);
        buttonRemove.setForeground(Color.RED);
        buttonRemove.setBackground(Color.BLACK);
        buttonRemove.addActionListener((ActionEvent e) -> {
            ((EmployeeMenu) State.employeeMenu).removeLodge();
            employeePanel.remove(JesseSitesMastery6.employeePanel.getComponents()[employeePanel.getComponents().length - 1]);
            ((EmployeeMenu) State.employeeMenu).loadTableLodges();
        });
        employeePanel.add(buttonRemove);
        // add house button
        JButton buttonAddHouse = new JButton("Add House");
        buttonAddHouse.setBounds(530, 310, 100, 50);
        buttonAddHouse.setForeground(Color.PINK);
        buttonAddHouse.setBackground(Color.BLACK);
        buttonAddHouse.addActionListener((ActionEvent e) -> {
            ((EmployeeMenu) State.employeeMenu).addHouse();
            employeePanel.remove(employeePanel.getComponents()[employeePanel.getComponents().length - 1]);
            ((EmployeeMenu) State.employeeMenu).loadTableLodges();
        });
        employeePanel.add(buttonAddHouse);
        // add hotel button
        JButton buttonAddHotel = new JButton("Add Hotel");
        buttonAddHotel.setBounds(640, 310, 100, 50);
        buttonAddHotel.setForeground(Color.PINK);
        buttonAddHotel.setBackground(Color.BLACK);
        buttonAddHotel.addActionListener((ActionEvent e) -> {
            ((EmployeeMenu) State.employeeMenu).addHotel();
            employeePanel.remove(employeePanel.getComponents()[employeePanel.getComponents().length - 1]);
            ((EmployeeMenu) State.employeeMenu).loadTableLodges();
        });
        employeePanel.add(buttonAddHotel);
        // edit button
        JButton buttonEdit = new JButton("Save Edit");
        buttonEdit.setBounds(750, 310, 100, 50);
        buttonEdit.setForeground(Color.PINK);
        buttonEdit.setBackground(Color.BLACK);
        buttonEdit.addActionListener((ActionEvent e) -> {
            ((EmployeeMenu) State.employeeMenu).editLodge();
            employeePanel.remove(employeePanel.getComponents()[employeePanel.getComponents().length - 1]);
            ((EmployeeMenu) State.employeeMenu).loadTableLodges();
        });
        employeePanel.add(buttonEdit);
        // name label
        JLabel labelName = new JLabel();
        labelName.setBounds(370, 360, 100, 36);
        labelName.setText("Name: ");
        employeePanel.add(labelName);
        // name text field
        JTextField nameField = new JTextField();
        nameField.setBounds(420, 370, 100, 18);
        employeePanel.add(nameField);
        // description label
        JLabel labelDescription = new JLabel();
        labelDescription.setBounds(530, 360, 100, 36);
        labelDescription.setText("Description: ");
        employeePanel.add(labelDescription);
        // description text field
        JTextField descriptionField = new JTextField();
        descriptionField.setBounds(610, 370, 100, 18);
        employeePanel.add(descriptionField);
        // max occupants label
        JLabel labelMaxOccupants = new JLabel();
        labelMaxOccupants.setBounds(720, 360, 100, 36);
        labelMaxOccupants.setText("Max occupants: ");
        employeePanel.add(labelMaxOccupants);
        // max occupants text field
        JTextField maxOccupantsField = new JTextField();
        maxOccupantsField.setBounds(820, 370, 100, 18);
        employeePanel.add(maxOccupantsField);
        // nightly price label
        JLabel labelNightlyPrice = new JLabel();
        labelNightlyPrice.setBounds(370, 390, 100, 36);
        labelNightlyPrice.setText("Nightly Price: ");
        employeePanel.add(labelNightlyPrice);
        // nightly price text field
        JTextField nightlyPriceField = new JTextField();
        nightlyPriceField.setBounds(450, 400, 100, 18);
        employeePanel.add(nightlyPriceField);
        // parking fee label
        JLabel labelParkingFee = new JLabel();
        labelParkingFee.setBounds(560, 390, 100, 36);
        labelParkingFee.setText("Parking Fee: ");
        employeePanel.add(labelParkingFee);
        // parking fee text field
        JTextField parkingFeeField = new JTextField();
        parkingFeeField.setBounds(640, 400, 100, 18);
        employeePanel.add(parkingFeeField);
        // free breakfast label
        JLabel labelFreeBreakfast = new JLabel();
        labelFreeBreakfast.setBounds(750, 390, 150, 36);
        labelFreeBreakfast.setText("Free breakfast (t/f): ");
        employeePanel.add(labelFreeBreakfast);
        // free breakfast check box
        JCheckBox checkBoxFreeBreakfast = new JCheckBox();
        checkBoxFreeBreakfast.setBounds(880, 400, 25, 18);
        checkBoxFreeBreakfast.setBackground(Color.ORANGE);
        employeePanel.add(checkBoxFreeBreakfast);
        // bedrooms label
        JLabel labelBedrooms = new JLabel();
        labelBedrooms.setBounds(370, 420, 100, 36);
        labelBedrooms.setText("Bedrooms: ");
        employeePanel.add(labelBedrooms);
        // bedrooms text field
        JTextField bedroomsField = new JTextField();
        bedroomsField.setBounds(450, 430, 100, 18);
        employeePanel.add(bedroomsField);
        // washer label
        JLabel labelWasher = new JLabel();
        labelWasher.setBounds(560, 420, 100, 36);
        labelWasher.setText("Washer:");
        employeePanel.add(labelWasher);
        // washer check box
        JCheckBox checkBoxWasher = new JCheckBox();
        checkBoxWasher.setBounds(630, 425, 25, 25);
        checkBoxWasher.setBackground(Color.ORANGE);
        employeePanel.add(checkBoxWasher);
        // dryer label
        JLabel labelDryer = new JLabel();
        labelDryer.setBounds(680, 420, 100, 36);
        labelDryer.setText("Dryer: ");
        employeePanel.add(labelDryer);
        // dryer check box
        JCheckBox checkBoxDryer = new JCheckBox();
        checkBoxDryer.setBounds(740, 425, 25, 25);
        checkBoxDryer.setBackground(Color.ORANGE);
        employeePanel.add(checkBoxDryer);
        // vacancies label
        JLabel labelVacancies = new JLabel();
        labelVacancies.setBounds(370, 450, 100, 36);
        labelVacancies.setText("Vacancies: ");
        employeePanel.add(labelVacancies);
        // vacancies text field
        JTextField vacanciesField = new JTextField();
        vacanciesField.setBounds(450, 460, 100, 18);
        employeePanel.add(vacanciesField);
        // rooms label
        JLabel labelRooms = new JLabel();
        labelRooms.setBounds(560, 450, 100, 36);
        labelRooms.setText("Rooms: ");
        employeePanel.add(labelRooms);
        // rooms text field
        JTextField roomsField = new JTextField();
        roomsField.setBounds(620, 460, 100, 18);
        employeePanel.add(roomsField);
        // 24 hr lobby label
        JLabel label24HrLobby = new JLabel();
        label24HrLobby.setBounds(730, 450, 100, 36);
        label24HrLobby.setText("24 Hr Lobby: ");
        employeePanel.add(label24HrLobby);
        // 24 hr lobby check box
        JCheckBox checkBox24HrLobby = new JCheckBox();
        checkBox24HrLobby.setBounds(820, 455, 25, 25);
        checkBox24HrLobby.setBackground(Color.ORANGE);
        employeePanel.add(checkBox24HrLobby);
        // movie night label
        JLabel labelMovieNight = new JLabel();
        labelMovieNight.setBounds(370, 480, 100, 36);
        labelMovieNight.setText("Movie Night: ");
        employeePanel.add(labelMovieNight);
        // movie check box
        JCheckBox checkBoxMovieNight = new JCheckBox();
        checkBoxMovieNight.setBounds(460, 490, 25, 18);
        checkBoxMovieNight.setBackground(Color.ORANGE);
        employeePanel.add(checkBoxMovieNight);
        // images button
        JButton buttonImages = new JButton();
        buttonImages.setBounds(420, 520, 250, 50);
        buttonImages.setForeground(Color.PINK);
        buttonImages.setBackground(Color.BLACK);
        buttonImages.setText("Choose Selected Lodge Image(s)");
        buttonImages.addActionListener((ActionEvent) -> {
            EmployeeMenu.fileChooser.setMultiSelectionEnabled(true);
            int option = EmployeeMenu.fileChooser.showOpenDialog(employeePanel);

            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    File[] imageFiles;
                    imageFiles = EmployeeMenu.fileChooser.getSelectedFiles();
                    String statement = "INSERT INTO LodgeImages VALUES(?,?,?);";
                    PreparedStatement ps = con.prepareStatement(statement);
                    String statementCount = "SELECT MAX(IdNumber) FROM LodgeImages;";
                    PreparedStatement psCount = con.prepareStatement(statementCount);
                    ResultSet rsCount = psCount.executeQuery();
                    byte[] b;
                    int lodgeId = 0;
                    int lodgeImageCount;

                    rsCount.next();
                    lodgeImageCount = rsCount.getInt(1);

                    for (Component comp : employeePanel.getComponents()) {
                        if (comp instanceof JScrollPane) {
                            JViewport viewport = ((JScrollPane) comp).getViewport();
                            JTable table = (JTable) viewport.getView();

                            if (table.getSelectedRow() < 0) {
                                return;
                            }

                            lodgeId = Integer.parseInt(table.getModel().getValueAt(
                                    table.getSelectedRow(), 0).toString());
                        }
                    }

                    for (File file : imageFiles) {
                        lodgeImageCount = lodgeImageCount + 1;
                        b = Files.readAllBytes(file.toPath());
                        ps.setInt(1, lodgeImageCount);
                        ps.setInt(2, lodgeId);
                        ps.setBinaryStream(3, new ByteArrayInputStream(b, 0, b.length));
                        ps.executeUpdate();
                    }

                    ImageLoader loader = new ImageLoader();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        });
        employeePanel.add(buttonImages);
        // images menu button
        JButton buttonImagesMenu = new JButton();
        buttonImagesMenu.setBounds(990, 310, 250, 50);
        buttonImagesMenu.setForeground(Color.WHITE);
        buttonImagesMenu.setBackground(Color.BLACK);
        buttonImagesMenu.setText("View Selected Lodge Image(s)");
        buttonImagesMenu.addActionListener((ActionEvent) -> {
            try {
                JTable employeeLodges = null;

                if (EmployeeMenu.rsLodgeImg == null) {
                    return;
                }

                for (Component comp : JesseSitesMastery6.employeePanel.getComponents()) {
                    if (comp instanceof JScrollPane) {
                        JViewport viewport = ((JScrollPane) comp).getViewport();
                        employeeLodges = (JTable) viewport.getView();
                    }
                }

                if (employeeLodges.getSelectedRow() < 0) {
                    return;
                }

                State.lodgeImageMenu.load();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        );
        employeePanel.add(buttonImagesMenu);
        // begin date combobox
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < 396; i++) {
            dates.add(cal.toZonedDateTime().toString().substring(0, 10));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        JComboBox beginDate = new JComboBox();

        for (int i = 0; i < 396; i++) {
            beginDate.addItem(dates.get(i));
        }

        beginDate.setBounds(40, 340, 100, 20);
        beginDate.addActionListener((ActionListener) -> {
            Component[] components = employeePanel.getComponents();
            ((JComboBox) components[35]).removeAllItems();

            for (int i = ((JComboBox) components[34]).getSelectedIndex() + 1; i < 396; i++) {
                ((JComboBox) components[35]).addItem(dates.get(i));
            }
        });
        employeePanel.add(beginDate);
        // end date combobox
        JComboBox endDate = new JComboBox();
        endDate.setBounds(150, 340, 100, 20);
        employeePanel.add(endDate);
        // report label
        JLabel labelReport = new JLabel("Report Dates");
        labelReport.setBounds(110, 310, 100, 36);
        employeePanel.add(labelReport);
        // report button
        JButton buttonReport = new JButton("Print Report");
        buttonReport.setBounds(85, 370, 125, 50);
        buttonReport.setForeground(Color.PINK);
        buttonReport.setBackground(Color.BLACK);
        buttonReport.addActionListener((ActionEvent e) -> {
            if (endDate.getSelectedIndex() < 0) {
                return;
            }

            EmployeeMenuOrderLoader orderLoader = new EmployeeMenuOrderLoader(
                    beginDate.getSelectedItem().toString(),
                    endDate.getSelectedItem().toString());
        });
        employeePanel.add(buttonReport);
        // house group label
        JLabel labelHouseGroup = new JLabel();
        labelHouseGroup.setBounds(359, 412, 600, 50);
        labelHouseGroup.setText("<html><div style='"
                + "border: 2px solid black; "
                + "width: 440px; "
                + "height: 23px; "
                + "'>"
                + "<h2 style='"
                + " margin: 0px 0px 0px 355px; "
                + " color: red; "
                + "'>House Only</h2>"
                + "</div></html>");
        employeePanel.add(labelHouseGroup);
        // hotel group label
        JLabel labelHotelGroup = new JLabel();
        labelHotelGroup.setBounds(359, 384, 600, 200);
        labelHotelGroup.setText("<html><div style='"
                + "border: 2px solid black; "
                + "width: 440px; "
                + "height: 50px; "
                + "'>"
                + "<h2 style='"
                + " margin: 24px 0px 0px 360px; "
                + " color: red; "
                + "'>Hotel Only</h2>"
                + "</div></html>");
        employeePanel.add(labelHotelGroup);
        // connection status label
        EmployeeMenu.labelConnectionStatus.setBounds(1125, 6, 200, 36);
        EmployeeMenu.labelConnectionStatus.setFont(new Font("Verdana", Font.PLAIN, 18));
        employeePanel.add(EmployeeMenu.labelConnectionStatus);
    }

    public static void initManagerPanel() {
        managerPanel.setSize(1300, 1300);
        managerPanel.setLayout(null);
        managerPanel.setVisible(false);
        // user label
        JLabel labelUser = new JLabel();
        labelUser.setBounds(15, 6, 200, 36);
        labelUser.setFont(new Font("Verdana", Font.PLAIN, 18));
        managerPanel.add(labelUser);
        // log out button
        JButton buttonLogOut = new JButton("Log Out");
        buttonLogOut.setBounds(600, 550, 100, 50);
        buttonLogOut.setForeground(Color.RED);
        buttonLogOut.setBackground(Color.BLACK);
        buttonLogOut.addActionListener((ActionEvent e) -> {
            loginName = "Guest";
            userIsAManager = false;
            State.current = State.login;
            State.previous = State.managerMenu;
            employeeTabs.setVisible(false);
            employeeTabs.setEnabledAt(0, false);
            employeePanel.setVisible(false);
            employeeTabs.setEnabledAt(1, false);
            employeeTabs.setSelectedIndex(0);
            managerPanel.setVisible(false);
            loginPanel.setVisible(true);
        });
        managerPanel.add(buttonLogOut);
        // remove button
        JButton buttonRemove = new JButton("Remove");
        buttonRemove.setBounds(420, 335, 100, 50);
        buttonRemove.setForeground(Color.RED);
        buttonRemove.setBackground(Color.BLACK);
        buttonRemove.addActionListener((ActionEvent e) -> {
            ((ManagerMenu) State.managerMenu).removeEmployee();
            managerPanel.remove(managerPanel.getComponents()[managerPanel.getComponents().length - 1]);
            ((ManagerMenu) State.managerMenu).loadTableEmployees();
        });
        managerPanel.add(buttonRemove);
        // add button
        JButton buttonAdd = new JButton("Add");
        buttonAdd.setBounds(530, 335, 100, 50);
        buttonAdd.setForeground(Color.PINK);
        buttonAdd.setBackground(Color.BLACK);
        buttonAdd.addActionListener((ActionEvent e) -> {
            ((ManagerMenu) State.managerMenu).addEmployee();
            managerPanel.remove(managerPanel.getComponents()[managerPanel.getComponents().length - 1]);
            ((ManagerMenu) State.managerMenu).loadTableEmployees();
        });
        managerPanel.add(buttonAdd);
        // edit button
        JButton buttonEdit = new JButton("Save Edit");
        buttonEdit.setBounds(640, 335, 100, 50);
        buttonEdit.setForeground(Color.PINK);
        buttonEdit.setBackground(Color.BLACK);
        buttonEdit.addActionListener((ActionEvent e) -> {
            ((ManagerMenu) State.managerMenu).editEmployee();
            managerPanel.remove(managerPanel.getComponents()[managerPanel.getComponents().length - 1]);
            ((ManagerMenu) State.managerMenu).loadTableEmployees();
        });
        managerPanel.add(buttonEdit);
        // name label
        JLabel labelName = new JLabel();
        labelName.setBounds(370, 385, 100, 36);
        labelName.setText("Name: ");
        managerPanel.add(labelName);
        // name text field
        JTextField nameField = new JTextField();
        nameField.setBounds(420, 395, 100, 18);
        managerPanel.add(nameField);
        // address label
        JLabel labelAddress = new JLabel();
        labelAddress.setBounds(530, 385, 100, 36);
        labelAddress.setText("Address: ");
        managerPanel.add(labelAddress);
        // address text field
        JTextField addressField = new JTextField();
        addressField.setBounds(590, 395, 100, 18);
        managerPanel.add(addressField);
        // phone label
        JLabel labelPhone = new JLabel();
        labelPhone.setBounds(700, 385, 100, 36);
        labelPhone.setText("Phone: ");
        managerPanel.add(labelPhone);
        // phone text field
        JTextField phoneField = new JTextField();
        phoneField.setBounds(750, 395, 100, 18);
        managerPanel.add(phoneField);
        // hire date label
        JLabel labelHireDate = new JLabel();
        labelHireDate.setBounds(370, 415, 100, 36);
        labelHireDate.setText("Hire Date: ");
        managerPanel.add(labelHireDate);
        // hire date text field
        JTextField hireDateField = new JTextField();
        hireDateField.setBounds(430, 425, 100, 18);
        managerPanel.add(hireDateField);
        // is a manager label
        JLabel labelIsAManager = new JLabel();
        labelIsAManager.setBounds(540, 415, 100, 36);
        labelIsAManager.setText("Is a Manager: ");
        managerPanel.add(labelIsAManager);
        // is a manager check box
        JCheckBox checkBoxIsAManager = new JCheckBox();
        checkBoxIsAManager.setBounds(630, 420, 25, 25);
        checkBoxIsAManager.setBackground(Color.ORANGE);
        managerPanel.add(checkBoxIsAManager);
        // salary label
        JLabel labelSalary = new JLabel();
        labelSalary.setBounds(675, 415, 100, 36);
        labelSalary.setText("Salary: ");
        managerPanel.add(labelSalary);
        // salary text field
        JTextField salaryField = new JTextField();
        salaryField.setBounds(725, 425, 100, 18);
        managerPanel.add(salaryField);
        // login name label
        JLabel labelLoginName = new JLabel();
        labelLoginName.setBounds(370, 445, 100, 36);
        labelLoginName.setText("Login Name: ");
        managerPanel.add(labelLoginName);
        // login name text field
        JTextField loginNameField = new JTextField();
        loginNameField.setBounds(450, 455, 100, 18);
        managerPanel.add(loginNameField);
        // password label
        JLabel labelPassword = new JLabel();
        labelPassword.setBounds(560, 445, 100, 36);
        labelPassword.setText("Password: ");
        managerPanel.add(labelPassword);
        // password text field
        JTextField passwordField = new JTextField();
        passwordField.setBounds(630, 455, 100, 18);
        managerPanel.add(passwordField);
        // begin date combobox
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < 396; i++) {
            dates.add(cal.toZonedDateTime().toString().substring(0, 10));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        JComboBox beginDate = new JComboBox();

        for (int i = 0; i < 396; i++) {
            beginDate.addItem(dates.get(i));
        }

        beginDate.setBounds(40, 340, 100, 20);
        beginDate.addActionListener((ActionListener) -> {
            Component[] components = managerPanel.getComponents();
            ((JComboBox) components[22]).removeAllItems();

            for (int i = ((JComboBox) components[21]).getSelectedIndex() + 1; i < 396; i++) {
                ((JComboBox) components[22]).addItem(dates.get(i));
            }
        });
        managerPanel.add(beginDate);
        // end date combobox
        JComboBox endDate = new JComboBox();
        endDate.setBounds(150, 340, 100, 20);
        managerPanel.add(endDate);
        // report label
        JLabel labelReport = new JLabel("Report Dates");
        labelReport.setBounds(110, 310, 100, 36);
        managerPanel.add(labelReport);
        // report button
        JButton buttonReport = new JButton("Print Report");
        buttonReport.setBounds(85, 370, 125, 50);
        buttonReport.setForeground(Color.PINK);
        buttonReport.setBackground(Color.BLACK);
        buttonReport.addActionListener((ActionEvent e) -> {
            if (endDate.getSelectedIndex() < 0) {
                return;
            }

            ManagerMenuOrderLoader orderLoader = new ManagerMenuOrderLoader(
                    beginDate.getSelectedItem().toString(),
                    endDate.getSelectedItem().toString());
        });
        managerPanel.add(buttonReport);
        // connection status label
        ManagerMenu.labelConnectionStatus.setBounds(1125, 6, 200, 36);
        ManagerMenu.labelConnectionStatus.setFont(new Font("Verdana", Font.PLAIN, 18));
        managerPanel.add(ManagerMenu.labelConnectionStatus);
    }

    public static void initEmployeeTabs() {
        employeeTabs.setSize(1300, 750);
        employeeTabs.setVisible(false);
        employeeTabs.addTab("Employees View", employeePanel);
        employeeTabs.addTab("Managers View", managerPanel);
        employeeTabs.setEnabledAt(0, false);
        employeeTabs.setEnabledAt(1, false);
    }

    public static void initCustomerPanel() {
        customerPanel.setSize(1300, 750);
        customerPanel.setLayout(null);
        customerPanel.setVisible(false);
        // customer title
        JLabel labelCustomer = new JLabel("Customer View");
        labelCustomer.setBounds(560, 75, 200, 48);
        labelCustomer.setFont(new Font("Tahoma", Font.BOLD, 24));
        customerPanel.add(labelCustomer);
        // user label
        JLabel labelUser = new JLabel();
        labelUser.setBounds(15, 6, 200, 36);
        labelUser.setFont(new Font("Verdana", Font.PLAIN, 18));
        customerPanel.add(labelUser);
        // dates label 
        JLabel labelDates = new JLabel("Dates: ");
        labelDates.setBounds(470, 325, 100, 36);
        labelDates.setFont(new Font("Verdana", Font.PLAIN, 18));
        customerPanel.add(labelDates);
        // begin date combobox
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < 396; i++) {
            dates.add(cal.toZonedDateTime().toString().substring(0, 10));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        JComboBox beginDate = new JComboBox();

        for (int i = 0; i < 366; i++) {
            beginDate.addItem(dates.get(i));
        }

        beginDate.setBounds(540, 335, 100, 20);
        beginDate.addActionListener((ActionListener) -> {
            Component[] components = customerPanel.getComponents();
            ((JComboBox) components[4]).removeAllItems();

            for (int i = ((JComboBox) components[3]).getSelectedIndex() + 1;
                    i < ((JComboBox) components[3]).getSelectedIndex() + 31; i++) {
                ((JComboBox) components[4]).addItem(dates.get(i));
            }
        });
        customerPanel.add(beginDate);
        // end date combobox
        JComboBox endDate = new JComboBox();
        endDate.setBounds(650, 335, 100, 20);
        customerPanel.add(endDate);
        // order button
        JButton buttonOrder = new JButton("Add Order");
        buttonOrder.setBounds(770, 335, 125, 25);
        buttonOrder.setForeground(Color.PINK);
        buttonOrder.setBackground(Color.BLACK);
        buttonOrder.addActionListener((ActionEvent e) -> {
            JTable tableLodges = null;

            for (Component comp : JesseSitesMastery6.customerPanel.getComponents()) {
                if (comp instanceof JScrollPane) {
                    JViewport viewport = ((JScrollPane) comp).getViewport();
                    tableLodges = (JTable) viewport.getView();
                }
            }

            if (tableLodges.getSelectedRow() < 0 || endDate.getSelectedIndex() < 0
                    || tableLodges.getSelectedRow() < 0) {
                return;
            }

            CustomerMenu.orderLodgeNumber = Integer.parseInt(tableLodges.getModel().getValueAt(
                    tableLodges.getSelectedRow(), 0).toString());
            LocalDate date1 = LocalDate.parse(beginDate.getSelectedItem().toString(),
                    DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate date2 = LocalDate.parse(endDate.getSelectedItem().toString(),
                    DateTimeFormatter.ISO_LOCAL_DATE);
            Duration dateDifference = Duration.between(date1.atStartOfDay(),
                    date2.atStartOfDay());
            long dateDifferenceInDays = dateDifference.toDays();
            CustomerMenu.orderNumberOfDays = dateDifferenceInDays;
            CustomerMenu.orderTotal = dateDifferenceInDays * Double.parseDouble(
                    tableLodges.getModel().getValueAt(tableLodges.getSelectedRow(), 2).toString()
            );
            CustomerMenu.labelOrderTotal.setText("Order Total: $" + String.format("%.2f", CustomerMenu.orderTotal));
        });
        customerPanel.add(buttonOrder);
        // description text area
        JTextArea taDescription = new JTextArea("Description: ");
        taDescription.setEditable(false);
        taDescription.setLineWrap(true);
        taDescription.setBounds(400, 401, 250, 72);
        taDescription.setFont(new Font("Verdana", Font.PLAIN, 18));
        customerPanel.add(taDescription);
        // order total label
        CustomerMenu.labelOrderTotal = new JLabel("Order Total: ");
        CustomerMenu.labelOrderTotal.setBounds(410, 360, 200, 36);
        customerPanel.add(CustomerMenu.labelOrderTotal);
        // confirm order button
        JButton buttonConfirmOrder = new JButton("Confirm Order");
        buttonConfirmOrder.setBounds(770, 365, 125, 25);
        buttonConfirmOrder.setForeground(Color.PINK);
        buttonConfirmOrder.setBackground(Color.BLACK);
        buttonConfirmOrder.addActionListener((ActionEvent e) -> {
            if (CustomerMenu.orderLodgeNumber <= 0) {
                return;
            }

            CustomerMenuOrderCreator orderCreatore = new CustomerMenuOrderCreator(
                    beginDate.getSelectedItem().toString(),
                    endDate.getSelectedItem().toString());
        });
        customerPanel.add(buttonConfirmOrder);
        // cancel order button
        JButton buttonCancelOrder = new JButton("Cancel Order");
        buttonCancelOrder.setBounds(635, 365, 125, 25);
        buttonCancelOrder.setForeground(Color.RED);
        buttonCancelOrder.setBackground(Color.BLACK);
        buttonCancelOrder.addActionListener((ActionEvent e) -> {
            CustomerMenu.orderLodgeNumber = 0;
            CustomerMenu.orderNumberOfDays = 0;
            CustomerMenu.orderTotal = 0;
            CustomerMenu.labelOrderTotal.setText("Order Total: ");
        });
        customerPanel.add(buttonCancelOrder);
        // preview one label
        CustomerMenu.labelPreviewOne = new JLabel();
        customerPanel.add(CustomerMenu.labelPreviewOne);
        // preview two label
        CustomerMenu.labelPreviewTwo = new JLabel();
        customerPanel.add(CustomerMenu.labelPreviewTwo);
        // preview three label
        CustomerMenu.labelPreviewThree = new JLabel();
        customerPanel.add(CustomerMenu.labelPreviewThree);
        // begin date report combobox
        GregorianCalendar calReport = new GregorianCalendar();
        calReport.setTime(new Date());
        ArrayList<String> datesReport = new ArrayList<>();

        for (int i = 0; i < 396; i++) {
            datesReport.add(calReport.toZonedDateTime().toString().substring(0, 10));
            calReport.add(Calendar.DAY_OF_MONTH, 1);
        }

        JComboBox beginDateReport = new JComboBox();

        for (int i = 0; i < 396; i++) {
            beginDateReport.addItem(datesReport.get(i));
        }

        beginDateReport.setBounds(40, 340, 100, 20);
        // end date report combobox
        JComboBox endDateReport = new JComboBox();
        endDateReport.setBounds(150, 340, 100, 20);
        customerPanel.add(endDateReport);
        // listener for begin date report combobox
        beginDateReport.addActionListener((ActionListener) -> {
            (endDateReport).removeAllItems();

            for (int i = (beginDateReport).getSelectedIndex() + 1; i < 396; i++) {
                (endDateReport).addItem(dates.get(i));
            }
        });
        customerPanel.add(beginDateReport);
        // report label
        JLabel labelReport = new JLabel("Report Dates");
        labelReport.setBounds(110, 310, 100, 36);
        customerPanel.add(labelReport);
        // report button
        JButton buttonReport = new JButton("Print Report");
        buttonReport.setBounds(85, 370, 125, 50);
        buttonReport.setForeground(Color.PINK);
        buttonReport.setBackground(Color.BLACK);
        buttonReport.addActionListener((ActionEvent e) -> {
            if (endDateReport.getSelectedIndex() < 0) {
                return;
            }

            CustomerMenuOrderLoader orderLoader = new CustomerMenuOrderLoader(
                    beginDateReport.getSelectedItem().toString(),
                    endDateReport.getSelectedItem().toString());
        });
        customerPanel.add(buttonReport);
        // log out button
        JButton buttonExit = new JButton("Exit");
        buttonExit.setBounds(600, 575, 100, 50);
        buttonExit.setForeground(Color.RED);
        buttonExit.setBackground(Color.BLACK);
        buttonExit.addActionListener((ActionEvent e) -> {
            loginName = "Guest";
            verifiedCustomerIdNumber = 0;
            CustomerMenu.orderLodgeNumber = 0;
            CustomerMenu.orderNumberOfDays = 0;
            CustomerMenu.orderTotal = 0;
            CustomerMenu.labelOrderTotal.setText("Order Total: ");
            State.current = State.login;
            State.previous = State.customerMenu;
            customerPanel.setVisible(false);
            loginPanel.setVisible(true);
        });
        customerPanel.add(buttonExit);
        // connection status label
        CustomerMenu.labelConnectionStatus.setBounds(1125, 6, 200, 36);
        CustomerMenu.labelConnectionStatus.setFont(new Font("Verdana", Font.PLAIN, 18));
        customerPanel.add(CustomerMenu.labelConnectionStatus);
    }

    public static void initLodgeImagePanel() {
        lodgeImagePanel.setSize(1300, 750);
        lodgeImagePanel.setLayout(null);
        lodgeImagePanel.setVisible(false);
        // images title label
        JLabel labelImagesTitle = new JLabel("Images Menu");
        labelImagesTitle.setBounds(20, 10, 200, 48);
        labelImagesTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        lodgeImagePanel.add(labelImagesTitle);
        // lodge image label
        lodgeImagePanel.add(LodgeImageMenu.labelLodgeImage);
        // next image button
        JButton buttonNextImage = new JButton("Next");
        buttonNextImage.setBounds(657, 430, 100, 50);
        buttonNextImage.setForeground(Color.WHITE);
        buttonNextImage.setBackground(Color.BLACK);
        buttonNextImage.addActionListener((ActionEvent e) -> {
            try {
                if (LodgeImageMenu.imageIndex == LodgeImageMenu.imageData.length - 1) {
                    return;
                }

                LodgeImageMenu.imageIndex = LodgeImageMenu.imageIndex + 1;
                byte[] b = LodgeImageMenu.imageData[LodgeImageMenu.imageIndex].getBytes(1,
                        (int) LodgeImageMenu.imageData[LodgeImageMenu.imageIndex].length());
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                BufferedImage img = ImageIO.read(in);
                ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(
                        400, 400, java.awt.Image.SCALE_SMOOTH));
                lodgeImagePanel.remove(LodgeImageMenu.labelLodgeImage);
                // lodge image label
                LodgeImageMenu.labelLodgeImage = new JLabel(imgIcon);
                LodgeImageMenu.labelLodgeImage.setBounds(455, 20, 400, 400);
                lodgeImagePanel.add(LodgeImageMenu.labelLodgeImage);
            } catch (Exception x) {
                x.printStackTrace();
            }
        });
        lodgeImagePanel.add(buttonNextImage);
        // previous image button
        JButton buttonPreviousImage = new JButton("Previous");
        buttonPreviousImage.setBounds(547, 430, 100, 50);
        buttonPreviousImage.setForeground(Color.WHITE);
        buttonPreviousImage.setBackground(Color.BLACK);
        buttonPreviousImage.addActionListener((ActionEvent e) -> {
            try {
                if (LodgeImageMenu.imageIndex == 0) {
                    return;
                }

                LodgeImageMenu.imageIndex = LodgeImageMenu.imageIndex - 1;
                byte[] b = LodgeImageMenu.imageData[LodgeImageMenu.imageIndex].getBytes(1,
                        (int) LodgeImageMenu.imageData[LodgeImageMenu.imageIndex].length());
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                BufferedImage img = ImageIO.read(in);
                ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(
                        400, 400, java.awt.Image.SCALE_SMOOTH));
                lodgeImagePanel.remove(LodgeImageMenu.labelLodgeImage);
                // lodge image label
                LodgeImageMenu.labelLodgeImage = new JLabel(imgIcon);
                LodgeImageMenu.labelLodgeImage.setBounds(455, 20, 400, 400);
                lodgeImagePanel.add(LodgeImageMenu.labelLodgeImage);
            } catch (Exception x) {
                x.printStackTrace();
            }
        });
        lodgeImagePanel.add(buttonPreviousImage);
        // close images button
        JButton buttonCloseImages = new JButton("Close Images Menu");
        buttonCloseImages.setBounds(552, 490, 200, 50);
        buttonCloseImages.setForeground(Color.RED);
        buttonCloseImages.setBackground(Color.BLACK);
        buttonCloseImages.addActionListener((ActionEvent e) -> {
            if (State.previous == State.customerMenu) {
                State.current = State.previous;
                customerPanel.setVisible(true);
            }

            if (State.previous == State.employeeMenu) {
                State.current = State.previous;
                employeeTabs.setVisible(true);
            }

            State.previous = State.lodgeImageMenu;
            lodgeImagePanel.setVisible(false);
        });
        lodgeImagePanel.add(buttonCloseImages);
    }

    public static boolean verifyCustomer(String loginNameInput, String passwordInput) {
        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            Person person = JesseSitesMastery6.people.get(i);

            if (person.loginName.equals(loginNameInput) && person.password.equals(passwordInput)
                    && person instanceof Customer) {
                verifiedCustomerIdNumber = person.idNumber;
                return true;
            }
        }

        return false;
    }

    public static boolean verifyEmployee(String loginNameInput, String passwordInput) {
        for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
            Person person = JesseSitesMastery6.people.get(i);

            if (person.loginName.equals(loginNameInput) && person.password.equals(passwordInput)) {
                if (((TravelAgencyEmployee) person).isAManager) {
                    userIsAManager = true;
                }

                return true;
            }
        }

        return false;
    }

    public static Boolean validatePassword(String password) {
        Pattern p = Pattern.compile("^[a-z0-9 ]+$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        boolean lacksSpecial = m.find();
        boolean lacksUppercase = false;

        if (password.equals(password.toLowerCase())) {
            lacksUppercase = true;
        }

        if (password.length() < 8 || lacksSpecial || lacksUppercase) {
            return false;
        }

        return true;
    }

    public static void registerCustomer(String username, String password) {
        if (Login.isLoading) {
            return;
        }

        if (validatePassword(password)) {
            int greatestCustomerId = 0;
            Customer newCustomer = new Customer();

            for (int i = 0; i < JesseSitesMastery6.people.size(); i++) {
                if (JesseSitesMastery6.people.get(i) instanceof Customer) {
                    if (JesseSitesMastery6.people.get(i).idNumber > greatestCustomerId) {
                        greatestCustomerId = JesseSitesMastery6.people.get(i).idNumber;
                    }
                }
            }

            newCustomer.loginName = username;
            newCustomer.password = password;
            newCustomer.idNumber = greatestCustomerId + 1;
            JesseSitesMastery6.people.add(newCustomer);

            try {
                State.login.save();
            } catch (Exception exception) {
                System.out.println(exception + "\n");
            }
        }
    }
}
