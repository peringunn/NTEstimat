import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * Created by Tomas Alnæs on 26.08.2014.
 */
public class PiConnect {

    public static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String JDBC_URL = "jdbc:sqlserver://172.16.1.18:1433";
    //public static final String JDBC_URL = "jdbc:sqlserver://10.29.64.90:1433";
    public static final String JDBC_USER = "tomas";
    public static final String JDBC_PASSWORD = "temp123";

    private Connection connection = null;

    public boolean openDB() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            DriverManager.setLoginTimeout(20);
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !connection.isClosed();
    }

    public void closeDB() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

    public String readTag(String tag) throws SQLException {
        if (connection == null)
            openDB();
        String returnVerdi = "";
        //String tag = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.REAKTOR_REG_4018FT010_MEAS";
        String SQL = "SELECT * FROM OPENQUERY(\"LINKEDPI\", 'SELECT * FROM [piarchive].[pisnapshot] where value is not null AND tag = " +
                "''" + tag + "''" + " order by time desc')";

        //System.out.println(SQL);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);

        while (resultSet.next()) {
            //System.out.println(resultSet.getString("value"));
            returnVerdi = resultSet.getString("value");
       }
        //String returnVerdi = resultSet.getString("value");
        closeDB();
        return returnVerdi;



    }

    public String readTagHist(String tag, String tid) throws SQLException {
        if (connection == null)
            openDB();
        String returnVerdi = "";
        //String tag = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.REAKTOR_REG_4018FT010_MEAS";

        //String SQL = "SELECT * FROM OPENQUERY(\"LINKEDPI\", 'SELECT time, value FROM piarchive..picomp  where tag = " +
        //        "''" + tag + "''" + " and time >= ''01/01/2014 14:00''')";


        String SQL = "SELECT * FROM OPENQUERY(\"LINKEDPI\", 'SELECT time, value FROM piarchive..piinterp  where tag = " +
                "''" + tag + "''" + " and time = ''" + tid + "''')";

        //String SQL = "SELECT * FROM OPENQUERY(\"LINKEDPI\", 'SELECT * FROM [piarchive].[pisnapshot] where value is not null AND tag = " +
        //        "''" + tag + "''" + " order by time desc')";

        //System.out.println(SQL);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL);

        while (resultSet.next()) {
            //System.out.println(resultSet.getString("value"));
            returnVerdi = resultSet.getString("value");
        }
        //String returnVerdi = resultSet.getString("value");
        closeDB();
        return returnVerdi;



    }

    public void writeToDB(String tag, Double value, String timeStamp) throws SQLException {
        if (connection == null)
            openDB();

        //String newTimeStamp = reformatDateTime(timeStamp);
        String SQL = "INSERT INTO [LINKEDPI].[piarchive]..[picomp] (tag, time, value, flags) " +
                "VALUES('" + tag + "','" + timeStamp + "'," + "'" +  value +"'" + ", 'Q')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL);
        //System.out.println("Wrote: " + SQL + "\n");
    }

    public String reformatDateTime(String date) {
        // Reformat datetime
        String reformatDateTime;
        java.util.Date datetime = null;
        try {
            datetime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reformatDateTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(datetime);

        return reformatDateTime;
    }

}
