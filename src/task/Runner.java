package task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import mongoDB.DocumentServices;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.CategoryChart.*;

import javax.swing.text.*;

public class Runner {
	public static void main (String[] args) throws BadLocationException, IOException {
	    /*add documents to mongo db*/
		//DocumentServices.addDocument(new File("text6","text6.txt"));

        /*open all documents containing the tag entered*/
        //DocumentServices.openDocumentsByTag("Turkey");

        /*open new window containing all sentences containing the word enetered*/
        //DocumentServices.showSentencesContaining("Donald Trump");

        /*creates a bar chart of tags usage amounts*/
		//ChartCreator.tagChart();

        /*performs a similarity check on the file entered*/
        //DocumentServices.plagiarismChecker(new File("text5","text5.txt").getText());
	}
}
