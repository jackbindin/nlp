package mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import task.*;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.IOException;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.regex;

public class DocumentServices {
    public static Connection con = new Connection();
    public static MongoCollection collection = con.getDatabase();

    //function for adding documents to mongoDB
    public static void addDocument(File file){
        //empty string list to store tags of the file being passed in
        List<String> tagNames = new LinkedList<>();
        //loops through each tag in the file, gets its name and then adds it to the tag list
        for(Tag tag:file.getTags()){
            tagNames.add(tag.getName());
        }
        //coverting file passed in to document
        Document doc = new Document("name",file.getName())
                .append("text",file.getText())
                //adding list of tag names generated to the document
                .append("tags",tagNames);
        //adding document to database
        collection.insertOne(doc);
        System.out.println("Document added");
    }

    //function that queries documents in the database on the attribute and the value [assed in
    public static List<Document> queryDocuments(String attribute, String value){
        //empty list of documents
        List<Document> docs = new LinkedList<>();
        //creating search criteria
        Document search = new Document(attribute,value);
        //searching the database for documents that match the search criteria
        MongoCursor<Document> mongoDocs = collection.find(search).iterator();
        //adding each document found to the list of documents
        while(mongoDocs.hasNext()){
            docs.add(mongoDocs.next());
        }
        return docs;
    }

    //function that open documents that have the tag passed in as a parameter
    public static void openDocumentsByTag(String tagName) throws BadLocationException {
        //querying the database for docs with tag equal to the parameter passed in
        List<Document> docs = queryDocuments("tags",tagName);
        //looping through each doc
        for(Document doc:docs){
            //setting up output screen
            OutputWindow ow = new OutputWindow();
            //getting text of current doc
            ow.outputText.setText(doc.getString("text"));
            //setting up highlighter
            Highlighter highlighter = ow.outputText.getHighlighter();
            DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                    new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            //highlights output where the text equals the parameter passed in
            for (int index = 0; index + tagName.length() < ow.outputText.getText().length(); index++) {
                String match = ow.outputText.getText(index, tagName.length());
                if (tagName.equals(match)) {
                    highlighter.addHighlight(index, index +     tagName.length(), highlightPainter);
                }
            }
            ow.setTitle(doc.getString("name"));
            ow.setSize(500,500);
            ow.setVisible(true);
        }
    }

    //function that opens a screen that shows all sentences within the db that contains the string passed in
    public static void showSentencesContaining(String query) throws IOException {
        NLPProcessor nlp = new NLPProcessor();
        //finding all docs that contain parameter
        MongoCursor<Document> mongoDocs = collection.find(regex("text", ".*" + Pattern.quote(query) + ".*")).iterator();
        //emtpy list of sentences
        List<String> sentences = new LinkedList<>();
        while(mongoDocs.hasNext()){
            //getting all sentences from docs generated
            String[] sentencesFound = nlp.getSentenaces(mongoDocs.next().getString("text"));
            //looping through each sentence
            for(String sentence:sentencesFound){
                //if the sentence contains the parameter, adding to the list
                if(sentence.contains(query)){
                    sentences.add(sentence);
                }
            }
        }
        //setting up screen
        OutputWindow ow = new OutputWindow();
        ow.setTitle(query);
        for(String sentence:sentences){
            ow.outputText.setText(ow.outputText.getText()+sentence+"\n"+"\n");
        }
        ow.setSize(500,500);
        ow.setVisible(true);
    }

    public static int tagAmount(String tag){
        return queryDocuments("tags",tag).size();
    }
    //function that lists all tags within the database so that a tag chart can be generated.
    public static List<String> allTags(){
        List<String> tags = new LinkedList<>();
        MongoCursor<Document> mongoDocs = collection.find().iterator();
        while(mongoDocs.hasNext()){
            List<String> tagsIn = (List<String>) mongoDocs.next().get("tags");
            for(String tagIn:tagsIn){
                boolean found = false;
                for(String tag:tags){
                    if(tag.equals(tagIn)){
                        found=true;
                    }
                }
                if(!found){
                    tags.add(tagIn);
                }
            }
        }
        return tags;
    }

    public static void plagiarismChecker(String text) throws IOException, BadLocationException {
        NLPProcessor nlp = new NLPProcessor();
        //getting number of sentences from the text passed in
        double numOfSentences = nlp.getSentenaces(text).length;
        double similarSentencesCount = 0;
        //getting sentences from the text passed in
        String[] sentencesIn = nlp.getSentenaces(text);
        //creating an empty list for similar sentences
        List<String> similarSentences = new LinkedList<>();
        for(String sentence:sentencesIn){
            MongoCursor<Document> mongoDocs = collection.find(regex("text", ".*" + Pattern.quote(sentence) + ".*")).iterator();
            while(mongoDocs.hasNext()){
                for(String sentenceFound: nlp.getSentenaces(mongoDocs.next().getString("text"))){
                    if(sentence.equals(sentenceFound)){
                        //adding to variables if similar sentence is found
                        similarSentencesCount++;
                        similarSentences.add(sentence);
                    }
                }
            }
        }
        //getting similarity as a percentage.
        double total = (similarSentencesCount/numOfSentences)*100;
        //setting up screen
        OutputWindow ow = new OutputWindow();
        Highlighter highlighter = ow.outputText.getHighlighter();
        DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        //setting up text for screen
        String textBuilder="";
        for(String sentence:sentencesIn){
            textBuilder=textBuilder+sentence;
            for(String similarSentence:similarSentences){
                //every similar sentence has a quote of what document the text is originally from.
                if(sentence.equals(similarSentence)){
                    MongoCursor<Document> mongoDocs = collection.find(regex("text", ".*" + Pattern.quote(similarSentence) + ".*")).iterator();
                    textBuilder=textBuilder+"("+mongoDocs.next().getString("name")+")";
                }
            }
            textBuilder=textBuilder+"\n";
        }
        //highlighting each similar sentence.
        ow.outputText.setText(textBuilder+"\n"+"\n"+total+"% similarity");
        for(String sentence:similarSentences){
            for (int index = 0; index + sentence.length() < ow.outputText.getText().length(); index++) {
                String match = ow.outputText.getText(index, sentence.length());
                if (sentence.equals(match)) {
                    highlighter.addHighlight(index, index +     sentence.length(), highlightPainter);
                }
            }
        }
        ow.setTitle("plagiarism checker");
        ow.setSize(500,500);
        ow.setVisible(true);
    }

}
