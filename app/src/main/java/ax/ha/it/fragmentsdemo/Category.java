package ax.ha.it.fragmentsdemo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "category_table")
public class Category {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "categoryKey")
    public String categoryWord;
    public Category(@NonNull int key) {
        if(key == 0) {
            this.categoryWord = "Lifestyle";
        }
        if(key == 1) {
            this.categoryWord = "Technology";
        }
        if(key == 2) {
            this.categoryWord = "Miscellaneous";
        }
    }

    @NonNull
    public String getCategoryWord() {
        return categoryWord;
    }
}
