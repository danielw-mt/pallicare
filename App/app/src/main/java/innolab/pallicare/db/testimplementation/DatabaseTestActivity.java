//package innolab.pallicare.db.testimplementation;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import innolab.pallicare.R;
//import innolab.pallicare.db.entities.MeasurementScale;
//import innolab.pallicare.viewmodel.TestViewModel;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import java.util.Date;
//import java.util.List;
//
//public class DatabaseTestActivity extends AppCompatActivity {
//
//    private TestViewModel mTestViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_database_test);
//
////        RecyclerView recyclerView = findViewById(R.id.test_recycler_view);
////        final TestListAdapter adapter = new TestListAdapter(this);
////        recyclerView.setAdapter(adapter);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
////        mTestViewModel = new ViewModelProvider(this).get(TestViewModel.class); ??
//        mTestViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TestViewModel.class);
//
//        mTestViewModel.getAllScaleMeasurements().observe(this, measurementScales -> {
//
//            Log.d("DATABASE_TEST", measurementScales.toString());
//
//            TextView t = (TextView) findViewById(R.id.textView3);
//            t.setText(measurementScales.toString());
////                adapter.setContent(measurementScales);
//
//        });
////        db = PallicareRoomDatabase.getDatabase(getApplicationContext());
////        repo = PallicareRepository.getDatabase(db);
//
//        // TODO remove testing
//        String db_path = getApplicationContext().getDatabasePath("pallicare_database").getPath();
//        Log.d("DATABASE_TEST", "db path: " + db_path);
////
////        LiveData<List<MeasurementScale>> weights = repo.getAllScaleMeasurementsByPatientId(10);
////
//
//
////        repo.insertMeasurementScale(100, 50.5f);
////        weights = repo.getAllScaleMeasurements();
////        Log.d("DATABASE_TEST", weights.toString());
//
////        TextView t = (TextView) findViewById(R.id.textView3);
////        t.setText(weights.toString());
//
//        test();
//    }
//
//
//    public void test() {
//
//        // test bp measurement
//    MeasurementScale ms_1 = new MeasurementScale(90, 1, 1, 50, new Date(System.currentTimeMillis()), 900);
////    MeasurementScale ms_2 = new MeasurementScale(90000, 1, 1, 70, new Date(System.currentTimeMillis()), 800);
////    MeasurementScale ms_3 = new MeasurementScale(1000, 1, 1, 30, new Date(System.currentTimeMillis()), 700);
//
//    mTestViewModel.addScaleMeasurement(ms_1);
////    mTestViewModel.insertMeasurementScale(ms_2);
////    mTestViewModel.insertMeasurementScale(ms_3);
//
//    }
//}
