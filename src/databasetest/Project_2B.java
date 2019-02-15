package databasetest;

import java.sql.*;
import org.json.simple.*;

public class Project_2B {

    public static void main(String[] args) throws SQLException {
        
        Project_2B.getJSONData();

        
    }
    public static JSONArray getJSONData() throws SQLException{
        
        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;

        boolean hasresults;
        JSONArray jsonObjects = new JSONArray(); 
        JSONArray headers = new JSONArray();
        String query, key, value;
        String results = "";
        int resultCount, columnCount, updateCount = 0;

        try {

            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "$1Hotroddog$";
            System.out.println("Connecting to " + server + "...");

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection(server, username, password);

            if (conn.isValid(0)) {

                System.out.println("Connected Successfully!");
                
                 
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                System.out.println("Submitting Query ...");
                
                hasresults = pstSelect.execute();                
                
                System.out.println("Getting Results ...");
                
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        
                        /* Get Column Names; Print as Table Header */
                        
                        for (int i = 2; i <= columnCount; i++) {
                            key = metadata.getColumnLabel(i);
                            headers.add(key);
                        }
                        
                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            System.out.println();
                            JSONObject jsonObject = new JSONObject();
                            /* Loop Through ResultSet Columns; Print Values */

                            for (int i = 0; i <= columnCount - 2; i++) {
                                String colName = (String)headers.get(i);
                                jsonObject.put(headers.get(i), resultset.getString(colName));
                                
                               
                            }
                            
                             jsonObjects.add(jsonObject);

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                }
             
                
            }
            
            results = JSONValue.toJSONString(jsonObjects);
            
            conn.close();
        }
              catch (Exception e) {
           System.err.println(e.toString());
       }

       finally {

           if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }

           if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }

           if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }

       }
        
                
        
        return jsonObjects;
    }
        
  }
