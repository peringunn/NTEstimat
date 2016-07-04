import java.io.*;

/**
 * Created by Per-Olav on 08.08.2015.
 */
public class WriteToFile {
    public void writeToFile(String tag1, String tag2, String tag3, String tag4, String tag5,
                             String tag6, String tag7, String tag8, String produkt) throws IOException
    {
        //teller++;

        FileWriter write = new FileWriter("C:/Users/Per-Olav/EstimateNt.arff", true);
        PrintWriter print_line = new PrintWriter(write);

        if (tag1 == null || tag1.equals(""))
            tag1 = "?";
        if (tag2 == null || tag2.equals(""))
            tag2 = "?";
        if (tag3 == null || tag3.equals(""))
            tag3 = "?";
        if (tag4 == null || tag4.equals(""))
            tag4 = "?";
        if (tag5 == null || tag5.equals(""))
            tag5 = "?";
        if (tag6 == null || tag6.equals(""))
            tag6 = "?";
        if (tag7 == null || tag7.equals(""))
            tag7 = "?";
        if (tag8 == null || tag8.equals(""))
            tag8 = "?";


        //Skriver produktnavn og prosessverdier ned til fil
        print_line.println( tag1 + ", " + tag2 + ", " + tag3 + ", " + tag4 + ", " + tag5 + ", " + tag6 + ", " + tag7
                + ", " + tag8 +  ", " + produkt);

        print_line.close();

        //Skriver ut oppdatering til konsol
        //System.out.print(teller + "* ");
        //if(teller == 200 || teller == 400 || teller == 600 || teller == 800 || teller == 1000 || teller == 1200 || teller == 1400)
        //    System.out.println("");


    }
    public void writeLine(String line, int brake) throws IOException
    {
        FileWriter write = new FileWriter("C:/Users/Per-Olav/EstimateNt.arff", true);
        PrintWriter print_line = new PrintWriter(write);
        if(brake==0)
        {
            print_line.print(line);
        }
        else
        {
            print_line.println(line);
        }

        print_line.close();

    }
    public void createFile()
    {
        try {
            PrintWriter writer = new PrintWriter("C:/Users/Per-Olav/EstimateNt.arff", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
