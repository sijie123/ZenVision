package xyz.sijie123.zenvision;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


class Request {
    String name;
    Double longi;
    Double lati;
    int status;
    Bitmap photo;
    int type;

    Request(String name, Double longi, Double lati, int status, Bitmap photo, int type) {
        this.name = name;
        this.longi = longi;
        this.lati = lati;
        this.status = status;
        this.photo = photo;
        this.type = type;
    }

}

class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {


    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView name,t;
        ImageView photo;
        ImageView bar;
        Context context;

        PersonViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            bar = (ImageView)itemView.findViewById(R.id.imageView);
            t = (TextView)itemView.findViewById(R.id.hidden);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            name = (TextView)itemView.findViewById(R.id.name);
            photo = (ImageView)itemView.findViewById(R.id.imageView2);
            context =  itemView.getContext();
         //   personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, viewer.class);
            i.putExtra("status", 1);
            i.putExtra("name", name.getText());
            i.putExtra("hidden", String.valueOf(t.getText()));
            i.putExtra("longt", 103.8);
            i.putExtra("lat", 1.3);

            Drawable myDrawable = photo.getDrawable();
            Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            myLogo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            i.putExtra("pic", encodedImage);

            context.startActivity(i);
        }
    }

    List<Request> requests;

    RVAdapter(List<Request> requests){
        this.requests = requests;
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    //TODO
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.name.setText(requests.get(i).name);
        Log.w("test", requests.get(i).name);
        //personViewHolder.bar.setImageDrawable(R.color.colorPrimary);
        personViewHolder.photo.setImageBitmap(requests.get(i).photo);
      //  personViewHolder.bar.setImageDrawable();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}

public class MainActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_main);





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), requester.class);
                startActivity(i);
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });




        String url = "http://128.199.155.97/getreq.php?type=1&user=1";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            List<Request> requests = new ArrayList<>();
                            JSONArray arr = response.getJSONArray("data");

                            for( int i = 0; i < arr.length(); i++) {
                                JSONObject x = arr.getJSONObject(i);
                                String name = x.getString("name");
                                Double longi = x.getDouble("lon");
                                Double lati = x.getDouble("lat");
                                int status = x.getInt("accepted");
                                byte[] decodedString = Base64.decode(x.getString("photo"), Base64.DEFAULT);
                                Bitmap pic = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                requests.add(new Request(name, longi, lati, status, pic, 1));
                            }


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

    }

    public void update(List<Request> rq) {


        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        List<Request> requests;


        RVAdapter adapter = new RVAdapter(rq);
        mRecyclerView.setAdapter(adapter);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        long highScore = pref.getInt("pts", 50);

        TextView pts = (TextView) findViewById(R.id.pts_lb);
        pts.setText(String.valueOf(highScore) + " Credits");
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
