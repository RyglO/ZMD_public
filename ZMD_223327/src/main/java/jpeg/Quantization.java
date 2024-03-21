package jpeg;

import Jama.Matrix;

public class Quantization {

    // Metoda pro kvantizaci
    public static Matrix quantize(Matrix input, int blockSize, double quality, boolean matrixY) {
        Matrix quantizationMatrix = getQuantizationMatrix(blockSize, quality, matrixY);
        Matrix quantizedMatrix = new Matrix(input.getRowDimension(), input.getColumnDimension());

        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix block = input.getMatrix(i,i + blockSize - 1, j, j + blockSize -1);

                for (int k = 0; k < block.getRowDimension(); k ++) {
                    for (int l =0; l < block.getColumnDimension(); l ++) {
                        double value = block.get(k, l) / quantizationMatrix.get(k, l);
                        block.set(k,l, value >= -0.2 && value <= 0.2 ? (double) Math.round(value*100)/100 : (double) Math.round(value*10)/10);
                    }
                }
                quantizedMatrix.setMatrix(i,i + blockSize - 1, j, j + blockSize -1,block);
            }
        }
        return quantizedMatrix;
    }

    // Metoda pro inverzni kvantizaci
    public static Matrix inverseQuantize(Matrix input, int blockSize, double quality, boolean matrixY) {
        Matrix quantizationMatrix = getQuantizationMatrix(blockSize, quality, matrixY);
        Matrix inverseQuantizedMatrix = new Matrix(input.getRowDimension(), input.getColumnDimension());
        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix block = input.getMatrix(i,i + blockSize - 1, j, j + blockSize -1);
                inverseQuantizedMatrix.setMatrix(i,i + blockSize - 1, j, j + blockSize -1,block.arrayTimes(quantizationMatrix));
            }
        }
        return inverseQuantizedMatrix;
    }

    // Metoda pro ziskani kvantizacni matice
    public static Matrix getQuantizationMatrix (int blockSize, double quality, boolean matrixY) {
        if (quality == 100) {
            return new Matrix(blockSize, blockSize, 1);
        }
        Matrix resultMatrix;
        if (matrixY) {
            resultMatrix = matrixYval;
        } else {
            resultMatrix = matrixVal;
        }
        if (blockSize < 8) {
            for (int i = 0; i < blockSize; i += 2){
                resultMatrix = ColorSampling.downSample(resultMatrix);
                resultMatrix = resultMatrix.transpose();
            }
        } else if (blockSize > 8) {
            for (int i = 8; i <= blockSize; i += 8) {
                resultMatrix = ColorSampling.upSample(resultMatrix);
                resultMatrix = resultMatrix.transpose();
            }
        }
        double alpha;
        if (quality < 50) {
            alpha = 50 / quality;
        } else {
            alpha = 2 - ((2 * quality) / 100);
        }
        return resultMatrix.times(alpha);
    }


    // Kvantizacni matice 8x8 pro jasovou slozku
    private static final double[][] quantizationMatrix8Y = {
            {16, 11, 10, 16, 24, 40, 51, 61},
            {12, 12, 14, 19, 26, 58, 60, 55},
            {14, 13, 16, 24, 40, 57, 69, 56},
            {14, 17, 22, 29, 51, 87, 80, 62},
            {18, 22, 37, 56, 68, 109, 103, 77},
            {24, 35, 55, 64, 81, 104, 113, 92},
            {49, 64, 78, 87, 103, 121, 120, 101},
            {72, 92, 95, 98, 112, 100, 103, 99}};

    // Kvantizacni matice 8x8 pro chromaticke slozky
    private static final double[][] quantizationMatrix8C = {
            {17, 18, 24, 47, 99, 99, 99, 99},
            {18, 21, 26, 66, 99, 99, 99, 99},
            {24, 26, 56, 99, 99, 99, 99, 99},
            {47, 66, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99}};

    public static Matrix matrixVal = new Matrix(8,8).constructWithCopy(quantizationMatrix8C);
    public static Matrix matrixYval = new Matrix(8,8).constructWithCopy(quantizationMatrix8Y);
}
