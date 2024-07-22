//package innolab.pallicare.db.testimplementation;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import innolab.pallicare.R;
//import innolab.pallicare.db.entities.MeasurementScale;
//
//public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {
//
//    class TestViewHolder extends RecyclerView.ViewHolder {
//        private final TextView testItemView;
//
//        private TestViewHolder(View itemView) {
//            super(itemView);
//            testItemView = itemView.findViewById(R.id.textView);
//        }
//    }
//
//
//    private final LayoutInflater mInflater;
//    private List<MeasurementScale> mTests; // Cached copy of words
//
//    public TestListAdapter(Context context) {
//        mInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
//        return new TestViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(TestViewHolder holder, int position) {
//        if (mTests != null) {
//            MeasurementScale current = mTests.get(position);
//            holder.testItemView.setText(current.toString());
//        } else {
//            // Covers the case of data not being ready yet.
//            holder.testItemView.setText("No weight");
//        }
//    }
//
//    public void setContent(List<MeasurementScale> m) {
//        mTests = m;
//        notifyDataSetChanged();
//    }
//
//    // mWords has not been updated (means initially, it's null, and we can't return null).
//    @Override
//    public int getItemCount() {
//        if (mTests != null)
//            return mTests.size();
//        else return 0;
//    }
//}
