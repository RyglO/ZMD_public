package jpeg;

import enums.SamplingType;
import Jama.Matrix;

public class ColorSampling {


    public static Matrix sampleDown(Matrix inputMatrix, SamplingType samplingType)
    {
        Matrix sampledMatrix = null;

        switch (samplingType){
            case S_4_4_4 -> sampledMatrix = inputMatrix;
            case S_4_2_2 -> sampledMatrix = downSample(inputMatrix);
            case S_4_2_0 -> {
                sampledMatrix = downSample(inputMatrix);
                sampledMatrix = sampledMatrix.transpose();
                sampledMatrix = downSample(sampledMatrix);
                sampledMatrix = sampledMatrix.transpose();
            }
            case S_4_1_1 -> {
                sampledMatrix = downSample(inputMatrix);
                sampledMatrix = downSample(sampledMatrix);
            }
        }
        return sampledMatrix;
    }

    public static Matrix sampleUp(Matrix inputMatrix, SamplingType samplingType)
    {
        Matrix sampledMatrix = null;

        switch (samplingType){
            case S_4_4_4 -> sampledMatrix = inputMatrix;
            case S_4_2_2 -> sampledMatrix = upSample(inputMatrix);
            case S_4_2_0 -> {
                sampledMatrix = upSample(inputMatrix);
                sampledMatrix = sampledMatrix.transpose();
                sampledMatrix = upSample(sampledMatrix);
                sampledMatrix = sampledMatrix.transpose();
            }
            case S_4_1_1 -> {
                sampledMatrix = upSample(inputMatrix);
                sampledMatrix = upSample(sampledMatrix);
            }
        }
        return sampledMatrix;

    }


    public static Matrix downSample(Matrix matrix){
        int rowsCount = matrix.getRowDimension();
        int columnsCount = matrix.getColumnDimension();
        double[][] newMatrix = new double[rowsCount][columnsCount/2];

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j+=2) {
                newMatrix[i][j/2] = matrix.get(i,j);
            }
        }
        return new Matrix(newMatrix);
    }
    public static Matrix upSample(Matrix matrix){
        Matrix newMatrix = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension()*2);
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            newMatrix.setMatrix(0, matrix.getRowDimension()-1, 2*i, 2*i, matrix.getMatrix(0, matrix.getRowDimension()-1, i, i));
            newMatrix.setMatrix(0, matrix.getRowDimension()-1, 2*i+1, 2*i+1, matrix.getMatrix(0, matrix.getRowDimension()-1, i, i));
        }
        return newMatrix;
    }
}
