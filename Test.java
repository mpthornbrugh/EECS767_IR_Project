import java.io.*;
import java.util.*;

public class Test {
    private static String eliminateHTMLEntities (String x) {
        x = x.replace("&#160;", " ");
        x = x.replace("&amp;", " ");
        return x;
    }

    private static String processWord (String x) {
        x = x.replace(",", "");
        x = x.replace("$", "");
        x = x.replace(";", "");
        x = x.replace("(", "");
        x = x.replace(")", "");
        if (isNumeric(x)) {
            return x;
        }
        if (isDate(x)) {
            return x;
        }
        x = x.replaceAll("\\W", "");
        return x;
    }

    public static boolean isDate(String x) {
        if (x.contains("-")) {
            for (String str : x.split("-")) {
                if (!isNumeric(str)) {
                    return false;
                }
                return true;
            }
        }
        if (x.contains("/")) {
            for (String str : x.split("-")) {
                if (!isNumeric(str)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isWeb (String x) {
        x = x.toLowerCase();
        if (x.contains(".com") || x.contains(".org") || x.contains(".net") || x.contains(".int") || x.contains(".edu") || x.contains(".gov") || x.contains(".mil")) {
            return true;
        }
        return false;
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static boolean isNumeric(String str)
    {
        try
        {
          double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
          return false;
        }
        return true;
    }

    public static void tokenizeFile(String filename, String outFile) throws IOException{
        BufferedReader inputStream = new BufferedReader(new FileReader(filename));
        File UIFile = new File("file.txt");
        // if File doesnt exists, then create it
        if (!UIFile.exists()) {
            UIFile.createNewFile();
        }
        FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
        BufferedWriter outputStream= new BufferedWriter(filewriter);
        String count;
        while ((count = inputStream.readLine()) != null) {
            outputStream.write(count);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        String fileText = new Scanner(new File("file.txt")).useDelimiter("\\Z").next();
        fileText = eliminateHTMLEntities(fileText);

        UIFile.delete();

        boolean isDeleting = false;
        String newText = "";
        for (int i = 0; i < fileText.length(); i++){
            char c = fileText.charAt(i);
            if (isDeleting) {
                if (c == '>') {
                    isDeleting = false;
                    newText += " ";
                }
            }
            else {
                if (c == '<') {
                    isDeleting = true;
                    //If a script tag is found we don't want the stuff inbetween
                    if ('s' == fileText.charAt(i+1) && 'c' == fileText.charAt(i+2) && 'r' == fileText.charAt(i+3) && 'i' == fileText.charAt(i+4) && 'p' == fileText.charAt(i+5) && 't' == fileText.charAt(i+6)) { //Check for script
                        i = i+6;
                        c = fileText.charAt(i);
                        while (c != '>') {
                            i++;
                            c = fileText.charAt(i);
                        }
                    }
                    //If a comment is found we don't want anything in the comment
                    if ('!' == fileText.charAt(i+1) && '-' == fileText.charAt(i+2) && '-' == fileText.charAt(i+3)) {
                        i = i+3;
                        while (!('-' == fileText.charAt(i) && '-' == fileText.charAt(i+1) && '>' == fileText.charAt(i+2))) {
                            i++;
                        }
                    }
                }
                else {
                    newText += c;
                }
            }
        }

        newText = newText.replaceAll("\\s+"," ");

        String updatedText = "";

        for (String retval: newText.split(" ")){
            retval = retval.toUpperCase();
            String s = retval;
            if (!isWeb(retval)) {
                s = processWord(retval);
            }
            s = s.replaceAll("\\s","");
            if (!s.equals("")) {
                updatedText += s;
                updatedText += " ";
            }
        }

        String[] stringArray = updatedText.split(" ");

        Arrays.sort(stringArray);

        System.out.print("numWords: ");
        System.out.print(stringArray.length);
        System.out.print(" ");

        int cnt = -1;
        String curWord = "";

        Map<String,Integer> repetitionMap= new HashMap<String,Integer>();
        for(String str : stringArray){

            if(repetitionMap.containsKey(str)) {
                repetitionMap.put(str,repetitionMap.get(str) + 1);
            }
            else {
                repetitionMap.put(str, 1);
            }
        }

        Map<String, Integer> map = new TreeMap<String, Integer>(repetitionMap);

        PrintWriter writer = new PrintWriter(outFile, "UTF-8");

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            writer.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        writer.close();
    }

    public static void main(String [] args) throws IOException {
        File dir = new File("docsnew");
        File[] directoryListing = dir.listFiles();
        int count = 0;
        long startTime, stopTime, elapsedTime;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String outFile = "out/" + count + ".txt";
                String inFile = "docsnew/" + child.getName();
                startTime = System.currentTimeMillis();
                tokenizeFile(inFile, outFile);
                stopTime = System.currentTimeMillis();
                System.out.print(stopTime - startTime);
                System.out.print(" ");
                count++;
                System.out.println(inFile);
            }
        } else {
            System.out.println("Directory docsnew doesn't exist");
        }

        //tokenizeFile("Acadia_National_Park.htm", "1.txt");
    }
}
























