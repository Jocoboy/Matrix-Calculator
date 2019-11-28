public class MatrixArith {

    /**
     * 
     * @param matrixA
     * @param matrixB
     * @return matrixA + matrixB
     * @throws MatrixArithException
     */
    public static Matrix add(Matrix matrixA, Matrix matrixB) throws MatrixArithException {
        /**
         * Row and column of matrix A.
         */
        int row = matrixA.array.length;
        int column = matrixA.array[0].length;

        Matrix outputMatrix = null;
        double[][] outputArray = new double[row][column];

        if (row != matrixB.array.length || column != matrixB.array[0].length) {
            outputArray = null;
            outputMatrix = new Matrix("null", outputArray);
            throw new MatrixArithException(
                    "Invalid operation." + "\n" + "All rows and columns must have the same number of elements.");
        } else {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    outputArray[i][j] = matrixA.array[i][j] + matrixB.array[i][j];
                }
            }
            outputMatrix = new Matrix("temp", outputArray);
        }
        return outputMatrix;
    }

    /**
     * 
     * @param matrixA
     * @param matrixB
     * @return matrixA - matrixB
     * @throws MatrixArithException
     */
    public static Matrix sub(Matrix matrixA, Matrix matrixB) throws MatrixArithException {
        /**
         * Row and column of matrix A.
         */
        int row = matrixA.array.length;
        int column = matrixA.array[0].length;

        Matrix outputMatrix = null;
        double[][] outputArray = new double[row][column];

        if (row != matrixB.array.length || column != matrixB.array[0].length) {
            outputArray = null;
            outputMatrix = new Matrix("null", outputArray);
            throw new MatrixArithException(
                    "Invalid operation." + "\n" + "All rows and columns must have the same number of elements.");
        } else {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    outputArray[i][j] = matrixA.array[i][j] - matrixB.array[i][j];
                }
            }
            outputMatrix = new Matrix("temp", outputArray);
        }
        return outputMatrix;
    }

    /**
     * 
     * @param matrixA
     * @param matrixB
     * @return matrixA × matrixB
     * @throws MatrixArithException
     */
    public static Matrix mul(Matrix matrixA, Matrix matrixB) throws MatrixArithException {
        /**
         * Row of matrix B. Column of matrix A.
         */
        int row = matrixB.array.length;
        int column = matrixA.array[0].length;

        Matrix outputMatrix = null;
        double[][] outputArray = new double[matrixA.array.length][matrixB.array[0].length];

        if (column != row) {
            outputArray = null;
            outputMatrix = new Matrix("null", outputArray);
            throw new MatrixArithException("Invalid operation." + "\n"
                    + "The row of matrix B and the column of matrix A must have the same number of elements.");
        } else {
            for (int i = 0; i < matrixA.array.length; i++) {
                for (int j = 0; j < matrixB.array[0].length; j++) {
                    for (int k = 0; k < row; k++) {
                        outputArray[i][j] += matrixA.array[i][k] * matrixB.array[k][j];
                    }
                }
            }
            outputMatrix = new Matrix("temp", outputArray);
        }
        return outputMatrix;
    }

    /**
     * 
     * @param matrixA
     * @param matrixB
     * @return matrixA × matrixB-¹
     * @throws MatrixArithException
     */
    public static Matrix div(Matrix matrixA, Matrix matrixB) throws MatrixArithException {
        Matrix outputMatrix = null;
        try {
            outputMatrix = mul(matrixA, inv(matrixB));
        } catch (MatrixArithException e) {
            throw new MatrixArithException(e.toString());
        }
        return outputMatrix;
    }

    /**
     * 
     * @param matrix
     * @return matrix-¹
     * @throws MatrixArithException
     */
    public static Matrix inv(Matrix matrix) throws MatrixArithException {

        int row = matrix.array.length;

        Matrix outputMatrix = null;
        double[][] outputArray = new double[row][row];
        double[][] expandArray = new double[row][row * 2];

        if (row != matrix.array[0].length) {
            outputArray = null;
            outputMatrix = new Matrix("null", outputArray);
            throw new MatrixArithException(
                    "Invalid operation." + "\n" + "The row and column must have the same number of elements.");
        } else {

            /********* construct module ************/
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < row; j++) {
                    expandArray[i][j] = matrix.array[i][j];
                }
            }
            for (int i = 0; i < row; i++) {
                for (int j = row; j < row * 2; j++) {
                    if (i == j - row) {
                        expandArray[i][j] = 1;
                    } else {
                        expandArray[i][j] = 0;
                    }
                }
            }

            /********* inverse module ************/
            for (int i = 0; i < row; i++) {
                int k;
                // Ensure that the element located in (i, i) is non-zero.
                if (expandArray[i][i] == 0) {
                    for (k = i + 1; k < row; k++) {
                        if (expandArray[k][i] != 0) {
                            // Swap row i and row k.
                            for (int j = 0; j < row * 2; j++) {
                                double temp = expandArray[i][j];
                                expandArray[i][j] = expandArray[k][j];
                                expandArray[k][j] = temp;
                            }
                            break;
                        }
                    }
                    if (k == row) {
                        outputArray = null;
                        outputMatrix = new Matrix("null", outputArray);
                        throw new MatrixArithException(
                                "Invalid operation." + "\n" + "One of columns has no non-zero elements.");
                    }
                }
                // Simplify row i.
                for (int j = row * 2 - 1; j >= i; j--) {
                    expandArray[i][j] = expandArray[i][j] / expandArray[i][i];
                }
                // (A E) -> (E A-¹)
                for (k = 0; k < row; k++) {
                    if (k != i) {
                        for (int j = 0; j < row * 2; j++) {
                            expandArray[k][j] = expandArray[k][j] - expandArray[k][i] * expandArray[i][j];
                        }
                    }
                }
            }
            /********* inverse module ************/

            /********* export module ************/
            for (int i = 0; i < row; i++) {
                for (int j = row; j < row * 2; j++) {
                    outputArray[i][j - row] = expandArray[i][j];
                }
            }
            outputMatrix = new Matrix("temp", outputArray);
            /********* export module ************/

        }
        return outputMatrix;
    }
}