package exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private static SafetyList list;

    @BeforeEach
    void beforeAll() {
        list = new SafetyList();
    }

    @RepeatedTest(value = 3)
    void testThreadSafetys() throws InterruptedException {

        Thread thread1 = new Thread(new ListThread(list));
        Thread thread2 = new Thread(new ListThread(list));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertThat(list.getSize()).isEqualTo(2000);
    }
}
