package exercise;

// BEGIN
public class MinThread extends Thread {
    private int[] numbers;
    private int min;

    public MinThread(int[] numbers) {
        this.numbers = numbers;
    }

    public int getMin() {
        return min;
    }

    public void run() {
        min = numbers[0];
        for (int i = 0; i < numbers.length - 1; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
    }
}
// END
