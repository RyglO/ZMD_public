package jpeg;
import Jama.Matrix;
import enums.ColorType;
import enums.QualityType;
import enums.SamplingType;
import enums.TransformType;
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

    public double mse, sae, mae, psnr, ssim, mssim;
    private double redMSE, blueMSE, greenMSE, yMSE, cbMSE, crMSE;
    private double redSAE, blueSAE, greenSAE, ySAE, cbSAE, crSAE;
    private double redMAE, blueMAE, greenMAE, yMAE, cbMAE, crMAE;
    private double redPSNR, bluePSNR, greenPSNR, yPSNR, cbPSNR, crPSNR;



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

        modifiedY = new Matrix(imageHeight, imageWidth);
        modifiedCb = new Matrix(imageHeight, imageWidth);
        modifiedCr = new Matrix(imageHeight, imageWidth);
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
                color.length, color[0].length, BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < bfImage.getHeight(); h++) {
            for (int w = 0; w < bfImage.getWidth(); w++) {
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
                color.getColumnDimension(), color.getRowDimension(),
                BufferedImage.TYPE_INT_RGB);
        for(int h = 0; h < bfImage.getHeight(); h++){
            for (int w = 0; w < bfImage.getWidth(); w++) {
                bfImage.setRGB(w,h,
                        (new Color(roundRange(color.get(h,w)),
                                roundRange(color.get(h,w)),
                                roundRange(color.get(h,w))).getRGB()));
            }
        }
        return bfImage;
    }
    //Need this for Y, Cb, Cr transformed 
    public static int roundRange(double num) {
        return Math.min(Math.max((int) Math.round(num), 0), 255);
    }
    /**
     * Přepočet z originální grayscale do upraveného RGB
     * Jakmile budeme pracovat s grayscale obrzem, je nutné změnit paramaetry z orig grayscale na modified grayscale.
     */
    public void convertToRGB()
    {
        int[][][] pom = ColorTransform.convertModifiedYcBcRtoRGB(modifiedY, modifiedCb, modifiedCr);
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
        originalY = modifiedY = pom[0];
        originalCb = modifiedCb = pom[1];
        originalCr = modifiedCr = pom[2];
    }

    public void downSample(SamplingType samplingType)
    {
        modifiedCb = ColorSampling.sampleDown(modifiedCr, samplingType);
        modifiedCr = ColorSampling.sampleDown(modifiedCb, samplingType);
    }
    public void upSample(SamplingType samplingType){
        modifiedCb = ColorSampling.sampleUp(modifiedCr, samplingType);
        modifiedCr = ColorSampling.sampleUp(modifiedCr, samplingType);
    }

    public String PSNRcount(QualityType qualityType){

        return "";
    }
    public void MAEcount(){
        redMAE = Quality.countMAE(convertIntToDouble(originalRed), convertIntToDouble(modifiedRed));
        greenMAE = Quality.countMAE(convertIntToDouble(originalGreen), convertIntToDouble(modifiedGreen));
        blueMAE = Quality.countMAE(convertIntToDouble(originalBlue), convertIntToDouble(modifiedBlue));
        yMAE =  Quality.countMAE(originalY.getArray(), modifiedY.getArray());
        cbMAE =  Quality.countMAE(originalCb.getArray(), modifiedCb.getArray());
        crMAE =  Quality.countMAE(originalCr.getArray(), modifiedCr.getArray());
    }
    public void MSEcount(){
        redMSE = Quality.countMSE(convertIntToDouble(originalRed), convertIntToDouble(modifiedRed));
        greenMSE = Quality.countMSE(convertIntToDouble(originalGreen), convertIntToDouble(modifiedGreen));
        blueMSE = Quality.countMSE(convertIntToDouble(originalBlue), convertIntToDouble(modifiedBlue));
        yMSE =  Quality.countMSE(originalY.getArray(), modifiedY.getArray());
        cbMSE =  Quality.countMSE(originalCb.getArray(), modifiedCb.getArray());
        crMSE =  Quality.countMSE(originalCr.getArray(), modifiedCr.getArray());
    }
    public void SAEcount(){

        redSAE = Quality.countSAE(convertIntToDouble(originalRed), convertIntToDouble(modifiedRed));
        greenSAE = Quality.countSAE(convertIntToDouble(originalGreen), convertIntToDouble(modifiedGreen));
        blueSAE = Quality.countSAE(convertIntToDouble(originalBlue), convertIntToDouble(modifiedBlue));
        ySAE =  Quality.countSAE(originalY.getArray(), modifiedY.getArray());
        cbSAE =  Quality.countSAE(originalCb.getArray(), modifiedCb.getArray());
        crSAE =  Quality.countSAE(originalCr.getArray(), modifiedCr.getArray());
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



    public static double[][] convertIntToDouble(int[][] intArray) {
        double[][] doubleArray = new double[intArray.length][intArray[0].length];
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[0].length; j++) {
                doubleArray[i][j] = (double) intArray[i][j];
            }
        }
        return doubleArray;
    }

    public void GetQualityValues(QualityType color)
    {
        switch (color){
            case Red:
                mae = redMAE;
                sae = redSAE;
                mse = redMSE;
                psnr = Quality.countPSNR(redMSE);
                break;
            case Green:
                mae = greenMAE;
                sae = greenSAE;
                mse = greenMSE;
                psnr = Quality.countPSNR(greenMSE);
                break;
            case Blue:
                mae = blueMAE;
                sae = blueSAE;
                mse = blueMSE;
                psnr = Quality.countPSNR(blueMSE);
                break;
            case Y:
                mae = yMAE;
                sae = ySAE;
                mse = yMSE;
                psnr = Quality.countPSNR(yMSE);
                break;
            case Cb:
                mae = cbMAE;
                sae = cbSAE;
                mse = cbMSE;
                psnr = Quality.countPSNR(cbMSE);
                break;
            case Cr:
                mae = crMAE;
                sae = crSAE;
                mse = crMSE;
                psnr = Quality.countPSNR(crMSE);
                break;
            case RGB:
                mae = (redMAE + greenMAE + blueMAE) /3;
                sae = (redSAE + greenSAE + blueSAE) /3;
                mse = (redMSE + greenMSE + blueSAE) /3;
                psnr = Quality.countPSNR(mse);
                break;
            case YCbCr:
                mae = (yMAE + cbMAE + crMAE) /3;
                sae = (ySAE + cbSAE + crSAE) /3;
                mse = (yMSE + cbMSE + crSAE) /3;
                psnr = Quality.countPSNR(mse);
                break;

        }
    }

    //Transformace
    public void transformImage(TransformType type, Boolean inverse, int blockSize){
        if(inverse){
            modifiedY = Transform.inverseTransform(modifiedY, type, blockSize);
            modifiedCb = Transform.inverseTransform(modifiedCb, type, blockSize);
            modifiedCr = Transform.inverseTransform(modifiedCr, type, blockSize);
        }
        else{
            modifiedY = Transform.transform(modifiedY, type, blockSize);
            modifiedCb = Transform.transform(modifiedCb, type, blockSize);
            modifiedCr = Transform.transform(modifiedCr, type, blockSize);
        }
    }

}
