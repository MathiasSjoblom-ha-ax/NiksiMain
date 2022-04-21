package ax.ha.it.fragmentsdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ax.ha.it.fragmentsdemo.Advice;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    /*
    private final String[] contentItems;
    private final String[] authorItems;
    private final String[] categoryItems;

     */
    private final Advice[] advice;


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
    }

    public CustomAdapter(Advice[] advice) {
        this.advice = advice;
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
        holder.content.setText(advice[position].getContent());
        holder.author.setText(advice[position].getAuthor());
        holder.category.setText(advice[position].getCategory());
    }

    @Override
    public int getItemCount() {
        return advice.length;
    }
}
