package ninh.main.thefirstgame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    ImageView curView = null, laterView = null;
    TextView tvPoint;
    private int countPair = 0;
    final int[] drawable = new int[] {
            R.drawable.img0,
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
    };
    int[] pos = {0,1,2,3,4,5,0,1,2,3,4,5};
    int currentPos = -1;
    Button btnStart;
    TextView tvTime;
    private CountDownTimer countDownTimer;
    private long timeLeftMiliSecond = 120000;
    Boolean start = false;
    void randomPos(){
        Random rand = new Random();
        for (int i = 0; i < pos.length; i++) {
            int randomIndexToSwap = rand.nextInt(pos.length);
            int temp = pos[randomIndexToSwap];
            pos[randomIndexToSwap] = pos[i];
            pos[i] = temp;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.button);
        tvTime = findViewById(R.id.tvTime);
        tvPoint = findViewById(R.id.tvPoint);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = (GridView)findViewById(R.id.gridView);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomPos();
                start = true;
                startTimer();
                gridView.setAdapter(imageAdapter);
            }
            private void startTimer(){
                countDownTimer = new CountDownTimer(120000,1000) {
                    @Override
                    public void onTick(long l) {
                        long minutes = ((l/1000)%3600)/60;
                        long second = (l/1000)%60;
                        String timeFormat = String.format(Locale.getDefault(),"%02d:%02d", minutes,second);
                        tvTime.setText(timeFormat);
                        start = true;
                    }

                    @Override
                    public void onFinish() {
                        start = false;
                        tvTime.setText("00:00");
                        Toast.makeText(MainActivity.this, "Time's up!!", Toast.LENGTH_SHORT).show();

                    }
                }.start();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                laterView = (ImageView) view;
                if (currentPos < 0 ) {
                    currentPos = position;
                    curView = (ImageView) view;
                    curView.setImageResource(drawable[pos[position]]);
                }
                else {
                    if (currentPos == position) {
                        Toast.makeText(MainActivity.this, "Heyy!!!", Toast.LENGTH_SHORT).show();
                    } else{
                        // Not matching!
                        if (pos[currentPos] != pos[position]) {
                            laterView.setImageResource(drawable[pos[position]]);
                            Handler handler =  new Handler();
                            Runnable delayImg = new Runnable() {
                                public void run() {
                                    curView.setImageResource(R.drawable.ava);
                                    laterView.setImageResource(R.drawable.ava);
                                }
                            };
                            handler.postDelayed(delayImg, 1000);
                        } else {
                            // Matching!
                            laterView.setImageResource(drawable[pos[position]]);
                            countPair++;
                            tvPoint.setText(countPair+"");
                            //Check win
                            if (countPair == 6 && start == true) {
                                cancelTimes();
                                Toast.makeText(MainActivity.this, "You Win!", Toast.LENGTH_LONG).show();
                            } else {
                                if (countPair != 6 && start == false)
                                Toast.makeText(MainActivity.this, "GAME OVER!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        currentPos = -1;
                    }
                }
            }
            private void cancelTimes() {
                countDownTimer.cancel();
            }
        });

    }
}