import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Per-Olav on 13.07.2015.
 */
public class GUIMain extends JFrame{
    private JButton cmdGetData;
    private JPanel rootPanel;
    public JTextArea txtInfoPane;
    private JButton cmdRunWekaEst;
    private JButton cmdExit;
    private JButton cmdGetProsessValue;
    private JLabel lblIcon;
    private JButton cmdReadWekaRules;
    private JProgressBar progressBarWekaRules;
    private JScrollPane scrollPanel;
    private JButton cmdCancelDBConnect;
    private JProgressBar progressBarDBConnect;
    private JButton cmdRunRandomForest;
    private JProgressBar progressBarRandomForest;
    private JButton cmdStopRandomForest;
    private JTextField txtTidsintervallEstimering;
    private JTextField txtTidsintervallNyttTre;
    private JButton cmdClearInfoPanel;
    private JButton browseTagButton;
    private Thread threadWekaRules;
    private Thread threadDBConnect;
    private Thread threadWekaEstimat;
    private Thread threadGetPrValues;
    private Thread threadRunRandomForest;
    private int progressWekaRules;



    public GUIMain() {
        super("Main GUI");
        setContentPane(rootPanel);
        setSize(1200, 800);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        ImageIcon imgIcon = new ImageIcon("images/neuralNet.jpg");
        lblIcon.setIcon(imgIcon);
        lblIcon.setSize(800,800);


        //Knapp for henting av datasett
        cmdGetData.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                //DBConnect dbConnect = new DBConnect();
                try {
                    //String test = dbConnect.readTag("");
                    onDBConnect();
                } catch (InterruptedException e1) {

                    e1.printStackTrace();
                }
            }


        });

        //Knapp for henting av prosessverdier
        cmdGetProsessValue.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                try{
                    onGetPrValues();
                } catch (InterruptedException e1)
                {
                    txtInfoPane.append("Exception: " + e1);
                }
            }
        });

        cmdRunRandomForest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    txtTidsintervallEstimering.disable();
                    txtTidsintervallNyttTre.disable();
                    onRunRandomForest();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        });

        //Kanselerer DBConnect operasjon
        cmdCancelDBConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threadDBConnect.stop();
                txtInfoPane.setText("Kanselert operasjon DBConnect");
            }
        });

        //Knapp for avslutting av program
        cmdExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Avslutter program
                System.exit(0);
            }
        });

        cmdStopRandomForest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threadRunRandomForest.stop();
                txtTidsintervallEstimering.enable();
                txtTidsintervallNyttTre.enable();
                txtInfoPane.append("\n\nProgram er kanselert");
            }
        });
        cmdClearInfoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtInfoPane.setText("");
            }
        });
        browseTagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //GUITagBrowse guiTagBrowse = new GUITagBrowse();

            }
        });
    }

    //***********************Starter threads*************************
    private void onReadWekaRules() throws InterruptedException
    {
        //threadWekaRules = new Thread(new ReadWekaRules(this));
        //threadWekaRules.start();
    }

    private void onDBConnect() throws InterruptedException
    {
        threadDBConnect = new Thread(new DBConnect(this));
        threadDBConnect.start();
    }
    private void onWekaEstimat() throws InterruptedException
    {
        //threadWekaEstimat = new Thread(new WekaEstimat(this));
        //threadWekaEstimat.start();
    }
    private void onGetPrValues() throws InterruptedException
    {
        //threadGetPrValues = new Thread(new GetPrValues(this));
        //threadGetPrValues.start();

    }
    private void onRunRandomForest() throws InterruptedException
    {
        threadRunRandomForest = new Thread(new WekaRandomForest(this));
        threadRunRandomForest.start();

    }

    //***********************Informasjonsflyt fra kjørende threads**********************
    public void setProgress(int p) {
        this.progressBarWekaRules.setValue(p);
    }

    public  void setInfoText(String s)
    {
        this.txtInfoPane.setText(s);
    }
    public void setProgressBarRandomForest(int p)
    {
        this.progressBarRandomForest.setValue(p);
    }
    public String getEstimeringsIntervall()
    {
        return this.txtTidsintervallEstimering.getText();
    }
    public String getTidsIntervallNyttTre()
    {
        return this.txtTidsintervallNyttTre.getText();
    }
}
