package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Stopwords;

public class Documents {

	ArrayList<Document> docs;
	Map<String, Integer> termToIndexMap;
	ArrayList<String> indexToTermMap;
	Map<String, Integer> termCountMap;

	public Documents() {
		docs = new ArrayList<Document>();
		termToIndexMap = new HashMap<String, Integer>();
		indexToTermMap = new ArrayList<String>();
		termCountMap = new HashMap<String, Integer>();
	}

	public void readDocs(String filename) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(filename)), "UTF8"));
			StringTokenizer tokenizer = null;
			String line = null;
			while ((line = reader.readLine()) != null) {
				tokenizer = new StringTokenizer(line, ",");
				ArrayList<String> tokens = new ArrayList<>();
				while (tokenizer.hasMoreTokens()) {
					tokens.add(tokenizer.nextToken());
				}
				this.docs.add(new Document(tokens, termToIndexMap, indexToTermMap, termCountMap));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Document {
		
		int[] docWords;

		public Document(ArrayList<String> tokens, Map<String, Integer> termToIndexMap,
				ArrayList<String> indexToTermMap, Map<String, Integer> termCountMap) {
			
			ArrayList<String> words = tokens;
			for (int i = 0; i < words.size(); i++) {
				if (Stopwords.isStopword(words.get(i)) || isNoiseWord(words.get(i))) {
					words.remove(i);
					i--;
				}
			}
			// Transfer word to index
			this.docWords = new int[words.size()];
			for (int i = 0; i < words.size(); i++) {
				String word = words.get(i);
				if (!termToIndexMap.containsKey(word)) {
					int newIndex = termToIndexMap.size();
					termToIndexMap.put(word, newIndex);
					indexToTermMap.add(word);
					termCountMap.put(word, new Integer(1));
					docWords[i] = newIndex;
				} else {
					docWords[i] = termToIndexMap.get(word);
					termCountMap.put(word, termCountMap.get(word) + 1);
				}
			}
			words.clear();
		}

		public boolean isNoiseWord(String string) {
			// TODO Auto-generated method stub
			string = string.toLowerCase().trim();
			Pattern MY_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");
			Matcher m = MY_PATTERN.matcher(string);
			// filter @xxx and URL
			if (string.matches(".*www\\..*") || string.matches(".*\\.com.*")
					|| string.matches(".*http:.*"))
				return true;
			if (!m.matches()) {
				return true;
			} else
				return false;
		}
	}
}
