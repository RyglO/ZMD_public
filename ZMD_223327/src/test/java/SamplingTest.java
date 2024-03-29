import Jama.Matrix;
import enums.SamplingType;
import jpeg.ColorSampling;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SamplingTest {

    // Input array
    static double[][] testArray = {{166.69, 160.9, 93.8, 83.32, 156.19, 238.48, 32.0, 69.17, 138.41, 16.03, 121.56, 62.69, 129.12, 45.86, 126.04, 137.77}, {135.48, 226.8, 63.72, 93.05, 59.53, 38.38, 140.2, 101.96, 213.72, 181.37, 235.39, 156.72, 47.2, 128.73, 52.84, 22.57}, {228.34, 102.61, 239.09, 101.42, 64.62, 176.99, 226.41, 128.49, 131.43, 173.19, 135.59, 249.42, 75.23, 200.23, 229.6, 210.49}, {100.47, 180.57, 75.35, 34.6, 242.1, 42.74, 148.85, 71.66, 12.6, 1.52, 88.7, 103.07, 8.74, 133.35, 254.14, 126.8}, {138.41, 34.84, 12.22, 116.72, 252.75, 175.21, 48.44, 32.27, 147.61, 244.31, 101.74, 118.07, 26.27, 72.25, 88.1, 176.17}, {212.74, 108.04, 141.88, 118.06, 116.63, 65.11, 81.01, 212.82, 155.81, 48.91, 25.28, 27.82, 223.7, 173.15, 92.49, 109.03}, {41.52, 139.52, 248.72, 78.49, 74.62, 122.99, 23.04, 53.4, 74.53, 99.08, 71.86, 152.58, 70.05, 242.63, 235.53, 233.88}, {16.82, 72.96, 73.3, 83.59, 180.15, 39.04, 153.42, 22.2, 190.11, 220.0, 92.24, 239.72, 206.9, 165.85, 130.16, 247.87}};

    // Expected output arrays from testArray
    static double[][] s411Down = {{166.69, 156.19, 138.41, 129.12}, {135.48, 59.53, 213.72, 47.2}, {228.34, 64.62, 131.43, 75.23}, {100.47, 242.1, 12.6, 8.74}, {138.41, 252.75, 147.61, 26.27}, {212.74, 116.63, 155.81, 223.7}, {41.52, 74.62, 74.53, 70.05}, {16.82, 180.15, 190.11, 206.9}};
    static double[][] s420Down = {{166.69, 93.8, 156.19, 32.0, 138.41, 121.56, 129.12, 126.04}, {228.34, 239.09, 64.62, 226.41, 131.43, 135.59, 75.23, 229.6}, {138.41, 12.22, 252.75, 48.44, 147.61, 101.74, 26.27, 88.1}, {41.52, 248.72, 74.62, 23.04, 74.53, 71.86, 70.05, 235.53}};
    static double[][] s422Down = {{166.69, 93.8, 156.19, 32.0, 138.41, 121.56, 129.12, 126.04}, {135.48, 63.72, 59.53, 140.2, 213.72, 235.39, 47.2, 52.84}, {228.34, 239.09, 64.62, 226.41, 131.43, 135.59, 75.23, 229.6}, {100.47, 75.35, 242.1, 148.85, 12.6, 88.7, 8.74, 254.14}, {138.41, 12.22, 252.75, 48.44, 147.61, 101.74, 26.27, 88.1}, {212.74, 141.88, 116.63, 81.01, 155.81, 25.28, 223.7, 92.49}, {41.52, 248.72, 74.62, 23.04, 74.53, 71.86, 70.05, 235.53}, {16.82, 73.3, 180.15, 153.42, 190.11, 92.24, 206.9, 130.16}};
    static double[][] s444Down = {{166.69, 160.9, 93.8, 83.32, 156.19, 238.48, 32.0, 69.17, 138.41, 16.03, 121.56, 62.69, 129.12, 45.86, 126.04, 137.77}, {135.48, 226.8, 63.72, 93.05, 59.53, 38.38, 140.2, 101.96, 213.72, 181.37, 235.39, 156.72, 47.2, 128.73, 52.84, 22.57}, {228.34, 102.61, 239.09, 101.42, 64.62, 176.99, 226.41, 128.49, 131.43, 173.19, 135.59, 249.42, 75.23, 200.23, 229.6, 210.49}, {100.47, 180.57, 75.35, 34.6, 242.1, 42.74, 148.85, 71.66, 12.6, 1.52, 88.7, 103.07, 8.74, 133.35, 254.14, 126.8}, {138.41, 34.84, 12.22, 116.72, 252.75, 175.21, 48.44, 32.27, 147.61, 244.31, 101.74, 118.07, 26.27, 72.25, 88.1, 176.17}, {212.74, 108.04, 141.88, 118.06, 116.63, 65.11, 81.01, 212.82, 155.81, 48.91, 25.28, 27.82, 223.7, 173.15, 92.49, 109.03}, {41.52, 139.52, 248.72, 78.49, 74.62, 122.99, 23.04, 53.4, 74.53, 99.08, 71.86, 152.58, 70.05, 242.63, 235.53, 233.88}, {16.82, 72.96, 73.3, 83.59, 180.15, 39.04, 153.42, 22.2, 190.11, 220.0, 92.24, 239.72, 206.9, 165.85, 130.16, 247.87}};

    // Expected output arrays from downsampled testArray
    static double[][] s411Up = {{166.69, 166.69, 166.69, 166.69, 156.19, 156.19, 156.19, 156.19, 138.41, 138.41, 138.41, 138.41, 129.12, 129.12, 129.12, 129.12}, {135.48, 135.48, 135.48, 135.48, 59.53, 59.53, 59.53, 59.53, 213.72, 213.72, 213.72, 213.72, 47.2, 47.2, 47.2, 47.2}, {228.34, 228.34, 228.34, 228.34, 64.62, 64.62, 64.62, 64.62, 131.43, 131.43, 131.43, 131.43, 75.23, 75.23, 75.23, 75.23}, {100.47, 100.47, 100.47, 100.47, 242.1, 242.1, 242.1, 242.1, 12.6, 12.6, 12.6, 12.6, 8.74, 8.74, 8.74, 8.74}, {138.41, 138.41, 138.41, 138.41, 252.75, 252.75, 252.75, 252.75, 147.61, 147.61, 147.61, 147.61, 26.27, 26.27, 26.27, 26.27}, {212.74, 212.74, 212.74, 212.74, 116.63, 116.63, 116.63, 116.63, 155.81, 155.81, 155.81, 155.81, 223.7, 223.7, 223.7, 223.7}, {41.52, 41.52, 41.52, 41.52, 74.62, 74.62, 74.62, 74.62, 74.53, 74.53, 74.53, 74.53, 70.05, 70.05, 70.05, 70.05}, {16.82, 16.82, 16.82, 16.82, 180.15, 180.15, 180.15, 180.15, 190.11, 190.11, 190.11, 190.11, 206.9, 206.9, 206.9, 206.9}};
    static double[][] s420Up = {{166.69, 166.69, 93.8, 93.8, 156.19, 156.19, 32.0, 32.0, 138.41, 138.41, 121.56, 121.56, 129.12, 129.12, 126.04, 126.04}, {166.69, 166.69, 93.8, 93.8, 156.19, 156.19, 32.0, 32.0, 138.41, 138.41, 121.56, 121.56, 129.12, 129.12, 126.04, 126.04}, {228.34, 228.34, 239.09, 239.09, 64.62, 64.62, 226.41, 226.41, 131.43, 131.43, 135.59, 135.59, 75.23, 75.23, 229.6, 229.6}, {228.34, 228.34, 239.09, 239.09, 64.62, 64.62, 226.41, 226.41, 131.43, 131.43, 135.59, 135.59, 75.23, 75.23, 229.6, 229.6}, {138.41, 138.41, 12.22, 12.22, 252.75, 252.75, 48.44, 48.44, 147.61, 147.61, 101.74, 101.74, 26.27, 26.27, 88.1, 88.1}, {138.41, 138.41, 12.22, 12.22, 252.75, 252.75, 48.44, 48.44, 147.61, 147.61, 101.74, 101.74, 26.27, 26.27, 88.1, 88.1}, {41.52, 41.52, 248.72, 248.72, 74.62, 74.62, 23.04, 23.04, 74.53, 74.53, 71.86, 71.86, 70.05, 70.05, 235.53, 235.53}, {41.52, 41.52, 248.72, 248.72, 74.62, 74.62, 23.04, 23.04, 74.53, 74.53, 71.86, 71.86, 70.05, 70.05, 235.53, 235.53}};
    static double[][] s422Up = {{166.69, 166.69, 93.8, 93.8, 156.19, 156.19, 32.0, 32.0, 138.41, 138.41, 121.56, 121.56, 129.12, 129.12, 126.04, 126.04}, {135.48, 135.48, 63.72, 63.72, 59.53, 59.53, 140.2, 140.2, 213.72, 213.72, 235.39, 235.39, 47.2, 47.2, 52.84, 52.84}, {228.34, 228.34, 239.09, 239.09, 64.62, 64.62, 226.41, 226.41, 131.43, 131.43, 135.59, 135.59, 75.23, 75.23, 229.6, 229.6}, {100.47, 100.47, 75.35, 75.35, 242.1, 242.1, 148.85, 148.85, 12.6, 12.6, 88.7, 88.7, 8.74, 8.74, 254.14, 254.14}, {138.41, 138.41, 12.22, 12.22, 252.75, 252.75, 48.44, 48.44, 147.61, 147.61, 101.74, 101.74, 26.27, 26.27, 88.1, 88.1}, {212.74, 212.74, 141.88, 141.88, 116.63, 116.63, 81.01, 81.01, 155.81, 155.81, 25.28, 25.28, 223.7, 223.7, 92.49, 92.49}, {41.52, 41.52, 248.72, 248.72, 74.62, 74.62, 23.04, 23.04, 74.53, 74.53, 71.86, 71.86, 70.05, 70.05, 235.53, 235.53}, {16.82, 16.82, 73.3, 73.3, 180.15, 180.15, 153.42, 153.42, 190.11, 190.11, 92.24, 92.24, 206.9, 206.9, 130.16, 130.16}};
    static double[][] s444Up = {{166.69, 160.9, 93.8, 83.32, 156.19, 238.48, 32.0, 69.17, 138.41, 16.03, 121.56, 62.69, 129.12, 45.86, 126.04, 137.77}, {135.48, 226.8, 63.72, 93.05, 59.53, 38.38, 140.2, 101.96, 213.72, 181.37, 235.39, 156.72, 47.2, 128.73, 52.84, 22.57}, {228.34, 102.61, 239.09, 101.42, 64.62, 176.99, 226.41, 128.49, 131.43, 173.19, 135.59, 249.42, 75.23, 200.23, 229.6, 210.49}, {100.47, 180.57, 75.35, 34.6, 242.1, 42.74, 148.85, 71.66, 12.6, 1.52, 88.7, 103.07, 8.74, 133.35, 254.14, 126.8}, {138.41, 34.84, 12.22, 116.72, 252.75, 175.21, 48.44, 32.27, 147.61, 244.31, 101.74, 118.07, 26.27, 72.25, 88.1, 176.17}, {212.74, 108.04, 141.88, 118.06, 116.63, 65.11, 81.01, 212.82, 155.81, 48.91, 25.28, 27.82, 223.7, 173.15, 92.49, 109.03}, {41.52, 139.52, 248.72, 78.49, 74.62, 122.99, 23.04, 53.4, 74.53, 99.08, 71.86, 152.58, 70.05, 242.63, 235.53, 233.88}, {16.82, 72.96, 73.3, 83.59, 180.15, 39.04, 153.42, 22.2, 190.11, 220.0, 92.24, 239.72, 206.9, 165.85, 130.16, 247.87}};

    @Test
    void sampleTest() {
        Matrix testS411Down = ColorSampling.sampleDown(new Matrix(testArray), SamplingType.S_4_1_1);
        Matrix testS420Down = ColorSampling.sampleDown(new Matrix(testArray), SamplingType.S_4_2_0);
        Matrix testS422Down = ColorSampling.sampleDown(new Matrix(testArray), SamplingType.S_4_2_2);
        Matrix testS444Down = ColorSampling.sampleDown(new Matrix(testArray), SamplingType.S_4_4_4);

        assertEquals(s411Down.length, testS411Down.getRowDimension(), "Error: Sampling 4:1:1 has wrong row dimension");
        assertEquals(s411Down[0].length, testS411Down.getColumnDimension(), "Error: Sampling 4:1:1 has wrong column dimension");
        assertArrayEquals(s411Down, testS411Down.getArray(), "Error: Sampling 4:1:1 returns different Matrix than expected. In Sampling class, check method sampleDown() for correct return type for sampling S411.");
        System.out.println("Sampling down 4:1:1: OK");


        assertEquals(s420Down.length, testS420Down.getRowDimension(), "Error: Sampling 4:2:0 has wrong row dimension");
        assertEquals(s420Down[0].length, testS420Down.getColumnDimension(), "Error: Sampling 4:2:0 has wrong column dimension");
        assertArrayEquals(s420Down, testS420Down.getArray(), "Error: Sampling 4:2:0 returns different Matrix than expected. In Sampling class, check method sampleDown() for correct return type for sampling S420.");
        System.out.println("Sampling down 4:2:0: OK");


        assertEquals(s422Down.length, testS422Down.getRowDimension(), "Error: Sampling 4:2:2 has wrong row dimension");
        assertEquals(s422Down[0].length, testS422Down.getColumnDimension(), "Error: Sampling 4:2:2 has wrong column dimension");
        assertArrayEquals(s422Down, testS422Down.getArray(), "Error: Sampling 4:2:2 returns different Matrix than expected. In Sampling class, check method sampleDown() for correct return type for sampling S422.");
        System.out.println("Sampling down 4:2:2: OK");

        assertArrayEquals(s444Down, testS444Down.getArray(), "Error: Sampling 4:4:4 returns different Matrix than expected. In Sampling class, check method sampleDown() for correct return type for sampling S444.");
        System.out.println("Sampling down: OK");


        Matrix testS411Up = ColorSampling.sampleUp(testS411Down, SamplingType.S_4_1_1);
        Matrix testS420Up = ColorSampling.sampleUp(testS420Down, SamplingType.S_4_2_0);
        Matrix testS422Up = ColorSampling.sampleUp(testS422Down, SamplingType.S_4_2_2);
        Matrix testS444Up = ColorSampling.sampleUp(testS444Down, SamplingType.S_4_4_4);

        assertArrayEquals(s411Up, testS411Up.getArray(), "Error: Sampling 4:1:1 returns different Matrix than expected. In Sampling class, check method sampleUp() for correct return type for sampling S411.");
        assertArrayEquals(s420Up, testS420Up.getArray(), "Error: Sampling 4:2:0 returns different Matrix than expected. In Sampling class, check method sampleUp() for correct return type for sampling S420.");
        assertArrayEquals(s422Up, testS422Up.getArray(), "Error: Sampling 4:2:2 returns different Matrix than expected. In Sampling class, check method sampleUp() for correct return type for sampling S422.");
        assertArrayEquals(s444Up, testS444Up.getArray(), "Error: Sampling 4:4:4 returns different Matrix than expected. In Sampling class, check method sampleUp() for correct return type for sampling S444.");

        System.out.println("Sampling up: OK");
    }
}
