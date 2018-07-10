import controllers.Request;
import controllers.TransactionManager;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertTrue;

public class TransactionManagerTest {


    @Test
    public void testLessThanSixtySeconds() throws Exception{
        long millis = System.currentTimeMillis() % 1000;

        boolean response = TransactionManager.handleTransaction(new Request().setAmount(100).setTimestamp(millis));
        assertTrue(response);
    }


    @Test
    public void testMoreThanSixtySeconds() throws Exception{
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        boolean response = TransactionManager.handleTransaction(new Request().setAmount(100).setTimestamp(cal.getTimeInMillis()));
        assertTrue(response);
    }
}