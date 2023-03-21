package exercise;

import java.util.ArrayList;

class SafetyList {
    // BEGIN
    ArrayList<Integer> list = new ArrayList<Integer>();

    public synchronized void add(Integer num) {
        list.add(num);
    }
    public Integer get(Integer index) {
        return list.get(index);
    }
    public Integer getSize() {
        return list.size();
    }
    // END
}
