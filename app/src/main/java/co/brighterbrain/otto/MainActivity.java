package co.brighterbrain.otto;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import junit.framework.Test;

import static android.R.attr.button;
import static co.brighterbrain.otto.R.*;
import static co.brighterbrain.otto.R.layout.*;

public class MainActivity extends AppCompatActivity {
    private static Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        if (savedInstanceState==null){
            getFragmentManager().beginTransaction().
                    add(id.container,new PlaceholderFragment()).commit();
        }
        bus = new Bus(ThreadEnforcer.MAIN);
        bus.register(this);
    }
    @Subscribe
    public void getMessage(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if (id==R.id.action_settings){
            TestData t = new TestData();
            t.message="Hello from activity";
            bus.post(t);
        return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class TestData {
        public String message;
    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(layout.fragment_main, container, true);
            View button = rootView.findViewById(id.fragmentbutton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bus.post("Hello from the fragment");
                }
            });
            bus.register(this);
            return  rootView;
            }
        @Subscribe
        public void getMessage(MainActivity.TestData data)
        {
            Toast.makeText(getActivity(),data.message , Toast.LENGTH_LONG).show();
          }
        }

         @Produce
    public  String produceEvent(){
             return "starting Event";
         }
    }


