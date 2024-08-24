package com.example.civiladvocacyapplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class CivicAPI
{
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static final String API_KEY = "AIzaSyAUdPBzQAaN8Evs2RSeRW_SCxlsY_1EnFk",
            PRE_KEY_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=",
            PRE_LOC_URL = "&address=";
    private static String URL = PRE_KEY_URL + API_KEY + PRE_LOC_URL, name = "", office = "", party = "",
            user_street = "", user_city = "", user_state = "", user_zip = "", userNormalizedLocation = "";



    // The actual data is acquired and parsed here
    public static void callAPI(MainActivity mainActivity, String queriedLocation)
    {
        // Needed to make activity-field static due to activity-recognition-issues
        CivicAPI.mainActivity = mainActivity;

        // Append to URL the queried location
        URL += queriedLocation;

        queue = Volley.newRequestQueue(CivicAPI.mainActivity);
        Response.Listener <JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                // Clear recycler view when querying new location
                mainActivity.alp.clear();
                try
                {
                    JSONObject initialQuery = new JSONObject(response.toString());
                    JSONArray jsonArrayOffices = initialQuery.getJSONArray("offices");
                    JSONArray jsonArrayOfficials = initialQuery.getJSONArray("officials");
                    JSONObject jsonObjectOffice;
                    JSONArray jsonArrayOfficialIndices;
                    JSONObject politician;
                    JSONObject jsonObjectAddress;
                    JSONArray jsonArrayChannels;
                    JSONObject channel;

                    // Displaying normalized form of user's current location / queried location
                    JSONObject jsonObjectNormalizedInput = initialQuery.getJSONObject("normalizedInput");
                    user_street = jsonObjectNormalizedInput.getString("line1");
                    user_city = jsonObjectNormalizedInput.getString("city");
                    user_state = jsonObjectNormalizedInput.getString("state");
                    user_zip = jsonObjectNormalizedInput.getString("zip");

                    // Format the string to display properly
                    String placeholder = ", ";
                    if (user_city.isEmpty() || user_state.isEmpty())
                        placeholder = "";
                    userNormalizedLocation = user_street + " " + user_city
                            + placeholder + user_state + " " + user_zip;
                    MainActivity.setNormalizedInput(userNormalizedLocation);

                    // Parse each office
                    int i = 0, officesLen = jsonArrayOffices.length();
                    while (i < officesLen)
                    {
                        jsonObjectOffice = jsonArrayOffices.getJSONObject(i);
                        office = jsonObjectOffice.getString("name");
                        jsonArrayOfficialIndices = jsonObjectOffice.getJSONArray("officialIndices");

                        // Parse each politician in each office
                        int j = 0, officialIndicesLen = jsonArrayOfficialIndices.length();
                        while (j < officialIndicesLen)
                        {
                            politician = jsonArrayOfficials.getJSONObject(jsonArrayOfficialIndices.getInt(j));
                            name = politician.getString("name");
                            party = politician.getString("party");

                            // Re-initialize fields for every politician
                            String image = "", facebook = "", twitter = "", youtube = "", state = "",
                                    city = "", zip = "", line1 = "", line2 = "", address = "",
                                    phone = "", email = "", website = "";

                            // Basic checks since every politician may or may not hold data
                            if (politician.has("photoUrl"))
                                image = politician.getString("photoUrl");

                            if (politician.has("address"))
                            {
                                jsonObjectAddress = politician.getJSONArray("address").getJSONObject(0);
                                state = jsonObjectAddress.getString("state");
                                city = jsonObjectAddress.getString("city");
                                zip = jsonObjectAddress.getString("zip");
                                line1 = jsonObjectAddress.getString("line1");
                                line2 = jsonObjectAddress.optString("line2");
                                address = line1 + " " + line2 + " " + city + ", " + state + " " + zip;
                            }

                            if (politician.has("urls"))
                                website = politician.getJSONArray("urls").get(0).toString();

                            if (politician.has("emails"))
                                email = politician.getJSONArray("emails").get(0).toString();

                            if (politician.has("phones"))
                                phone = politician.getJSONArray("phones").get(0).toString();

                            if (politician.has("channels"))
                            {
                                jsonArrayChannels = politician.getJSONArray("channels");

                                // Parse however many channels each politican has
                                int k = 0;
                                while (k < jsonArrayChannels.length())
                                {
                                    channel = jsonArrayChannels.getJSONObject(k);
                                    switch (channel.getString("type"))
                                    {
                                        case "Facebook":
                                            facebook = channel.getString("id");

                                        case "Twitter":
                                            twitter = channel.getString("id");

                                        case "YouTube":
                                            youtube = channel.getString("id");
                                    }
                                    k++;
                                }
                            }

                            // "Create" a new politician from acquired data
                            Politician p = new Politician(name, office, party, image, address, website,
                                    email, phone, facebook, youtube, twitter);

                            // Needed to use static mainActivity due to activity-recognition errors
                            mainActivity.alp.add(p);
                            j++;
                        }
                        i++;
                    }
                    PoliticianAdapter pa = new PoliticianAdapter(mainActivity, mainActivity.alp);
                    mainActivity.recyclerView.setAdapter(pa);

                    // Every time recycler view is updated, notify
                    pa.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    System.out.println("CATCH BLOCK: onResponse() in callAPI()");
                }
            }
        };
        Response.ErrorListener error = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                try
                {
                    System.out.println("TRY BLOCK: onErrorResponse() in callAPI()");
                }
                catch (Exception e)
                {
                    System.out.println("CATCH BLOCK: onErrorResponse() in callAPI()");
                }
            }
        };
        queue.add(new JsonObjectRequest(Request.Method.GET, URL, null, listener, error));

        // Reset URL for the next location query to come
        URL = PRE_KEY_URL + API_KEY + PRE_LOC_URL;
    }
}
