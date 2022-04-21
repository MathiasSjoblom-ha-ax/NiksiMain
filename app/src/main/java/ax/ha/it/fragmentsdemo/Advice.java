package ax.ha.it.fragmentsdemo;

public class Advice {

    String content;
    String author;
    String category;

    public Advice (String content, String author, String category) {
        this.content = content;
        this.author = author;
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
