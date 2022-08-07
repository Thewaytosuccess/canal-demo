package com.ph.canal;

import java.net.InetSocketAddress;
import java.util.List;

import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class CanalService {

    @Value("${canal.port:11111}")
    private int canalPort = 11111;

    @Value("${canal.destination:example}")
    private String canalInstanceName = "example";

    @Value("${canal.username:}")
    private String canalUsername = "";

    @Value("${canal.password:}")
    private String canalPassword = "";

    @Value("${canal.batchSize:1000}")
    private Integer batchSize = 1000;

    @Value("${canal.tryCount:120}")
    private Integer tryCount = 120;

    public static void main(String[] args){
        new CanalService().handle();
    }

    @PostConstruct
    public void handle(){
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(AddressUtils.getHostIp(), canalPort),
                canalInstanceName, canalUsername, canalPassword);
        int emptyCount = 0;
        try {
            connector.connect();
            //订阅任意库，任意表的binlog
            connector.subscribe(".*\\..*");
            //指针重新指向未被消费的数据
            connector.rollback();
            while (emptyCount < tryCount) {

                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        log.error("",e);
                    }
                } else {
                    emptyCount = 0;
                    log.info("message[batchId={},size={}]", batchId, size);
                    printEntry(message.getEntries());
                }

                // 提交确认
                connector.ack(batchId);
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            log.info("empty too many times, exit");
        } finally {
            connector.disconnect();
        }
    }

    private void printEntry(List<Entry> entries) {
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of rowChange has an error , data:" + entry.toString(), e);
            }

            EventType eventType = rowChange.getEventType();
            CanalEntry.Header header = entry.getHeader();
            log.info(String.format("================binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    header.getLogfileName(), header.getLogfileOffset(),
                    header.getSchemaName(), header.getTableName(), eventType));

            for (RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    log.info("----------------- after insertion ---------");
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    log.info("----------------- before updating ---------");
                    printColumn(rowData.getBeforeColumnsList());
                    log.info("----------------- after updating ---------");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private void printColumn(List<Column> columns) {
        for (Column column : columns) {
            log.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
