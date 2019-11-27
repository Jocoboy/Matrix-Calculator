public class Matrix {

    String name;
    double[][] array;

    public Matrix(String inputName, double[][] inputArray) {
        this.name = inputName;

        int row = inputArray.length;
        int column = inputArray[0].length;
        this.array = new double[row][column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                this.array[i][j] = inputArray[i][j];
            }
        }
    }

}