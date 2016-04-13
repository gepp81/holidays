package ar.com.gep.holydays;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

import ar.com.gep.holydays.services.HolidayDTO;
import ar.com.gep.holydays.services.NoLaborableAPI;
import ar.com.gep.holydays.util.HolidayStorage;
import ar.com.gep.holydays.util.WeekCounter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<List<HolidayDTO>> {


    private Map<String, Object> holeyDates;

    /**
     * @param dayOfMonth
     * @param month
     * @param year
     * @return
     */
    private DateTime getDateTime(int dayOfMonth, int month, int year) {
        return new DateTime(year, month + 1, dayOfMonth, 0, 0);
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    private WeekCounter countWeekEndsDays(DateTime startDate, DateTime endDate) {
        WeekCounter week = new WeekCounter();
        Map<Long, Object> holyDays = HolidayStorage.getHoleyDates(startDate, endDate, this);
        week.setHolidays(holyDays);
        week.count(startDate, endDate);
        return week;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchHolydaysForThisYear();

        Button search = (Button) findViewById(R.id.button);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent();
                DatePicker before = (DatePicker) parent.findViewById(R.id.datePicker);
                DatePicker after = (DatePicker) parent.findViewById(R.id.datePicker2);
                DateTime beforeTime = getDateTime(before.getDayOfMonth(), before.getMonth(), before.getYear());
                DateTime afterTime = getDateTime(after.getDayOfMonth(), after.getMonth(), after.getYear());

                if (beforeTime.isBefore(afterTime)) {
                    showDays(parent, beforeTime, afterTime);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.invalidDate, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param view
     * @param beforeTime
     * @param afterTime
     */
    private void showDays(View view, DateTime beforeTime, DateTime afterTime) {
        WeekCounter counter = countWeekEndsDays(beforeTime, afterTime);
        StringBuilder builder = new StringBuilder();
        CheckBox box = (CheckBox) view.findViewById(R.id.checkSabado);
        int total = 0;
        if (box.isChecked()) {
            builder.append("Sábados: ").append(counter.getSaturday()).append(". ");
            total += counter.getSaturday();
        }
        box = (CheckBox) view.findViewById(R.id.checkDomingo);
        if (box.isChecked()) {
            builder.append("Domingos: ").append(counter.getSunday()).append(". ");
            total += counter.getSunday();
        }
        box = (CheckBox) view.findViewById(R.id.checkFeriado);
        if (box.isChecked()) {
            builder.append("Feriados: ").append(counter.getHoliday()).append(". ");
            total += counter.getHoliday();
        }
        if (total > 0) {
            builder.append("TOTAL: ").append(total);
        }
        Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchHolydaysForThisYear() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NoLaborableAPI.NO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NoLaborableAPI api = retrofit.create(NoLaborableAPI.class);

        Call<List<HolidayDTO>> call = api.getHolyDays(String.valueOf(DateTime.now().getYear()));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<List<HolidayDTO>> response, Retrofit retrofit) {
        HolidayStorage.saveHolidayYear(response, this);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e("Holydays", "error", t);
    }


}
