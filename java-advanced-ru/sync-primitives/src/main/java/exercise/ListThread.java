package exercise;

// BEGIN
public class ListThread extends Thread {
    SafetyList safetyList;

    public ListThread(SafetyList safetyList) {
        this.safetyList = safetyList;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            safetyList.add(i);
        }
    }
}
// END
