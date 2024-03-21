package jpeg;

import Jama.Matrix;

public class Quality {
    public static double countMSE(double[][] original, double[][] modified) {
        double resultmse = 0;
        int width = original.length;
        int height = original[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double error = original[i][j] - modified[i][j];
                resultmse += error * error;
            }
        }
        resultmse /= (width * height);
        return resultmse;
    }
    public static double countMAE(double[][] original, double[][] modified) {

        double resultmae = 0;
        int w = original.length;
        int h = original[0].length;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                resultmae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        resultmae /= (w * h);
        return resultmae;
    }
    public static double countSAE(double[][] original, double[][] modified) {
        double resultsae = 0;
        int w = original.length;
        int h = original[0].length;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                resultsae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        return resultsae;
    }

    public static double countPSNR(double MSE) {
        double psnr = 10 * Math.log10(255 * 255 / MSE);
        return psnr;
    }


    public static double countPSNRforRGB(double mseRed, double mseGreen, double mseBlue) {
        double mse = (mseRed + mseGreen + mseBlue) / 3;
        double rgb = countPSNR(mse);
        return rgb;
    }

    public static double countSSIM(Matrix original, Matrix modified) {
        throw new RuntimeException("TBD...");
    }

    public static double countMSSIM(Matrix original, Matrix modified) {
        throw new RuntimeException("TBD...");
    }

}