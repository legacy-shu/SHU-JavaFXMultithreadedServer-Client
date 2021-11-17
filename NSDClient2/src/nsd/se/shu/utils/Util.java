package nsd.se.shu.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class Util {
    public static String imageToBase64(String path) throws IOException {
        InputStream input = new FileInputStream(path);
        BufferedImage originalImage = ImageIO.read(input);
        BufferedImage reimage = resizeImage(originalImage,100,100);
        byte[] byteArray = toByteArrayAutoClosable(reimage, "jpg");
        String imageString = Base64.getEncoder().withoutPadding().encodeToString(byteArray);
        return imageString;
    }
    public static BufferedImage base64ToIamge(String imageString) throws IOException {
        byte[] decodeImg = Base64.getDecoder().decode(imageString);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decodeImg));
        return img;
    }
    private static byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
