
import java.io.*;
import java.util.*;

public class Test {
    public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    public static int fileCount = 0; //N in idf
    public static List<Double> idfList = new ArrayList<Double>();

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
        Stemmer s = new Stemmer();
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
        if (Arrays.asList(stopwords).contains(x)) {
            return "";
        }
        for (int i = 0; i < x.length(); i++) {
            s.add(x.charAt(i));
        }
        s.stem();
        x = s.toString();
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

        String updatedText = processString(newText);

        String[] stringArray = updatedText.split(" ");

        System.out.print("numWords: ");
        System.out.print(stringArray.length);
        System.out.print(" ");

        int cnt = -1;
        String curWord = "";

        Map<String,Integer> repetitionMap= new TreeMap<String,Integer>();
        for(String str : stringArray){

            if(repetitionMap.containsKey(str)) {
                repetitionMap.put(str,repetitionMap.get(str) + 1);
            }
            else {
                repetitionMap.put(str, 1);
            }
        }

        PrintWriter writer = new PrintWriter(outFile, "UTF-8");

        Iterator it = repetitionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            writer.println(pair.getKey() + " " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        writer.close();
    }

    public static List<List<Integer>> indexDirectory(String directoryName) throws IOException{
        File dir = new File(directoryName);
        File[] directoryListing = dir.listFiles();

        Map<String, List<Integer>> repetitionMap = new TreeMap<String, List<Integer>>();

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
                    List<Integer> l = new ArrayList<Integer>();
                    l.add(repetitionMap.get(newStringArray[0]).get(0) + value);
                    l.add(repetitionMap.get(newStringArray[0]).get(1) + 1);
                    repetitionMap.put(newStringArray[0],l);
                }
                else {
                    numWords++;
                    List<Integer> l = new ArrayList<Integer>();
                    l.add(value);
                    l.add(1);
                    repetitionMap.put(newStringArray[0], l);
                }
            }
        }

        PrintWriter writer = new PrintWriter("final.txt", "UTF-8");
        PrintWriter writer2 = new PrintWriter("final_words.txt", "UTF-8");

        for (Map.Entry<String, List<Integer>> entry : repetitionMap.entrySet())
        {
            String key = entry.getKey();
            allWords.add(key);

            writer.println(entry.getKey() + " " + entry.getValue().get(0) + " " + entry.getValue().get(1));
            writer2.println(entry.getKey());
        }

        writer.close();
        writer2.close();

        List<List<Integer>> index = new ArrayList<List<Integer>>();
        fileCount = 0;

        for (File child : directoryListing) {
            if ('.' == child.getName().charAt(0)) {
                continue;
            }
            fileCount++;
            String fileText = new Scanner(child).useDelimiter("\\Z").next();
            String[] fileTextArray = fileText.split("\n");
            String curWord = fileTextArray[0].split(" ")[0];
            int curValue = Integer.parseInt(fileTextArray[0].split(" ")[1]);
            int x = 0;
            List<Integer> fileIndex = new ArrayList<Integer>();
            for (String element : allWords) {
                if (element.equals(curWord)) {
                    fileIndex.add(curValue);
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

    public static String processString(String str) {
        String processedString = "";
        for (String retval: str.split(" ")){
            retval = retval.toLowerCase();
            String s = retval;
            if (!isWeb(retval)) {
                s = processWord(retval);
            }
            else {
                s = s.replace("\"", "");
            }
            s = s.replaceAll("\\s","");
            if (!s.equals("")) {
                processedString += s;
                processedString += " ";
            }
        }
        if (!processedString.equals("")) {
            processedString = processedString.substring(0, processedString.length() - 1);
        }

        return processedString;
    }

    public static void runQuery(Map<String, Integer> wordHash, List<List<Integer>> index, String processedQuery) throws IOException{
        String[] qArgs = processedQuery.split(" ");

        int count = 0;

        File dir = new File("out");
        File[] directoryListing = dir.listFiles();

        boolean foundAFile = false;


        for (File child : directoryListing) {
            if ('.' == child.getName().charAt(0)) {
                continue;
            }
            List<Integer> curList = index.get(count);
            count++;
            boolean failedQuery = false;
            boolean hadArgsInHash = false;
            for (String arg : qArgs) {
                if (wordHash.containsKey(arg)) {
                    hadArgsInHash = true;
                    int hashValue = wordHash.get(arg);
                    if (curList.get(hashValue) == 0) {
                        failedQuery = true;
                    }
                }
            }
            if (!failedQuery && hadArgsInHash) {
                foundAFile = true;
                System.out.println("Document " + child.getName() + " satisfies the query.");
            }
        }

        if (!foundAFile) {
            System.out.println("None of the documents had the words in your query.");
        }
    }

    public static double dotProduct(List<Double> a, List<Double> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += a.get(i) * b.get(i);
        }
        return sum;
    }

    public static void runVectorQuery(Map<String, Integer> wordHash, List<List<Double>> index, String processedQuery) {
        if (processedQuery.length() < 1) {
            System.out.println("Query contains only stop words.");
            return;
        }

        File dir = new File("docsnew");
        File[] directoryListing = dir.listFiles();

        //Create the query vector from the queryText
        List<Integer> intQueryVector = new ArrayList<Integer>();
        for (int i = 0; i < wordHash.size(); i++) {
            intQueryVector.add(0);
        }
        for (String arg : processedQuery.split(" ")) {
            if (wordHash.containsKey(arg)) {
                int hashValue = wordHash.get(arg);
                intQueryVector.set(hashValue, (intQueryVector.get(hashValue) + 1));
            }
        }

        List<Double> doubleQueryVector = new ArrayList<Double>();
        double vectorLength = 0, sum = 0;
        for (int i = 0; i < intQueryVector.size(); i++) {
            double val = intQueryVector.get(i) * idfList.get(i);
            doubleQueryVector.add(val);
            sum += (val * val);
        }
        vectorLength = Math.sqrt(sum);
        for (int i = 0; i < doubleQueryVector.size(); i++) {
            doubleQueryVector.set(i, (doubleQueryVector.get(i)/vectorLength));
        }

        //Query vector finished. Now need to compare to all documents.
        int docAmount = index.size();
        List<Double> similarityList = new ArrayList<Double>();
        for (int i = 0; i < docAmount; i++) {
            double dotVal = dotProduct(doubleQueryVector, index.get(i));
            similarityList.add(dotVal);
        }

        //similarityList now has the values that are needed for the ranking of the documents.
        Map<Double,String> repetitionMap= new TreeMap<Double,String>(Collections.reverseOrder());
        for(int i = 0; i < similarityList.size(); i++){
            double key = similarityList.get(i);
            if (key > 0) {
                //
                if(repetitionMap.containsKey(key)) {
                    //If two of them have exactly the same value then we would end up here which will be very rare.
                    //repetitionMap.put(key ,repetitionMap.get(key) + "," + Integer.toString(i));
                }
                else {
                    repetitionMap.put(key, Integer.toString(i));
                }
            }
        }

        String successString = "";

        Iterator it = repetitionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            successString += pair.getValue() + ",";
            it.remove(); // avoids a ConcurrentModificationException
        }

        successString = successString.substring(0, successString.length() - 1);

        for (String str : successString.split(",")) {
            int val = Integer.parseInt(str) + 1;
            System.out.println("Document " + directoryListing[val] + " satisfies the query.");
        }
    }

    public static List<List<Double>> createInvertedIndex(List<List<Integer>> index, Map<String, Integer> wordHash) throws IOException {
        String fileText = new Scanner(new File("final.txt")).useDelimiter("\\Z").next();
        //Create a list that will hold all the idf values for all the terms
        for (String dictStr : fileText.split("\n")) {
            String[] strArr = dictStr.split(" ");
            String str = strArr[0];
            int termCount = Integer.parseInt(strArr[1]);
            int docCount = Integer.parseInt(strArr[2]);
            double idf = Math.log10(fileCount/docCount);
            idfList.add(idf);
        }
        int outerListSize = index.size(); //Documents
        int innerListSize = index.get(0).size(); //Terms
        List<List<Double>> booleanModelIndex = new ArrayList<List<Double>>();
        for (int i = 0; i < outerListSize; i++) {
            List<Double> termsList = new ArrayList<Double>();
            double vectorLength = 0, sum = 0;
            for (int j = 0; j < innerListSize; j++) {
                double val = index.get(i).get(j) * idfList.get(j);
                termsList.add(val);
                sum += (val * val);
            }
            vectorLength = Math.sqrt(sum);
            for (int j = 0; j < innerListSize; j++) {
                termsList.set(j, (termsList.get(j)/vectorLength));
            }
            booleanModelIndex.add(termsList);
        }

        return booleanModelIndex;
    }

    public static void main(String [] args) throws IOException {
        Stemmer s = new Stemmer();

        System.out.println("Please select from the following list:");
        System.out.println("1. Tokenize and Index");
        System.out.println("2. Tokenize only");
        System.out.println("3. Index only");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        reader.nextLine();

        if (n == 1 || n == 2) {
/*#####################################################################*/
/*######################Starting the file tokenizing###################*/
/*#####################################################################*/
            File dir = new File("docsnew");
            File[] directoryListing = dir.listFiles();
            int count = 10;
            long startTime, stopTime, elapsedTime;
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if ('.' == child.getName().charAt(0)) {
                        continue;
                    }
                    String outFile = "out/" + (child.getName().substring(0, child.getName().indexOf("."))) + ".txt";
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
        List<List<Integer>> booleanIndex = new ArrayList<List<Integer>>();
        List<List<Double>> vectorIndex = new ArrayList<List<Double>>();
        Map<String, Integer> wordHash = new HashMap<String, Integer>();
        if (n == 1 || n == 3) {
/*#####################################################################*/
/*######################Starting the indexing process##################*/
/*#####################################################################*/
            wordHash = getWordHash();
            long start = System.currentTimeMillis();
            System.out.println("Creating Boolean Index.");
            booleanIndex = indexDirectory("out");
            System.out.println("Time to index(ms): " + (System.currentTimeMillis() - start));

            System.out.println("Creating Vector Model.");
            start = System.currentTimeMillis();
            vectorIndex = createInvertedIndex(booleanIndex, wordHash);
            System.out.println("Time to index(ms): " + (System.currentTimeMillis() - start));

            
            System.out.println("Please select from the following list:");
            System.out.println("1. Boolean Model Query");
            System.out.println("2. Vector Model Query");
            n = reader.nextInt();
            reader.nextLine();
/*#####################################################################*/
/*######################Ending the indexing process####################*/
/*#####################################################################*/
            if (n == 1) {
/*#####################################################################*/
/*##############Starting the boolean querying process##################*/
/*#####################################################################*/
                while (true) {
                    System.out.println("Please input a query you would like to search for:");
                    String query = reader.nextLine();

                    long queryStart = System.currentTimeMillis();
                    String formattedQuery = processString(query);

                    if (formattedQuery.length() < 1) {
                        System.out.println("The query contains only stop words.");
                    }
                    else {
                        runQuery(wordHash, booleanIndex, formattedQuery);
                        System.out.println("Time to query(ms): " + (System.currentTimeMillis() - queryStart));
                    }

                    System.out.println("Would you like to do another query? y/n");
                    String again = reader.nextLine();
                    if (again.toUpperCase().equals("N")) {
                        break;
                    }
                }
/*#####################################################################*/
/*##############Ending the boolean querying process####################*/
/*#####################################################################*/
            }
            else if (n == 2) {
/*#####################################################################*/
/*##############Starting the vector querying process###################*/
/*#####################################################################*/
                while (true) {
                    System.out.println("Please input a query you would like to search for:");
                    String query = reader.nextLine();

                    long queryStart = System.currentTimeMillis();
                    String formattedQuery = processString(query);

                    if (formattedQuery.length() < 1) {
                        System.out.println("The query contains only stop words.");
                    }
                    else {
                        runVectorQuery(wordHash, vectorIndex, formattedQuery);
                        System.out.println("Time to query(ms): " + (System.currentTimeMillis() - queryStart));
                    }

                    System.out.println("Would you like to do another query? y/n");
                    String again = reader.nextLine();
                    if (again.toUpperCase().equals("N")) {
                        break;
                    }
                }
/*#####################################################################*/
/*################Ending the vector querying process###################*/
/*#####################################################################*/                
            }
        }
    }
}
























