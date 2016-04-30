/* To Run Our Project
Mac:
./compile_script_mac
./start_script_mac

Linux:
./compile_script_linux
./start_script_linux

Windows(Currently not working):
./compile_script_windows
./start_script_windows
*/

import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
    public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    public static int fileCount = 0; //N in idf
    public static List<Double> idfList = new ArrayList<Double>();
    public static List<Integer> currentShowingDocs = new ArrayList<Integer>();
    public static List<Integer> relevantDocsList = new ArrayList<Integer>();
    public static List<Integer> nonRelevantDocsList = new ArrayList<Integer>();
    public static String dirName = "";
    public static List<List<Integer>> booleanIndexMain = new ArrayList<List<Integer>>();
    public static List<List<Double>> vectorIndexMain = new ArrayList<List<Double>>();
    public static Map<String, Integer> wordHashMain = new HashMap<String, Integer>();
    public static List<List<String>> docWords = new ArrayList<List<String>>();
    public static List<List<List<Integer>>> postingList = new ArrayList<List<List<Integer>>>();

    /*
    This function is used to initialize the posting list so that it can be updated later.
    */
    public static void initializePostingList(int numTerms) {
        File dir = new File("words");
        File[] directoryListing = dir.listFiles();
        for (int i = 0; i < numTerms; i++) {
            List<List<Integer>> firstInner = new ArrayList<List<Integer>>();
            for (File child : directoryListing) {
                List<Integer> secondInner = new ArrayList<Integer>();
                firstInner.add(secondInner);
            }
            postingList.add(firstInner);
        }
    }

    /*
    fds
    */
    public static void addDocWords(String filename, int arrayIndex, Map<String, Integer> wordHash) throws FileNotFoundException {
        String fileText = new Scanner(new File(filename)).useDelimiter("\\Z").next();
        int count = 0;
        List<String> thisDocWords = new ArrayList<String>();
        for (String str : fileText.split(" ")) {
            thisDocWords.add(str);
            int hashVal = wordHash.get(str);
            postingList.get(hashVal).get(arrayIndex).add(count);
            count++;
        }
        docWords.add(thisDocWords);
    }

    /*
    This function is used to check if an integer value is in an Integer list
    */
    public static boolean isInList(List<Integer> x, Integer val) {
        for (Integer i : x) {
            if (val == i) {
                return true;
            }
        }
        return false;
    }

    /*
    This function is used to print out a list of lists. This is our main indexing data structure
    */
    public static void printListOfLists (List x, PrintWriter writer) throws IOException{
        for (int i = 0; i < x.size(); i++) {
            printList(x, writer);
            writer.println();
        }
    }

    /*
    This function is used to print out a single list
    */
    public static void printList (List x, PrintWriter writer) {
        writer.println(Arrays.toString(x.toArray()));
    }

    /*
    This funciton is used to eliminate any extraneous html elements that aren't needed such as encoded spaces
    */
    private static String eliminateHTMLEntities (String x) {
        x = x.replace("&#160;", " ");
        x = x.replace("&amp;", " ");
        return x;
    }

    /*
    This function is used to eliminate stop words, stem the word and to make sure it is only a word.
    */
    private static String processWord (String x) {
        Stemmer s = new Stemmer();
        x = x.replace(",", "");
        x = x.replace("$", "");
        x = x.replace(";", "");
        x = x.replace("(", "");
        x = x.replace(")", "");
        x = x.replace("\"", "");
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

    /*
    This function is used to check if a given string is a date
    */
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

    /*
    This function is used to check if a given string is a web address
    */
    private static boolean isWeb (String x) {
        x = x.toLowerCase();
        if (x.contains(".com") || x.contains(".org") || x.contains(".net") || x.contains(".int") || x.contains(".edu") || x.contains(".gov") || x.contains(".mil")) {
            return true;
        }
        return false;
    }

    /*
    This function is used in order to print out a mapping of strings to integers. Such maps are used throughout the parsing and indexing process
    */
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

    /*
    This function is used in order to determine if a given string is numeric.
    */
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

    /*
    This is the main function for the parsing process. It takes a file and strips it of all html and converts it to only words
    */
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

        //---- These two lines could be eliminated, but speed things up ----//
        Document doc = Jsoup.parse(fileText);//value is your variable having html content
        fileText = doc.text();//gives you plain text

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

        Writer testingOut = new BufferedWriter(new FileWriter(filename.replace("in/", "words/"), true));
        testingOut.append(updatedText);
        testingOut.close();

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

    /*
    This function is used as the main part of the indexing process. It takes the directory of the parsed files and indexes them together
    */
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

    /*
    This function generates a hash table for all of the words that have been indexed.
    */
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

    /*
    This function is used to process a string in parsing. It is used in conjunction with processWord
    */
    public static String processString(String str) {
        String processedString = "";
        for (String retval: str.split(" ")){
            retval = retval.toLowerCase();
            String s = retval;
            if (!isWeb(retval)) {
                s = processWord(retval);
            }
            else {
                s = s.replace("\"", "").replace("{", "").replace("}", "");
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

    /*
    This is the function that runs the Boolean Model Query
    */
    public static String runQuery(Map<String, Integer> wordHash, List<List<Integer>> index, String processedQuery) throws IOException{
        String[] qArgs = processedQuery.split(" ");

        int count = 0;

        File dir = new File("out");
        File[] directoryListing = dir.listFiles();

        boolean foundAFile = false;

        String returnString = "";

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
                returnString += child.getName() + ",";
                System.out.println("Document " + child.getName() + " satisfies the query.");
            }
        }
        returnString = returnString.substring(0, returnString.length() - 1);

        if (!foundAFile) {
            System.out.println("None of the documents had the words in your query.");
        }
        return returnString;
    }

    /*
    This function is used to calculate the dot product of two list "vectors"
    */
    public static double dotProduct(List<Double> a, List<Double> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += a.get(i) * b.get(i);
        }
        return sum;
    }

    /*
    This function is used to subtract list b from list a. AKA vector subtraction
    */
    public static List<Double> subLists(List<Double> a, List<Double> b) {
        List<Double> c = new ArrayList<Double>();
        for (int i = 0; i < a.size(); i++) {
            c.add(a.get(i) - b.get(i));
        }
        return c;
    }

    /*
    This function is used to add list b to list a. AKA vector addition
    */
    public static List<Double> addLists(List<Double> a, List<Double> b) {
        List<Double> c = new ArrayList<Double>();
        for (int i = 0; i < a.size(); i++) {
            c.add(a.get(i) + b.get(i));
        }
        return c;
    }

    /*
    This is the function that runs the Vector Model Query
    */
    public static String runVectorQuery(Map<String, Integer> wordHash, List<List<Double>> index, String processedQuery) throws FileNotFoundException {
        if (processedQuery.length() < 1) {
            System.out.println("Query contains only stop words.");
            return "";
        }

        int hv = wordHash.get("hous");
        System.out.println(hv);

        File dir = new File("docsnew");
        File[] directoryListing = dir.listFiles();

        String[] fileListing = new Scanner(new File("indexed_files_names.txt")).useDelimiter("\\Z").next().split("\n");

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
        double sum = 0.0;
        for (int i = 0; i < intQueryVector.size(); i++) {
            double val = intQueryVector.get(i) * idfList.get(i);
            doubleQueryVector.add(val);
            sum += (val * val);
        }
        Double vectorLength = Math.sqrt(sum);
        for (int i = 0; i < doubleQueryVector.size(); i++) {
            if (doubleQueryVector.get(i) == 0.0 || vectorLength == 0.0) {
                doubleQueryVector.set(i, 0.0);
            }
            else {
                doubleQueryVector.set(i, (doubleQueryVector.get(i)/vectorLength));
            }
        }

        //Need to add and subtract the relevant and nonrelevant documents
        //Add relevant docs
        for (Integer i : relevantDocsList) {
            List<Double> addList = index.get(i);
            doubleQueryVector = addLists(doubleQueryVector, addList);
        }
        //Subtract non relevant docs
        for (Integer i : nonRelevantDocsList) {
            List<Double> subList = index.get(i);
            doubleQueryVector = subLists(doubleQueryVector, subList);
        }
        //Normalize vector

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

        String returnStr = "";

        List<Integer> tempList = new ArrayList<Integer>();

        for (String str : successString.split(",")) {
            int val = Integer.parseInt(str);
            tempList.add(val);
            System.out.println("Document " + fileListing[val] + " satisfies the query.");
            returnStr += fileListing[val] + ",";
        }

        returnStr = returnStr.substring(0, returnStr.length() - 1);

        currentShowingDocs = tempList;

        return returnStr;
    }

    /*
    This is the function that creates the data structures for the vector model. It expands the boolean model.
    */
    public static List<List<Double>> createInvertedIndex(List<List<Integer>> index, Map<String, Integer> wordHash) throws IOException {
        String fileText = new Scanner(new File("final.txt")).useDelimiter("\\Z").next();
        //Create a list that will hold all the idf values for all the terms
        for (String dictStr : fileText.split("\n")) {
            String[] strArr = dictStr.split(" ");
            String str = strArr[0];
            int termCount = Integer.parseInt(strArr[1]);
            int docCount = Integer.parseInt(strArr[2]);
            double idf = Math.log10(((double)fileCount)/((double)docCount));
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

    public static void createJsonFile(List<List<Double>> index) throws IOException {
        /*
        {
            document1: {
                            vector: {num for all terms},
                            url: string
                        },
            document2: {num for all terms},
            ...
        }
        */
        long start = System.currentTimeMillis();
        /*#####################################################################*/
        /*##########################Starting index.json########################*/
        /*#####################################################################*/
        System.out.println("index.json");
        Writer output = new BufferedWriter(new FileWriter("index.json"));
        output.append("[\n");
        File urlDir = new File("in");
        File[] urlListing = urlDir.listFiles();

        int lastUrl = urlListing.length - 1;

        File dir = new File("out");
        int count = 0;
        for (File child : dir.listFiles()) {
            String name = child.getName().replace(" ", "_").replace(".txt", "");
            List<Double> curList = index.get(count);
            output.append("\t{\"name\":\"" + name + "\",\n");//document1:
            output.append("\t\"vector\": [");
            int listSize = curList.size();
            for (int i = 0; i < listSize; i++) {
                output.append("" + curList.get(i) + "");
                if (i < listSize-1) {
                    output.append(",");
                }
            }
            output.append("],\n");
            output.append("\t\"url\": \"" + urlListing[count] + "\"\n");
            if (count == lastUrl) {
                output.append("\t}");
            }
            else {
                output.append("\t},");
            }
            

            count++;
        }
        output.append("]");
        output.close();
        /*#####################################################################*/
        /*############################Ending index.json########################*/
        /*#####################################################################*/
        long end = System.currentTimeMillis();
        System.out.println("Time taken(ms):" + (end - start));
        start = end;
        /*#####################################################################*/
        /*##########################Starting word.json#########################*/
        /*#####################################################################*/
        System.out.println("word.json");
        Writer out2 = new BufferedWriter(new FileWriter("word.json"));
        String fileText = new Scanner(new File("final_words.txt")).useDelimiter("\\Z").next();
        out2.append("{\n");
        count = 0;
        String[] wordArr = fileText.split("\n");
        int arrSize = wordArr.length;
        for (int i = 0; i < arrSize; i++) {
            String str = wordArr[i];
            str = str.replace("{", "").replace("}", "").replace(":", "");

            out2.append("\t\"" + str + "\":" + count);
            if (i < arrSize-1) {
                out2.append(",\n");
            }
            else {
                out2.append("\n");
            }
            count++;
        }
        out2.append("}");
        out2.close();
        /*#####################################################################*/
        /*############################Ending word.json#########################*/
        /*#####################################################################*/
        end = System.currentTimeMillis();
        System.out.println("Time taken(ms):" + (end - start));
        start = end;
        /*#####################################################################*/
        /*#######################Starting idfList.json#########################*/
        /*#####################################################################*/
        System.out.println("idfList.json");
        Writer out3 = new BufferedWriter(new FileWriter("idfList.json"));
        int listSize = idfList.size();
        out3.append("{\n\t\"idfList\":[");
        for (int i = 0; i < listSize; i++) {
            out3.append("" + idfList.get(i) + "");
            if (i > listSize-2) {
                out3.append("]\n");
            }
            else {
                out3.append(",\n\t");
            }
        }
        out3.append("}");
        out3.close();
        /*#####################################################################*/
        /*#########################Ending idfList.json#########################*/
        /*#####################################################################*/
        end = System.currentTimeMillis();
        System.out.println("Time taken(ms):" + (end - start));
        start = end;
        /*#####################################################################*/
        /*#######################Starting postings.json########################*/
        /*#####################################################################*/
        System.out.println("postings.json");
        Writer out4 = new BufferedWriter(new FileWriter("postings.json"));
        out4.append("[\n");
        for (int i = 0; i < arrSize; i++) {
            String str = wordArr[i];
            out4.append("\t{\n");
            count = 0;
            for (File child : dir.listFiles()) {
                String name = child.getName().replace(" ", "_").replace(".txt", "");
                out4.append("\t\t\"" + name + "\": [");
                List<Integer> curPosting = postingList.get(i).get(count);
                int postingSize = curPosting.size();
                for (int j = 0; j < postingSize; j++) {
                    out4.append("" + curPosting.get(j));
                    if (j < postingSize - 1) {
                        out4.append(",");
                    }
                }
                if (count == lastUrl) {
                    out4.append("]\n");
                }
                else {
                    out4.append("],\n");
                }
                count++;
            }
            if (i > arrSize - 2) {
                out4.append("\t}");
            }
            else {
                out4.append("\t},");
            }
        }
        out4.append("]");
        out4.close();
        /*#####################################################################*/
        /*########################Ending postings.json#########################*/
        /*#####################################################################*/
        end = System.currentTimeMillis();
        System.out.println("Time taken(ms):" + (end - start));
        start = end;
        /*#####################################################################*/
        /*######################Starting wordLocs.json#########################*/
        /*#####################################################################*/
        System.out.println("wordLocs.json");
        Writer out5 = new BufferedWriter(new FileWriter("wordLocs.json"));
        out5.append("{\n");
        count = 0;
        for (File child : dir.listFiles()) {
            String name = child.getName().replace(" ", "_").replace(".txt", "");
            out5.append("\t\"" + name + "\": [");
            List<String> curDoc = docWords.get(count);
            listSize = curDoc.size();
            for (int i = 0; i < listSize; i++) {
                out5.append("\"" + curDoc.get(i) + "\"");
                if (i > listSize - 2) {
                    //
                }
                else {
                    out5.append(",");
                }
            }
            if (count == lastUrl) {
                out5.append("\t]\n");
            }
            else {
                out5.append("\t],\n");
            }
            count++;
        }
        out5.append("}");
        out5.close();
        /*#####################################################################*/
        /*########################Ending wordLocs.json#########################*/
        /*#####################################################################*/
        end = System.currentTimeMillis();
        System.out.println("Time taken(ms):" + (end - start));
        start = end;
    }

    public static void main(String [] args) throws IOException {
        System.out.println("Please select from the following list:");
        System.out.println("1. Create index");
        System.out.println("2. Run Web Crawler");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        reader.nextLine();
        dirName = "docsnew";

        if (n == 1) {
            File dummy = new File("out");
            for(File dummyFile: dummy.listFiles()) dummyFile.delete();
            dummy = new File("words");
            for(File dummyFile: dummy.listFiles()) dummyFile.delete();
/*#####################################################################*/
/*######################Starting the file tokenizing###################*/
/*#####################################################################*/
            File dir = new File("in");
            File[] directoryListing = dir.listFiles();
            long startTime, stopTime, elapsedTime;
            PrintWriter fileNameWriter = new PrintWriter("indexed_files_names.txt", "UTF-8");
            String[] crawledFileArr = new Scanner(new File("crawled_to_files.txt")).useDelimiter("\\Z").next().split("\n");
            int counter = 0;

            if (directoryListing != null) {
                System.out.println("Indexing...");
                for (File child : directoryListing) {
                    if ('.' == child.getName().charAt(0)) {
                        continue;
                    }
                    String outFile = "out/" + (child.getName().substring(0, child.getName().indexOf("."))) + ".txt";
                    String inFile = "in/" + child.getName();
                    startTime = System.currentTimeMillis();
                    tokenizeFile(inFile, outFile);
                    stopTime = System.currentTimeMillis();
                    System.out.print("Time Taken(ms): ");
                    System.out.print(stopTime - startTime);
                    System.out.print(" ");
                    System.out.println(inFile);
                    fileNameWriter.println(child.getName());
                    counter++;
                }
            } else {
                System.out.println("Directory in doesn't exist");
            }

            fileNameWriter.close();
/*#####################################################################*/
/*######################Ending the file tokenizing#####################*/
/*#####################################################################*/
/*#####################################################################*/
/*######################Starting the indexing process##################*/
/*#####################################################################*/
            long start = System.currentTimeMillis();
            System.out.println("Creating Boolean Index.");
            booleanIndexMain = indexDirectory("out");
            System.out.println("Time to index(ms): " + (System.currentTimeMillis() - start));
            
            wordHashMain = getWordHash();

            System.out.println("Creating Vector Model.");
            start = System.currentTimeMillis();
            vectorIndexMain = createInvertedIndex(booleanIndexMain, wordHashMain);
            System.out.println("Time to index(ms): " + (System.currentTimeMillis() - start));

            dir = new File("words");
            directoryListing = dir.listFiles();
            counter = 0;

            initializePostingList(vectorIndexMain.get(0).size());

            start = System.currentTimeMillis();
            if (directoryListing != null) {
                System.out.println("Creating Posting Lists");
                for (File child : directoryListing) {
                    String wordFileName = "words/" + child.getName();
                    addDocWords(wordFileName, counter, wordHashMain);
                    counter++;
                }
            }
            else {
                System.out.println("Directory words doesn't exist");
            }
            System.out.println("Time to create(ms): " + (System.currentTimeMillis() - start));

            System.out.println("Creating Json Files");
            start = System.currentTimeMillis();
            createJsonFile(vectorIndexMain);
            System.out.println("Time to create(ms): " + (System.currentTimeMillis() - start));
/*#####################################################################*/
/*######################Ending the indexing process####################*/
/*#####################################################################*/
        }
        if (n == 2) {
/*#####################################################################*/
/*#################Starting the web crawling process###################*/
/*#####################################################################*/
            File dir = new File("crawled");
            File file_to_text = new File("crawled_to_files.txt");
            file_to_text.delete();
            for(File file: dir.listFiles()) file.delete();
            WebCrawler w = new WebCrawler();
            w.processPage("http://www.ku.edu", "ku.edu");
/*#####################################################################*/
/*###################Ending the web crawling process###################*/
/*#####################################################################*/
        }
    }
}
























