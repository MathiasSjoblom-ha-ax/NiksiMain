package ax.ha.it.fragmentsdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private final String[] contentItems;
    private final String[] authorItems;
    private final String[] categoryItems;


    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        private final TextView content;
        private final TextView author;
        private final TextView category;

        public CustomViewHolder(View view) {
            super(view);

            content = (TextView) view.findViewById(R.id.contentItem);
            author = (TextView) view.findViewById(R.id.authorItem);
            category = (TextView) view.findViewById(R.id.categoryItem);
        }

        public TextView getContent() {
            return content;
        }
        public TextView getAuthor() {
            return author;
        }
        public TextView getCategory() {
            return category;
        }
    }

    public CustomAdapter(String[] contentItems, String[] authorItems, String[] categoryItems) {
        this.contentItems = contentItems;
        this.authorItems = authorItems;
        this.categoryItems = categoryItems;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.content.setText(contentItems[position]);
        holder.author.setText(authorItems[position]);
        holder.category.setText(categoryItems[position]);
    }

    @Override
    public int getItemCount() {
        return contentItems.length;
    }
}
