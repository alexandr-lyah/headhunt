package com.headhunt.utils.commonutils.dbutils;

import com.headhunt.utils.commonutils.fileutils.UtilitiesFile;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by sagraw200 on 6/3/2015.
 */
public class UtilitiesHiveConnect {

    private static final Logger log = Logger.getLogger(UtilitiesHiveConnect.class);
    private static final String driverName = "org.apache.hive.jdbc.HiveDriver";
    private String uname;
    private String passw;

    private String propFile = "ingest.properties";
    private Properties prop = new Properties();

    private String hiveurl;

    public Connection conn;
    public Statement stmt;

    public UtilitiesHiveConnect(String _uname, String _passw, String _hiveurl) {
        this.uname = _uname;
        this.passw = _passw;
        this.hiveurl = _hiveurl;
        connect();
    }

    public UtilitiesHiveConnect() {
        UtilitiesFile uf = new UtilitiesFile();
        prop = uf.loadPropFile(propFile);

        uname = prop.getProperty("user.name").trim();
        passw = prop.getProperty("password").trim();
        hiveurl = prop.getProperty("hive.url").trim();

        connect();
    }

    private void connect() {
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(hiveurl, uname, passw);
            System.out.println("Conn done..");
            stmt = conn.createStatement();
        } catch (Exception ex) {
            log.error("Exception in connect(): " + ex);
            System.exit(1);
        }
    }
}
