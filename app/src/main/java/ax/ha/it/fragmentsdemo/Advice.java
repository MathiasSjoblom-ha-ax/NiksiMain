package ax.ha.it.fragmentsdemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "advice_table")
public class Advice implements Serializable {

    String content;
    String author;
    String category;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "myKey")
    public int PrimaryKey;

    public Advice (@NonNull String content, String author, String category) {
        this.content = content;
        this.author = author;
        this.category = category;
    }

    public int getPrimaryKey() { return PrimaryKey; }

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
