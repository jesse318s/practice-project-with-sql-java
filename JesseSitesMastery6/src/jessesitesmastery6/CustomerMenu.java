/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 *
 * @author jesse
 */
public class CustomerMenu extends State {

    static int orderLodgeNumber;
    static long orderNumberOfDays;
    static double orderTotal;
    // order total label
    static JLabel labelOrderTotal;
    // image preview labels
    static JLabel labelPreviewOne;
    static JLabel labelPreviewTwo;
    static JLabel labelPreviewThree;
    // image data
    static Blob[] imageData;
    // connection status label
    static JLabel labelConnectionStatus = new JLabel();

    @Override
    public void enter() {
        Component[] components = JesseSitesMastery6.customerPanel.getComponents();

        CustomerMenuLoader loader = new CustomerMenuLoader();
        ((JLabel) components[1]).setText("User: " + JesseSitesMastery6.loginName);
        update();
    }

    @Override
    public void update() {
        while (true) {
            JesseSitesMastery6.customerPanel.revalidate();
            JesseSitesMastery6.customerPanel.repaint();

            if (!JesseSitesMastery6.customerPanel.isVisible()) {
                break;
            }
        }

        State.current.enter();
    }

    @Override
    public void load() {
        try {
            employeeMenu.load();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    @Override
    public void save() {
        try {
            employeeMenu.save();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    public void loadTableLodges() {
        Object[][] data = new Object[JesseSitesMastery6.lodges.size()][3];

        for (Component comp : JesseSitesMastery6.customerPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JesseSitesMastery6.customerPanel.remove(comp);
            }
        }

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            data[i][0] = JesseSitesMastery6.lodges.get(i).idNumber;
            data[i][1] = JesseSitesMastery6.lodges.get(i).name;
            data[i][2] = JesseSitesMastery6.lodges.get(i).basePricePerNight;
        }

        // lodges scroll panel
        String[] col = new String[]{"Lodge ID", "Name", "Nightly Price"};
        JTable tableLodges = new JTable(data, col);
        tableLodges.setDefaultEditor(Object.class, null);
        JScrollPane lodgesScrollableTextArea = new JScrollPane(tableLodges);
        lodgesScrollableTextArea.setBounds(400, 125, 500, 200);
        tableLodges.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!CustomerMenuLoader.isLoading) {
                    displaySelectedLodgeDetails();
                }
            }
        });
        JesseSitesMastery6.customerPanel.add(lodgesScrollableTextArea);
    }

    public void displaySelectedLodgeDetails() {
        Component[] components = JesseSitesMastery6.customerPanel.getComponents();
        JTable lodges = null;
        int selectedLodgeImageCount = 0;

        for (Component comp : JesseSitesMastery6.customerPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) comp).getViewport();
                lodges = (JTable) viewport.getView();
            }
        }

        for (int i = 0; i < JesseSitesMastery6.lodges.size(); i++) {
            if (JesseSitesMastery6.lodges.get(i).idNumber
                    == Integer.parseInt(lodges.getModel().getValueAt(
                            lodges.getSelectedRow(), 0).toString())) {
                ((JTextArea) components[6]).setText("Description: "
                        + JesseSitesMastery6.lodges.get(i).description);
            }
        }

        try {
            EmployeeMenu.rsLodgeImg.first();

            for (int i = 0; i < EmployeeMenu.lodgeImgCount; i++) {
                if (EmployeeMenu.rsLodgeImg.getInt(1) == Integer.parseInt(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 0).toString())) {
                    selectedLodgeImageCount++;
                }

                EmployeeMenu.rsLodgeImg.next();
            }

            imageData = new Blob[selectedLodgeImageCount];
            selectedLodgeImageCount = 0;
            EmployeeMenu.rsLodgeImg.first();

            for (int i = 0; i < EmployeeMenu.lodgeImgCount; i++) {
                if (EmployeeMenu.rsLodgeImg.getInt(1) == Integer.parseInt(lodges.getModel().getValueAt(
                        lodges.getSelectedRow(), 0).toString())) {
                    imageData[selectedLodgeImageCount] = EmployeeMenu.rsLodgeImg.getBlob(2);
                    selectedLodgeImageCount++;
                }

                EmployeeMenu.rsLodgeImg.next();
            }

            State.lodgeImageMenu.save();
            JesseSitesMastery6.customerPanel.remove(labelPreviewOne);
            JesseSitesMastery6.customerPanel.remove(labelPreviewTwo);
            JesseSitesMastery6.customerPanel.remove(labelPreviewThree);

            if (imageData.length > 0) {
                byte[] b = imageData[0].getBytes(1, (int) imageData[0].length());
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                BufferedImage img = ImageIO.read(in);
                ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(
                        70, 70, java.awt.Image.SCALE_SMOOTH));

                // preview one label
                labelPreviewOne = new JLabel(imgIcon);
                labelPreviewOne.setBounds(660, 405, 70, 70);
                labelPreviewOne.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            State.lodgeImageMenu.load();
                        } catch (Exception x) {
                            x.printStackTrace();
                        }
                    }
                });
                JesseSitesMastery6.customerPanel.add(labelPreviewOne);
            }

            if (imageData.length > 1) {
                byte[] b = imageData[1].getBytes(1, (int) imageData[1].length());
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                BufferedImage img = ImageIO.read(in);
                ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(
                        70, 70, java.awt.Image.SCALE_SMOOTH));

                // preview two label
                labelPreviewTwo = new JLabel(imgIcon);
                labelPreviewTwo.setBounds(740, 405, 70, 70);
                labelPreviewTwo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            State.lodgeImageMenu.load();
                        } catch (Exception x) {
                            x.printStackTrace();
                        }
                    }
                });
                JesseSitesMastery6.customerPanel.add(labelPreviewTwo);
            }

            if (imageData.length > 2) {
                byte[] b = imageData[2].getBytes(1, (int) imageData[2].length());
                ByteArrayInputStream in = new ByteArrayInputStream(b);
                BufferedImage img = ImageIO.read(in);
                ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(
                        70, 70, java.awt.Image.SCALE_SMOOTH));

                // preview three label
                labelPreviewThree = new JLabel(imgIcon);
                labelPreviewThree.setBounds(820, 405, 70, 70);
                labelPreviewThree.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            State.lodgeImageMenu.load();
                        } catch (Exception x) {
                            x.printStackTrace();
                        }
                    }
                });
                JesseSitesMastery6.customerPanel.add(labelPreviewThree);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void confirmOrder(String beginDate, String endDate) {
        try {
            String statement = "INSERT INTO Orders VALUES(?,?,?,?,?,?);";
            PreparedStatement ps = JesseSitesMastery6.con.prepareStatement(statement);
            String statementCount = "SELECT MAX(IdNumber) FROM Orders;";
            PreparedStatement psCount = JesseSitesMastery6.con.prepareStatement(statementCount);
            ResultSet rsCount = psCount.executeQuery();
            int orderCount;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date1 = dateFormat.parse(beginDate);
            java.util.Date date2 = dateFormat.parse(endDate);

            rsCount.next();
            orderCount = rsCount.getInt(1);
            ps.setInt(1, orderCount + 1);
            ps.setInt(2, orderLodgeNumber);
            ps.setInt(3, JesseSitesMastery6.verifiedCustomerIdNumber);
            ps.setDate(4, new java.sql.Date(date1.getTime()));
            ps.setDate(5, new java.sql.Date(date2.getTime()));
            ps.setBigDecimal(6, new BigDecimal(orderTotal));
            ps.executeUpdate();

            orderLodgeNumber = 0;
            orderNumberOfDays = 0;
            orderTotal = 0;
            labelOrderTotal.setText("Order Total: ");
        } catch (Exception x) {
            x.printStackTrace();
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
                    = JesseSitesMastery6.con.prepareStatement("SELECT IdNumber, "
                            + "LodgeIdNumber, StartDate, EndDate, Total "
                            + "FROM Orders "
                            + "WHERE CustomerIdNumber = " + JesseSitesMastery6.verifiedCustomerIdNumber + " "
                            + "AND StartDate >= '" + beginDate + "' "
                            + "AND EndDate <= '" + endDate + "';");
            ResultSet rs = ps.executeQuery();
            PreparedStatement psCount
                    = JesseSitesMastery6.con.prepareStatement("SELECT COUNT(*) "
                            + "FROM Orders "
                            + "WHERE CustomerIdNumber = " + JesseSitesMastery6.verifiedCustomerIdNumber + " "
                            + "AND StartDate >= '" + beginDate + "' "
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
                    + "<div><h1>Order History between Dates</h1><ul>";

            for (int i = 0; i < count; i++) {
                rs.next();
                html += "<li>Order ID: " + rs.getInt(1) + "</li>";
                html += "<li>Lodge ID: " + rs.getInt(2) + "</li>";
                html += "<li>Begin Date: " + rs.getString(3) + "</li>";
                html += "<li>End Date: " + rs.getString(4) + "</li>";
                html += "<li style='margin-bottom:10px;'>Total: $" + rs.getBigDecimal(5) + "</li>";
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
