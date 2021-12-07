import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public class StringReassemblyTest {

    @Test
    public void combinationTest_Real() {//test combining two strings
        String str1 = "Rea";
        String str2 = "eal";
        String concat = "Real";
        int overlap = 2;
        String test = StringReassembly.combination(str1, str2, overlap);
        assertEquals(concat, test);
    }

    @Test //removing ello and adding hello
    public void addToSetAvoidingSubstringsTest_hello() {
        Set<String> strSet = new Set1L<>();
        strSet.add("ello");
        strSet.add("tom");
        String str = "hello";
        StringReassembly.addToSetAvoidingSubstrings(strSet, str);
        assertTrue(strSet.contains(str));
        assertTrue(!strSet.contains("ello"));
        assertTrue(strSet.contains("tom"));
    }

    @Test //adding one thats not a substring to anything
    public void addToSetAvoidingSubstringsTest_not() {
        Set<String> strSet = new Set1L<>();
        strSet.add("ello");
        strSet.add("tom");
        String str = "not";
        StringReassembly.addToSetAvoidingSubstrings(strSet, str);
        assertTrue(strSet.contains(str));
        assertTrue(strSet.contains("ello"));
        assertTrue(strSet.contains("tom"));
    }

    @Test //adding an string to an empty set
    public void addToSetAvoidingSubstringsTest_empty() {
        Set<String> strSet = new Set1L<>();
        String str = "hello";
        StringReassembly.addToSetAvoidingSubstrings(strSet, str);
        assertTrue(strSet.contains(str));
    }

    @Test //replaces if it starts with the main string
    public void linesFromInputTest1() {
        Set<String> lines = new Set1L<>();
        String name = "data/test.txt";
        SimpleReader in = new SimpleReader1L(name);
        lines = StringReassembly.linesFromInput(in);
        assertTrue(lines.contains("hello"));
        assertTrue(!lines.contains("ello"));

    }

    @Test //replaces if it starts with a substring
    public void linesFromInputTest2() {
        Set<String> lines = new Set1L<>();
        String name = "data/test2.txt";
        SimpleReader in = new SimpleReader1L(name);
        lines = StringReassembly.linesFromInput(in);
        assertTrue(lines.contains("hello"));
        assertTrue(!lines.contains("ello"));
    }

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(this.outputStreamCaptor));
    }

    @Test //two newlines
    public void printWithLineSeparatorsTest_twoTilde() {
        SimpleWriter out = new SimpleWriter1L();
        String tilde = ("Hello~~ t");
        StringReassembly.printWithLineSeparators(tilde, out);
        assertEquals("Hello\n\n t", this.outputStreamCaptor.toString().trim());
        out.close();
    }

    @Test //newline at the begining
    public void printWithLineSeparatorsTest_beginTilde() {
        SimpleWriter out = new SimpleWriter1L();
        String tilde = ("~Hello");
        StringReassembly.printWithLineSeparators(tilde, out);
        assertEquals("\nHello",
                this.outputStreamCaptor.toString().stripTrailing());
    }

}
