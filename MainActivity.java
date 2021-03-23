package e.user.gumibusremake;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskManager tm = new TaskManager();

    private static ArrayList<Bus> busList;
    static ArrayList<BusStop> busStopList;
    ArrayList<ArriveData> arriveDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ReadTextFile("C:\\Users\\user\\Desktop\\GumiBusRemake\\app\\src\\main\\res\\test.txt");

        try {
            tm.getAllStopList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }
    public static void settingStopList(ArrayList<BusStop> tmpList){
        busStopList = tmpList;

        BusNumFragment myBusNum = new BusNumFragment();
        Bundle myBundle = new Bundle();
        myBundle.putSerializable("stopList", busStopList);
        myBusNum.setArguments(myBundle);
    }
    /*
    public void ReadTextFile(String path){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream is = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line="";
            while((line=reader.readLine())!=null){
                strBuffer.append(line+"\n");
            }

            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(strBuffer.toString());
    }
    */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch(menuItem.getItemId()){
                        case R.id.navigation_favorites :
                            selectedFragment = new FavoritesFragment();
                            break;

                        case R.id.navigation_busName :
                            selectedFragment = new BusNameFragment();
                            break;
                        case R.id.navigation_busNum :
                            selectedFragment = new BusNumFragment();
                            break;
                        case R.id.navigation_findWay :
                            selectedFragment = new FindWayFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
