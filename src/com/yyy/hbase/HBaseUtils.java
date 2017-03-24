package com.yyy.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;

public class HBaseUtils {

	private static final String HBASER_MASTER_IP = "128.6.5.42";
	private static final String HBASER_MASTER_PORT = "60000";
	private static final String QUORUM_IP = "128.6.5.42";
	private static final String CLIENTPORT = "2181";
	private static Configuration conf = null;
	private static Connection conn = null;

	
	public static void main(String[] args) throws IOException {
		getHConnection();
		List<Result> l = HBaseDAO.scanRowKeyByFilter("table", null);
		for (Result result : l) {
			System.out.println(new String(result.getRow()));
		}
	}
	/**
	 * 获取全局唯一的Configuration实例
	 * 
	 * @return
	 */
	public static synchronized Configuration getConfiguration() {
		if (conf == null) {
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", QUORUM_IP);
			conf.set("hbase.zookeeper.property.clientPort", CLIENTPORT);
			conf.set("hbase.master", HBASER_MASTER_IP + ":" + HBASER_MASTER_PORT);
		}
		return conf;
	}

	/**
	 * 获取全局唯一的HConnection实例
	 * 
	 * @return
	 * @throws ZooKeeperConnectionException
	 */
	public static synchronized Connection getHConnection() {
		if (conf == null) {
			conf = getConfiguration();
		}
		if (conn == null) {
			try {
				conn = ConnectionFactory.createConnection(conf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Hbase 连接成功!");
		return conn;
	}
}