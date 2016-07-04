import java.sql.SQLException;


/**
 * Created by Per-Olav on 14.07.2015.
 */
public class GetProsessValues {

    public String[][] getValues() {

        //Deklarere kopling mot PI database
        PiConnect piCon = new PiConnect();
        String[][] sendValue = new String[8][8];

        //setProgress.progressBar1.setValue(50);

        //Henter prosessverdiene fra PI database


            //Definerer navnet til taggene i matrise
            sendValue[0][0] = "Rastoff";
            sendValue[0][1] = "Svovel";
            sendValue[0][2] = "Duggpunkt";
            sendValue[0][3] = "LuftSvovel";
            sendValue[0][4] = "LuftKonverter";
            sendValue[0][5] = "LuftSO3";
            sendValue[0][6] = "Molar";
            sendValue[0][7] = "MolarStp";


        String Duggpunkt = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.AW5101.LUFT_AIN_13MT378_MEAS";
        String LuftSvovel = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI001_MEAS";
        String LuftKonverter = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI002_MEAS";
        String LuftSO3 = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI003_MEAS";
        String Molar = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning";
        String MolarStp = "HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning_SPT";

            //Henter ut verdiene til taggene fra PI historian
            try{
                if(piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.REAKTOR_REG_4018FT010_MEAS") == null)
                    sendValue[1][0] = "0";
                else
                    sendValue[1][0] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.REAKTOR_REG_4018FT010_MEAS");
                if(piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Svovelsystem.AW5101.SVOVEL_REG_14FT519_MEAS")== null)
                    sendValue[1][1] = "0";
                else
                    sendValue[1][1] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Svovelsystem.AW5101.SVOVEL_REG_14FT519_MEAS");
                if(piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.AW5101.LUFT_AIN_13MT378_MEAS") == null)
                    sendValue[1][2] = "0";
                else
                    sendValue[1][2] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.AW5101.LUFT_AIN_13MT378_MEAS");
                if(piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI001_MEAS") == null)
                    sendValue[1][3] = "0";
                else
                    sendValue[1][3] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI001_MEAS");
                if(piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI002_MEAS") == null)
                    sendValue[1][4] = "0";
                else
                    sendValue[1][4] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI002_MEAS");
                if(piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI003_MEAS") == null)
                    sendValue[1][5] = "0";
                else
                    sendValue[1][5] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Prosessluft.LUFT2_AIN_4010FI003_MEAS");
                if (piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning") == null)
                    sendValue[1][6] = "0";
                else
                    sendValue[1][6] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning");
                if (piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning_SPT") == null)
                    sendValue[1][7] = "0";
                else
                    sendValue[1][7] = piCon.readTag("HMI_Unger.HMI_Server:HMI_Server:S_AVD.Fodesystem_Raastoff.MolarBeregning_SPT");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sendValue;

    }


}
