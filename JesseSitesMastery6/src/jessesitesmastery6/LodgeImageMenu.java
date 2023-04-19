/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author jesse
 */
public class LodgeImageMenu extends State {

    // lodge image label
    static JLabel labelLodgeImage = new JLabel();
    // image data and index
    static Blob[] imageData;
    static int imageIndex;

    @Override
    public void enter() {
        update();
    }

    @Override
    public void update() {
        while (true) {
            JesseSitesMastery6.lodgeImagePanel.revalidate();
            JesseSitesMastery6.lodgeImagePanel.repaint();

            if (!JesseSitesMastery6.lodgeImagePanel.isVisible()) {
                break;
            }
        }

        State.current.enter();
    }

    @Override
    public void load() {
        try {
            byte[] b = imageData[0].getBytes(1,
                    (int) imageData[0].length());
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            BufferedImage img = ImageIO.read(in);
            ImageIcon imgIcon = new ImageIcon(img.getScaledInstance(
                    400, 400, java.awt.Image.SCALE_SMOOTH));

            JesseSitesMastery6.lodgeImagePanel.remove(LodgeImageMenu.labelLodgeImage);
            imageIndex = 0;
            // lodge image label
            LodgeImageMenu.labelLodgeImage = new JLabel(imgIcon);
            LodgeImageMenu.labelLodgeImage.setBounds(455, 20, 400, 400);
            JesseSitesMastery6.lodgeImagePanel.add(LodgeImageMenu.labelLodgeImage);

            if (State.current == State.customerMenu) {
                State.previous = State.customerMenu;
            }

            if (State.current == State.employeeMenu) {
                State.previous = State.employeeMenu;
            }

            State.current = State.lodgeImageMenu;
            JesseSitesMastery6.lodgeImagePanel.setVisible(true);

            if (State.previous == State.customerMenu) {
                JesseSitesMastery6.customerPanel.setVisible(false);
                return;
            }

            JesseSitesMastery6.employeeTabs.setVisible(false);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public void save() {
        if (State.current == State.customerMenu) {
            LodgeImageMenu.imageData = CustomerMenu.imageData;
            return;
        }

        LodgeImageMenu.imageData = EmployeeMenu.imageData;
    }
}
