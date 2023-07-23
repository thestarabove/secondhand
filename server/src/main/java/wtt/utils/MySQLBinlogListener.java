package wtt.utils;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
@Component
public class MySQLBinlogListener implements BinaryLogClient.EventListener {
    private final RedisTemplate<String,Object> redisTemplate;

    public MySQLBinlogListener(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onEvent(Event event) {
        EventData eventData=event.getData();
        //System.out.println(event.getData()+"---------------------------------event");
      if ( eventData instanceof WriteRowsEventData){
          WriteRowsEventData writeRowsEventData = (WriteRowsEventData) eventData;
          System.out.println(writeRowsEventData+"-------------------------------------addData");
          for (Serializable[] entry : writeRowsEventData.getRows()){
              System.out.println(entry[0]+"-------------------------addEntry");
          }
      }

        if ( eventData instanceof UpdateRowsEventData){
            UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) eventData;
            System.out.println(updateRowsEventData+"-------------------------------------updateData");
            for (Map.Entry<Serializable[], Serializable[]> entry : updateRowsEventData.getRows()){
                System.out.println(entry+"-------------------------updateEntry");
            }
        }

        if ( eventData instanceof DeleteRowsEventData){
            DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData) eventData;
            System.out.println(deleteRowsEventData+"-------------------------------------deleteData");
            for (Serializable[] entry : deleteRowsEventData.getRows()){
                System.out.println(entry+"-------------------------deleteEntry");
            }
        }
    }
    private String extractKeyFromEventData(UpdateRowsEventData eventData) {
        // 从事件数据中提取键值
        // ...
        return null;
    }

    private String extractValueFromEventData(Serializable[] rowData) {
        // 从事件数据中提取值
        // ...
        return null;
    }
}
