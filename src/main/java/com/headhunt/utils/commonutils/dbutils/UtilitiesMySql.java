package com.headhunt.utils.commonutils.dbutils;

import java.sql.*;
import java.util.*;

/**
 *
 * @author sagraw001c
 */
public class UtilitiesMySql {

    public enum ENV {
        BETA, PROD
    }

    private String URL;
    private String USER;
    private String PASS;

    private Connection conn = null;
    public Statement stmt = null;
    public ResultSet rs = null;

    public UtilitiesMySql(String url, String uname, String passw, ENV env) {
        if (env.equals(ENV.BETA)) {
            URL = url;
            USER = uname;
            PASS = passw;

        } else if (env.equals(ENV.PROD)) {
            URL = url;
            USER = uname;
            PASS = passw;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error in MySql connection: " + env.name() + " --> " + ex);
            System.exit(1);
        }
        System.out.println("Connected to database... " + env.name());
    }

    public UtilitiesMySql(Properties prop, ENV env) {
        if (env.equals(ENV.BETA)) {
            URL = prop.getProperty("beta.mysql").trim();
            USER = prop.getProperty("beta.user").trim();
            PASS = prop.getProperty("beta.pass").trim();

        } else if (env.equals(ENV.PROD)) {
            URL = prop.getProperty("prod.mysql").trim();
            USER = prop.getProperty("prod.user").trim();
            PASS = prop.getProperty("prod.pass").trim();
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error in MySql connection: " + env.name() + " --> " + ex);
            System.exit(1);
        }
        System.out.println("Connected to database... " + env.name());

    }

    public ResultSet queryDB(String sql) {
        try {
            rs = stmt.executeQuery(sql);
        } catch (Exception ex) {
            System.out.println("Error in executing query: " + ex);
            System.exit(1);
        }
        return rs;
    }

    public int insertDB(String sql) {
        try {
            return stmt.executeUpdate(sql);
        } catch (Exception ex) {
            System.out.println("Error in executing query: " + ex);
            System.exit(1);
        }
        return Integer.MIN_VALUE;
    }

    public int parseCountResultSet(ResultSet rs) {
        try {
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println("Error in parsing count result set: " + ex);
            System.exit(1);
        }
        return -1;
    }

    public List<String> parseNaResultSet(ResultSet rs) {
        List<String> result = new ArrayList<String>();
        try {
            while (rs.next()) {
                String text = null;
                int count = -1;
                if (rs.getString("text") != null)
                    text = rs.getString("text").trim();
                if (rs.getInt("s") > 0)
                    count = rs.getInt("s");
                result.add(text);
            }
        } catch (Exception ex) {
            System.out.println("Error in parsing parseNaResultSet(): " + ex);
        }
        return result;
    }

    public Map<String, String> parseNLPResponse(ResultSet rs) {
        Map<String, String> map = new HashMap<>();
        try {
            while (rs.next()) {
                String action = null, text = null;
                if (rs.getString("action") != null) {
                    action = rs.getString("action").trim();
                }
                if (rs.getString("text") != null) {
                    text = rs.getString("text").trim();
                }

                if (action != null && text != null) {
                    map.put(text, action);
                }
            }
        }
        catch (Exception ex) {}

        return map;
    }

    public static List<Map<String, Object>> parseFullTable(ResultSet rs) {
        List<Map<String, Object>> sessions = new ArrayList<Map<String, Object>>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                Map<String, Object> columns = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
//                    columns.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                sessions.add(columns);
            }
        } catch(Exception ex) {
            System.out.println("Exception in parseFullTable() --> " + ex);
        }
        return sessions;
    }

    public void insertListMapToTable(List<Map<String, Object>> datamaplist, String tableName) {
        for (Map<String, Object> dataMap : datamaplist) {
            try {
                StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
                StringBuilder placeholders = new StringBuilder();

                for (Iterator<String> iter = dataMap.keySet().iterator(); iter.hasNext(); ) {
                    sql.append(iter.next());
                    placeholders.append("?");

                    if (iter.hasNext()) {
                        sql.append(",");
                        placeholders.append(",");
                    }
                }

                sql.append(") VALUES (").append(placeholders).append(")");
                PreparedStatement pStmt = conn.prepareStatement(sql.toString());
//        System.out.println(sql);
                int i = 1;
                for (Object value : dataMap.values()) {
                    pStmt.setObject(i, value);
                    i++;
                }

                int affectedRows = pStmt.executeUpdate();
            } catch (Exception ex) {
                System.out.println("Exception in insertListMapToTable() --> " + ex);
            }
        }

    }

    public void closeConn() {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (Exception ex) {
            System.out.println("Error in closing connections.. " + ex);
            System.exit(1);
        }
    }

    public List<String> parseNLPResponseForAnnotation(ResultSet rs, int count) {
        List<String> result = new ArrayList<>();              // list of sessions to be returned

        Map<String, Integer> actionCount = new HashMap<>();    // action --> count
        Map<String, String> sessionToAction = new HashMap<>();    // session_id --> action
        Map<String, String> sessionToActionCapped = new HashMap<>();   // sessionId --> action

        int totalRow = 0;
        try {
            while (rs.next()) {
                totalRow++;
                String session_id = null, action = null;

                if (rs.getString("session_id") != null) {
                    session_id = rs.getString("session_id").trim();
                }
                if (rs.getString("action") != null) {
                    action = rs.getString("action").trim();
                }

                if (actionCount.containsKey(action)) {
                    actionCount.put(action, actionCount.get(action)+1);
                } else {
                    actionCount.put(action, 1);
                }
                sessionToAction.put(session_id, action);
            }

            for (String key : actionCount.keySet()) {
                int thisactioncount = actionCount.get(key);
                double newCount = ((double)thisactioncount*(double)count) / (double)totalRow;
                actionCount.put(key, (int)newCount + 1);
            }

            for (String session_id : sessionToAction.keySet()) {
                String action = sessionToAction.get(session_id);
                if (actionCount.get(action) != null && actionCount.get(action) > 0) {
                    // take this session Id
                    sessionToActionCapped.put(session_id, action);
                    actionCount.put(action, actionCount.get(action)-1);
                }
            }

            for (String s_id : sessionToActionCapped.keySet()) {
                count--;
                result.add(s_id);
                if (count < 0) {
                    break;
                }
            }

        }
        catch (Exception ex) {}

        return result;
    }

    public String getQueriesFromList(List<String> list) {
        System.out.println("Original size of query: " + list.size());
        Iterator<String> iter = list.iterator();
        while(iter.hasNext()) {
            String q = iter.next();
            if (q.contains("'") || q.contains("\"")) {
                iter.remove();
            }
        }
        System.out.println("New size of query: " + list.size());

        StringBuilder builder = new StringBuilder();
        builder.append("(");
        if (!list.isEmpty()) {
            for (String query : list) {
                builder.append("'").append(query).append("'");
                break;
            }
            for (String query : list) {
                builder.append(", ").append("'").append(query).append("'");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public List<String> parseSessionId(ResultSet rs) {
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                if (rs.getString("session_id") != null) {
                    list.add(rs.getString("session_id").trim());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in parseSessionId: " + ex);
        }
        return list;
    }

    public List<String> parseSessionIdSessionTable(ResultSet rs) {
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                if (rs.getString("id") != null) {
                    list.add(rs.getString("id").trim());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in parseSessionIdSessionTable: " + ex);
        }
        return list;
    }

    public static String getListIntoString(List<String> list, String field) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(field).append(" = ").append(list.get(0));
        if (list.size() > 1) {
            for (int i = 1; i< list.size(); ++i) {
                builder.append(" or ").append(field).append(" = ").append(list.get(i));
            }
        }
        builder.append(")");
        return builder.toString();
    }

}
