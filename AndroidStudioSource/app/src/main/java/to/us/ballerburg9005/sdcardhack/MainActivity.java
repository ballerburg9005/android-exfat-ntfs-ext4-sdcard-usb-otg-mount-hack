package to.us.ballerburg9005.sdcardhack;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runcommand();
            }
        });
        runcommand();
    }
    public void runcommand()
    {
        Toast.makeText(getApplicationContext(), "started, please wait 15 seconds ...", Toast.LENGTH_LONG).show();
        try {
            Process p = Runtime.getRuntime().exec("su -M -c \"/system/bin/mountsd\"");
            p.waitFor();
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
        }
    }
}