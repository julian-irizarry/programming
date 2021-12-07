import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Put a short phrase describing the program here.
 *
 * @author Julian Irizarry
 *
 */
public final class Glossary {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
    }

    /**
     * Orders in alphabetical order.
     *
     * @param list
     *            map of words and definitions
     * @return ordered array for keys
     */
    private static String[] alphaOrder(Map<String, String> list) {
        /*
         * Put your code for myMethod here
         */
        String[] alpha = new String[list.size()];

        int i = 0;
        for (Map.Pair<String, String> p : list) {
            alpha[i] = p.key();
            i++;
        }

        //bubble sort for alphabetizing
        for (int j = 0; j < alpha.length; j++) {
            for (int k = 0; k < alpha.length; k++) {
                if (alpha[j].compareToIgnoreCase(alpha[k]) < 0) {
                    String temp = alpha[k];
                    alpha[k] = alpha[j];
                    alpha[j] = temp;

                }
            }
        }
        return alpha;

    }

    /**
     * Parse inFile.
     *
     * @param input
     *            input file
     * @return terms puts words and definition into a map
     */
    private static Map<String, String> linesFromInput(SimpleReader input) {
        /*
         * Put your code for myMethod here
         */
        Map<String, String> terms = new Map1L<>();

        while (!input.atEOS()) {
            /*
             * Store in set
             */
            String temp1 = input.nextLine();
            String temp2 = input.nextLine();
            String temp3 = input.nextLine();
            while (!temp3.isEmpty()) {
                temp2 = temp2 + " " + temp3;
                temp3 = input.nextLine();
            }

            terms.add(temp1, temp2);
        }

        return terms;
    }

    /**
     * @param term
     *            String word
     * @param value
     *            String definition
     * @param folderName
     *            String for the folder name
     * @param array
     *            ordered terms
     */
    private static void subPage(String term, String value, String folderName,
            String[] array) {
        /*
         * Creates file for term
         */
        String termHTML = folderName + "/" + term + ".html";
        SimpleWriter out = new SimpleWriter1L(termHTML);

        out.println("<html> <head> <title>" + term + "</title>");
        out.println("</head> <body>");
        out.println("<h1 style=\"color:red;\"><i>" + term + "</i></h1>");

        /*
         * check for terms in definition
         */
        String[] valueSplit = value.split(" ");

        for (int i = 0; i < array.length; i++) {
            for (int k = 0; k < valueSplit.length; k++) {
                if ((valueSplit[k].equals(array[i])) || valueSplit[k]
                        .substring(0, valueSplit[k].length() - 1)
                        .equals(array[i])
                        /*
                         * compares unicode value for any punctuation
                         */
                        && (valueSplit[k].substring(0, 1).compareTo("A") < 0
                                || valueSplit[k]
                                        .substring(valueSplit[k].length() - 1)
                                        .compareTo("A") < 0)) {
                    /*
                     * if there's punctuation attached to term
                     */
                    if (!valueSplit[k].equals(array[i])) {

                        /*
                         * so hyperlink doesn't reach punctuation
                         */
                        if (valueSplit[k].indexOf(array[i]) == 0) {
                            valueSplit[k] = "<a href=\"" + array[i] + ".html"
                                    + "\">"
                                    + valueSplit[k].substring(0,
                                            array[i].length())
                                    + "</a>" + valueSplit[k]
                                            .charAt(valueSplit[k].length() - 1);
                        } else { //to add if punctuation is before word
                            valueSplit[k] = valueSplit[k].charAt(0)
                                    + "<a href=\"" + array[i] + ".html" + "\">"
                                    + valueSplit[k].substring(1,
                                            array[i].length() + 1)
                                    + "</a>";
                        }
                    } else {
                        valueSplit[k] = "<a href=\"" + array[i] + ".html"
                                + "\">" + valueSplit[k] + "</a>";
                    }
                }
            }
        }

        /*
         * put value back together
         */
        String value1 = "";
        for (int i = 0; i < valueSplit.length; i++) {
            value1 = value1 + " " + valueSplit[i];
        }

        out.println("<p>" + value1 + "</p>");
        out.println("<hr>");
        out.println("<p>Return to <a href=\"index.html\">glossary</a>.</p>");
        out.println("</body></html>");

        out.close();

    }

    /**
     * Outputs HTML.
     *
     * @param folderName
     *            String for name
     * @param set
     *            Map of word and definitions
     * @param order
     *            ordered array
     * @param out
     *            output file
     */
    private static void htmlOut(String folderName, Map<String, String> set,
            String[] order, SimpleWriter out) {
        /*
         * Prints out html format
         */
        out.println("<html> <head> <title>" + "Glossary" + "</title>");
        out.println("</head> <body>");
        out.println("<h1>" + "Glossary" + "</h1><hr>");
        /*
         * html ordered list containing terms
         */
        out.println("<ol type=\"i\">");
        for (int i = 0; i < order.length; i++) {
            out.println("<li><a href=\"" + order[i] + ".html" + "\">" + order[i]
                    + "</a></li>");
            subPage(order[i], set.value(order[i]), folderName, order);
        }
        out.print("</ol>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Get input file name
         */
        out.print("Input file: ");
        String inputFileName = in.nextLine();
        SimpleReader inFile = new SimpleReader1L(inputFileName);

        /*
         * Get folder name
         */
        out.print("Enter folder name for contents: ");
        String folderName = in.nextLine();
        SimpleWriter output = new SimpleWriter1L(folderName + "/index.html");

        /*
         * Get word and definition pairs and then get the ordered key list
         */
        Map<String, String> glossary = linesFromInput(inFile);
        String[] order = alphaOrder(glossary);
        htmlOut(folderName, glossary, order, output);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
