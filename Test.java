import java.io.*;
import java.util.*;

public class Test {
    public static void printListOfLists (List x, PrintWriter writer) throws IOException{
        for (int i = 0; i < x.size(); i++) {
            printList(x, writer);
            writer.println();
        }
    }

    public static void printList (List x, PrintWriter writer) {
        writer.println(Arrays.toString(x.toArray()));
    }

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
        int count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
            count++;
        }
        System.out.println("numWords: " + count);
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
            else {
                s = s.replace("\"", "");
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
            writer.println(pair.getKey() + " " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        writer.close();
    }

    public static List<List> indexDirectory(String directoryName) throws IOException{
        File dir = new File(directoryName);
        File[] directoryListing = dir.listFiles();

        Map<String, Integer> repetitionMap = new HashMap<String, Integer>();

        int numFiles = directoryListing.length;
        int numWords = 0;
        List<String> allWords = new ArrayList<String>();

        for (File child : directoryListing) {
            if ('.' == child.getName().charAt(0)) {
                continue;
            }
            String fileText = new Scanner(child).useDelimiter("\\Z").next();

            for (String str : fileText.split("\n")) {
                String[] newStringArray = str.split(" ");
                int value = Integer.parseInt(newStringArray[1]);
                if (repetitionMap.containsKey(newStringArray[0])) {
                    repetitionMap.put(newStringArray[0],repetitionMap.get(newStringArray[0]) + value);
                }
                else {
                    numWords++;
                    repetitionMap.put(newStringArray[0], value);
                }
            }
        }

        Map<String, Integer> map = new TreeMap<String, Integer>(repetitionMap);

        for (Map.Entry<String, Integer> entry : map.entrySet())
        {
          String key = entry.getKey();
          allWords.add(key);
        }

        PrintWriter writer = new PrintWriter("final.txt", "UTF-8");
        PrintWriter writer2 = new PrintWriter("final_words.txt", "UTF-8");

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            writer.println(pair.getKey() + " " + pair.getValue());
            writer2.println(pair.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }
        writer.close();
        writer2.close();

        List<List> index = new ArrayList<List>();

        for (File child : directoryListing) {
            if ('.' == child.getName().charAt(0)) {
                continue;
            }
            String fileText = new Scanner(child).useDelimiter("\\Z").next();
            String[] fileTextArray = fileText.split("\n");
            String curWord = fileTextArray[0].split(" ")[0];
            int x = 0;
            List<Integer> fileIndex = new ArrayList<Integer>();
            for (String element : allWords) {
                if (element.equals(curWord)) {
                    fileIndex.add(1);
                    x++;
                    if (x != fileTextArray.length) {
                        curWord = fileTextArray[x].split(" ")[0];
                    }
                }
                else {
                    fileIndex.add(0);
                }
            }
            index.add(fileIndex);
        }

        return index;

        // Uncomment this if you'd like to output the index. Be warned it will be a big file and this will take a considerable amount of time.
        // PrintWriter writer3 = new PrintWriter("index.txt", "UTF-8");
        // printListOfLists(index, writer3);
        // writer3.close();
    }

    public static Map<String, Integer> getWordHash() throws IOException{
        String fileText = new Scanner(new File("final_words.txt")).useDelimiter("\\Z").next();
        Map<String, Integer> hash = new HashMap<String, Integer>();
        int count = 0;
        for (String str : fileText.split("\n")) {
            hash.put(str, count);
            count++;
        }
        return hash;
    }

    public static void main(String [] args) throws IOException {
        System.out.println("Please select from the following list:");
        System.out.println("1. Tokenize and Index");
        System.out.println("2. Tokenize only");
        System.out.println("3. Index only");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();

        if (n == 1 || n == 2) {
/*#####################################################################*/
/*######################Starting the file tokenizing###################*/
/*#####################################################################*/
        File dir = new File("docsnew");
        File[] directoryListing = dir.listFiles();
        int count = 0;
        long startTime, stopTime, elapsedTime;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if ('.' == child.getName().charAt(0)) {
                    continue;
                }
                String outFile = "out/" + count + ".txt";
                String inFile = "docsnew/" + child.getName();
                startTime = System.currentTimeMillis();
                tokenizeFile(inFile, outFile);
                stopTime = System.currentTimeMillis();
                System.out.print("Time Taken(ms): ");
                System.out.print(stopTime - startTime);
                System.out.print(" ");
                count++;
                System.out.println(inFile);
            }
        } else {
            System.out.println("Directory docsnew doesn't exist");
        }
/*#####################################################################*/
/*######################Ending the file tokenizing#####################*/
/*#####################################################################*/
        }
        if (n == 1 || n == 3) {
/*#####################################################################*/
/*######################Starting the indexing process##################*/
/*#####################################################################*/
        List<List> index = new ArrayList<List>();
        Map<String, Integer> wordHash = new HashMap<String, Integer>();
        wordHash = getWordHash();
        long start = System.currentTimeMillis();
        index = indexDirectory("out");
        System.out.println("Time to index(ms): " + (System.currentTimeMillis() - start));
        System.out.println("Size of list: " + index.size());

        System.out.println("Testing if I can get all documents containing: WORLDS");

        int hashValue = wordHash.get("WORLDS");
        int y = 0;
        for (List<Integer> list : index) {
            if (list.get(hashValue) == 1) {
                System.out.println("Document " + y + " contains WORLDS.");
            }
            y++;
        }

/*#####################################################################*/
/*######################Ending the indexing process####################*/
/*#####################################################################*/
        }
    }
}
























