import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import java.sql.Date;


/**
 * Created by Per-Olav on 08.08.2015.
 */
public class WekaRandomForest implements Runnable{
private GUIMain gui;
    private Thread threadDBConnect;

    public WekaRandomForest(GUIMain gui)
    {
        this.gui = gui;
    }
    public void run()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DateFormat dateFormatPI = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PiConnect piConnect = new PiConnect();
        Date tid = new Date();
        Date tidsStempel = new Date();

        while (true) {
            GetProsessValues prosvalues = new GetProsessValues();
            WriteToFile skriv = new WriteToFile();
            UpdateTrainingSet updateTrainingSet = new UpdateTrainingSet();


            String getEst = gui.getEstimeringsIntervall();


            //Henter ut nye data for å appende treningsdatasett
            //Henter inn dagens dato 14.07.2015

            gui.txtInfoPane.append(dateFormat.format(tid) + "\n");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -0);


            try {
                //updateTrainingSet.readTagLoop(dateFormat.format(cal.getTime()).toString());
                System.out.println(dateFormat.format(tid).toString());
                updateTrainingSet.readTagLoop(dateFormat.format(tid).toString());
                //updateTrainingSet.readTagLoop("13.08.2015");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tid = new Date();
            gui.txtInfoPane.append("\n\nTraining dataset er hentet ut: " + dateFormat.format(tid) +
                    "\nFilen (TrainingSet.arff) er oppdatert med nye data\n\nLærer nytt beslutningstre!");

            //gui.setProgressBarRandomForest((loop+1)*10);

            //Henter ut datafilen for trening av data
            BufferedReader breader = null;
            try {
                breader = new BufferedReader(new FileReader("c:/TrainingSet.arff"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //lager en ny instanse
            Instances train = null;
            try {
                train = new Instances(breader);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Trener decision tree
            train.setClassIndex(train.numAttributes() - 1);

            try {
                breader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            RandomForest rfor = new RandomForest();
            try {
                rfor.buildClassifier(train);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Evaluation eval = null;
            try {
                eval = new Evaluation(train);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //eval.crossValidateModel(rip, train, 2, new Random(1));
            try {
                eval.evaluateModel(rfor, train);
            } catch (Exception e) {
                e.printStackTrace();
            }
            gui.txtInfoPane.append(eval.toSummaryString("\nResult\n\n", true));
            gui.txtInfoPane.append(eval.fMeasure(1) + " " + eval.precision(1) + " " + eval.recall(1));
            gui.txtInfoPane.append("\n\nEstimering av produkt starter!");

            int treningsIntervall = Integer.parseInt(gui.getTidsIntervallNyttTre());


                    //for (int i = 0; i < testDataSet.numInstances(); i++) {
                for (int y = 0; y < treningsIntervall; y++) {

                    //lager en ny fil for klar til bruk for estimering
                    skriv.createFile();

                    //Skriver inn nye data (header) til arff fil for bruk til estimering
                    try {
                        skriv.writeLine("@relation NTEditertWeka", 1);
                        skriv.writeLine("@attribute ' Rastoff' numeric", 1);
                        skriv.writeLine("@attribute ' Svovel' numeric", 1);
                        skriv.writeLine("@attribute ' Duggpunkt' numeric", 1);
                        skriv.writeLine("@attribute ' Luft svovelovn' numeric", 1);
                        skriv.writeLine("@attribute ' Luft Konverter' numeric", 1);
                        skriv.writeLine("@attribute ' Luft SO3 filter' numeric", 1);
                        skriv.writeLine("@attribute ' Molar' numeric", 1);
                        skriv.writeLine("@attribute ' Molar_stp' numeric", 1);
                        skriv.writeLine("@attribute ' NT' numeric", 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Henter ut alle klassene som da er de forskjellige produktene
                    int numClasses = train.numClasses();
                    for (int i = 0; i < numClasses; i++) {
                        String classValues = train.classAttribute().value(i);
                        //gui.txtInfoPane.append("Class value " + i + " is " + classValues + "\n");
                        try {
                            skriv.writeLine("'" + classValues + "'", 0);
                            if (i < numClasses - 1)
                                skriv.writeLine(",", 0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    try {
                        skriv.writeLine("}", 1);
                        skriv.writeLine("@data", 1);

                        //Henter prosessverdier fra OsiSoftPI
                        String values[][] = prosvalues.getValues();

                        //Skriver inn prosessverdiene i filen for estimering av produkt
                        skriv.writeToFile(values[1][0], values[1][1], values[1][2], values[1][3], values[1][4], values[1][5], values[1][6], values[1][7], "?");
                        gui.txtInfoPane.append("\n\nProsess verdier: " +values[1][0]+ " -,- " + values[1][1]+  " -,- " + values[1][2]+  " -,- " + values[1][3]+  " -,- " + values[1][4]+
                                " -,- " + values[1][5]+  " -,- " + values[1][6]+  " -,- " + values[1][7]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //Predict the value
                    BufferedReader testData = null;
                    try {
                        testData = new BufferedReader(new FileReader("c:/Estimate.arff"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Instances testDataSet = null;
                    try {
                        testDataSet = new Instances(testData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    testDataSet.setClassIndex(testDataSet.numAttributes() - 1);

                    //double actualClass = testDataSet.instance(i).classValue();
                    //String actual = testDataSet.classAttribute().value((int) actualClass);
                    Instance newInst = testDataSet.instance(0);
                    double predNB = 0;
                    try {
                        predNB = rfor.classifyInstance(newInst);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String predString = testDataSet.classAttribute().value((int) predNB);
                    tidsStempel = new Date();
                    gui.txtInfoPane.append("\nEstimert produkt utført: " + dateFormat.format(tidsStempel) +" -> " + predString);
                    try {
                        piConnect.writeToDB("S_Avd_Produkt_Estimert", predNB, dateFormatPI.format(tidsStempel));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        int estimeringsIntervall = Integer.parseInt(gui.getEstimeringsIntervall())*60000;

                        Thread.sleep(estimeringsIntervall);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                gui.txtInfoPane.append("\nDone\n\n");


            //}

        }
    }

}
