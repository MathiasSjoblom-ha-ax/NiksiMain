package ax.ha.it.fragmentsdemo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "category_table")
public class Category {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "category")
    @SerializedName(value = "name")
    public String categoryWord;

    public Category(@NonNull String categoryWord) {
        this.categoryWord = categoryWord;
    }

    @NonNull
    public String getCategory() {
        return categoryWord;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryWord='" + categoryWord + '\'' +
                '}';
    }
}
