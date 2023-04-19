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
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author jesse
 */
public class EmployeeMenu extends State {

    // image file chooser
    static JFileChooser fileChooser = new JFileChooser();
    // image data
    static Blob[] imageData;
    // lodge image count
    static int lodgeImgCount;
    // lodge image result set
    static ResultSet rsLodgeImg;
    // connection status label
    static JLabel labelConnectionStatus = new JLabel();
    // loading status
    static boolean isLoading = false;
    // loading all images status
    static boolean isLoadingImage = false;

    @Override
    public void enter() {
        try {
            Component[] components = JesseSitesMastery6.employeePanel.getComponents();

            EmployeeMenuLoader loader = new EmployeeMenuLoader();
            ((JLabel) components[0]).setText("User: " + JesseSitesMastery6.loginName);
            update();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    @Override
    public void update() {
        while (true) {
            JesseSitesMastery6.employeePanel.revalidate();
            JesseSitesMastery6.employeePanel.repaint();

            if (JesseSitesMastery6.employeeTabs.getSelectedIndex() == 1) {
                State.current = State.managerMenu;
                State.previous = State.employeeMenu;
                break;
            }

            if (!JesseSitesMastery6.employeeTabs.isVisible()) {
                break;
            }
        }

        State.current.enter();
    }

    @Override
    public void load() throws SQLException {
        if (isLoading) {
            return;
        }

        isLoading = true;

        PreparedStatement psLodges
                = JesseSitesMastery6.con.prepareStatement(
                        "SELECT (SELECT COUNT(*) FROM Hotels), IdNumber, `Name`, `Description`, MaxOccupants, "
                        + "BasePricePerNight, ParkingFee, HasFreeBreakfast, "
                        + "Vacancies, NumberOfRooms, Has24HourLobby, HasMovieNight "
                        + "FROM Hotels "
                        + "UNION ALL "
                        + "SELECT (SELECT COUNT(*) FROM Houses), IdNumber, `Name`, `Description`, "
                        + "MaxOccupants, BasePricePerNight, ParkingFee, HasFreeBreakfast, "
                        + "NumberOfBedrooms, HasWasher, HasDryer, NULL AS `NULL` "
                        + "FROM Houses;"
                );
        String[] hotelsData;
        String hotelInfo[];
        int hotelCount = 1;
        ResultSet rsLodges = psLodges.executeQuery();
        String[] housesData;
        String houseInfo[];
        int houseCount = 1;
        JesseSitesMastery6.lodges.clear();
        hotelsData = new String[11];
        housesData = new String[10];

        for (int i = 0; i < hotelCount; i++) {
            rsLodges.next();

            for (int j = 0; j < 11; j++) {
                hotelCount = rsLodges.getInt(1);
                hotelsData[j] = rsLodges.getString(j + 2);
            }

            hotelInfo = hotelsData;
            Hotel hotel = new Hotel(Integer.parseInt(hotelInfo[0]), hotelInfo[1],
                    hotelInfo[2], Integer.parseInt(hotelInfo[3]),
                    Double.parseDouble(hotelInfo[4]), Double.parseDouble(hotelInfo[5]),
                    Integer.parseInt(hotelInfo[6]) == 1, Integer.parseInt(hotelInfo[7]),
                    Integer.parseInt(hotelInfo[8]), Integer.parseInt(hotelInfo[9]) == 1,
                    Integer.parseInt(hotelInfo[10]) == 1
            );
            JesseSitesMastery6.lodges.add(hotel);
            loadTableLodges();
            ((CustomerMenu) State.customerMenu).loadTableLodges();
        }

        for (int i = 0; i < houseCount; i++) {
            rsLodges.next();

            for (int j = 0; j < 10; j++) {
                houseCount = rsLodges.getInt(1);
                housesData[j] = rsLodges.getString(j + 2);
            }

            houseInfo = housesData;
            House house = new House(Integer.parseInt(houseInfo[0]), houseInfo[1],
                    houseInfo[2], Integer.parseInt(houseInfo[3]), Double.parseDouble(houseInfo[4]),
                    Double.parseDouble(houseInfo[5]), Integer.parseInt(houseInfo[6]) == 1,
                    Integer.parseInt(houseInfo[7]), Integer.parseInt(houseInfo[8]) == 1,
                    Integer.parseInt(houseInfo[9]) == 1
            );
            JesseSitesMastery6.lodges.add(house);
            loadTableLodges();
            ((CustomerMenu) State.customerMenu).loadTableLodges();
        }

        isLoading = false;
    }

    @Override
    public void save() throws SQLException {
        String statementHouses = "INSERT INTO Houses VALUES(?,?,?,?,?,?,?,?,?,?);";
        String statementHotels = "INSERT INTO Hotels VALUES(?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement psDeleteHouses = JesseSitesMastery6.con.prepareStatement("DELETE FROM Houses;");
        PreparedStatement psDeleteHotels = JesseSitesMastery6.con.prepareStatement("DELETE FROM Hotels;");

        psDeleteHouses.executeUpdate();
        psDeleteHotels.executeUpdate();

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i) instanceof House) {
                House house = (House) JesseSitesMastery6.lodges.get(i);
                PreparedStatement ps = JesseSitesMastery6.con.prepareStatement(statementHouses);

                ps.setInt(1, house.idNumber);
                ps.setString(2, house.name);
                ps.setString(3, house.description);
                ps.setInt(4, house.maxOccupants);
                ps.setBigDecimal(5, new BigDecimal(house.basePricePerNight));
                ps.setBigDecimal(6, new BigDecimal(house.parkingFee));
                ps.setBoolean(7, house.hasFreeBreakfast);
                ps.setInt(8, house.numberOfBedrooms);
                ps.setBoolean(9, house.hasWasher);
                ps.setBoolean(10, house.hasDryer);
                ps.executeUpdate();
            }

            if (JesseSitesMastery6.lodges.get(i) instanceof Hotel) {
                Hotel hotel = (Hotel) JesseSitesMastery6.lodges.get(i);
                PreparedStatement ps = JesseSitesMastery6.con.prepareStatement(statementHotels);

                ps.setInt(1, hotel.idNumber);
                ps.setString(2, hotel.name);
                ps.setString(3, hotel.description);
                ps.setInt(4, hotel.maxOccupants);
                ps.setBigDecimal(5, new BigDecimal(hotel.basePricePerNight));
                ps.setBigDecimal(6, new BigDecimal(hotel.parkingFee));
                ps.setBoolean(7, hotel.hasFreeBreakfast);
                ps.setInt(8, hotel.vacancies);
                ps.setInt(9, hotel.numberOfRooms);
                ps.setBoolean(10, hotel.has24HourLobby);
                ps.setBoolean(11, hotel.hasMovieNight);
                ps.executeUpdate();
            }
        }
    }

    public void loadTableLodges() {
        Object[][] data = new Object[JesseSitesMastery6.lodges.size()][11];

        for (Component comp : JesseSitesMastery6.employeePanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JesseSitesMastery6.employeePanel.remove(comp);
            }
        }

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            data[i][0] = JesseSitesMastery6.lodges.get(i).idNumber;
            data[i][1] = JesseSitesMastery6.lodges.get(i).name;
            data[i][2] = JesseSitesMastery6.lodges.get(i).description;
            data[i][3] = JesseSitesMastery6.lodges.get(i).maxOccupants;
            data[i][4] = JesseSitesMastery6.lodges.get(i).basePricePerNight;
            data[i][5] = JesseSitesMastery6.lodges.get(i).parkingFee;
            data[i][6] = JesseSitesMastery6.lodges.get(i).hasFreeBreakfast;

            if (JesseSitesMastery6.lodges.get(i) instanceof House) {
                data[i][7] = ((House) JesseSitesMastery6.lodges.get(i)).numberOfBedrooms;
                data[i][8] = ((House) JesseSitesMastery6.lodges.get(i)).hasWasher;
                data[i][9] = ((House) JesseSitesMastery6.lodges.get(i)).hasDryer;
            }

            if (JesseSitesMastery6.lodges.get(i) instanceof Hotel) {
                data[i][7] = ((Hotel) JesseSitesMastery6.lodges.get(i)).vacancies;
                data[i][8] = ((Hotel) JesseSitesMastery6.lodges.get(i)).numberOfRooms;
                data[i][9] = ((Hotel) JesseSitesMastery6.lodges.get(i)).has24HourLobby;
                data[i][10] = ((Hotel) JesseSitesMastery6.lodges.get(i)).hasMovieNight;
            }
        }

        // lodges scroll panel
        String[] col = new String[]{"Lodge ID", "Name", "Description",
            "Max Occupants", "Nightly Price", "Parking Fee", "Free Breakfast",
            "Bedrooms | Vacancies", "Washer | Rooms", "Dryer | 24 Hour Lobby",
            "Movie Night"};
        JTable tableLodges = new JTable(data, col);
        tableLodges.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tcm = tableLodges.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(75);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(100);
        tcm.getColumn(3).setPreferredWidth(100);
        tcm.getColumn(4).setPreferredWidth(100);
        tcm.getColumn(5).setPreferredWidth(100);
        tcm.getColumn(6).setPreferredWidth(100);
        tcm.getColumn(7).setPreferredWidth(150);
        tcm.getColumn(8).setPreferredWidth(100);
        tcm.getColumn(9).setPreferredWidth(150);
        tcm.getColumn(10).setPreferredWidth(100);
        JScrollPane lodgesScrollableTextArea = new JScrollPane(tableLodges);
        lodgesScrollableTextArea.setBounds(40, 50, 1200, 250);
        tableLodges.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isLoadingImage) {
                    loadSelectedLodgeImages();
                }
            }
        }
        );
        JesseSitesMastery6.employeePanel.add(lodgesScrollableTextArea);
    }

    public static void loadLodgeImages() {
        try {
            if (isLoadingImage) {
                return;
            }

            isLoadingImage = true;

            PreparedStatement ps = JesseSitesMastery6.con.prepareStatement("SELECT LodgeIdNumber, Img "
                    + "FROM LodgeImages;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rsLodgeImg = ps.executeQuery();
            PreparedStatement psCount = JesseSitesMastery6.con.prepareStatement(
                    "SELECT COUNT(*) FROM LodgeImages;");
            ResultSet rsCount = psCount.executeQuery();

            rsCount.next();
            lodgeImgCount = rsCount.getInt(1);
            isLoadingImage = false;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void loadSelectedLodgeImages() {
        JTable lodges = null;
        int selectedLodgeImageCount = 0;

        if (rsLodgeImg == null) {
            return;
        }

        for (Component comp : JesseSitesMastery6.employeePanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) comp).getViewport();
                lodges = (JTable) viewport.getView();
            }
        }

        try {
            rsLodgeImg.first();

            for (int i = 0; i < lodgeImgCount; i++) {
                if (rsLodgeImg.getInt(1) == Integer.parseInt(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 0).toString())) {
                    selectedLodgeImageCount++;
                }

                rsLodgeImg.next();
            }

            imageData = new Blob[selectedLodgeImageCount];
            selectedLodgeImageCount = 0;
            rsLodgeImg.first();

            for (int i = 0; i < lodgeImgCount; i++) {
                if (rsLodgeImg.getInt(1) == Integer.parseInt(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 0).toString())) {
                    imageData[selectedLodgeImageCount] = rsLodgeImg.getBlob(2);
                    selectedLodgeImageCount++;
                }

                rsLodgeImg.next();
            }

            State.lodgeImageMenu.save();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void editLodge() {
        if (isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.employeePanel.getComponents();
        JTable lodges = (JTable) ((JScrollPane) components[JesseSitesMastery6.employeePanel.getComponents().length - 1]).getViewport().getView();
        int editLodgeId;

        if (lodges.getSelectedRow() < 0) {
            return;
        }

        if (lodges.getCellEditor() != null) {
            lodges.getCellEditor().stopCellEditing();
        }

        editLodgeId = Integer.parseInt(lodges.getModel().getValueAt(
                lodges.getSelectedRow(), 0).toString());

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i).idNumber == editLodgeId) {
                JesseSitesMastery6.lodges.get(i).name = lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 1).toString();
                JesseSitesMastery6.lodges.get(i).description = lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 2).toString();
                JesseSitesMastery6.lodges.get(i).maxOccupants = Integer.parseInt(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 3).toString());
                JesseSitesMastery6.lodges.get(i).basePricePerNight = Double.parseDouble(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 4).toString());
                JesseSitesMastery6.lodges.get(i).parkingFee = Double.parseDouble(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 5).toString());
                JesseSitesMastery6.lodges.get(i).hasFreeBreakfast = Boolean.parseBoolean(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 6).toString());

                if (JesseSitesMastery6.lodges.get(i) instanceof House) {
                    ((House) JesseSitesMastery6.lodges.get(i)).numberOfBedrooms = Integer.parseInt(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 7).toString());
                    ((House) JesseSitesMastery6.lodges.get(i)).hasWasher = Boolean.parseBoolean(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 8).toString());
                    ((House) JesseSitesMastery6.lodges.get(i)).hasDryer = Boolean.parseBoolean(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 9).toString());
                }

                if (JesseSitesMastery6.lodges.get(i) instanceof Hotel) {
                    ((Hotel) JesseSitesMastery6.lodges.get(i)).vacancies = Integer.parseInt(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 7).toString());
                    ((Hotel) JesseSitesMastery6.lodges.get(i)).numberOfRooms = Integer.parseInt(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 8).toString());
                    ((Hotel) JesseSitesMastery6.lodges.get(i)).has24HourLobby = Boolean.parseBoolean(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 9).toString());
                    ((Hotel) JesseSitesMastery6.lodges.get(i)).hasMovieNight = Boolean.parseBoolean(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 10).toString());
                }
            }
        }

        try {
            save();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    public void addHotel() {
        if (isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.employeePanel.getComponents();
        Hotel hotel = new Hotel();
        int greatestLodgeId = 0;

        hotel.name = ((JTextField) components[7]).getText();
        hotel.description = ((JTextField) components[9]).getText();

        if (!((JTextField) components[11]).getText().equals("")) {
            hotel.maxOccupants = Integer.parseInt(((JTextField) components[11]).getText());
        }

        if (!((JTextField) components[13]).getText().equals("")) {
            hotel.basePricePerNight = Double.parseDouble(((JTextField) components[13]).getText());
        }

        if (!((JTextField) components[15]).getText().equals("")) {
            hotel.parkingFee = Double.parseDouble(((JTextField) components[15]).getText());
        }

        hotel.hasFreeBreakfast = ((JCheckBox) components[17]).isSelected();

        if (!((JTextField) components[25]).getText().equals("")) {
            hotel.vacancies = Integer.parseInt(((JTextField) components[25]).getText());
        }

        if (!((JTextField) components[27]).getText().equals("")) {
            hotel.numberOfRooms = Integer.parseInt(((JTextField) components[27]).getText());
        }

        hotel.has24HourLobby = ((JCheckBox) components[29]).isSelected();
        hotel.hasMovieNight = ((JCheckBox) components[31]).isSelected();

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i).idNumber > greatestLodgeId) {
                greatestLodgeId = JesseSitesMastery6.lodges.get(i).idNumber;
            }
        }

        hotel.idNumber = greatestLodgeId + 1;
        JesseSitesMastery6.lodges.add(hotel);

        try {
            save();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    public void addHouse() {
        if (isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.employeePanel.getComponents();
        House house = new House();
        int greatestLodgeId = 0;

        house.name = ((JTextField) components[7]).getText();
        house.description = ((JTextField) components[9]).getText();

        if (!((JTextField) components[11]).getText().equals("")) {
            house.maxOccupants = Integer.parseInt(((JTextField) components[11]).getText());
        }

        if (!((JTextField) components[13]).getText().equals("")) {
            house.basePricePerNight = Double.parseDouble(((JTextField) components[13]).getText());
        }

        if (!((JTextField) components[15]).getText().equals("")) {
            house.parkingFee = Double.parseDouble(((JTextField) components[15]).getText());
        }

        house.hasFreeBreakfast = ((JCheckBox) components[17]).isSelected();

        if (!((JTextField) components[19]).getText().equals("")) {
            house.numberOfBedrooms = Integer.parseInt(((JTextField) components[19]).getText());
        }

        house.hasWasher = ((JCheckBox) components[21]).isSelected();
        house.hasDryer = ((JCheckBox) components[23]).isSelected();

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i).idNumber > greatestLodgeId) {
                greatestLodgeId = JesseSitesMastery6.lodges.get(i).idNumber;
            }
        }

        house.idNumber = greatestLodgeId + 1;
        JesseSitesMastery6.lodges.add(house);

        try {
            save();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    public void removeLodge() {
        if (isLoading) {
            return;
        }

        Component[] components = JesseSitesMastery6.employeePanel.getComponents();
        JTable lodges = (JTable) ((JScrollPane) components[JesseSitesMastery6.employeePanel.getComponents().length - 1]).getViewport().getView();
        int removeLodgeId = Integer.parseInt(lodges.getModel().getValueAt(
                lodges.getSelectedRow(), 0).toString());

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i).idNumber == removeLodgeId) {
                JesseSitesMastery6.lodges.remove(i);
            }
        }

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i).idNumber > removeLodgeId) {
                JesseSitesMastery6.lodges.get(i).idNumber--;
            }
        }

        try {
            String statementDelete = "DELETE FROM LodgeImages WHERE LodgeIdNumber = ?;";
            PreparedStatement psDelete = JesseSitesMastery6.con.prepareStatement(statementDelete);
            psDelete.setInt(1, removeLodgeId);
            String statementDecrement = "UPDATE LodgeImages SET LodgeIdNumber = "
                    + "LodgeIdNumber - 1 WHERE LodgeIdNumber > ?;";
            PreparedStatement psDecrement = JesseSitesMastery6.con.prepareStatement(statementDecrement);
            psDecrement.setInt(1, removeLodgeId);

            psDelete.executeUpdate();
            psDecrement.executeUpdate();
        } catch (Exception x) {
            x.printStackTrace();
        }

        try {
            save();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
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
                    = JesseSitesMastery6.con.prepareStatement("SELECT CustomerIdNumber, SUM(Total) "
                            + "FROM Orders "
                            + "WHERE StartDate >= '" + beginDate + "' "
                            + "AND EndDate <= '" + endDate + "' "
                            + "AND CustomerIdNumber != 0 "
                            + "GROUP BY CustomerIdNumber;");
            ResultSet rs = ps.executeQuery();
            PreparedStatement psCount
                    = JesseSitesMastery6.con.prepareStatement("SELECT COUNT(DISTINCT CustomerIdNumber) "
                            + "FROM Orders "
                            + "WHERE StartDate >= '" + beginDate + "' "
                            + "AND EndDate <= '" + endDate + "' "
                            + "AND CustomerIdNumber != 0;");
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
                    + "<div><h1>Total Spending for Customers</h1><ul>";

            for (int i = 0; i < count; i++) {
                rs.next();
                html += "<li>Customer ID: " + rs.getInt(1) + "</li>";
                html += "<li style='margin-bottom:10px;'>Spending: $" + rs.getBigDecimal(2) + "</li>";
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
