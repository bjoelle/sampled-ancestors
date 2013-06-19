package beast.app.tools;

import beast.evolution.tree.Tree;
import beast.util.NexusParser;

import java.io.*;

/**
 * @author Alexandra Gavryushkina
 */

public class SampledAncestorTreeAnalyser {

    public static String convertTreetoRankedRepresentation (String strIn) {

        Tree tree = new Tree();

        StringBuilder buf = new StringBuilder();
        boolean skipping = false;

        for (int i=0; i < strIn.length(); i++) {
            if (!skipping) {
                if (strIn.charAt(i) != ':') {
                    buf.append(strIn.charAt(i));
                } else {
                    if (strIn.charAt(i+1) != '(') {
                        skipping = true;
                        i++;
                    } else {
                        buf.append(strIn.charAt(i+1));
                        i++;
                    }
                }
            } else {
                if (strIn.charAt(i) == ',' || strIn.charAt(i) == ')') {
                    buf.append(strIn.charAt(i));
                    skipping = false;
                }
            }
        }

        return buf.toString();

    }




    public static void main(String[] args) throws IOException, Exception {

        int percentCredSet = 0;

        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        String a= new String();
        try {
            System.out.println("Type the size of credible set in percents: ");
            a = buf.readLine();
            percentCredSet = Integer.parseInt(a);
        } catch (IOException err) {
            System.out.println("Error");
        }

        if (0 >= percentCredSet || percentCredSet > 100) {
            System.out.println("The percent for credible set is out of the interval (0,100]" + "95% credible set will be shown");
            percentCredSet = 95;
        }

        String message = "Choose file .trees";
        java.awt.Frame frame = new java.awt.Frame();
        java.awt.FileDialog chooser = new java.awt.FileDialog(frame, message,
                java.awt.FileDialog.LOAD);
//        chooser.show();
        chooser.setVisible(true);
        if (chooser.getFile() == null) {
            System.out.println("The file was not chosen.");
            System.exit(0);
        }
        java.io.File file = new java.io.File(chooser.getDirectory(), chooser.getFile());
        chooser.dispose();
        frame.dispose();

        //String home = System.getProperty("user.home");
        FileReader reader = null;
        //HashSet<Integer> taxaLabelSet = new HashSet<Integer>(Arrays.asList(new Integer[] {1, 2, 3}));

        try {



            reader = new FileReader(file);
            //NexusImporter importer = new NexusImporter(reader);

            NexusParser parser = new NexusParser();
            parser.parseFile(file);



            SampledAncestorTreeTrace trace = new SampledAncestorTreeTrace(parser);


            //SampledAncestorTreeTrace trace = new SampledAncestorTreeTrace(importer.getTrees(), importer.getLabelCount());

            SampledAncestorTreeAnalysis analysis = new SampledAncestorTreeAnalysis(trace, percentCredSet);

            analysis.perform();
        }
        catch (IOException e) {
            //
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}