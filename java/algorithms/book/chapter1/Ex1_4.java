package chapter1;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by outrun on 4/10/16.
 */
public class Ex1_4 {

    private final static String DIR = "src/main/resources/chapter1/Ex1_4/";

    private HashMap<String, Boolean> filenames = new HashMap<String, Boolean>();

    private String includeAFile(String filename) {
        filenames.put(filename, true);

        String retStr = "";

        File file = new File(DIR + filename);
//        Reader reader = null;new InputStreamReader(new FileInputStream(file));
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
//            reader = null;new InputStreamReader(new FileInputStream(file));
//            char[] buf = new char[8];
//            int c = 0;
//            while ((c = reader.read(buf)) != -1) {
//                retStr += new String(buf);
//            }


            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#include")) {
                    String filenameInclude = line.substring(line.indexOf("#include") + 9);

                    if (filenames.get(filenameInclude) != null) {
                        throw new RuntimeException("duplicated filename");
                    } else {
                        String fileStr = includeAFile(filenameInclude);
                        retStr += fileStr;
                    }
                } else {
                    retStr += line;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    reader = null;
                }
            }
        }

        return retStr;
    }

    @Test
    public void includeFile() {
        File file = new File(DIR + "Ex1_2.java");
        System.out.println(Ex1_4.class.getResource("."));
        System.out.println(Ex1_4.class.getClassLoader().getResource("."));
        System.out.println(file.getAbsolutePath());

        System.out.println(includeAFile("a.h"));
    }
}
