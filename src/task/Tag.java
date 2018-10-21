package task;

public class Tag {
    private String name;
    private File file;
    private int numOfTags;
    public Tag(String nameIn, File fileIn){
        setName(nameIn);
        setFile(fileIn);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }




}
