package ru.javaops.masterjava.matrix;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final List<Callable<Void>> list = IntStream.range(0, matrixSize)
                .parallel()
                .mapToObj(i -> new Callable<Void>() {
                    private final int[] columnB = IntStream.range(0, matrixSize).map(j -> matrixB[j][i]).toArray();

                    @Override
                    public Void call() throws Exception {
                        for (int j = 0; j < matrixSize; j++) {
                            int[] rowA = matrixA[j];
                            matrixC[j][i] = IntStream.range(0, matrixSize).map(k -> rowA[k]*columnB[k]).sum();
                        }
                        return null;
                    }
                }).collect(Collectors.toList());

        executor.invokeAll(list);

        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] columnB = new int[matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                columnB[j] = matrixB[j][i];
            }

            for (int j = 0; j < matrixSize; j++) {
                int[] rowA = matrixA[j];
                matrixC[j][i] = IntStream.range(0,matrixSize).map(k -> rowA[k] * columnB[k]).sum();
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
