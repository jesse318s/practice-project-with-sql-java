/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author jesse
 */
public class ImageLoader implements Runnable {

    Thread t;

    ImageLoader() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            if (!EmployeeMenu.isLoadingImage) {
                EmployeeMenu.isLoadingImage = true;

                PreparedStatement ps = JesseSitesMastery6.con.prepareStatement("SELECT LodgeIdNumber, Img "
                        + "FROM LodgeImages;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                EmployeeMenu.rsLodgeImg = ps.executeQuery();
                PreparedStatement psCount = JesseSitesMastery6.con.prepareStatement(
                        "SELECT COUNT(*) FROM LodgeImages;");
                ResultSet rsCount = psCount.executeQuery();

                rsCount.next();
                EmployeeMenu.lodgeImgCount = rsCount.getInt(1);
                EmployeeMenu.loadSelectedLodgeImages();
                EmployeeMenu.isLoadingImage = false;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
