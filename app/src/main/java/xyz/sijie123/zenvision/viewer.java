package xyz.sijie123.zenvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class viewer extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer);

    //    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TextView status = (TextView) findViewById(R.id.status);
        final TextView name = (TextView) findViewById(R.id.name_full);
        TextView longt = (TextView) findViewById(R.id.long_full);
        TextView lat = (TextView) findViewById(R.id.lat_full);
        ImageView pic = (ImageView) findViewById(R.id.pic);

        Button acc = (Button) findViewById(R.id.accept);
        Button rej = (Button) findViewById(R.id.reject);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        //    status.setText(extras.getInt("status"));
            name.setText(extras.getString("name"));
            if (extras.getString("hidden") == "1") {
                acc.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = "http://128.199.155.97/hardcode.php";

                        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        // Result handlingv
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                // Error handling
                                System.out.println("Something went wrong!");
                                error.printStackTrace();

                            }
                        });


// Add the request to the queue
                        Volley.newRequestQueue(getBaseContext()).add(stringRequest);
                    }
                });
            }
            else {
                acc.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(getBaseContext(), submitter.class);
                        i.putExtra("status", 1);
                        i.putExtra("name", name.getText());
                        i.putExtra("longt", 103.8);
                        i.putExtra("lat", 1.3);
                        startActivity(i);
                    }
                });



            }
            longt.setText("Longitude: " + String.valueOf(extras.getDouble("longt")));
            lat.setText("Latitude: " +String.valueOf(extras.getDouble("lat")));
            byte[] decodedString = Base64.decode(extras.getString("pic"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            pic.setImageBitmap(decodedByte);
        }





        rej.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        });

/*
        String url = "http://httpbin.org/get?site=code&network=tutsplus";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            response = response.getJSONObject("args");

                            List<Request> requests = new ArrayList<>();

                            String name = response.getString("name");
                            Double longi = response.getDouble("longi");
                            Double lati = response.getDouble("lati");
                            int status = response.getInt("status");
                            byte[] decodedString = Base64.decode(response.getString("pic"), Base64.DEFAULT);
                            Bitmap pic = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            requests.add(new Request(name, longi, lati, status, pic));

                            update(requests);

                     //       System.out.println("Site: "+site+"\nNetwork: "+network);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


// Add the request to the queue
        Volley.newRequestQueue(this).add(jsonRequest);
*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_public) {
            Intent i = new Intent(getBaseContext(), AllReq.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_my) {
            Intent i = new Intent(getBaseContext(), my.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_main) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
