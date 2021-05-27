package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.setting:
                Intent i = new Intent(AboutUs.this, Setting.class);
                startActivity(i);
                return true;
            case R.id.aboutUs:
                Intent i1 = new Intent(AboutUs.this, AboutUs.class);
                startActivity(i1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void logout() {
        //FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(),FirstPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
