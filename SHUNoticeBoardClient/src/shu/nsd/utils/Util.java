package shu.nsd.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class Util {
    public static String imageToBase64(String path){
        BufferedImage originalImage = getBufferedImage(path);
        BufferedImage resizeImage = resizeImage(originalImage,150,150);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(resizeImage, "jpg", out);
            return Base64.getEncoder().withoutPadding().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Image base64ToIamge(String imageString){
        byte[] decodeImg = Base64.getDecoder().decode(imageString);
        BufferedImage img;
        try {
            img = ImageIO.read(new ByteArrayInputStream(decodeImg));
            return SwingFXUtils.toFXImage(img, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Image getImage(String path){
        BufferedImage buffImg = getBufferedImage(path);
        Image img = null;
        if(buffImg != null)
            img = getImage(buffImg);
        return img;
    }
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
    private static BufferedImage getBufferedImage(String path){
        BufferedImage image = null;
        try (FileInputStream inputStream = new FileInputStream(path)) {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    private static Image getImage(BufferedImage bufferedImage){
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

}
