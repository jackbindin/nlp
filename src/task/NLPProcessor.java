package task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

public class NLPProcessor {
	//generates an array of sentences that are within the string
	public String[] getSentenaces(String text) throws FileNotFoundException, IOException {
		try (InputStream modelIn = new FileInputStream("en-sent.bin")) {
			  SentenceModel model = new SentenceModel(modelIn);
			  SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			  return sentenceDetector.sentDetect(text);
			}
		
	}
	//generates an array of words that are within the string
	public String[] getWords(String text) throws FileNotFoundException, IOException {
		try (InputStream modelIn = new FileInputStream("en-token.bin")) {
			  TokenizerModel model = new TokenizerModel(modelIn);
			  Tokenizer tokenizer = new TokenizerME(model);
			  return tokenizer.tokenize(text);
		}
	}
	//generates an array of names that are within the string
	public Span[] getNames(String[] text) throws FileNotFoundException, IOException {
		try (InputStream modelIn = new FileInputStream("en-ner-person.bin")) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(model);
			return nameFinder.find(text);
		}
	}
	//generates an array of locations that are within the string
	public Span[] getLocations(String[] text) throws FileNotFoundException, IOException{
		try (InputStream modelIn = new FileInputStream("en-ner-location.bin")) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME locationFinder = new NameFinderME(model);
			return locationFinder.find(text);
		}
	}

	
}
