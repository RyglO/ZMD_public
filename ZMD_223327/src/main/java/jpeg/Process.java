package jpeg;
import Jama.Matrix;
import enums.ColorType;
import graphics.Dialogs;

import java.awt.*;
import java.awt.image.BufferedImage;

import static enums.ColorType.*;

/**
 * Třída, která bude obsahovat všechnu práci s obrázkem.
 */
public class Process {

    private BufferedImage originalImage;
    private int imageHeight;
    private int imageWidth;
    private int [][] originalRed, modifiedRed;
    private int [][] originalGreen, modifiedGreen;
    private int [][] originalBlue, modifiedBlue;

    private Matrix originalY, modifiedY;
    private Matrix originalCb, modifiedCb;
    private Matrix originalCr, modifiedCr;


    /**
     * Kontruktor
     * Musím předat adresu originálního obrázku.
     * Zároveň z něj musím inicializovat i jednotlivé matice s barvami.
     * @param path Cesta k obrázku
     */
    public Process(String path) {

        this.originalImage = Dialogs.loadImageFromPath(path);

        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();

        originalRed = new int[imageHeight][imageWidth];
        originalGreen = new int[imageHeight][imageWidth];
        originalBlue = new int[imageHeight][imageWidth];

        originalY = new Matrix(imageHeight, imageWidth);
        originalCb = new Matrix(imageHeight, imageWidth);
        originalCr = new Matrix(imageHeight, imageWidth);
        setOriginalRGB();
    }
    /**
     * Naplnění matic originálními barvami
     */
    private void setOriginalRGB(){
        for(int h = 0; h < imageHeight; h++){
            for (int w = 0; w < imageWidth; w++){
                Color color = new Color(originalImage.getRGB(w,h));
                originalRed[h][w] = color.getRed();
                originalGreen[h][w] = color.getGreen();
                originalBlue[h][w] = color.getBlue();
            }
        }
    }

    /**
     * Zobrazení obrázku v upravených RGB barvách.
     * @return Vrácení výsledného obrázku
     */
    public BufferedImage getImageFromRGB(){
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++){
            for (int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,
                        (new Color(modifiedRed[h][w],
                                modifiedGreen[h][w],
                                modifiedBlue[h][w])).getRGB());
            }
        }
        return bfImage;
    }
    /**
     * Na základě parametrů musím rozhodnout, kterou barevnou složku chci zobrazit.
     * Nově vytvořený enum ColorType nabízí možnost RED, GREEN, BLUE, které jsou později použity při rozhodování ve switchi.
     * @param color Matice barvy která bude vykreslena
     * @param type Barva, kteou chceme zobrazit
     * @return - Vrácení výsledného obrázku
     */
    public BufferedImage showOneColorImageFromRGB(int[][] color, enums.ColorType type)
    {
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                switch (type) {
                    case RED:
                        bfImage.setRGB(w, h, (new Color(color[h][w], 0, 0)).getRGB());
                        break;
                    case GREEN:
                        bfImage.setRGB(w, h, (new Color(0, color[h][w], 0)).getRGB());
                        break;
                    case BLUE:
                        bfImage.setRGB(w, h, (new Color(0, 0, color[h][w])).getRGB());
                        break;
                }
            }
        }

        return bfImage;
    }

    /**
     * Jakmile se zavolá tato metoda, zobrazí se obrázek v barvě, která je předána jako parametr.
     * Když je stejná hodnota v každo složce R G B, bude obrázek šedý.
     * @param color Matice barvy kterou chci zobrazit.
     * @return Vrácení výsledného obrázku
     */
    public BufferedImage showOneColorImageFromYCbCr(Matrix color)
    {
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < imageHeight; h++){
            for (int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,
                        (new Color((int)color.get(h,w),
                                (int)color.get(h,w),
                                (int)color.get(h,w)).getRGB()));
            }
        }
        return bfImage;
    }
    /**
     * Přepočet z originální grayscale do upraveného RGB
     * Jakmile budeme pracovat s grayscale obrzem, je nutné změnit paramaetry z orig grayscale na modified grayscale.
     */
    public void convertToRGB()
    {
        int[][][] pom = ColorTransform.convertModifiedYcBcRtoRGB(originalY, originalCb, originalCr);
        modifiedRed = pom[0];
        modifiedGreen = pom[1];
        modifiedBlue = pom[2];
    }
    /**
     * Přepočet z originálních barev do grayscale.
     */
    public void convertToYCbCr()
    {
        Matrix[] pom = ColorTransform.convertOriginalRGBtoYcBcR(originalRed, originalGreen, originalBlue);
        originalY = pom[0];
        originalCb = pom[1];
        originalCr = pom[2];
    }

    //Volání na základě stisku tlačítek pro zobrazení konkrétní barevné složky.
    public BufferedImage showOrigBlue()
    {
        return showOneColorImageFromRGB(originalBlue, BLUE);
    }
    public BufferedImage showOrigGreen()
    {
        return showOneColorImageFromRGB(originalGreen, GREEN);
    }
    public BufferedImage showOrigRed()
    {
        return showOneColorImageFromRGB(originalRed,  RED);
    }
    public BufferedImage showModifBlue()
    {
        return showOneColorImageFromRGB(modifiedBlue, BLUE);
    }
    public BufferedImage showModifGreen()
    {
        return showOneColorImageFromRGB(modifiedGreen, GREEN);
    }
    public BufferedImage showModifRed()
    {
        return showOneColorImageFromRGB(modifiedRed,  RED);
    }
    public BufferedImage showOrigY()
    {
        return  showOneColorImageFromYCbCr(originalY);
    }
    public BufferedImage showModifY()
    {
        return  showOneColorImageFromYCbCr(modifiedY);
    }
    public BufferedImage showOrigCb()
    {
        return  showOneColorImageFromYCbCr(originalCb);
    }
    public BufferedImage showModifCb()
    {
        return  showOneColorImageFromYCbCr(modifiedCb);
    }
    public BufferedImage showOrigCr()
    {
        return  showOneColorImageFromYCbCr(originalCr);
    }
    public BufferedImage showModifCr()
    {
        return  showOneColorImageFromYCbCr(modifiedCr);
    }
}
