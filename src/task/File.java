package task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class File {
    private String name;
    private String filePath;
    private String text;
    private List<Tag> tags = new LinkedList();
    TagGenerator tg = new TagGenerator();


    public File(String nameIn, String filePathIn) throws IOException {
        setName(nameIn);
        setFilePath(filePathIn);
        //reads the text within the file
        text = FileReader.read(filePathIn);
        //this adds each tag that is found within this file using the TagGenerator class
        tg.generate(this);
    }

    private void setFilePath(String filePathIn) {
        filePath = filePathIn;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(List<Tag> tagsIn){
        tags=tagsIn;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getText(){
        return text;
    }
}
