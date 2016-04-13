import java.io.*;
import java.util.*;
import java.net.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
	public static ArrayList<String> urls = new ArrayList<String>();

	public static int numUrlsFound = 0;

	public static void main(String [] args) {
		//
	}

	public static void addUrl(String filename) throws IOException {
		Writer output = new BufferedWriter(new FileWriter("crawled_to_files.txt", true));
		output.append(filename);
		output.close();
	}

	public static void saveUrl(final String filename, final String urlString) throws MalformedURLException, IOException {
	    BufferedInputStream in = null;
	    FileOutputStream fout = null;
	    try {
	        in = new BufferedInputStream(new URL(urlString).openStream());
	        fout = new FileOutputStream(filename);

	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}

	public static void processPage(String URL, String hostName) throws IOException {
		boolean hasUrl = false;
		for (String str : urls) {
			if (URL.equals(str)) {
				hasUrl = true;
				break;
			}
		}

		if (hasUrl) {

		}
		else if (urls.size() > 300) {

		}
		else {
			urls.add(URL);

			try {
				Document doc = Jsoup.connect(URL).get();

				saveUrl(("crawled/" + numUrlsFound + ".htm"), URL);
				addUrl(URL + "\n");
				numUrlsFound++;

				System.out.println(URL);

				Elements questions = doc.select("a[href]");
				for(Element link: questions){
					if(link.attr("href").contains(hostName) && (link.attr("href").contains("http:") || link.attr("href").contains("http:"))) {
						String nextUrl = link.attr("abs:href");
						boolean isNew = true;
						for (String str : urls) {
							if (nextUrl.equals(str)) {
								isNew = false;
								break;
							}
						}
						if (isNew) {
							processPage(link.attr("abs:href"), hostName);
						}
					}
				}
			}
			catch (IllegalArgumentException e) {
				System.out.println("Exception: " + e);
			}
			catch (org.jsoup.HttpStatusException e) {

			}
			catch (MalformedURLException e) {

			}
			catch (SocketTimeoutException e) {

			}
			catch (org.jsoup.UnsupportedMimeTypeException e) {

			}
		}
	}
}














