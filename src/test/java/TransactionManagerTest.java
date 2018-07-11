import com.n26.api.Request;
import com.n26.api.Response;
import com.n26.api.TransactionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Calendar;

import static junit.framework.TestCase.*;

@DirtiesContext
public class TransactionManagerTest {

    long timeAtExecution;
    long olderTime;

    @Before
    public void setup(){
        timeAtExecution = System.currentTimeMillis();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        olderTime = cal.getTimeInMillis();

        TransactionManager.reset();
    }

//    @Test
//    public void testEmptyRequest() throws Exception{
//        boolean response = TransactionManager.handleTransaction(new Request());
//        assertFalse(response);
//    }

//    @Test
//    public void testMissingTimestamp() throws Exception{
//        boolean response = TransactionManager.handleTransaction(new Request().setAmount(100));
//        assertFalse(response);
//    }

//    @Test
//    public void testMissingAmount() throws Exception{
//        boolean response = TransactionManager.handleTransaction(new Request().setTimestamp(timeAtExecution));
//        assertFalse(response);
//    }

    @Test
    public void testLessThanSixtySeconds() throws Exception{
        boolean response = TransactionManager.handleTransaction(new Request().setAmount(100).setTimestamp(timeAtExecution));
        assertTrue(response);
    }


    @Test
    public void testMoreThanSixtySeconds() throws Exception{

        boolean response = TransactionManager.handleTransaction(new Request().setAmount(100).setTimestamp(olderTime));
        assertFalse(response);
    }

    @Test
    public void testCalculateSum() throws Exception{
        Request request = new Request().setTimestamp(timeAtExecution).setAmount(100);
        assertTrue(TransactionManager.handleTransaction(request));

        Response response = TransactionManager.getStatistics();
        assertEquals(100.0, response.getSum());

        request = new Request().setTimestamp(System.currentTimeMillis()).setAmount(200);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(300.0, response.getSum());

        request = new Request().setTimestamp(System.currentTimeMillis()).setAmount(300);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(600.0, response.getSum());
    }

    @Test
    public void testCalculateMax() throws Exception{
        Request request = new Request().setTimestamp(timeAtExecution).setAmount(100);
        assertTrue(TransactionManager.handleTransaction(request));

        Response response = TransactionManager.getStatistics();
        assertEquals(100.0, response.getMax());

        request = new Request().setTimestamp(timeAtExecution).setAmount(200);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(200.0, response.getMax());

        request = new Request().setTimestamp(timeAtExecution).setAmount(300);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(300.0, response.getMax());
    }


    @Test
    public void testCalculateMin() throws Exception{
        Request request = new Request().setTimestamp(timeAtExecution).setAmount(100);
        assertTrue(TransactionManager.handleTransaction(request));

        Response response = TransactionManager.getStatistics();
        assertEquals(100.0, response.getMin());

        request = new Request().setTimestamp(timeAtExecution).setAmount(200);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(100.0, response.getMin());

        request = new Request().setTimestamp(timeAtExecution).setAmount(300);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(100.0, response.getMin());
    }

    @Test
    public void testCalculateAvg() throws Exception{
        Request request = new Request().setTimestamp(timeAtExecution).setAmount(100);
        assertTrue(TransactionManager.handleTransaction(request));

        Response response = TransactionManager.getStatistics();
        assertEquals(100.0, response.getAvg());

        request = new Request().setTimestamp(timeAtExecution).setAmount(200);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(150.0, response.getAvg());

        request = new Request().setTimestamp(timeAtExecution).setAmount(300);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(200.0, response.getAvg());
    }

    @Test
    public void testCalculateCount() throws Exception{
        Request request = new Request().setTimestamp(timeAtExecution).setAmount(100);
        assertTrue(TransactionManager.handleTransaction(request));

        Response response = TransactionManager.getStatistics();
        assertEquals(1, response.getCount());

        request = new Request().setTimestamp(timeAtExecution).setAmount(200);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(2, response.getCount());

        request = new Request().setTimestamp(timeAtExecution).setAmount(300);
        assertTrue(TransactionManager.handleTransaction(request));

        response = TransactionManager.getStatistics();
        assertEquals(3, response.getCount());
    }

    @Test
    public void testGetStatisticsUnderSixtySeconds() throws Exception{
        Request request = new Request().setTimestamp(timeAtExecution).setAmount(100);
        assertTrue(TransactionManager.handleTransaction(request));

        Response response = TransactionManager.getStatistics();

        assertEquals(100.0, response.getSum());
        assertEquals(100.0, response.getMax());
        assertEquals(100.0, response.getMin());
        assertEquals(100.0, response.getAvg());
        assertEquals(1, response.getCount());

        request = new Request().setTimestamp(olderTime).setAmount(200);
        assertFalse(TransactionManager.handleTransaction(request));
        response = TransactionManager.getStatistics();

        assertEquals(100.0, response.getSum());
        assertEquals(100.0, response.getMax());
        assertEquals(100.0, response.getMin());
        assertEquals(100.0, response.getAvg());
        assertEquals(1, response.getCount());


        request = new Request().setTimestamp(timeAtExecution).setAmount(300);
        assertTrue(TransactionManager.handleTransaction(request));
        response = TransactionManager.getStatistics();

        assertEquals(400.0, response.getSum());
        assertEquals(300.0, response.getMax());
        assertEquals(100.0, response.getMin());
        assertEquals(200.0, response.getAvg());
        assertEquals(2, response.getCount());
    }
}