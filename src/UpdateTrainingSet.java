import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class UpdateTrainingSet {

    //Deklarerer db connect
    public static final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    public static final String JDBC_URL = "jdbc:jtds:sqlserver://172.18.150.30:1433/SANLEGG";
    public static final String JDBC_USER = "sa";
    public static final String JDBC_PASSWORD = "fox";

    private Connection connection = null;
    private int teller = 0;

    //Åpner kopling mot database
    public boolean openDB() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

        } catch (Exception e) {
            //gui.setLblStatusDBConnect("Kunne ikke kople seg til databasen -> Exeption: " + e);
            //e.printStackTrace();
        }
        return !connection.isClosed();
    }

    //Lukker databasetilkopling
    public void closeDB() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }


    //Script for skriving til fil
    private void writeToFile(String tag1, String tag2, String tag3, String tag4, String tag5,
                             String tag6, String tag7, String tag8,  String produkt) throws IOException
    {
        teller++;
        FileWriter write = new FileWriter("c:/TrainingSet.arff", true);
        PrintWriter print_line = new PrintWriter(write);

        //Skriver produktnavn og prosessverdier ned til fil
        if(tag1.equals("null") || tag2.equals("null") || tag3.equals("null") || tag4.equals("null") || tag5.equals("null") || tag6.equals("null") || tag7.equals("null") ||
                tag8.equals("null") )
        {}
        else {
            print_line.println(tag1 + "," + tag2 + "," + tag3 + "," + tag4 + "," + tag5 + "," + tag6 + "," + tag7
                    + "," + tag8 + "," +  "'" + produkt + "'");

            print_line.close();
        }


    }

    public void readTagLoop(String datoSet) throws SQLException {

        //Åpner kopling mot PI database
        if (connection == null)
            try {
                openDB();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        PiConnect piCon = new PiConnect();
        String returnVerdi = "";
        String produkt = "";
        String dato_s = "";
        String dato = "";

        //Prosessverdier som skal hentes fra historian
        String Rastoff = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.REAKTOR_REG_4018FT010_MEAS";
        String Svovel = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Svovelsystem.AW5101.SVOVEL_REG_14FT519_MEAS";
        String Duggpunkt = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.AW5101.LUFT_AIN_13MT378_MEAS";
        String LuftSvovel = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI001_MEAS";
        String LuftKonverter = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI002_MEAS";
        String LuftSO3 = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI003_MEAS";
        String Molar = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning";
        String MolarStp = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning_SPT";

        String valueRastoff = "";
        String valueSvovel = "";
        String valueDuggpunkt = "";
        String valueLuftSvovel = "";
        String valueLuftKonverter = "";
        String valueLuftSO3 = "";
        String valueMolar = "";
        String valueMolarStp = "";


        //Sql spørring som henter ut produkt fra Ungernet med timestampet
        String SQL = "SELECT * FROM Ntregister where dato > '" + datoSet + "'";
        String SQLCount = "SELECT COUNT(*) FROM Ntregister where dato > '" + datoSet + "'";

        Statement stateProdNt = null;
        ResultSet resultProdNt = null;

        try{
            stateProdNt = connection.createStatement();
            resultProdNt = stateProdNt.executeQuery(SQL);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }



        //System.out.println(SQL);
        Statement statement = null;
        //******Statement statCount = null;
        try {
            statement = connection.createStatement();
            //*****statCount = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        //****ResultSet resultSetCount = null;
        try {
            resultSet = statement.executeQuery(SQL);
            //***resultSetCount = statCount.executeQuery(SQLCount)

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Henter ut data fra Ufacid spørring Ungernet
        try {
            while (resultSet.next()) {
                //System.out.println(resultSet.getString("value"));
                produkt = resultSet.getString("Produkt");
                dato_s = resultSet.getString("Dato");



                try {
                    java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(dato_s);
                    dato = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Henter ut tag fra pi respektivt med tidsstempel
                valueRastoff = piCon.readTagHist(Rastoff, dato_s);
                valueSvovel = piCon.readTagHist(Svovel, dato_s);
                valueDuggpunkt = piCon.readTagHist(Duggpunkt, dato_s);
                valueLuftSvovel = piCon.readTagHist(LuftSvovel, dato_s);
                valueLuftKonverter = piCon.readTagHist(LuftKonverter, dato_s);
                valueLuftSO3 = piCon.readTagHist(LuftSO3, dato_s);
                valueMolar = piCon.readTagHist(Molar, dato_s);
                valueMolarStp = piCon.readTagHist(MolarStp, dato_s);

                //Skriver verdiene til fil
                try {
                    writeToFile(
                            produkt,
                            valueRastoff,
                            valueSvovel,
                            valueDuggpunkt,
                            valueLuftSvovel,
                            valueLuftKonverter,
                            valueLuftSO3,
                            valueMolar,
                            valueMolarStp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement2 = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Lukker database connect
        try {
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Totalt: " + teller + "linjer");
        //return returnVerdi;

    }
    private void updateHeaderTrainFile()
    {

    }

}
