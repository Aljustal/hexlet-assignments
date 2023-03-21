package exercise;

// BEGIN
public class MaxThread extends Thread {
    private int[] numbers;
    private int max;

    public MaxThread(int[] numbers) {
        this.numbers = numbers;
    }

    public int getMax() {
        return max;
    }

    public void run() {
        max = numbers[0];
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
    }
}
// END
