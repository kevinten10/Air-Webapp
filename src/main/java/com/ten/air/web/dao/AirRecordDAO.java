package com.ten.air.web.dao;

import com.ten.air.web.entity.AirRecord;
import com.ten.air.web.util.JdbcConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AirRecord数据库访问
 */
public class AirRecordDAO {
    private static final AirRecordDAO DAO = new AirRecordDAO();

    public static AirRecordDAO getDao() {
        return DAO;
    }

    /**
     * FIXME 展示最近的20条信息
     */
    private static final String SELECT_ALL = "SELECT * FROM air_record ORDER BY record_time desc LIMIT 20";

    /**
     * 获取数据
     */
    public List<AirRecord> getAllRecord() throws SQLException {
        Connection c = JdbcConnection.getConnection();
        PreparedStatement p = c.prepareStatement(SELECT_ALL);
        ResultSet result = p.executeQuery();

        List<AirRecord> records = new ArrayList<>();
        while (result.next()) {
            AirRecord record = new AirRecord();

            // 数据库字段映射
            record.setId(Integer.valueOf(result.getString("id")));
            record.setSource(result.getString("source"));
            record.setImei(result.getString("imei"));
            record.setTemperature(result.getString("temperature"));
            record.setHumidity(result.getString("humidity"));
            record.setPm25(result.getString("pm25"));
            record.setUndefinedData(result.getString("undefined_data"));
            record.setRecordTime(result.getString("record_time"));
            record.setUpdateTime(result.getString("update_time"));
            record.setIsDeleted(Integer.valueOf(result.getString("is_deleted")));

            records.add(record);
        }
        return records;
    }

    /**
     * FIXME 展示最新的信息
     */
    private static final String SELECT_LAST = "(SELECT\n" +
            "        imei, temperature, humidity, pm25\n" +
            "        FROM air_record\n" +
            "        WHERE temperature != '0.0' AND humidity != '0.0'\n" +
            "        ORDER BY record_time DESC\n" +
            "        LIMIT 1)\n" +
            "        UNION\n" +
            "        (SELECT\n" +
            "        imei, temperature, humidity, pm25\n" +
            "        FROM air_record\n" +
            "        WHERE pm25 != '0'\n" +
            "        ORDER BY record_time DESC\n" +
            "        LIMIT 1)";

    /**
     * 获取最新数据
     */
    public List<AirRecord> selectLast() throws SQLException {
        Connection c = JdbcConnection.getConnection();
        PreparedStatement p = c.prepareStatement(SELECT_LAST);
        ResultSet result = p.executeQuery();

        List<AirRecord> records = new ArrayList<>();
        while (result.next()) {
            AirRecord record = new AirRecord();

            // 数据库字段映射
            record.setImei(result.getString("imei"));
            record.setTemperature(result.getString("temperature"));
            record.setHumidity(result.getString("humidity"));
            record.setPm25(result.getString("pm25"));

            records.add(record);
        }
        return records;
    }
}
