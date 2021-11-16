import com.tj712.wc.util.ClickHouseUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/9
 * @Time: 15:48
 * @author: ThinkPad
 */

public class ClickHouseUtilTest {
    @Test
    public void TestClickHouseUtilTest() throws Exception{
        BasicDataSource dataSource = new BasicDataSource();
        ClickHouseUtil.getConn(dataSource);
    }

    @Test
    public void Test() throws Exception{
        Tuple2<Integer, String> person = Tuple2.of(18, "cxk");
        Integer age = person.f0;
        String name = person.f1;
        System.out.println(age+name);
    }
}
