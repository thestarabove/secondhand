package wtt.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wtt.utils.MySQLBinlogListener;

import java.io.IOException;

@Configuration
public class BinLogConfig {
    @Bean
    public BinaryLogClient binaryLogClient(MySQLBinlogListener listener) throws IOException {
        BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "root");
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        client.setEventDeserializer(eventDeserializer);
        new Thread(() -> {
            client.registerEventListener(listener);
            try {
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return client;
    }
}
