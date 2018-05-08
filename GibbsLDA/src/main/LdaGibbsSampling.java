package main;

import java.io.File;
import java.io.IOException;

import com.FileUtil;

import conf.PathConfig;


public class LdaGibbsSampling {
	
	public static class modelparameters {
		float alpha = 0.5f; //usual value is 50 / K
		float beta = 0.1f;//usual value is 0.1
		int topicNum = 100;
		int iteration = 100;
		int saveStep = 10;
		int beginSaveIters = 50;
	}
	
	public enum parameters{
		alpha, beta, topicNum, iteration, saveStep, beginSaveIters;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String resultPath = PathConfig.LdaResultsPath;
		
		Documents docSet = new Documents();
		docSet.readDocs("/Users/ashish/Downloads/Geo_Tweets_2016/tweets_news_text_only.txt");
		System.out.println("wordMap size " + docSet.termToIndexMap.size());
		FileUtil.mkdir(new File(resultPath));
		LdaModel model = new LdaModel(0.1, 0.01, 500, 25, 100, 100);
		System.out.println("1 Initialize the model ...");
		model.initializeModel(docSet);
		System.out.println("2 Learning and Saving the model...");
		model.inferenceModel(docSet);
		System.out.println("3 Output the final model...");
		model.saveIteratedModel(500, docSet);
		System.out.println("Done!");
		System.out.println("Perpexity = " + model.logPerplexity());
	}
}

