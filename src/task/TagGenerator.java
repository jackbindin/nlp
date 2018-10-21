package task;

import opennlp.tools.util.Span;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TagGenerator {
    public void generate(File file) throws IOException {
        //accessing nlp class:
        NLPProcessor pr = new NLPProcessor();
        //reading text of file passed in:
        String text = file.getText();
        //getting words from nlp class:
        String[] words = pr.getWords(text);
        //getting names from nlp class:
        Span[] namesIn = pr.getNames(words);
        //getting locations from nlp class:
        Span[] locationsIn = pr.getLocations(words);
        //creating empty list of tags:
        List<Tag> tags = new LinkedList<>();

        //adding list of tags to the file passed in:
        //for names
        for(Tag tag:tagger(namesIn,words,file)){
            tags.add(tag);
        }
        //for locations
        for(Tag tag:tagger(locationsIn,words,file)){
            tags.add(tag);
        }
        file.setTags(tags);
    }

    public List<Tag> tagger(Span[] tagTypes, String[] words, File file){
        // creatign empty tag list
        List<Tag> tags = new LinkedList<>();
        //looping through each tag passed in
        for(Span tagIn:tagTypes) {
            StringBuilder builder = new StringBuilder();
            //getting name from array
            for(int i = tagIn.getStart(); i<tagIn.getEnd();i++) {
                //adding space between start word and end word of tag
                builder.append(words[i]).append(" ");
            }
            //removing space at the end of the name
            builder.deleteCharAt(builder.length()-1);
            //boolean to check to see if the current tag has already been set
            boolean used = false;
            //goes through each tag to see if it has already been used for this file:
            for(Tag tag:tags){
                if(tag.getName().equals(builder.toString())){
                    used = true;
                }
            }
            //if the tag has not been used, add it to the list of tags for this file:
            if(!used){
                Tag tagAdded = new Tag(builder.toString(),file);
                tags.add(tagAdded);
            }
        }
        return tags;
    }

}
